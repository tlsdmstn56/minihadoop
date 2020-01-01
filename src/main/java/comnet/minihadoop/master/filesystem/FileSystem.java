package comnet.minihadoop.master.filesystem;

import comnet.minihadoop.common.User;
import comnet.minihadoop.master.slavemanager.SlaveSocketManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;



public class FileSystem {
    private static Logger logger = LoggerFactory.getLogger("main");
    HashMap<String, ArrayList<File>> fs = new HashMap<>();
    public FileSystem() {
    }
    public void addUser(String userID) {
        synchronized (fs) {
            fs.put(userID, new ArrayList<>());
        }
    }
    public void addFile(String userID, String filename, SlaveSocketManager s, long filesize) {
        File file = new File(userID, filename, s, filesize);
        synchronized (fs) {
            fs.get(userID).add(file);
        }
    }
    public List<File> getFiles(String userID) {
        List<File> ls = null;
        synchronized (fs) {
             ls = (List<File>)fs.get(userID).clone();
        }
        return ls;
    }
}
