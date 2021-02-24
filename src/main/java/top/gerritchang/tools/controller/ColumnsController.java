package top.gerritchang.tools.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ColumnsController {

    @PostMapping("/generateCol")
    public Map generateCol(String SQL){
        String[] arr = SQL.split("from");
        return null;
    }
}
