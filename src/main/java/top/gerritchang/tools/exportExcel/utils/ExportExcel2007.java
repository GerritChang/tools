package top.gerritchang.tools.exportExcel.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import top.gerritchang.tools.exportExcel.entity.DataGrid;
import top.gerritchang.tools.exportExcel.exception.DataTableListNullException;

import java.util.List;
import java.util.Map;

public class ExportExcel2007 extends AbstractExportExcel {

    @Override
    public SXSSFWorkbook exportMultiHeaderExcel(String sheetName,
                                       int headerRowTotal, int headerColTotal,
                                       List<Map> dataList, List<DataGrid> list) {
        SXSSFWorkbook sxssfWorkbook = initWorkBook();
        Sheet sheet = sxssfWorkbook.createSheet(sheetName);
        XSSFCellStyle style = (XSSFCellStyle) sxssfWorkbook.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        this.initHeader(sheet, headerRowTotal, headerColTotal);
        this.handlerTitleMerge(list, sheet, sxssfWorkbook);
        return null;
    }

    @Override
    public SXSSFWorkbook exportSingleHeaderExcel(String sheetName,
                                        int headerRowTotal, int headerColTotal,
                                        List<Map> dataList, List<DataGrid> list) {
        SXSSFWorkbook sxssfWorkbook = initWorkBook();
        Sheet sheet = sxssfWorkbook.createSheet(sheetName);
        XSSFCellStyle style = (XSSFCellStyle) sxssfWorkbook.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setBorderRight(XSSFCellStyle.BORDER_THIN);
        this.initHeader(sheet, headerColTotal, list);
        return null;
    }

    @Override
    public SXSSFWorkbook initWorkBook() {
        SXSSFWorkbook sxssfWorkbook = new SXSSFWorkbook(200);
        sxssfWorkbook.setCompressTempFiles(true);
        return sxssfWorkbook;
    }

    @Override
    public void initHeader(Sheet sheet, int headerRowTotal, int headerColTotal) {
        for (int i = 1; i < headerRowTotal; i++) {
            Row row = sheet.createRow(i);
            for (int j = 1; j < headerColTotal; j++) {
                row.createCell(j);
            }
        }
    }

    @Override
    public void initHeader(Sheet sheet, int headerColTotal, List<DataGrid> list) {
        Row row = sheet.createRow(1);
        for (int j = 1; j < headerColTotal; j++) {
            Cell cell = row.createCell(j);
            cell.setCellType(Cell.CELL_TYPE_STRING);
            cell.setCellValue(list.get(j - 1).getCellValue());
        }
    }

    @Override
    public void handlerTitleMerge(List<DataGrid> list, Sheet sheet,
                                  SXSSFWorkbook sxssfWorkbook) {
        if (list != null && list.size() > 0) {
            for (DataGrid dataGrid : list) {
                this.mergeRegion(sheet, sxssfWorkbook, dataGrid.getCellValue(),
                        dataGrid.getCoordinate_y(),
                        dataGrid.getCoordinate_y() + dataGrid.getRowspan(),
                        dataGrid.getCoordinate_x(),
                        dataGrid.getCoordinate_x() + dataGrid.getColspan());
            }
        } else {
            throw new DataTableListNullException("表头数据为空，请先设置表头数据");
        }
    }

    @Override
    public void appendRow(SXSSFWorkbook sxssfWorkbook, Sheet sheet, int rowIndex, Map rowMap,
                          String[] dbColumns, int[] valueType, short[] textAlign) {
        Row row = sheet.createRow(rowIndex);
        for (int i = 1; i <= dbColumns.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellType(valueType[i]);
            XSSFCellStyle style = (XSSFCellStyle) sxssfWorkbook.createCellStyle();
            style.setAlignment(textAlign[i]);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            cell.setCellValue(rowMap.get(dbColumns[i]).toString());
        }
    }

    private void mergeRegion(Sheet sheet, SXSSFWorkbook sxssfWorkbook, String value,
                             int startRow, int endRow, int startColumn, int endColumn) {
        CellRangeAddress cellRangeAddress = new CellRangeAddress(startRow, endRow, startColumn, endColumn);
        sheet.addMergedRegion(cellRangeAddress);
        Row row = sheet.getRow(startRow);
        Cell cell = CellUtil.getCell(row, startColumn);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(value);
        XSSFCellStyle style = (XSSFCellStyle) sxssfWorkbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        cell.setCellStyle(style);
        RegionUtil.setBorderTop(XSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet, sxssfWorkbook);
        RegionUtil.setBorderBottom(XSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet, sxssfWorkbook);
        RegionUtil.setBorderLeft(XSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet, sxssfWorkbook);
        RegionUtil.setBorderRight(XSSFCellStyle.BORDER_THIN, cellRangeAddress, sheet, sxssfWorkbook);
    }

}
