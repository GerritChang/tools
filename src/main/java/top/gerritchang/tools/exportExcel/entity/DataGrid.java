package top.gerritchang.tools.exportExcel.entity;

public class DataGrid {
    //单元格开始的坐标
    private int coordinate_x;
    private int coordinate_y;
    //单元格占的行数
    private int rowspan;
    //单元格占的列数
    private int colspan;
    //单元格值位置
    private String textAlign;
    //单元格内容
    private String cellValue;

    public DataGrid(int coordinate_x, int coordinate_y, int rowspan, int colspan, String textAlign, String cellValue) {
        this.coordinate_x = coordinate_x;
        this.coordinate_y = coordinate_y;
        this.rowspan = rowspan;
        this.colspan = colspan;
        this.textAlign = textAlign;
        this.cellValue = cellValue;
    }

    public int getCoordinate_x() {
        return coordinate_x;
    }

    public void setCoordinate_x(int coordinate_x) {
        this.coordinate_x = coordinate_x;
    }

    public int getCoordinate_y() {
        return coordinate_y;
    }

    public void setCoordinate_y(int coordinate_y) {
        this.coordinate_y = coordinate_y;
    }

    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    public String getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
    }

    public String getCellValue() {
        return cellValue;
    }

    public void setCellValue(String cellValue) {
        this.cellValue = cellValue;
    }

    @Override
    public String toString() {
        return "DataTable{" +
                "coordinate_x=" + coordinate_x +
                ", coordinate_y=" + coordinate_y +
                ", rowspan=" + rowspan +
                ", colspan=" + colspan +
                ", textAlign='" + textAlign + '\'' +
                ", cellValue='" + cellValue + '\'' +
                '}';
    }
}
