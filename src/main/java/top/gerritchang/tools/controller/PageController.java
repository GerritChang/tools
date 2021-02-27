package top.gerritchang.tools.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    private final String INDEX = "index";
    private final String SQLGENERATE = "sqlGenerate";
    private final String MYBATISSQLGENERATE = "mybatisInsertUpdate";
    private final String DRAGGABLE = "draggable";
    private final String SETUPMENU = "setupMenu";
    private final String COLUMNS = "columns";
    private final String UPLOAD = "upload";
    private final String WEBSOCKET = "websocketTest";
    private final String DOWNLOAD = "download";
    private final String IMPORT = "importExcel";
    private final String FOREACH = "foreach";

    @RequestMapping("/")
    public String toIndex(){
        return INDEX;
    }

    @RequestMapping("/index")
    public String getIndex(){
        return INDEX;
    }

    @RequestMapping("/sqlGenerate")
    public String sqlGenerate(){
        return SQLGENERATE;
    }

    @RequestMapping("/mybatisSqlGenerate")
    public String mybatisSqlGenerate(){
        return MYBATISSQLGENERATE;
    }

    @RequestMapping("/draggable")
    public String getDRAGGABLE(){
        return DRAGGABLE;
    }

    @GetMapping("/setupMenu")
    public String setupMenu(){
        return SETUPMENU;
    }

    @GetMapping("/columns")
    public String columns(){
        return COLUMNS;
    }

    @GetMapping("/upload")
    public String upload(){
        return UPLOAD;
    }

    @GetMapping("/websocket")
    public String websocket(){
        return WEBSOCKET;
    }

    @GetMapping("/downloadPage")
    public String download(){
        return DOWNLOAD;
    }

    @GetMapping("/import")
    public String getImport(){
        return IMPORT;
    }

    @GetMapping("/foreach")
    public String getForeach(){
        return FOREACH;
    }
}
