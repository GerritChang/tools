package top.gerritchang.tools.importOffice.exception;

public class ValueContainsSpecialCharException extends Exception {

    private static final long serialVersionUID = 1L;

    public ValueContainsSpecialCharException(){
        super();
    }

    public ValueContainsSpecialCharException(String msg) {
        super(msg);
    }

    public ValueContainsSpecialCharException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ValueContainsSpecialCharException(Throwable cause) {
        super(cause);
    }

}
