package comnet.minihadoop.slave;

import comnet.minihadoop.common.Configuration;
import comnet.minihadoop.common.message.slave.ConnectionRequest;
import comnet.minihadoop.common.message.slave.ConnectionResponse;
import comnet.minihadoop.common.message.slave.Job;
import comnet.minihadoop.slave.handler.ReplicationHandler;
import comnet.minihadoop.slave.handler.ReplicationSenderHandler;
import comnet.minihadoop.slave.handler.UploadHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SlaveServer{
    private static Logger logger = LoggerFactory.getLogger("main");
    private Configuration config;
    public SlaveServer(Configuration config) {
        this.config = config;
        masterHost = this.config.get("master-host",String.class);
        masterPort = this.config.get("master-welcome-port",Integer.class);
        listeningPort = this.config.get("listening-port",Integer.class);
        healthCheckPort = this.config.get("health-check-port",Integer.class);
        datadir = this.config.get("data-dir",String.class);
        jobQueue = new ConcurrentLinkedQueue<>();
        healthCheckReceivedCount = new AtomicInteger();
        this.executorService = Executors.newCachedThreadPool();
    }
    private String datadir;
    private Thread healthCheckerThread;
    private HealthChecker healthCheckerRunnable;
    private Thread healthCheckerListenerThread;
    private HealthCheckerListener healthCheckerListenerRunnable;
    private ServerSocket listeningSocket;
    private String masterHost;
    private int masterPort, healthCheckPort, masterHealthCheckPort;
    private String host;
    private int listeningPort;
    private ConcurrentLinkedQueue<Job> jobQueue;
    private int healthCheckPeriod = -1;
    private AtomicInteger healthCheckReceivedCount;
    private int id;
    private ExecutorService executorService;
    public void run() {
        // connection to the master
        try{
            InetSocketAddress addr = new InetSocketAddress(masterHost, masterPort);
            Socket socket = new Socket();
            socket.connect(addr, 1000*
                    this.config.get("master-discovery-timeout",Integer.class));
            logger.info("connection to master estabilisehd");
            ConnectionRequest.newBuilder().
                    setHostname(InetAddress.getLocalHost().getHostAddress()).
                    setPort(listeningPort).
                    setHealthCheckPort(healthCheckPort).
                    build().
                    writeDelimitedTo(socket.getOutputStream());
            ConnectionResponse res = ConnectionResponse.parseDelimitedFrom(socket.getInputStream());
            id = res.getAck();
            healthCheckPeriod = res.getHealthCheckPeriod();
            masterHealthCheckPort = res.getHealthCheckPort();
            logger.info("get response "+res.getAck());
            socket.close();
        } catch (IOException e ) {
            logger.error("error whiling opening slave Welcome Socket"+e.toString());
            return;
        }

        healthCheckReceivedCount.set(1);
        // opening welcome socket
        try {
            listeningSocket = new ServerSocket(listeningPort);
            logger.info("listening socket opened");
        } catch(IOException e) {
            logger.error("error whiling opening slave Welcome Socket"+e.toString());
        }
        // launch health checker
        healthCheckerRunnable = new HealthChecker(healthCheckPeriod,
                healthCheckReceivedCount, listeningSocket);
        healthCheckerThread = new Thread(healthCheckerRunnable);
        healthCheckerThread.start();
        try{
            healthCheckerListenerRunnable = new HealthCheckerListener(healthCheckReceivedCount,
                    healthCheckPort, masterHost, masterHealthCheckPort);
        } catch(IOException e) {
            logger.error("health check message listener socket opening failed" + e.getMessage());
        }
        healthCheckerListenerThread = new Thread(healthCheckerListenerRunnable);
        healthCheckerListenerThread.start();

        try{
            while(true) {
                Socket socket = listeningSocket.accept();
                Job job = Job.parseDelimitedFrom(socket.getInputStream());
                switch(job.getType()) {
                    case UPLOAD_REQ:
                        logger.debug("launching debug handler");
                        executorService.submit(new UploadHandler(socket, job.getUploadRequest(), config));
                        break;
                    case REPLICATE_READY_REQ:
                        logger.debug("launching rep ready req");
                        executorService.submit(new ReplicationHandler(socket, job.getReplicationReadyRequest(),config));
                        break;
                    case REPLICATE_REQ:
                        logger.debug("launching rep request");
                        executorService.submit(new ReplicationSenderHandler(socket, job.getReplicateRequest(),config));
                        break;
                }
            }
        } catch (SocketException e ) {
            logger.info("listening Socket closed " + e.toString());
        } catch (IOException e) {
            logger.info("error in listening socket" + e.toString());
        } finally {
            cleanUpThread();
        }
    }
    void cleanUpThread() {
        healthCheckerThread.interrupt();
        healthCheckerListenerRunnable.shutdown();
        logger.info("thread clean up");
    }

    public int getId() {
        return id;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }
}


