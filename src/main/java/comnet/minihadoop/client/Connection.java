package comnet.minihadoop.client;


import com.google.protobuf.ByteString;
import comnet.minihadoop.common.Configuration;
import comnet.minihadoop.common.exception.InitialMasterConnectionError;
import comnet.minihadoop.common.exception.WrongIDPWException;
import comnet.minihadoop.common.message.cli.*;
import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Connection implements Runnable {
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static Logger logger = LoggerFactory.getLogger("consoleLogger");
    private Socket socket;
    final String masterHostName;
    final int masterPort;
    final int port;
    final String ID;
    final String password;
    HashMap<String, Cmd> cmdToEnum;
    enum Cmd {
        UPLOAD
    }
    public Connection(Configuration config) throws WrongIDPWException, InitialMasterConnectionError {
        masterHostName = config.get("master-hostname",String.class);
        masterPort = config.get("master-port",Integer.class);
        ID = config.get("ID",String.class);
        password = config.get("PW",String.class);
        port = config.get("port",Integer.class);
        consoleHeader = ID+" >> ";
        cmdToEnum = new HashMap<>();
        cmdToEnum.put("upload",Cmd.UPLOAD);
        try {
            socket = new Socket(masterHostName, masterPort);
        } catch (IOException e) {
            throw new InitialMasterConnectionError(e);
        }
        CLIMessage msg = makeCLIMessage(ID, password);
        CLIMessage recv = null;
        try{
            msg.writeDelimitedTo(socket.getOutputStream());
            recv = CLIMessage.parseDelimitedFrom(socket.getInputStream());
        } catch (IOException e) {
            throw new InitialMasterConnectionError(e);
        }
        LoginResponse res = recv.getLoginResponse();

        if (res.getSuccess()) { // if success
            return;
        }
        if (res.getError() == LoginResponse.Type.INVALID_ID) {
            throw new WrongIDPWException("ID is not registered");
        }
        else if (res.getError() == LoginResponse.Type.WRONG_PW) {
            throw new WrongIDPWException("Wrong PW");
        }

    }
    String consoleHeader;
    String[] getUserCommand() {
        Scanner scan = new Scanner(System.in);
        System.out.print(consoleHeader);
        try {
            return scan.nextLine().split("\\s");
        } catch(NoSuchElementException e) {
            return null;
        }

    }
    @Override
    public void run() {
        System.out.println(System.getProperty("user.dir").replace("\\","/"));
        while(true) {
            String[] cmd = getUserCommand();
            if (cmd == null) {
                break;
            }
            switch(cmd[0]) {
                case "upload":
                    handleUpload(cmd);
                    break;
                case "ls":
                    handleLs(cmd);
                    break;
                default:
                    System.out.println("ERROR: unknown command: "+cmd[0]);
            }
        }
    }
    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    void handleLs(String[] cmd) {
        if (cmd.length != 1) {
            System.out.println("ERROR: syntax error: ls");
            return;
        }
        ListRequest req = ListRequest.newBuilder()
                .setPath(".").build();
        CLIMessage msg = CLIMessage.newBuilder()
                .setType(CLIMessage.Type.LS_REQ)
                .setListRequest(req)
                .build();
        int reply = 3;
        do {
            reply--;
            try{
                msg.writeDelimitedTo(socket.getOutputStream());
                msg = CLIMessage.parseDelimitedFrom(socket.getInputStream());
            } catch (IOException e) {
                System.out.println("ERROR: upload request sending failed: "+e.getMessage());
                return;
            }
        } while(msg.getListResponse().getFilesList().isEmpty()&&(reply > 0));
        ListResponse res = msg.getListResponse();
        List<ListResponse.File> list = res.getFilesList();
        System.out.println();
        System.out.printf("%-20s|%15s\n", "Filename","File Size");
        System.out.printf("--------------------|---------------\n");
        for(ListResponse.File s :list) {
            System.out.printf("%-20s|%15d\n", s.getFilename(), s.getFilesize());
        }
        System.out.println();
    }
    void handleUpload(String[] cmd) {
        if (cmd.length != 2) {
            System.out.println("ERROR: syntax error: upload [filename]");
            return;
        }
        File file = new File(cmd[1]);
        if (!file.exists()) {
            System.out.println("ERROR: file not exist");
            return;
        }
        long fileSize = file.length();
        UploadRequest req = UploadRequest.newBuilder()
                .setFilename(file.getName())
                .setFilesize(fileSize).build();
        CLIMessage msg = CLIMessage.newBuilder()
                .setType(CLIMessage.Type.UPLOAD_REQ)
                .setUploadRequest(req)
                .build();
        try{
            msg.writeDelimitedTo(socket.getOutputStream());
            msg = CLIMessage.parseDelimitedFrom(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("ERROR: upload request sending failed: "+e.getMessage());
            return;
        }
        UploadResponse res = msg.getUploadResponse();
        if (res.getStatus()== UploadResponse.Type.INTERNAL_ERROR) {
            System.out.println("ERROR: upload request sending failed: Internal Server Error");
            return;
        }
        Socket socket = null;
        ServerSocket listeningSocket = null;
        try{
            listeningSocket = new ServerSocket(port);
            listeningSocket.setSoTimeout(30000);
            String hostname = InetAddress.getLocalHost().getHostName();
            UploadReady ready = UploadReady.newBuilder().setHostname(hostname).setPort(port).build();
            msg = CLIMessage.newBuilder()
                    .setType(CLIMessage.Type.UPLOAD_READY)
                    .setUploadReady(ready)
                    .build();
            msg.writeDelimitedTo(this.socket.getOutputStream());
            socket = listeningSocket.accept();
        } catch (IOException e) {
            System.out.println("ERROR: upload request sending failed: "+e.getMessage());
            try{
                socket.close();
            } catch(IOException e1) {
                e1.printStackTrace();
            }
            return;
        } finally {
            try{
                listeningSocket.close();
            } catch(IOException e1) {
                e1.printStackTrace();
            }
        }
        int readBytes;
        double startTime = 0;
        try (ProgressBar pb = new ProgressBar("Uploading", fileSize, ProgressBarStyle.ASCII)) {
            FileInputStream fis = null;
            OutputStream os =null;
            try{
                fis = new FileInputStream(file);
                startTime = System.currentTimeMillis();
                os = socket.getOutputStream();
                while ((readBytes = fis.read(buffer)) > 0) {
                    os.write(buffer, 0, readBytes);
                    pb.stepBy(readBytes);
                }
                fis.close();
            } catch (IOException e) {
                System.out.println("ERROR: upload failed: "+e.getMessage());
                return;
            }finally {
                try{
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try{
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try{
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } // progress bar stops automatically after completion of try-with-resource block

        double endTime = System.currentTimeMillis();
        double diffTime = (endTime - startTime)/ 1000;
        double transferSpeed = (fileSize / 1000)/ diffTime;
        System.out.println("time: " + diffTime+ " second(s)");
        System.out.println("Average transfer speed: " + transferSpeed + " KB/s");
    }
    private CLIMessage makeCLIMessage(String ID, String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch(NoSuchAlgorithmException e) {
            logger.error("Never Happen: " +e.getMessage());
            return null;
        }
        md.update(password.getBytes(StandardCharsets.ISO_8859_1));
        byte[] digested = md.digest();
        ByteString hashed = ByteString.copyFrom(digested);
        LoginRequest req = LoginRequest.newBuilder()
                .setID(ID)
                .setPassword(hashed)
                .build();
        return CLIMessage.newBuilder()
                .setType(CLIMessage.Type.LOGIN_REQ)
                .setLoginRequest(req)
                .build();
    }
}
