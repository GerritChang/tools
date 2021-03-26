package top.gerritchang.tools.upload.entity;

import java.sql.Blob;

public class UploadEntity {

    //文件id
    private String fileId;
    //文件名
    private String fileName;
    //文件大小
    private long fileSize;
    //文件后缀类型
    private String fileType;
    //文件
    private Blob fileContent;
    //文件关联业务主键
    private String businessId;
    //文件的创建者
    private String creater;
    //文件的上传时间
    private String uploadTime;
    //上传到MongoDB的id
    private String mongodbId;
    //MongoDB的文件类型
    private String mongodbType;
    //文件的MD5校验值
    private String md5Value;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Blob getFileContent() {
        return fileContent;
    }

    public void setFileContent(Blob fileContent) {
        this.fileContent = fileContent;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getMongodbId() {
        return mongodbId;
    }

    public void setMongodbId(String mongodbId) {
        this.mongodbId = mongodbId;
    }

    public String getMongodbType() {
        return mongodbType;
    }

    public void setMongodbType(String mongodbType) {
        this.mongodbType = mongodbType;
    }

    public String getMd5Value() {
        return md5Value;
    }

    public void setMd5Value(String md5Value) {
        this.md5Value = md5Value;
    }

    @Override
    public String toString() {
        return "UploadEntity{" +
                "fileId='" + fileId + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", fileType='" + fileType + '\'' +
                ", fileContent=" + fileContent +
                ", businessId='" + businessId + '\'' +
                ", creater='" + creater + '\'' +
                ", uploadTime='" + uploadTime + '\'' +
                ", mongodbId='" + mongodbId + '\'' +
                ", mongodbType='" + mongodbType + '\'' +
                ", md5Value='" + md5Value + '\'' +
                '}';
    }
}
