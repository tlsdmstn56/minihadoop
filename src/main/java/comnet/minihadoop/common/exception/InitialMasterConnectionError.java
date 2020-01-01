package comnet.minihadoop.common.exception;

public class InitialMasterConnectionError extends Exception {
    public InitialMasterConnectionError() {
        super();
    }
    public InitialMasterConnectionError(String msg) {
        super(msg);
    }
    public InitialMasterConnectionError(Throwable e) {
        super(e);
    }
}
