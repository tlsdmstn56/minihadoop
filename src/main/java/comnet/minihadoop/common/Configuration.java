package comnet.minihadoop.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.IllegalArgumentException;

public class Configuration {
    private static Logger logger = LoggerFactory.getLogger("consoleLogger");
    private Configuration() {}
    public static Configuration getInstance() {
        return LazyHolder.INSTANCE;
    }


    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
    public void setEmpty() {
        this.data = new HashMap<>();
    }
    public void addEntry(String key, Object val) {
        synchronized (data){
            data.put(key, val);
        }
    }
    public <T> T get(String key, Class<T> clazz) {
        synchronized (data) {
            return clazz.cast(this.data.get(key));
        }
    }
    private static class LazyHolder {
        private static final Configuration INSTANCE = new Configuration();
    }
    private Map<String, Object> data;
}