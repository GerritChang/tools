package top.gerritchang.tools.mybatis

import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import top.gerritchang.tools.entity.UploadEntity

@Mapper
interface SaveDataMapper {
    /**
     * 把上传后的结果写入到数据库中
     * @param entity
     * @return
     */
    @Insert("insert into UploadFileInfo(fileId,fileName,fileSize,fileType,fileContent,businessId,creater,uploadTime," +
            "mongodbId,mongodbType,md5Value) values(#{fileId},#{fileName},#{fileSize},#{fileType},null,#{businessId}," +
            "#{creater},to_date(#{uploadTime},'yyyy-MM-dd HH24:mi:ss'),#{mongodbId},#{mongodbType},#{md5Value})")
    fun insertIntoDataBase(entity: UploadEntity?): Int

    /**
     * 根据业务主键和用户名查询文件列表
     * @param businessId
     * @param creater
     * @return
     */
    @Select("select * from UploadFileInfo where businssId=#{businssId} and creater=#{creater}")
    fun queryFileList(businessId: String?, creater: String?): List<UploadEntity?>?

    /**
     * 根据文件id删除文件
     * @param fileId
     * @return
     */
    @Delete("delete from UploadFileInfo where fileId=#{fileId}")
    fun deleteFileByFileId(fileId: String?): Int

    /**
     * 根据文件id查询文件信息
     * @param fileId
     * @return
     */
    @Select("select * from UploadFileInfo where fileId=#{fileId}")
    fun queryMongoIdAndTypeByFileId(fileId: String?): UploadEntity?
}
