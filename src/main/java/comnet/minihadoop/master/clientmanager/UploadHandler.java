package comnet.minihadoop.master.clientmanager;

import comnet.minihadoop.common.message.cli.*;
import comnet.minihadoop.common.message.cli.UploadRequest;
import comnet.minihadoop.common.message.cli.UploadResponse;
import comnet.minihadoop.common.message.slave.*;
import comnet.minihadoop.common.util.Triple;
import comnet.minihadoop.master.MasterServer;
import comnet.minihadoop.master.filesystem.FileSystem;
import comnet.minihadoop.master.slavemanager.SlaveManager;
import comnet.minihadoop.master.slavemanager.SlaveSocketManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

class UploadHandler {
    private UploadRequest uploadRequest;
    private SlaveManager slaveManager;
    private Socket socket;
    private String userID;
    private FileSystem fileSystem;
    private MasterServer masterServer;

    public UploadHandler(UploadRequest uploadRequest, SlaveManager slaveManager, Socket socket, String userID, MasterServer masterServer) {
        this.uploadRequest = uploadRequest;
        this.slaveManager = slaveManager;
        this.socket = socket;
        this.userID = userID;
        this.fileSystem = masterServer.getFileSystem();
        this.masterServer = masterServer;
    }
    private static Logger logger = LoggerFactory.getLogger("main");

    public void handle() {
        long fileSize = uploadRequest.getFilesize();
        String filename = uploadRequest.getFilename();
        Triple<SlaveSocketManager,SlaveSocketManager,SlaveSocketManager> servers =
                slaveManager.getSlaveServer();
        // left
        Socket socketToSlave = getNewSlaveSocket(servers.getLeft());
        if (socketToSlave == null) {
            sendUploadFailMessage();
            return;
        }
        try{
            UploadResponse res = UploadResponse.newBuilder()
                    .setStatus(UploadResponse.Type.SUCCESS)
                    .build();
            CLIMessage msg = CLIMessage.newBuilder()
                    .setType(CLIMessage.Type.UPLOAD_RES).setUploadResponse(res).build();
            msg.writeDelimitedTo(socket.getOutputStream());
            msg = CLIMessage.parseDelimitedFrom(socket.getInputStream());
            logger.debug("ready message received from user "+userID);
            UploadReady ready = msg.getUploadReady();
            comnet.minihadoop.common.message.slave.UploadRequest req =
                    comnet.minihadoop.common.message.slave.UploadRequest.newBuilder()
                            .setClientHostname(ready.getHostname())
                            .setClientPort(ready.getPort())
                            .setFilename(filename).setUserID(userID)
                            .build();
            Job.newBuilder().setType(Job.Type.UPLOAD_REQ).setUploadRequest(req).build()
                    .writeDelimitedTo(socketToSlave.getOutputStream());
            logger.debug("upload request to slaveDst ");
            comnet.minihadoop.common.message.slave.UploadResponse resFromSlave =
                    comnet.minihadoop.common.message.slave.UploadResponse
                            .parseDelimitedFrom(socketToSlave.getInputStream());
            logger.debug("upload response from slaveDst");
            if (resFromSlave.getStatus() != comnet.minihadoop.common.message.slave.UploadResponse.Type.SUCCESS) {
                throw new IOException("Unknown error");
            }
            servers.getLeft().updateUsedSize(fileSize);
        } catch (IOException e){
            logger.warn("upload failed: "+e.getMessage());
        } finally {
            try{
                socketToSlave.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        logger.warn("initial upload success ");
        if (servers.getMiddle()!=null) {
            masterServer.getExecutorService().submit(new Replication(servers.getMiddle(),servers.getLeft(),userID, filename, fileSize));
        }
        if (servers.getRight()!=null) {
            masterServer.getExecutorService().submit(new Replication(servers.getRight(),servers.getLeft(),userID, filename, fileSize));
        }
        fileSystem.addFile(userID, filename, servers.getLeft(),fileSize);
        slaveManager.returnSlaveServer(servers);

    }
    Socket getNewSlaveSocket(SlaveSocketManager manager) {
        if (manager == null) {
            logger.warn("upload failed server is null");
            return null;
        }
        try{
            return new Socket(manager.getHostname(), manager.getPort());
        } catch (IOException e) {
            logger.warn("upload failed: "+e.getMessage());
            return null;
        }
    }
    void sendUploadFailMessage() {
        UploadResponse res = UploadResponse.newBuilder().setHostname("ERROR")
                .setPort(0)
                .setStatus(UploadResponse.Type.INTERNAL_ERROR)
                .build();
        CLIMessage msg = CLIMessage.newBuilder()
                .setType(CLIMessage.Type.UPLOAD_RES).setUploadResponse(res).build();
        try{
            msg.writeDelimitedTo(socket.getOutputStream());
        } catch(IOException e2) {
            logger.warn("client might be down: "+e2.getMessage());
        }
    }
    class Replication implements Runnable {
        private SlaveSocketManager slaveDst;
        private SlaveSocketManager slaveSrc;
        private String srcHost;
        private int srcPort;
        private String userID;
        private String filename;
        private long filesize;

        public Replication(SlaveSocketManager slaveDst,SlaveSocketManager slaveSrc,String userID, String filename, long filesize) {
            this.slaveDst = slaveDst;
            this.slaveSrc = slaveSrc;
            this.userID = userID;
            this.filename = filename;
            this.filesize = filesize;
        }
        Socket socketToDst;
        Socket socketToSrc;
        @Override
        public void run() {
            ReplicateReadyRequest repReadyReq = ReplicateReadyRequest.newBuilder()
                    .setFilename(filename).setUserID(userID).build();
            Job msg = Job.newBuilder().setType(Job.Type.REPLICATE_READY_REQ).setReplicationReadyRequest(repReadyReq).build();
            try{
                socketToDst = new Socket(slaveDst.getHostname(), slaveDst.getPort());
                msg.writeDelimitedTo(socketToDst.getOutputStream());
            } catch (IOException e) {
                try{
                    socketToDst.close();
                } catch(IOException e1) { }
                logger.warn("socket opening error "+e.getMessage());
                return;
            }
            try{
                msg = Job.parseDelimitedFrom(socketToDst.getInputStream());
            }catch (IOException e) {
                try{
                    socketToDst.close();
                } catch(IOException e1) { }
                logger.warn("socket opening error "+e.getMessage());
                return;
            }
            ReplicateReadyResponse res = msg.getReplicationReadyResponse();
            logger.info(res.getHostname());
            ReplicateRequest repreq = ReplicateRequest.newBuilder()
                    .setHostname(res.getHostname())
                    .setPort(res.getPort())
                    .setFilename(filename)
                    .setUserID(userID)
                    .build();
            msg = Job.newBuilder().setType(Job.Type.REPLICATE_REQ).setReplicateRequest(repreq).build();

            try{
                socketToSrc = new Socket(slaveSrc.getHostname(), slaveSrc.getPort());
                msg.writeDelimitedTo(socketToSrc.getOutputStream());
            } catch (IOException e) {
                try{
                    socketToSrc.close();
                } catch(IOException e1) { }
                logger.warn("socket opening error "+e.getMessage());
                return;
            }
            ReplicateResponse repRes = null;
            try{
                repRes = ReplicateResponse.parseDelimitedFrom(socketToSrc.getInputStream());
            } catch (IOException e) {
                logger.warn("replicate request sending error"+e.getMessage());
                return;
            }
            if (repRes.getStatus()!= ReplicateResponse.Type.SUCCESS) {
                logger.warn("replicate failed");
                return;
            }
            logger.info("success replication");
            slaveDst.updateUsedSize(filesize);
        }
    }
}
