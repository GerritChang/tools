package top.gerritchang.tools.menu.service;

import com.alibaba.fastjson.JSON;
import top.gerritchang.tools.setup.entity.SubmitFormEntity;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SetupService {

    @Value("${absolute.html.path}")
    private String HTML_PATH;

    @Value("${absolute.java.path}")
    private String JAVA_PATH;

    @Value("${absolute.json.path}")
    private String JSON_PATH;

    public boolean saveSetup(SubmitFormEntity entity) {
        boolean flag = false;
        try {
            OutputStream htmlOutput = new FileOutputStream(new File(HTML_PATH + entity.getHtmlFileName() + ".html"));
            htmlOutput.write(entity.getHtmlTexts().getBytes());
            htmlOutput.flush();
            htmlOutput.close();
            String javaCodes = entity.getJavaCodes();
            writeJavaCode(javaCodes);
            if (!"".equals(entity.getSelectTreeNode()) && entity.getSelectTreeNode() != null) {
                if ("0".equals(entity.getSelectTreeNode())) {

                }
            }
            readJsonFile(entity);
        } catch (Exception e) {

        }
        return flag;
    }

    /**
     * 写入Java文件
     *
     * @param javaCodes
     * @throws Exception
     */
    private void writeJavaCode(String javaCodes) throws Exception {
        String regex = "^public class [a-zA-Z_ ]{1,}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(javaCodes);
        if (matcher.find()) {
            String line = matcher.group(0);
            String javaName = line
                    .replaceAll("public", "")
                    .replaceAll("class", "")
                    .replaceAll(" ", "")
                    .trim();
            OutputStream javaOutput = new FileOutputStream(new File(JAVA_PATH + javaName + ".java"));
            String packageName = "package com.example.demo.controller;\n";
            javaOutput.write(packageName.getBytes());
            javaOutput.write(javaCodes.getBytes());
            javaOutput.flush();
            javaOutput.close();
        }
    }

    /**
     * 更新json文件
     * @param entity
     * @throws Exception
     */
    private void readJsonFile(SubmitFormEntity entity) throws Exception {
        InputStream inputStream = new FileInputStream(new File(JSON_PATH + "Menu.json"));
        String text = IOUtils.toString(inputStream, "utf8");
        inputStream.close();
        List<Map> list = JSON.parseArray(text, Map.class);
        System.out.println(list);
        int max = 0;
        for (int i = 0; i < list.size(); i++) {
            int num = Integer.parseInt(list.get(i).get("id").toString());
            if (max < num) {
                max = num;
            }
        }
        Map map = new HashMap();
        map.put("text", entity.getNodeNames());
        map.put("id", max + 1 + "");
        map.put("url", "");
        map.put("_parentId",entity.getSelectTreeNode());
        list.add(map);
        System.out.println(list);
        OutputStream outputStream = new FileOutputStream(new File(JSON_PATH + "Menu.json"));
        outputStream.write(JSON.toJSON(list).toString().getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
