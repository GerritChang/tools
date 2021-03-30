package top.gerritchang.tools.importOffice.utils;

import top.gerritchang.tools.importOffice.entity.ImportEntity;
import top.gerritchang.tools.importOffice.exception.ColumnIsNullException;
import top.gerritchang.tools.importOffice.exception.MissingColumnsException;
import top.gerritchang.tools.importOffice.exception.ValueContainsSpecialCharException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class ImportExcel2007Util {

    public static List<Map> importExcel(MultipartFile file, List<ImportEntity> list, List notNullColumns
            , String status) throws MissingColumnsException, ValueContainsSpecialCharException, ColumnIsNullException {
        List<Map> result = new ArrayList<>();
        try {
            //将上传的文件转化成字节流读取
            InputStream inputStream = file.getInputStream();
            //使用HSSFWorkBook读取Excel文件
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            //获取第一个sheet页
            XSSFSheet sheet = workbook.getSheetAt(0);
            //获得当前sheet的结束行
            int lastRowNum = sheet.getLastRowNum();
            //获取所有的列
            XSSFRow hssfRow = sheet.getRow(0);
            int colNumber = hssfRow.getPhysicalNumberOfCells();
            //将需要存的字段所在的列号记录到一个int数组里
            List<ImportEntity> listmap = new ArrayList();
            Set<String> set = new HashSet<>();
            //记录表头所在的行号
            int titleRowNumber = 0;
            for (int j = 0; j < colNumber; j++) {
                XSSFRow row = sheet.getRow(0);
                XSSFCell cell = row.getCell(j);
                String columnName = cell.getStringCellValue();
                ImportEntity entity = recordSheetColumnNumber(list, columnName, j);
                if (entity != null) {
                    set.add(entity.getColumnName());
                    if (set.size() > listmap.size()) {
                        listmap.add(entity);
                    }
                }
            }
            if (listmap != null && listmap.size() > 0) {
                titleRowNumber = 0;
                checkColumnsInListOrNot(list, listmap);
            }
            //开始获取结果集
            int dataRowNumber = Math.addExact(titleRowNumber, 1);
            for (int i = dataRowNumber; i <= lastRowNum; i++) {
                XSSFRow row = sheet.getRow(i);
                Map m = transColumnsToDbColumns(row, listmap);
                StringBuffer stringBuffer = new StringBuffer();
                m.forEach((key, value) -> {
                    stringBuffer.append(value);
                });
                if (StringUtils.isNotBlank(stringBuffer.toString())) {
                    Map map = setValueToMap(m, notNullColumns, status);
                    result.add(map);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<String> getSheetColumns(MultipartFile file){
        List<String> result = new ArrayList<>();
        try {
            InputStream inputStream = file.getInputStream();
            //使用HSSFWorkBook读取Excel文件
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            //获取第一个sheet页
            XSSFSheet sheet = workbook.getSheetAt(0);
            //获取所有的列
            XSSFRow hssfRow = sheet.getRow(0);
            int colNumber = hssfRow.getPhysicalNumberOfCells();
            for (int j = 0; j < colNumber; j++) {
                XSSFRow row = sheet.getRow(0);
                XSSFCell cell = row.getCell(j);
                String columnName = cell.getStringCellValue();
                result.add(columnName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 记录标题所在的列
     *
     * @param list
     * @param columnName
     * @param number
     * @return
     */
    private static ImportEntity recordSheetColumnNumber(List<ImportEntity> list,
                                                        String columnName, int number) {
        ImportEntity result = null;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getColumnName().equals(columnName)) {
                result = list.get(i);
                result.setColumnNumber(number);
            }
        }
        return result;
    }

    /**
     * 判断是否缺少列
     *
     * @param oldList
     * @param newList
     * @return
     * @throws MissingColumnsException
     */
    private static boolean checkColumnsInListOrNot(List<ImportEntity> oldList, List<ImportEntity> newList)
            throws MissingColumnsException {
        if (oldList.size() == newList.size()) {
            return true;
        } else {
            throw new MissingColumnsException("缺少必要的列");
        }
    }

    /**
     * 获取Excel表格里的结果集
     *
     * @param map
     * @return
     * @throws ColumnIsNullException
     */
    private static Map setValueToMap(Map map, List notNullColumn, String status)
            throws ColumnIsNullException {
        boolean flag = true;
        if (notNullColumn != null && !notNullColumn.isEmpty()) {
            flag = checkColumnsIsNull(notNullColumn, map, status);
        }
        if (flag) {
            return map;
        } else {
            throw new ColumnIsNullException();
        }
    }

    /**
     * 将Excel里的结果集转换成key value
     *
     * @param row
     * @param list
     * @return
     * @throws ValueContainsSpecialCharException
     */
    private static Map transColumnsToDbColumns(XSSFRow row, List<ImportEntity> list)
            throws ValueContainsSpecialCharException {
        Map map = new HashMap();
        for (int j = 0; j < list.size(); j++) {
            int columnNumber = list.get(j).getColumnNumber();
            String DBColumnName = list.get(j).getDbColumnName();
            XSSFCell cell = row.getCell(columnNumber);
            String cellValue = "";
            if (!"".equals(cell) && cell != null) {
                switch (cell.getCellType()) {
                    case XSSFCell.CELL_TYPE_NUMERIC: // 数字
                        if (DateUtil.isCellDateFormatted(cell)) {
                            Date date = cell.getDateCellValue();
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            cellValue = format.format(date);
                        } else {
                            cellValue = cell.getNumericCellValue() + "";
                        }
                        break;

                    case XSSFCell.CELL_TYPE_STRING: // 字符串
                        cellValue = cell.getStringCellValue();
                        break;

                    case XSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                        cellValue = cell.getBooleanCellValue() + "";
                        break;

                    case XSSFCell.CELL_TYPE_FORMULA: // 公式
                        cellValue = cell.getCellFormula() + "";
                        break;

                    case XSSFCell.CELL_TYPE_BLANK: // 空值
                        cellValue = "";
                        break;

                    case XSSFCell.CELL_TYPE_ERROR: // 故障
                        throw new ValueContainsSpecialCharException("内容中包含非法字符，不被支持");
                    default:
                        throw new ValueContainsSpecialCharException("内容中类型未知，不被支持");
                }
            }
            map.put(DBColumnName, cellValue);
        }
        return map;
    }

    /**
     * 校验列是否不能为空
     *
     * @param notNullColumn
     * @param map
     * @param status
     * @return
     * @throws ColumnIsNullException
     */
    private static boolean checkColumnsIsNull(List notNullColumn, Map map, String status)
            throws ColumnIsNullException {
        boolean flag = true;
        if ("all".equals(status)) {
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < notNullColumn.size(); i++) {
                stringBuffer.append(notNullColumn.get(i)).append("、");
                String string = map.get(notNullColumn.get(i)).toString();
                if ("".equals(string) || string == null) {
                    flag = false;
                }
            }
            String columns = stringBuffer.substring(0, stringBuffer.length() - 1);
            if (!flag) {
                throw new ColumnIsNullException(columns + "不能为空，请检查数据后重新导入");
            }
        } else if ("one".equals(status)) {
            List checkList = new ArrayList();
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < notNullColumn.size(); i++) {
                stringBuffer.append(notNullColumn.get(i)).append("、");
                String string = map.get(notNullColumn.get(i)).toString();
                if (!"".equals(string) && string != null) {
                    checkList.add(string);
                }
            }
            String columns = stringBuffer.substring(0, stringBuffer.length() - 1);
            if (checkList.size() < 1) {
                throw new ColumnIsNullException(columns + "至少有一列不能为空");
            }
        }
        return flag;
    }
}
