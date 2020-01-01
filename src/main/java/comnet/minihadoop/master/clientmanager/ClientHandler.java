package comnet.minihadoop.master.clientmanager;

import com.google.protobuf.ByteString;
import comnet.minihadoop.common.message.cli.*;
import comnet.minihadoop.common.message.healthcheck.HealthCheckAsk;
import comnet.minihadoop.common.message.slave.Job;
import comnet.minihadoop.common.message.slave.Slave;
import comnet.minihadoop.common.util.Triple;
import comnet.minihadoop.master.auth.UserManager;
import comnet.minihadoop.master.filesystem.File;
import comnet.minihadoop.master.slavemanager.SlaveManager;
import comnet.minihadoop.master.slavemanager.SlaveSocketManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.Buffer;

class ClientHandler implements Runnable {
    private static Logger logger = LoggerFactory.getLogger("hchecker");
    private Integer id;
    private Socket socket;
    private ClientListener listener;
    private UserManager userManager;
    public ClientHandler(Integer id, Socket socket, ClientListener listener, UserManager userManager) {
        this.id = id;
        this.socket = socket;
        this.listener = listener;
        this.userManager = userManager;
    }

    @Override
    public void run() {
        try{
            while(true) {
                CLIMessage cmd = null;
                cmd = CLIMessage.parseDelimitedFrom(socket.getInputStream());
                switch(cmd.getType())
                {
                    case HEALTH_CHECK:
                        break;
                    case LOGIN_REQ:
                        handleLoginRequest(cmd.getLoginRequest());
                        break;
                    case UPLOAD_REQ:
                        UploadHandler handler = new UploadHandler(cmd.getUploadRequest(),listener.getSlaveManager(),socket, userID, listener.getServer());
                        handler.handle();
                        break;
                    case LS_REQ:
                        handleListRequest(cmd.getListRequest());
                        break;
                }
            }
        }  catch(IOException e) {
            logger.warn("client closed connection");
        }
    }


    void handleListRequest(ListRequest req) {
        String path = req.getPath();
        ListResponse.Builder builder = ListResponse.newBuilder();
        for(File file : listener.getServer().getFileSystem().getFiles(userID)) {
            logger.debug(file.getFilename());
            ListResponse.File msg = ListResponse.File.newBuilder()
                    .setFilename(file.getFilename())
                    .setFilesize(file.getFilesize())
                    .build();
            builder.addFiles(msg);
        }
        ListResponse res = builder.build();
        CLIMessage msg = CLIMessage.newBuilder()
                .setType(CLIMessage.Type.LS_RES)
                .setListResponse(res)
                .build();
        try{
            msg.writeDelimitedTo(socket.getOutputStream());
        } catch (IOException e) {
            logger.warn("exception while wending list response| id: "+id);
        }
    }

    String userID;
    void handleLoginRequest(LoginRequest req) {
        userID = req.getID();
        ByteString hash = req.getPassword();
        LoginResponse.Builder resBuilder = LoginResponse.newBuilder();
        if (!userManager.isValidID(userID)) {
            resBuilder.setSuccess(false)
                    .setError(LoginResponse.Type.INVALID_ID);
        }
        else if (!userManager.isValidPW(userID, hash)) {
            resBuilder.setSuccess(false)
                    .setError(LoginResponse.Type.WRONG_PW);
        }
        else {
            // suucess
            resBuilder.setSuccess(true)
                    .setError(LoginResponse.Type.SUCCESS);
        }
        LoginResponse res =  resBuilder.build();
        CLIMessage msg = CLIMessage.newBuilder()
                .setType(CLIMessage.Type.LOGIN_RES)
                .setLoginResponse(res)
                .build();
        try{
            msg.writeDelimitedTo(socket.getOutputStream());
        } catch (IOException e) {
            logger.warn("exception while wending login response| id: "+id);
            shutdown();
        }
        if(!res.getSuccess()) {
            logger.warn("Authentication failed | id: "+id);
            shutdown();
        }
    }

    boolean isClientAlive() {
        HealthCheck hc = HealthCheck.newBuilder()
                .setSuccess(true)
                .build();
        CLIMessage msg = CLIMessage.newBuilder()
                .setType(CLIMessage.Type.HEALTH_CHECK)
                .setHc(hc)
                .build();
        synchronized (socket) {
            try{
                msg.writeDelimitedTo(socket.getOutputStream());
                return true;
            } catch (IOException e) {
                logger.warn("client closed connection");
                return false;
            }
        }
    }
    public void shutdown() {
        try{
            socket.close();
        } catch(IOException e) {
            logger.warn("exception while closing client handler socket| id: "+id);
        }
        logger.info("client handler terminated| id: "+id);
    }
}
