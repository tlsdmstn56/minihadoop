package comnet.minihadoop.master.slavemanager;

import comnet.minihadoop.common.Configuration;
import comnet.minihadoop.common.util.Triple;
import comnet.minihadoop.common.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

// TODO: implement
// TODO: synchronization
public class SlaveManager {
    private static Logger logger = LoggerFactory.getLogger("main");
    PriorityQueue<SlaveSocketManager> servers;
    HashMap<Integer, SlaveSocketManager> idToServer;
    Configuration config;
    public SlaveManager(Configuration config, int healthCheckPeriod) {
        idToServer = new HashMap<>();
        servers = new PriorityQueue<>();
        this.config = config;
        healthCheckPort = this.config.get("health-check-port", Integer.class);
        healthCheckThread = new Thread(new HealthChecker(this, healthCheckPeriod, healthCheckPort ));
        healthCheckThread.start();
    }
    int healthCheckPort;
    Thread healthCheckThread;
    public List<Tuple<String, Integer>> getAddressWithID() {
        ArrayList<Tuple<String, Integer>> ret = new ArrayList<>();
        synchronized (idToServer) {
            for (Integer id : idToServer.keySet()) {
                ret.add(new Tuple(idToServer.get(id).getHeaclthCheckDddress(), id));
            }
        }
        return ret;
    }
    /**
     * select three slave server
     * @return Triple possibly null element
     */
    public Triple<SlaveSocketManager, SlaveSocketManager, SlaveSocketManager> getSlaveServer() {
        synchronized (idToServer) {
            synchronized (servers) {
                SlaveSocketManager s1 = servers.poll();
                SlaveSocketManager s2 = servers.poll();
                SlaveSocketManager s3 = servers.poll();
                return new Triple<>(s1, s2, s3);
            }
        }
    }
    public void returnSlaveServer(Triple<SlaveSocketManager, SlaveSocketManager, SlaveSocketManager> s) {
        synchronized (idToServer) {
            synchronized (servers) {
                if (s.getLeft()!= null) {
                    servers.add(s.getLeft());
                }
                if (s.getMiddle()!= null) {
                    servers.add(s.getMiddle());
                }
                if (s.getRight()!= null) {
                    servers.add(s.getRight());
                }
            }
        }
    }
    public void addSlaveServer(String hostname, int port, int id, int healthCheckPort) {
        SlaveSocketManager server = new SlaveSocketManager(hostname, port, id, healthCheckPort);
        String key = hostname+":"+port;
        synchronized(idToServer) {
            synchronized (servers) {
                idToServer.put(server.getID(), server);
                servers.add(server);
            }
        }
        logger.info("slave " +key+" is added");
    }
    public SlaveSocketManager getSlaveSocketManagerByID(Integer id) {
        synchronized (idToServer) {
            return idToServer.get(id);
        }
    }
    public void removeSlaveServerById(Integer id) {
        synchronized (idToServer) {
            synchronized (servers) {
                SlaveSocketManager server = idToServer.get(id);
                if (server == null) {
                    logger.warn("trying to to remove non-exist server or removed server");
                }
                else {
                    servers.remove(server);
                    idToServer.remove(id);
                    logger.info("slave " +server.getHostname()+" is removed");
                }
            }
        }
    }
}
