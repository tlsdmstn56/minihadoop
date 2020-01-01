package comnet.minihadoop.master.filesystem;

import comnet.minihadoop.common.User;
import comnet.minihadoop.common.util.Triple;
import comnet.minihadoop.master.slavemanager.SlaveSocketManager;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class File  {
    private String userID;
    private String filename;
    private Triple<SlaveSocketManager,SlaveSocketManager,SlaveSocketManager> servers;
    private long filesize;

    public File(String userID, String filename, SlaveSocketManager server, long filesize) {
        this.userID = userID;
        this.filename = filename;
        this.servers = new Triple<>(server, null, null);
        this.filesize = filesize;
    }
    public void addServer(SlaveSocketManager s) {
        synchronized (servers) {
            if (servers.getMiddle() == null){
                servers.setMiddle(s);
                return;
            }
            if (servers.getRight() == null){
                servers.setRight(s);
            }
        }
    }
    public String getUserID() {
        return userID;
    }

    public String getFilename() {
        return filename;
    }

    public long getFilesize() {
        return filesize;
    }
}
