package top.gerritchang.tools.pdfGenerate.exception;

public class OutOfColumnsLengthException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public OutOfColumnsLengthException(){
        super();
    }

    public OutOfColumnsLengthException(String msg) {
        super(msg);
    }

    public OutOfColumnsLengthException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public OutOfColumnsLengthException(Throwable cause) {
        super(cause);
    }
}
