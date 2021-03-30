package top.gerritchang.tools.exportExcel.exception;

public class DataTableListNullException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataTableListNullException(){
        super();
    }

    public DataTableListNullException(String msg) {
        super(msg);
    }

    public DataTableListNullException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public DataTableListNullException(Throwable cause) {
        super(cause);
    }
}
