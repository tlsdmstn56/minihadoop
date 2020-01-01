package comnet.minihadoop.slave;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicInteger;

class HealthChecker implements Runnable {
    private static Logger logger = LoggerFactory.getLogger("hchecker");
    final int period; // 3*1000;
    ServerSocket socket;
    AtomicInteger cnt;

    public HealthChecker(int period, AtomicInteger cnt, ServerSocket socket) {
        this.period = period*3*1000;
        this.socket = socket;
        this.cnt = cnt;
    }

    @Override
    public void run() {
        try {
            while(cnt.get() > 0) {
                cnt.set(0);
                Thread.sleep(period);
            }
            logger.info("No response from master server");
        } catch (InterruptedException e) {
            logger.info("Interrupted");
        }
        try{
            socket.close();
        } catch(IOException e) {
            logger.warn("Shutting down listening socket by health checker");
        }
    }
}