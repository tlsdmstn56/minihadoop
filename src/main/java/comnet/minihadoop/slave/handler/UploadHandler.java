package comnet.minihadoop.slave.handler;

import comnet.minihadoop.common.Configuration;
import comnet.minihadoop.common.message.slave.UploadRequest;
import comnet.minihadoop.common.message.slave.UploadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class UploadHandler implements Runnable {
    private static Logger logger = LoggerFactory.getLogger("main");
    private final String clienthostname;
    private final int clientport;
    private final String filename;
    private final String userID;
    private Configuration config;
    private Socket socketToMaster;
    private UploadRequest uploadRequest;

    public UploadHandler(Socket socketToMaster, UploadRequest uploadRequest, Configuration config) {
        this.socketToMaster = socketToMaster;
        this.uploadRequest = uploadRequest;
        this.clienthostname = uploadRequest.getClientHostname();
        this.clientport = uploadRequest.getClientPort();
        this.filename = uploadRequest.getFilename();
        this.userID = uploadRequest.getUserID();
        this.config = config;
        logger.info("client port: "+clientport);
        logger.info("client hostname: "+clienthostname);
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
        Socket socket=null;
        BufferedOutputStream fos=null;
        BufferedInputStream is=null;
        try {
            String dir = config.get("data-dir",String.class);
            socket = new Socket(clienthostname, clientport);
            logger.debug("socket to client connected");
            File dataDir = new File(dir+ File.separator + userID);
            if (! dataDir.exists()){
                dataDir.mkdirs();
            }
            fos = new BufferedOutputStream(new FileOutputStream( dataDir.getAbsoluteFile()+ File.separator+filename, false));
            logger.debug("filestream opened");
            is = new BufferedInputStream(socket.getInputStream());
            logger.debug("inputstream opened");
            byte[] buffer = new byte[8192];
            int readBytes;
            logger.debug("copying file");
            while ((readBytes = is.read(buffer)) != -1) {
                fos.write(buffer, 0, readBytes);
            }
            logger.debug("copying file complete");
            UploadResponse res = UploadResponse.newBuilder().setStatus(UploadResponse.Type.SUCCESS).build();
            res.writeDelimitedTo(socketToMaster.getOutputStream());
        } catch (IOException e) {
            logger.info("upload failed: "+e.getMessage());
        } finally {
            try{
                is.close();
            } catch(IOException e) {
                logger.warn("instream close exception: "+e.getMessage());
            }
            try{
                fos.close();
            } catch(IOException e) {
                logger.warn("outstream close exception: "+e.getMessage());
            }
            try{
                socket.close();
            } catch(IOException e) {
                logger.warn("socket close exception: "+e.getMessage());
            }
            cleanup();
        }
    }
}
