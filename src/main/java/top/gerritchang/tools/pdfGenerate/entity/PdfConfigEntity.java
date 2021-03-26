package top.gerritchang.tools.pdfGenerate.entity;

public class PdfConfigEntity {

    private int pageTotals;
    private String paperDirection;
    private int marginTop;
    private int marginBottom;
    private int marginLeft;
    private int marginRight;

    public int getPageTotals() {
        return pageTotals;
    }

    public void setPageTotals(int pageTotals) {
        this.pageTotals = pageTotals;
    }

    public String getPaperDirection() {
        return paperDirection;
    }

    public void setPaperDirection(String paperDirection) {
        this.paperDirection = paperDirection;
    }

    public int getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public int getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(int marginLeft) {
        this.marginLeft = marginLeft;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }
}
