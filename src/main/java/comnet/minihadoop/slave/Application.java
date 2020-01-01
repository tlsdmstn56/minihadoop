package comnet.minihadoop.slave;

import comnet.minihadoop.common.Configuration;
import comnet.minihadoop.master.MasterServer;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

public class Application {
    private static Logger logger = LoggerFactory.getLogger("main");

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("f", true, "path of slave-config yaml file");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse( options, args);
        }
        catch (ParseException e) {
            logger.error("Parsing Error");
            return;
        }
        String configFilePath = cmd.getOptionValue("f", "./config/slave-config.yaml");
        InputStream input;
        try {
            input = new FileInputStream(new File(configFilePath));
        } catch (FileNotFoundException e) {
            logger.error("Cannot find config file: "+configFilePath);
            return;
        }
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(input);
        Configuration.getInstance().setData(data);
        SlaveServer server = new SlaveServer(Configuration.getInstance());
        server.run();
    }
}
