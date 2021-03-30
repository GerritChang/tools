package top.gerritchang.tools.exportExcel.utils;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import top.gerritchang.tools.exportExcel.entity.DataGrid;

import java.util.List;
import java.util.Map;

public abstract class AbstractExportExcel {

    public abstract SXSSFWorkbook exportMultiHeaderExcel(String sheetName,
                                                int headerRowTotal, int headerColTotal,
                                     List<Map> dataList, List<DataGrid> list);

    public abstract SXSSFWorkbook exportSingleHeaderExcel(String sheetName,
                                                 int headerRowTotal, int headerColTotal,
                                                List<Map> dataList, List<DataGrid> list);

    public abstract SXSSFWorkbook initWorkBook();

    public abstract void initHeader(Sheet sheet, int headerRowTotal, int headerColTotal);

    public abstract void initHeader(Sheet sheet, int headerColTotal, List<DataGrid> list);

    public abstract void handlerTitleMerge(List<DataGrid> list, Sheet sheet,
                                  SXSSFWorkbook sxssfWorkbook);

    public abstract void appendRow(SXSSFWorkbook sxssfWorkbook, Sheet sheet, int rowIndex, Map rowMap,
                          String[] dbColumns, int[] valueType, short[] textAlign);
}
