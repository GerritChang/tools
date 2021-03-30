package top.gerritchang.tools.exportExcel.controller;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import top.gerritchang.tools.exportExcel.service.ExportExcelHandlerService;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class ExportExcelController {

    @Resource
    private ExportExcelHandlerService exportExcelHandlerService;

    @GetMapping("/export")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) {
        String fileName = request.getParameter("fileName");
        OutputStream output = null;
        try {
            output = response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xlsx");
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            SXSSFWorkbook workbook = exportExcelHandlerService.handlerExcel("sheetName");
            workbook.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
