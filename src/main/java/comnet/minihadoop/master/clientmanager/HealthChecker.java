package comnet.minihadoop.master.clientmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

class HealthChecker implements Runnable{
    private static Logger logger = LoggerFactory.getLogger("hchecker");
    ClientListener listener;
    int period;
    public HealthChecker(int period, ClientListener listener) {
        this.listener = listener;
        this.period = period;
    }

    @Override
    public void run() {
        try{
            while(true) {
                checkAndRemove();
                Thread.sleep(period);
            }
        } catch(InterruptedException e) {
            logger.info("shutting down health checker");
        }
    }
    void checkAndRemove() {
        Set<Integer> ids = listener.getAliveID();
        for(Integer id : ids) {
            listener.checkRemoveClientByID(id);
        }
    }
}
