package comnet.minihadoop.master.slavemanager;

import comnet.minihadoop.common.message.healthcheck.HealthCheckResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

class HealthCheckerListener implements Runnable {
    private static Logger logger = LoggerFactory.getLogger("hchecker");
    public HealthCheckerListener(HealthChecker checker, HashMap<Integer, Integer> table, int port) {
        this.checker= checker;
        this.table=table;
        try{
            this.ds = new DatagramSocket(port);
        } catch(SocketException e) {
            e.printStackTrace();
        }
    }
    DatagramSocket ds;
    HealthChecker checker;
    HashMap<Integer, Integer> table;
    @Override
    public void run() {
        while(true) {
            byte[] buffer = new byte[12];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            try{
                ds.receive(dp);
            } catch(IOException e) {
                logger.warn("io exception in receiving pkt "+e.getMessage());
            }
            ByteArrayInputStream inStream = new ByteArrayInputStream(dp.getData());
            try{
                HealthCheckResponse res = HealthCheckResponse.parseDelimitedFrom(inStream);
                int id = res.getId();
                logger.info("health check received from "+dp.getAddress()+" "+id);
                table.put(id, 3);
            } catch(IOException e) {
                logger.error("reading health check response from slaves");
            }
        }
    }
    public void shutdown() {
        ds.close();
    }
}