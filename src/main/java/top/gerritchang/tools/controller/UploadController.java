package top.gerritchang.tools.controller;

import com.alibaba.druid.support.json.JSONUtils;
import top.gerritchang.tools.mybatis.DataManageService;
import top.gerritchang.tools.service.UploadFileService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
public class UploadController {

    @Value("${mongodb.download.url}")
    private String downloadPrefixUri;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private DataManageService dataManageService;

    /**
     * 这是上传文件的接口
     *
     * @param file
     * @param param
     * @return
     */
    @PostMapping("/uploadFile")
    public Map uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("param") String param) {
        Map resultMap = new HashMap();
        //将上传文件时携带的参数转换成所需List集合
        List<Map> list = (List<Map>) JSONUtils.parse(param);
        //调用校验和上传方法
        boolean flag = this.checkAndUpload(file, list);
        //返回上传结果
        resultMap.put("status", flag);
        return resultMap;
    }

    @GetMapping("/queryFileList")
    public List<Map> queryFileList(@RequestParam("businssId") String businssId,
                                   @RequestParam("username") String username) {
        //调用根据用户名和业务主键查询文件列表的方法
        List<Map> resultList = dataManageService.queryFileList(username, businssId);
        return resultList;
    }

    @GetMapping("/getFileDownloadUri")
    public String getFileDownloadUri(@RequestParam("id") String fileId) {
        //通过传入文件id，组合成从MongoDB中下载文件的URL
        return dataManageService.queryMongoUriByFileId(downloadPrefixUri, fileId);
    }

    @DeleteMapping("/deleteFileByFileId")
    public Map deleteFileByFileId(@RequestParam("id") String fileId) {
        //根据文件id删除文件
        boolean flag = dataManageService.deleteFileByFileId(fileId);
        Map resultMap = new HashMap();
        if (flag) {
            resultMap.put("status", true);
            resultMap.put("message", "删除成功");
        } else {
            resultMap.put("status", false);
            resultMap.put("message", "删除失败");
        }
        return resultMap;
    }

    private boolean checkAndUpload(MultipartFile file, List<Map> list) {
        try {
            InputStream inputStream = file.getInputStream();
            //获取文件名
            String fileName = file.getOriginalFilename();
            //使用迭代器遍历上传参数
            Iterator<Map> iterator = list.iterator();
            while (iterator.hasNext()) {
                Map map = iterator.next();
                //从上传的参数列表中找到上传的文件
                if (fileName.equals(map.get("filename").toString())) {
                    //计算文件的MD5值
                    String md5Value = DigestUtils.md5Hex(inputStream);
                    //判断上传文件的MD5值是否与前端计算的一致
                    if (md5Value.equals(map.get("md5value").toString())) {
                        //若一致则上传到MongoDB中
                        uploadFileService.uploadFile2MongoDb(fileName, file, map.get("username").toString(),
                                md5Value, list.size(), map.get("businessId").toString());
                        return true;
                    } else {
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
