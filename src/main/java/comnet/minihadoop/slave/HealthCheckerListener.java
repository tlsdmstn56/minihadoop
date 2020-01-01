package comnet.minihadoop.slave;

import comnet.minihadoop.common.message.healthcheck.HealthCheckAsk;
import comnet.minihadoop.common.message.healthcheck.HealthCheckResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicInteger;

class HealthCheckerListener implements Runnable {
    private static Logger logger = LoggerFactory.getLogger("hchecker");
    DatagramSocket healthCheckMessageListener;
    AtomicInteger cnt;
    public HealthCheckerListener(AtomicInteger cnt, int port, String masterHost, int masterPort) throws IOException {
        healthCheckMessageListener = new DatagramSocket(port);
        this.cnt = cnt;
        this.buffer = new byte[12];
        dp = new DatagramPacket(buffer,buffer.length);
        this.masterHost = masterHost;
        this.masterPort = masterPort;
    }
    String masterHost;
    int masterPort;
    byte[] buffer;
    DatagramPacket dp;
    @Override
    public void run() {
        try{
            while(true) {
                healthCheckMessageListener.receive(dp);
                cnt.incrementAndGet();
                HealthCheckAsk res = HealthCheckAsk
                        .parseDelimitedFrom(new ByteArrayInputStream(dp.getData()));
                logger.info("received hc: " + res.getId());
                ByteArrayOutputStream bos = new ByteArrayOutputStream(12);
                HealthCheckResponse send = HealthCheckResponse
                        .newBuilder()
                        .setId(res.getId())
                        .build();
                send.writeDelimitedTo(bos);
                byte[] out = bos.toByteArray();
                DatagramPacket outpkt = new DatagramPacket(out, out.length,
                        InetAddress.getByName(masterHost), masterPort);
                healthCheckMessageListener.send(outpkt);
            }
        } catch(SocketException e) {
            logger.info("listening socket closed "+e.toString());
        } catch (IOException e) {
            logger.error(""+e.toString());
        }
    }
    public void shutdown() {
        healthCheckMessageListener.close();
        logger.warn("healthcheckerlistener shutddown");
    }
}