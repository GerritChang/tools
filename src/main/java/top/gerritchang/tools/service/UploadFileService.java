package top.gerritchang.tools.service;

import com.alibaba.druid.support.json.JSONUtils;
import top.gerritchang.tools.mybatis.DataManageService;
import top.gerritchang.tools.utils.HttpUtils;
import top.gerritchang.tools.utils.StaticVarUtils;
import top.gerritchang.tools.websocket.UploadWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

@Service
public class UploadFileService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UploadWebSocket webSocket;

    @Autowired
    private DataManageService dataManageService;

    @Value("${mongodb.url}")
    private String MongoDBURI;

    @Value("${spring.cache.type}")
    private String cacheType;

    /**
     * 把文件上传到MongoDB中，上传成功则将文件信息写入数据库
     * @param fileName
     * @param file
     * @param username
     * @param md5Value
     * @param count
     * @param businessId
     * @throws IOException
     */
    @Async
    public void uploadFile2MongoDb(String fileName, MultipartFile file, String username,
                                   String md5Value, int count, String businessId)
            throws IOException {
        InputStream inputStream = file.getInputStream();
        //根据“.”将文件名拆分
        String[] fileSuffixArr = fileName.split("\\.");
        //取最后一个点后面的字符作为文件后缀
        String fileSuffix = fileSuffixArr[fileSuffixArr.length - 1];
        //根据文件后缀选择MongoDB文件类型
        String mongoType = this.getFileTypeByFileName(fileSuffix);
        //组装MongoDB上传文件的url
        String URI = MongoDBURI + mongoType;
        //上传文件获取返回结果
        String result = HttpUtils.uploadFile(URI, inputStream, fileName);
        //将返回的结果处理成map
        Map map = (Map) JSONUtils.parse(result);
        //判断MD5值是否与本地计算的相同，相同则证明上传成功
        if (map.get("MD5").toString().equals(md5Value)) {
            // 把上传后的文件信息写入到数据库里面
//            dataManageService.insertIntoDataBase(file, fileSuffix, businessId, username, map.get("FileID").toString(),
//                    mongoType, md5Value);
            //根据不同的缓存类型把上传结果存储到不同的地方
            switch (cacheType) {
                //如果是Redis，则调用数据写入Redis的方法
                case "redis":
                    this.saveTempDataToRedis(username, result, count);
                    break;
                case "none":
                    //如果是none或者其他调用方法存入静态变量
                    this.saveTempDataToStaticVar(username, result, count);
                    break;
                default:
                    //如果是none或者其他调用方法存入静态变量
                    this.saveTempDataToRedis(username, result, count);
                    break;
            }
            webSocket.sendMessage();
        }
    }

    /**
     * 根据文件类型匹配MongoDB里的文件类型
     *
     * @param fileSuffix
     * @return
     */
    private String getFileTypeByFileName(String fileSuffix) {
        String FileType = "";
        if ("avi".equals(fileSuffix) || "mov".equals(fileSuffix) || "rmvb".equals(fileSuffix) || "rm".equals(fileSuffix) ||
                "flv".equals(fileSuffix) || "mp4".equals(fileSuffix) || "3gp".equals(fileSuffix) || "mpeg".equals(fileSuffix) ||
                "wmv".equals(fileSuffix) || "mpg".equals(fileSuffix)) {
            FileType = "Video";
        } else if ("bmp".equals(fileSuffix) || "gif".equals(fileSuffix) || "jpeg".equals(fileSuffix) || "jpg".equals(fileSuffix) ||
                "png".equals(fileSuffix) || "swf".equals(fileSuffix) || "svg".equals(fileSuffix)) {
            FileType = "Image";
        } else if ("wav".equals(fileSuffix) || "mp3".equals(fileSuffix) || "wma".equals(fileSuffix) || "ogg".equals(fileSuffix) ||
                "ape".equals(fileSuffix) || "acc".equals(fileSuffix)) {
            FileType = "Audio";
        } else {
            FileType = "Document";
        }
        return FileType;
    }

    /**
     * 把上传结果存入Redis里面
     * @param username
     * @param resultStr
     * @param count
     */
    private void saveTempDataToRedis(String username, String resultStr, Integer count) {
        //先存入本次上传文件的数量
        redisTemplate.opsForHash().put(username, "count", count);
        //把最新的文件上传信息存储进Redis
        redisTemplate.opsForHash().put(username, UUID.randomUUID().toString(), resultStr);
    }

    private void saveTempDataToStaticVar(String username, String resultStr, Integer count) {
        Map map = (Map) JSONUtils.parse(resultStr);
        StaticVarUtils.putNewValueToMap(username, map, count);
    }

}
