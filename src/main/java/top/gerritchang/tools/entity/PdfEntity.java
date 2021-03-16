package top.gerritchang.tools.entity;

import java.util.List;
import java.util.Map;

public class PdfEntity {
    //文档标题
    private String title;
    //设置列数
    private int colums;
    //设置列宽比例
    private float[] columnWidths;
    //设置标题下内容名称
    private String[] littleTitleList;
    //设置标题下内容名称对应的参数名称
    private String[] littletitleParam;
    //传入参数值，根据参数名称取参数值
    private Map littleTitleContent;
    //设置标题下内容的rowspan
    private int[] littleTitleRowSpan;
    //设置标题下内容的colspan
    private int[] littleTitleColSpan;
    //设置表格的表头
    private String[] tableTitle;
    //设置表头对应的字段名称
    private String[] tableParam;
    //设置表格内容的样式，“center”居中，“left”居左，“right”居右
    private String[] tableAlignStyle;
    //设置表头的rowspan
    private int[] tableTitleRowSpan;
    //设置表头的colspan
    private int[] tableTitleColSpan;
    //表格里的内容
    private List<Map> list;
    //设置表格下的内容名称
    private String[] bottomTitleList;
    //设置表格下的内容参数名称
    private String[] bottomParamList;
    //表格下的内容
    private Map bottomContent;
    //设置表格下内容的rowspan
    private int[] bottomRowSpan;
    //设置表格下内容的colspan
    private int[] bottomColSpan;
    //设置表格下的内容是否带边框
    private int[] bottomBorder;
    //文件所在的目录路径
    private String filePath;
    //文件名称
    private String fileName;
    //文件的全路径
    private String fullFilePath;
    //文档的方向，1 纸张横向 0 纸张纵向
    private int PaperDirection;
    //内容到纸张的左边距
    private int marginLeft;
    //内容到纸张的右边距
    private int marginRight;
    //内容到纸张的上边距
    private int marginTop;
    //内容到纸张的下边距
    private int marginBottom;


    public int[] getBottomBorder() {
        return bottomBorder;
    }

    public void setBottomBorder(int[] bottomBorder) {
        this.bottomBorder = bottomBorder;
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

    public int getPaperDirection() {
        return PaperDirection;
    }

    public void setPaperDirection(int paperDirection) {
        PaperDirection = paperDirection;
    }

    public String getFullFilePath() {
        return fullFilePath;
    }

    public void setFullFilePath(String fullFilePath) {
        this.fullFilePath = fullFilePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColums() {
        return colums;
    }

    public void setColums(int colums) {
        this.colums = colums;
    }

    public float[] getColumnWidths() {
        return columnWidths;
    }

    public void setColumnWidths(float[] columnWidths) {
        this.columnWidths = columnWidths;
    }

    public String[] getLittleTitleList() {
        return littleTitleList;
    }

    public void setLittleTitleList(String[] littleTitleList) {
        this.littleTitleList = littleTitleList;
    }

    public String[] getLittletitleParam() {
        return littletitleParam;
    }

    public void setLittletitleParam(String[] littletitleParam) {
        this.littletitleParam = littletitleParam;
    }

    public Map getLittleTitleContent() {
        return littleTitleContent;
    }

    public void setLittleTitleContent(Map littleTitleContent) {
        this.littleTitleContent = littleTitleContent;
    }

    public int[] getLittleTitleRowSpan() {
        return littleTitleRowSpan;
    }

    public void setLittleTitleRowSpan(int[] littleTitleRowSpan) {
        this.littleTitleRowSpan = littleTitleRowSpan;
    }

    public int[] getLittleTitleColSpan() {
        return littleTitleColSpan;
    }

    public void setLittleTitleColSpan(int[] littleTitleColSpan) {
        this.littleTitleColSpan = littleTitleColSpan;
    }

    public String[] getTableTitle() {
        return tableTitle;
    }

    public void setTableTitle(String[] tableTitle) {
        this.tableTitle = tableTitle;
    }

    public String[] getTableParam() {
        return tableParam;
    }

    public void setTableParam(String[] tableParam) {
        this.tableParam = tableParam;
    }

    public String[] getTableAlignStyle() {
        return tableAlignStyle;
    }

    public void setTableAlignStyle(String[] tableAlignStyle) {
        this.tableAlignStyle = tableAlignStyle;
    }

    public int[] getTableTitleRowSpan() {
        return tableTitleRowSpan;
    }

    public void setTableTitleRowSpan(int[] tableTitleRowSpan) {
        this.tableTitleRowSpan = tableTitleRowSpan;
    }

    public int[] getTableTitleColSpan() {
        return tableTitleColSpan;
    }

    public void setTableTitleColSpan(int[] tableTitleColSpan) {
        this.tableTitleColSpan = tableTitleColSpan;
    }

    public List<Map> getList() {
        return list;
    }

    public void setList(List<Map> list) {
        this.list = list;
    }

    public String[] getBottomTitleList() {
        return bottomTitleList;
    }

    public void setBottomTitleList(String[] bottomTitleList) {
        this.bottomTitleList = bottomTitleList;
    }

    public String[] getBottomParamList() {
        return bottomParamList;
    }

    public void setBottomParamList(String[] bottomParamList) {
        this.bottomParamList = bottomParamList;
    }

    public Map getBottomContent() {
        return bottomContent;
    }

    public void setBottomContent(Map bottomContent) {
        this.bottomContent = bottomContent;
    }

    public int[] getBottomRowSpan() {
        return bottomRowSpan;
    }

    public void setBottomRowSpan(int[] bottomRowSpan) {
        this.bottomRowSpan = bottomRowSpan;
    }

    public int[] getBottomColSpan() {
        return bottomColSpan;
    }

    public void setBottomColSpan(int[] bottomColSpan) {
        this.bottomColSpan = bottomColSpan;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
