package top.gerritchang.tools.pdfGenerate.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XMLUtils {

    public Element xmlToElement(InputStream inputStream) throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputStream);
        return document.getRootElement();
    }

    public InputStream readXmlInJars(String fileName) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("itext/" + fileName);
        return classPathResource.getInputStream();
    }

    public InputStream readXmlInIDE(String fileName) throws Exception {
        File file = ResourceUtils.getFile(fileName);
        return new FileInputStream(file);
    }

    public Resource[] getAllFilesFromResources() throws IOException {
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("itext/*.xml");
        return resources;
    }
}
