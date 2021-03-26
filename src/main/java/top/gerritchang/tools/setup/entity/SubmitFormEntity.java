package top.gerritchang.tools.setup.entity;

public class SubmitFormEntity {

    private String nodeNames;

    private String htmlFileName;

    private String htmlTexts;

    private String javaCodes;

    private String selectTreeNode;

    private String urlSite;

    public String getUrlSite() {
        return urlSite;
    }

    public void setUrlSite(String urlSite) {
        this.urlSite = urlSite;
    }

    public String getSelectTreeNode() {
        return selectTreeNode;
    }

    public void setSelectTreeNode(String selectTreeNode) {
        this.selectTreeNode = selectTreeNode;
    }

    public String getNodeNames() {
        return nodeNames;
    }

    public void setNodeNames(String nodeNames) {
        this.nodeNames = nodeNames;
    }

    public String getHtmlFileName() {
        return htmlFileName;
    }

    public void setHtmlFileName(String htmlFileName) {
        this.htmlFileName = htmlFileName;
    }

    public String getHtmlTexts() {
        return htmlTexts;
    }

    public void setHtmlTexts(String htmlTexts) {
        this.htmlTexts = htmlTexts;
    }

    public String getJavaCodes() {
        return javaCodes;
    }

    public void setJavaCodes(String javaCodes) {
        this.javaCodes = javaCodes;
    }
}
