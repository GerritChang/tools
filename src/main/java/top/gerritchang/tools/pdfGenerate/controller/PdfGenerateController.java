package top.gerritchang.tools.pdfGenerate.controller;

import org.dom4j.Element;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.gerritchang.tools.pdfGenerate.service.GeneratePdfService;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class PdfGenerateController {

    @Resource
    private GeneratePdfService generatePdfService;

    @RequestMapping("/pdf")
    public void test(){
        List<Element> list = generatePdfService.getAllElements("");
        generatePdfService.elementForeach(list);
    }
}
