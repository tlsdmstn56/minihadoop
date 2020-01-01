package comnet.minihadoop.master.slavemanager;

import comnet.minihadoop.common.message.healthcheck.HealthCheckAsk;
import comnet.minihadoop.common.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;

public class HealthChecker implements Runnable {
    private static Logger logger = LoggerFactory.getLogger("hchecker");
    private SlaveManager servers;
    private int period;
    private HashMap<Integer, Integer> table;
    HealthChecker(SlaveManager s, int period, int port) {
        this.servers = s;
        this.period = period*500; // FIXME:
        table = new HashMap<>();
        healthCheckerListenerRunnable = new HealthCheckerListener(this, table, port);
        healthCheckerListernThread = new Thread(healthCheckerListenerRunnable);
        try{
            ds = new DatagramSocket();
        }catch(IOException e) {
            logger.error("healthcherk sending socket opening error " + e.getMessage());
        }

    }
    private DatagramSocket ds;
    private Thread healthCheckerListernThread;
    private HealthCheckerListener healthCheckerListenerRunnable;
    @Override
    public void run() {
        logger.info("starting healthchecker thread");
        healthCheckerListernThread.start();
        try {
            while(true) {
                List<Tuple<String, Integer>> addresses = servers.getAddressWithID();
                sendHealthCheckMessage(addresses);
                checkAndRemove();
                Thread.sleep(period);
            }
        } catch(InterruptedException e) {
            logger.info("shutting down health checker thread");
            healthCheckerListenerRunnable.shutdown();
        }

    }

    void sendHealthCheckMessage(List<Tuple<String, Integer>> addresses) {
        for (Tuple<String, Integer> entry : addresses) {
            String[] splittedAddress = entry.getFirst().split(":");
            Integer id = entry.getSecond();
            if (table.containsKey(id)) {
                table.put(id, new Integer(table.get(id).intValue()-1));
            } else {
                table.put(id, Integer.valueOf(3) );
            }
            try {
                ByteArrayOutputStream outStream = new ByteArrayOutputStream(12);
                HealthCheckAsk ask = HealthCheckAsk
                        .newBuilder()
                        .setId(entry.getSecond())
                        .build();
                ask.writeDelimitedTo(outStream);
                byte[] buffer = outStream.toByteArray();
                DatagramPacket dp = new DatagramPacket(buffer,
                        buffer.length,
                        InetAddress.getByName(splittedAddress[0]),
                        Integer.parseInt(splittedAddress[1]));
                logger.info("sending hc pct to "+entry.getSecond());
                logger.info("  sent address: "+entry.getFirst());
                try {
                    ds.send(dp);
                } catch(IOException e) {
                    logger.error("Error while sending health check message"+e.toString());
                }
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

    }

    void checkAndRemove() {
        table.entrySet()
             .removeIf( entry -> {
                if (entry.getValue().intValue() > 0) {
                    return false;
                } else {
                    servers.removeSlaveServerById(entry.getKey());
                    logger.info("slave id: "+entry.getKey()+" was removed");
                    return true;
                }
             });
    }
}


