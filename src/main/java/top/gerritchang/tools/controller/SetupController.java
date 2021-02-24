package top.gerritchang.tools.controller;

import top.gerritchang.tools.entity.SubmitFormEntity;
import top.gerritchang.tools.service.SetupService;
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
