package top.gerritchang.tools.setup.controller;

import top.gerritchang.tools.setup.entity.SubmitFormEntity;
import top.gerritchang.tools.menu.service.SetupService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class SetupController {

    @Resource
    private SetupService setupService;

    @PostMapping("/submitForm")
    @ResponseBody
    public void submitForm(SubmitFormEntity entity){
        boolean flag = setupService.saveSetup(entity);
        System.out.println(flag);
    }
}
