package comnet.minihadoop.master.slavemanager;

public class SlaveSocketManager implements Comparable<SlaveSocketManager> {
    private final String hostname;
    private final int port;
    private long usedsize;
    private final Integer id;
    private final int healthCheckPort;
    private final String heaclthCheckDddress;
    public SlaveSocketManager(String hostname, int port, Integer id, int healthCheckPort) {
        this.hostname = hostname;
        this.port = port;
        this.id = id;
        this.usedsize = 0;
        this.healthCheckPort = healthCheckPort;
        this.heaclthCheckDddress = hostname+":"+healthCheckPort;
    }
    public void updateUsedSize(long delta) {
        this.usedsize += delta;
    }
    @Override
    public int compareTo(SlaveSocketManager o) {
        if(this.usedsize >o.usedsize){
            return 1;
        }
        else if(this.usedsize <o.usedsize){
            return -1;
        }
        return 0;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public Integer getID() {return id;}

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public int getHealthCheckPort() {
        return healthCheckPort;
    }

    public String getHeaclthCheckDddress() {
        return heaclthCheckDddress;
    }
}
