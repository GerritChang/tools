package top.gerritchang.tools.vueComponents.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TaoBaoSearchController {

    @GetMapping("/taobao")
    public String taobaoSearch(){
        return "taobaoSearch";
    }
}
