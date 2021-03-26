package top.gerritchang.tools.menu.controller;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class MenuController {

    @Value("${absolute.json.path}")
    private String JSON_PATH;

    @GetMapping("/queryMenuList")
    public List<Map> queryMenuList(String id){
        List<Map> result = new ArrayList<>();
        try {
            InputStream inputStream = new FileInputStream(new File(JSON_PATH + "Menu.json"));
            String text = IOUtils.toString(inputStream, "utf8");
            inputStream.close();
            List<Map> list = JSON.parseArray(text, Map.class);
            if ("".equals(id) || null == id) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).containsKey("_parentId") && "0".equals(list.get(i).get("_parentId"))) {
                        result.add(list.get(i));
                    }
                }
            }else{
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).containsKey("_parentId") && id.equals(list.get(i).get("_parentId"))) {
                        result.add(list.get(i));
                    }
                }
            }
        }catch (Exception e){
            return null;
        }
        return result;
    }

}
