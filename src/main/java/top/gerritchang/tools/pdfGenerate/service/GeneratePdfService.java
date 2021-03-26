package top.gerritchang.tools.pdfGenerate.service;

import org.dom4j.Element;
import org.springframework.stereotype.Service;
import top.gerritchang.tools.pdfGenerate.entity.PdfConfigEntity;
import top.gerritchang.tools.pdfGenerate.entity.TableAttributesEntity;
import top.gerritchang.tools.pdfGenerate.utils.XMLUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service
public class GeneratePdfService {

    public List<Element> getAllElements(String xmlFileName) {
        if (xmlFileName == null || "".equals(xmlFileName)) {
            xmlFileName = "tableDemoConfig.xml";
        }
        List<Element> list = new ArrayList<>();
        try {
            XMLUtils xmlUtils = new XMLUtils();
            InputStream inputStream = xmlUtils.readXmlInJars(xmlFileName);
            Element root = xmlUtils.xmlToElement(inputStream);
            Element child = root.element("gerritchang");
            Element grandChild = child.element("itext");
            list = grandChild.elements();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Element elementForeach(List<Element> list) {
        Iterator<Element> iterator = list.iterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();
            Element temp = element.element("pageTotals");
            if (temp != null) {
                this.configurationForeach(element);
            } else {
                this.contentForeach(element);
            }
        }
        return null;
    }

    private PdfConfigEntity configurationForeach(Element element) {
        int pageTotals = Integer.parseInt(element.elementText("pageTotals"));
        String paperDirection = element.elementText("paperDirection");
        Element paperStyleElement = element.element("paperStyle");
        String marginTop = paperStyleElement.attribute("marginTop").getValue();
        int marginTopValue = !"".equals(marginTop) && marginTop != null ? Integer.parseInt(marginTop) : 10;
        String marginBottom = paperStyleElement.attribute("marginBottom").getValue();
        int marginBottomValue = !"".equals(marginBottom) && marginBottom != null ? Integer.parseInt(marginBottom) : 10;
        String marginLeft = paperStyleElement.attribute("marginLeft").getValue();
        int marginLeftValue = !"".equals(marginLeft) && marginLeft != null ? Integer.parseInt(marginLeft) : 10;
        String marginRight = paperStyleElement.attribute("marginRight").getValue();
        int marginRightValue = !"".equals(marginRight) && marginRight != null ? Integer.parseInt(marginRight) : 10;
        PdfConfigEntity entity = new PdfConfigEntity();
        entity.setPageTotals(pageTotals);
        entity.setPaperDirection(paperDirection);
        entity.setMarginTop(marginTopValue);
        entity.setMarginBottom(marginBottomValue);
        entity.setMarginLeft(marginLeftValue);
        entity.setMarginRight(marginRightValue);
        return entity;
    }

    private TableAttributesEntity contentForeach(Element element) {
        Element table = element.element("config").element("table");
        String id = table.attributeValue("id");
        int rows = Integer.parseInt(table.attributeValue("rows"));
        int columns = Integer.parseInt(table.attributeValue("columns"));
        double percentage = Double.parseDouble(table.attributeValue("percentage"));
        TableAttributesEntity entity = new TableAttributesEntity();
        entity.setId(id);
        entity.setColumns(columns);
        entity.setRows(rows);
        entity.setPercentage(percentage);
        return entity;
    }

    private void tableForeach(Element element) {
        Iterator<Element> iterator = element.elementIterator("tr");
        while (iterator.hasNext()) {
            Element tr = iterator.next();
            String border = tr.attributeValue("border");
            if (border != null && !"".equals(border)) {
                int borderWidth = Integer.parseInt(border);
                String borderColor = tr.attributeValue("borderColor");
                boolean foreach = Boolean.parseBoolean(tr.attributeValue("foreach"));
                if (foreach){

                }
            }
        }
    }
}
