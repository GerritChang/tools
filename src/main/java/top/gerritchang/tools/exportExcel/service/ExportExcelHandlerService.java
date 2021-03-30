package top.gerritchang.tools.exportExcel.service;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.gerritchang.tools.exportExcel.entity.DataGrid;
import top.gerritchang.tools.exportExcel.mybatis.StreamQueryMapper;
import top.gerritchang.tools.exportExcel.utils.ExportExcel2007;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExportExcelHandlerService {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public SXSSFWorkbook handlerExcel(String sheetName) {
        ExportExcel2007 exportExcel2007 = new ExportExcel2007();
        SXSSFWorkbook sxssfWorkbook = exportExcel2007.initWorkBook();
        Sheet sheet = sxssfWorkbook.createSheet(sheetName);
        exportExcel2007.initHeader(sheet, 10, initDataGrid());
        //exportExcel2007.handlerTitleMerge(initDataGrid(),sheet,sxssfWorkbook);
        StreamQueryMapper streamQueryMapper = sqlSessionTemplate.getMapper(StreamQueryMapper.class);
        final int[] rowIndex = {1};
        streamQueryMapper.streamQuery(new ResultHandler<Map>() {
            @Override
            public void handleResult(ResultContext<? extends Map> resultContext) {
                Map rowMap = resultContext.getResultObject();
                exportExcel2007.appendRow(sxssfWorkbook, sheet, rowIndex[0], rowMap, new String[10], new int[10], new short[10]);
                rowIndex[0] += 1;
            }
        });
        return sxssfWorkbook;
    }

    private List<DataGrid> initDataGrid() {
        List list = new ArrayList();
        list.add(new DataGrid(0, 0, 0, 0, null, null));
        list.add(new DataGrid(0, 0, 0, 0, null, null));
        list.add(new DataGrid(0, 0, 0, 0, null, null));
        list.add(new DataGrid(0, 0, 0, 0, null, null));
        list.add(new DataGrid(0, 0, 0, 0, null, null));
        list.add(new DataGrid(0, 0, 0, 0, null, null));
        return list;
    }
}
