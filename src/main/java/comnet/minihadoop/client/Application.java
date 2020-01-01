package comnet.minihadoop.client;

import comnet.minihadoop.common.Configuration;
import comnet.minihadoop.common.exception.InitialMasterConnectionError;
import comnet.minihadoop.common.exception.WrongIDPWException;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Application {
    private static Logger logger = LoggerFactory.getLogger("consoleLogger");
    private static Connection conn;
    private static String ID, PW;

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("p","port", true, "port to connect");
        options.addOption("m","masteraddress", true, "master address");
        options.addOption("h","help", false, "print help");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        Configuration.getInstance().setEmpty();
        try {
            cmd = parser.parse( options, args);
            if (cmd.hasOption("h")) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp( "client [options]", options );
                return;
            }
            String[] masterAddress = cmd.getOptionValue( "m", "localhost").split(":");
            Configuration.getInstance().addEntry("master-hostname",
                    masterAddress[0]);
            Configuration.getInstance().addEntry("master-port",
                    Integer.parseInt(masterAddress[1]));
            Configuration.getInstance().addEntry("port",
                    Integer.parseInt(cmd.getOptionValue( "p", "25000")));
        }
        catch (ParseException e) {
            System.out.println("ERROR: invalid option: "+e.getMessage());
            return;
        }
        getLoginInfoFromUser();
        Configuration.getInstance().addEntry("ID", ID);
        Configuration.getInstance().addEntry("PW", PW);
        try{
            conn = new Connection(Configuration.getInstance());
        } catch(WrongIDPWException e) {
            System.out.println("ERROR: authentication error: "+e.getMessage());
            return;
        } catch(InitialMasterConnectionError e) {
            System.out.println("ERROR: connection error: "+e.getMessage());
            return;
        }

        Thread thread = new Thread(conn);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            logger.info("Interrupted");
        }
    }
    static void getLoginInfoFromUser() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter ID: ");
        ID = scan.next();
        System.out.print("Enter password: ");
        PW = scan.next();
    }
}
