package top.gerritchang.tools.pdfGenerate.entity;

public class TableTrEntity {

    private int border;
    private String borderColor;
    private String foreachType;
    private Object data;

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public String getForeachType() {
        return foreachType;
    }

    public void setForeachType(String foreachType) {
        this.foreachType = foreachType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
