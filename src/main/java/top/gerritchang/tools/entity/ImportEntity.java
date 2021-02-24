package top.gerritchang.tools.entity;

public class ImportEntity {

    private String ColumnName;
    private int ColumnNumber;
    private String DbColumnName;

    public String getColumnName() {
        return ColumnName;
    }

    public void setColumnName(String columnName) {
        ColumnName = columnName;
    }

    public int getColumnNumber() {
        return ColumnNumber;
    }

    public void setColumnNumber(int columnNumber) {
        ColumnNumber = columnNumber;
    }

    public String getDbColumnName() {
        return DbColumnName;
    }

    public void setDbColumnName(String dbColumnName) {
        DbColumnName = dbColumnName;
    }

    @Override
    public String toString() {
        return "ImportEntity{" +
                "ColumnName='" + ColumnName + '\'' +
                ", ColumnNumber=" + ColumnNumber +
                ", DbColumnName='" + DbColumnName + '\'' +
                '}';
    }
}
