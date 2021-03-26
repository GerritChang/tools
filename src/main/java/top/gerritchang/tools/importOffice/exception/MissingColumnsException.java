package top.gerritchang.tools.importOffice.exception;

public class MissingColumnsException extends Exception {

    private static final long serialVersionUID = 1L;

    public MissingColumnsException(){
        super();
    }

    public MissingColumnsException(String msg) {
        super(msg);
    }

    public MissingColumnsException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public MissingColumnsException(Throwable cause) {
        super(cause);
    }
}
