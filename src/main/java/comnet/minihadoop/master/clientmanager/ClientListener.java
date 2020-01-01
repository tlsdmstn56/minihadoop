package comnet.minihadoop.master.clientmanager;

import comnet.minihadoop.common.Configuration;
import comnet.minihadoop.master.MasterServer;
import comnet.minihadoop.master.auth.UserManager;
import comnet.minihadoop.master.slavemanager.SlaveManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientListener implements Runnable {
    private static Logger logger = LoggerFactory.getLogger("main");
    private int port;
    private ServerSocket listener;
    private MasterServer server;
    private AtomicInteger id;
    private Configuration config;
    UserManager userManager;
    private SlaveManager slaveManager;
    public ClientListener(MasterServer server, int port, Configuration config, UserManager userManager, SlaveManager slaveManager) {
        this.config = config;
        this.userManager = userManager;
        this.port=port;
        this.server = server;
        this.clientHandlers = new HashMap<>();
        this.clientThreads = new HashMap<>();
        this.slaveManager = slaveManager;
        this.id = new AtomicInteger(0);
        try{
            listener = new ServerSocket(port);
        } catch(SocketException e) {
            logger.error("client socket opening error: "+ e.getMessage());
        } catch(IOException e) {
            logger.error("unknown error client socket opening error: " + e.getMessage());
        }
    }

    private HashMap<Integer, Thread> clientThreads;
    private HashMap<Integer, ClientHandler> clientHandlers;
    HealthChecker healthCheckerRunnable;
    Thread healthCheckerThread;
    @Override
    public void run() {
        // launch health checker
        healthCheckerRunnable = new HealthChecker(5000,this);
        healthCheckerThread = new Thread(healthCheckerRunnable);
        healthCheckerThread.start();
        // listening
        try {
            while(true) {
                Socket clientSocket = listener.accept();
                Integer thisID = id.incrementAndGet();
                ClientHandler handler = new ClientHandler(thisID, clientSocket, this, userManager);
                Thread thread = new Thread(handler);
                thread.start();
                synchronized (clientHandlers) {
                    synchronized (clientThreads) {
                        clientHandlers.put(thisID, handler);
                        clientThreads.put(thisID, thread);
                    }
                }

            }
        } catch (IOException e) {
            logger.info("client listener is closed: " + e.getMessage());
        }

    }
    public void shutdown() {
        try{
            listener.close();
            healthCheckerThread.interrupt();
        } catch (IOException e) {
            logger.info("client listeer shutdown: " + e.getMessage());
        }
    }
    Set<Integer> getAliveID() {
        synchronized (clientHandlers) {
            synchronized (clientThreads) {
                return clientHandlers.keySet();
            }
        }
    }

    void checkRemoveClientByID(Integer id) {
        synchronized (clientHandlers) {
            if(clientHandlers.get(id).isClientAlive()) {
                return;
            }
            synchronized (clientThreads) {
                ClientHandler handler = clientHandlers.remove(id);
                handler.shutdown();
                Thread t = clientThreads.remove(id);
                assert t.getState() == Thread.State.TERMINATED;
            }
        }
    }

    public SlaveManager getSlaveManager() {
        return slaveManager;
    }

    public MasterServer getServer() {
        return server;
    }
}
