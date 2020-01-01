package comnet.minihadoop.master;

import comnet.minihadoop.common.Configuration;
import comnet.minihadoop.common.message.slave.ConnectionRequest;
import comnet.minihadoop.common.message.slave.ConnectionResponse;
import comnet.minihadoop.master.auth.UserManager;
import comnet.minihadoop.master.clientmanager.ClientListener;
import comnet.minihadoop.master.filesystem.FileSystem;
import comnet.minihadoop.master.slavemanager.SlaveManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MasterServer implements Runnable {
    AtomicBoolean stop;
    private static Logger logger = LoggerFactory.getLogger("main");
    private Configuration config;
    UserManager userManager;
    public MasterServer(Configuration config) {
        this.config = config;
        this.userManager = new UserManager(config);
        masterHost = config.get("master-host",String.class);
        slavePort = config.get("slave-welcome-port",Integer.class);
        clientPort = config.get("client-welcome-port",Integer.class);
        healthCheckPeriod = config.get("health-check-period",Integer.class);
        healthCheckPort = config.get("health-check-port",Integer.class);
        stop = new AtomicBoolean(false);
        slaveManager = new SlaveManager(config, healthCheckPeriod);
        serverId = new AtomicInteger();
        serverId.set(0);
        fs = new FileSystem();
        for (String userID : userManager.getUsers()) {
            fs.addUser(userID);
        }
    }
    public FileSystem getFileSystem() {
        return fs;
    }
    private int healthCheckPort;
    private int healthCheckPeriod;
    private FileSystem fs;

    private ClientListener clientListenerRunnable;
    Thread clientListenerThread;
    private ServerSocket slaveWelcomeSocket;
    private String masterHost;
    private int clientPort;
    private int slavePort;
    private SlaveManager slaveManager;
    private AtomicInteger serverId;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public void run() {
        // opening client welcome socket/ slave welcome socket
        try{
            slaveWelcomeSocket = new ServerSocket(slavePort);
            logger.info("slave welcome socket initialized");
        } catch (IOException e) {
            logger.error("slave welcome socket opening error: "+e.getMessage());
        }
        clientListenerRunnable = new ClientListener(this, clientPort,config, userManager, slaveManager);
        clientListenerThread = new Thread(clientListenerRunnable);
        clientListenerThread.start();
        // slave listening
        try{
            while(!stop.get()){
                //creating socket and waiting for client connection
                Socket socket = slaveWelcomeSocket.accept();
                ConnectionRequest req = ConnectionRequest.parseDelimitedFrom(socket.getInputStream());
                logger.info("slave welcome socket accept");
                int thisServerId = serverId.incrementAndGet();
                ConnectionResponse.newBuilder()
                        .setAck(thisServerId)
                        .setHealthCheckPeriod(healthCheckPeriod)
                        .setHealthCheckPort(healthCheckPort)
                        .build()
                        .writeDelimitedTo(socket.getOutputStream());
                slaveManager.addSlaveServer(req.getHostname(), req.getPort(), thisServerId, req.getHealthCheckPort());
                socket.close();
            }
            slaveWelcomeSocket.close();
        } catch (IOException e ) {
            logger.error("error whiling opening slave Welcome Socket"+e.toString());
        } finally {
            clientListenerRunnable.shutdown();
            try {
                slaveWelcomeSocket.close();
            } catch(IOException e) {
                logger.error("error whiling closing slave Welcome Socket"+e.toString());
            }
        }
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}
