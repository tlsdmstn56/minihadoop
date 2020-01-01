package comnet.minihadoop.slave.handler;

import comnet.minihadoop.common.Configuration;
import comnet.minihadoop.common.message.slave.ReplicateRequest;
import comnet.minihadoop.common.message.slave.ReplicateResponse;
import comnet.minihadoop.common.message.slave.UploadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class ReplicationSenderHandler implements Runnable {
    private static Logger logger = LoggerFactory.getLogger("main");
    private final String filename;
    private final String userID;
    private final int port;
    private Configuration config;
    private ReplicateRequest req;
    private Socket socketToMaster;
    String hostname;

    public ReplicationSenderHandler(Socket socketToMaster, ReplicateRequest req, Configuration config) {
        this.socketToMaster = socketToMaster;
        this.req = req;
        this.config = config;
        this.filename = req.getFilename();
        this.userID= req.getUserID();
        this.hostname = req.getHostname();
        this.port = req.getPort();
        logger.debug("replication sender handler will send to"+hostname+":"+port);
    }
    void cleanup() {
        try{
            socketToMaster.close();
        } catch (IOException e) {
            logger.info("upload handler clean up");
        }
    }
    @Override
    public void run() {
        byte[] buffer = new byte[8192];
        int readBytes = 0;
        String dir = config.get("data-dir",String.class);
        File dataDir = new File(dir+ File.separator + userID);
        boolean success= true;
        logger.debug(hostname +":"+port);
        try (Socket socket = new Socket(hostname, port)){
            try(BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream())) {
                try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(dataDir))) {
                    while ((readBytes = bis.read(buffer)) != -1) {
                        bos.write(buffer, 0, readBytes);
                    }
                } catch (IOException e) {
                    success=false;
                }
            }catch (IOException e) {
                success=false;
            }
        } catch (IOException e) {
            success=false;
            logger.info("upload failed: "+e.getMessage());
        }
        try{
            if (success) {
                ReplicateResponse.newBuilder().setStatus(ReplicateResponse.Type.SUCCESS).build().writeDelimitedTo(socketToMaster.getOutputStream());
            } else {
                ReplicateResponse.newBuilder().setStatus(ReplicateResponse.Type.FAIL).build().writeDelimitedTo(socketToMaster.getOutputStream());
            }
        } catch (IOException e) {
            logger.warn("error while sending replicatte request ");
        }
        cleanup();
    }
}
