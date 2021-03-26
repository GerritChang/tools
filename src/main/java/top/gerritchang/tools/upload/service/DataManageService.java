package top.gerritchang.tools.upload.service;

import top.gerritchang.tools.upload.entity.UploadEntity;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.gerritchang.tools.upload.mybatis.SaveDataMapper;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DataManageService {

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    /**
     * 把文件信息写入到数据库
     * @param file
     * @param fileType
     * @param businessId
     * @param creater
     * @param mongoId
     * @param mongoType
     * @param md5Value
     * @return
     */
    public boolean insertIntoDataBase(MultipartFile file, String fileType, String businessId, String creater,
                                      String mongoId, String mongoType, String md5Value) {
        SaveDataMapper saveDataMapper = sqlSessionTemplate.getMapper(SaveDataMapper.class);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = format.format(new Date());
        UploadEntity entity = new UploadEntity();
        entity.setFileId(UUID.randomUUID().toString());
        entity.setFileName(file.getOriginalFilename());
        entity.setFileType(fileType);
        entity.setFileSize(file.getSize());
        entity.setBusinessId(businessId);
        entity.setCreater(creater);
        entity.setMongodbId(mongoId);
        entity.setMongodbType(mongoType);
        entity.setMd5Value(md5Value);
        entity.setUploadTime(currentTime);
        return saveDataMapper.insertIntoDataBase(entity) > 0 ? true : false;
    }

    /**
     * 根据文件id删除文件
     * @param fileId
     * @return
     */
    public boolean deleteFileByFileId(String fileId) {
        SaveDataMapper saveDataMapper = sqlSessionTemplate.getMapper(SaveDataMapper.class);
        return saveDataMapper.deleteFileByFileId(fileId) > 0 ? true : false;
    }

    /**
     * 根据文件ID处理成从MongoDB中下载文件的url
     * @param prefixUri MongoDB的url前缀
     * @param fileId 文件id
     * @return
     */
    public String queryMongoUriByFileId(String prefixUri, String fileId) {
        UploadEntity entity = this.queryMongoIdAndTypeByFileId(fileId);
        String MongoId = entity.getMongodbId();
        String MongoType = entity.getMongodbType();
        return prefixUri + MongoType + "&FileID=" + MongoId;
    }

    /**
     * 根据业务主键和用户名查询已上传的文件列表
     * @param username
     * @param businessId
     * @return
     */
    public List<Map> queryFileList(String username, String businessId) {
        List<UploadEntity> list = this.queryFilesFromDataBase(username, businessId);
        //使用迭代器遍历查询后的数据集
        Iterator<UploadEntity> iterator = list.iterator();
        List<Map> resultList = new ArrayList<>();
        while (iterator.hasNext()) {
            UploadEntity entity = iterator.next();
            //组合成前端需要的数据格式
            Map map = new HashMap();
            map.put("name", entity.getFileName());
            map.put("id", entity.getFileId());
            resultList.add(map);
        }
        return resultList;
    }

    private UploadEntity queryMongoIdAndTypeByFileId(String fileId) {
        SaveDataMapper saveDataMapper = sqlSessionTemplate.getMapper(SaveDataMapper.class);
        return saveDataMapper.queryMongoIdAndTypeByFileId(fileId);
    }

    private List<UploadEntity> queryFilesFromDataBase(String username, String businessId) {
        SaveDataMapper saveDataMapper = sqlSessionTemplate.getMapper(SaveDataMapper.class);
        return saveDataMapper.queryFileList(businessId, username);
    }

}
