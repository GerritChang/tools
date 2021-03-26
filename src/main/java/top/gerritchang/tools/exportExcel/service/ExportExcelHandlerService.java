package top.gerritchang.tools.exportExcel.service;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Service
public class ExportExcelHandlerService {

    public OutputStream handlerExcel(HttpServletResponse response){
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(200);
        sxssfWorkbook.setCompressTempFiles(true);
        XSSFCellStyle style = (XSSFCellStyle) sxssfWorkbook.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        return null;
    }
}
