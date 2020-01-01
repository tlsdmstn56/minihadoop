package comnet.minihadoop.slave.handler;

import comnet.minihadoop.common.Configuration;
import comnet.minihadoop.common.message.slave.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ReplicationHandler implements Runnable{
    private static Logger logger = LoggerFactory.getLogger("main");
    private final String filename;
    private final String userID;
    private Configuration config;
    private Socket socketToMaster;
    private ReplicateReadyRequest req;

    public ReplicationHandler(Socket socketToMaster, ReplicateReadyRequest req, Configuration config) {
        this.socketToMaster = socketToMaster;
        this.req = req;
        this.filename = req.getFilename();
        this.userID = req.getUserID();
        this.config = config;
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
        Socket socket = null;
        ServerSocket listeningSocket = null;
        try{
            listeningSocket = new ServerSocket(0);
            listeningSocket.setSoTimeout(30000);
            String hostname = InetAddress.getLocalHost().getHostName();
            logger.debug("replication hostname: "+hostname);
            ReplicateReadyResponse res = ReplicateReadyResponse.newBuilder()
                    .setHostname(hostname)
                    .setPort(listeningSocket.getLocalPort())
                    .build();
            Job.newBuilder()
                    .setType(Job.Type.REPLICATE_READY_RES)
                    .setReplicationReadyResponse(res)
                    .build()
                    .writeDelimitedTo(socketToMaster.getOutputStream());
            socket = listeningSocket.accept();
            logger.debug("replication accepted");
        } catch (IOException e) {
            logger.debug("replication failed "+e.getMessage());
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
        String dir = config.get("data-dir",String.class);
        File dataDir = new File(dir+ File.separator + userID);
        if (! dataDir.exists()){
            dataDir.mkdirs();
        }
        try(BufferedInputStream bis = new BufferedInputStream(socket.getInputStream())) {
            try(BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(dataDir.getAbsoluteFile()+ File.separator+filename, false))) {
                byte[] buffer = new byte[8192];
                int readBytes;
                logger.debug("copying file");
                while ((readBytes = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, readBytes);
                }
                logger.debug("copying file complete");
            }catch (IOException e) {
                logger.error("outputstream error");
            }

        }catch (IOException e) {
            logger.error("inputstream error");
        } finally{
            try{
                socket.close();
            }catch (IOException e){
                logger.warn("closing socket error");
            }
        }
    }
}
