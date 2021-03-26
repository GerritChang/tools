package top.gerritchang.tools.importOffice.controller;

import top.gerritchang.tools.importOffice.utils.ImportOffice2003Util;
import top.gerritchang.tools.importOffice.utils.ImportOffice2007Util;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ImportController {

    @RequestMapping("importExcel")
    public List<Map> importExcel(@RequestParam("wenjian") MultipartFile file) {
        List<String> result = new ArrayList<>();
        String fileName = file.getOriginalFilename();
        //裁剪出文件后缀名
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if ("xls".equals(suffix.toLowerCase())) {
            //2003版的Excel调用2003版的导入方法
            result = ImportOffice2003Util.getSheetColumns(file);
        } else if ("xlsx".equals(suffix.toLowerCase())) {
            //2007版的Excel调用2007版的导入方法
            result = ImportOffice2007Util.getSheetColumns(file);
        }
        List<Map> list = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            Map resultMap = new HashMap();
            resultMap.put("ID",result.get(i));
            resultMap.put("TEXT",result.get(i));
            list.add(resultMap);
        }
        return list;
    }
}
