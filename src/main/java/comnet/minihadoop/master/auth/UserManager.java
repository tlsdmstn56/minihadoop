package comnet.minihadoop.master.auth;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;
import comnet.minihadoop.common.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserManager {
    private static Logger logger = LoggerFactory.getLogger("main");
    private HashMap<String, byte[]> userTable;
    private Configuration config;
    public UserManager(Configuration config) {
        this.config = config;
        this.userTable = new HashMap<>();
        setupUserTable();
    }
    public Set<String> getUsers() {
        synchronized (userTable) {
            return userTable.keySet();
        }
    }
    private void setupUserTable() {
        String userFilePath = config.get("user-table", String.class );
        InputStream input;
        try {
            input = new FileInputStream(new File(userFilePath));
        } catch (FileNotFoundException e) {
            logger.error("Cannot find user.yaml file: "+userFilePath);
            return;
        }
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.error("Cannot no such hashing algorithm: "+e.getMessage());
            return;
        }
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(input);
        for( Map.Entry<String, Object> elem : data.entrySet() ){
            String password = String.valueOf(elem.getValue());
            md.update(password.getBytes(StandardCharsets.ISO_8859_1));
            byte[] digested = md.digest();
            userTable.put(elem.getKey(), digested);

        }
        logger.info("User table initialized: "+userTable.size() + " read");
    }
    public boolean isValidID(String id) {
        synchronized (userTable) {
            return userTable.containsKey(id);
        }
    }
    public boolean isValidPW(String id, ByteString pw) {
        synchronized (userTable) {
            assert userTable.containsKey(id);
            byte[] hashUser = pw.toByteArray();
            return MessageDigest.isEqual(userTable.get(id), hashUser);
        }

    }
}
