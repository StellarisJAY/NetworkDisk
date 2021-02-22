package com.jay.fs.entity;

import java.io.Serializable;

public class FileEntity implements Serializable {
    private static final long serialVersionUID = 6463317258550968812L;

    private Long fileId;
    private String filename;
    private Long size;
    private String type;
    private int directory;
    private String hdfsPath;
    private String updateDate;
    private Long uploader;
    private String imageTags;
    private String sfsPath;

    public FileEntity() {
    }

    public FileEntity(Long fileId, String filename, Long size, String type, int directory, String hdfsPath, String updateDate, Long uploader, String imageTags, String sfsPath) {
        this.fileId = fileId;
        this.filename = filename;
        this.size = size;
        this.type = type;
        this.directory = directory;
        this.hdfsPath = hdfsPath;
        this.updateDate = updateDate;
        this.uploader = uploader;
        this.imageTags = imageTags;
        this.sfsPath = sfsPath;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDirectory() {
        return directory;
    }

    public void setDirectory(int directory) {
        this.directory = directory;
    }

    public String getHdfsPath() {
        return hdfsPath;
    }

    public void setHdfsPath(String hdfsPath) {
        this.hdfsPath = hdfsPath;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUploader() {
        return uploader;
    }

    public void setUploader(Long uploader) {
        this.uploader = uploader;
    }

    public String getImageTags() {
        return imageTags;
    }

    public void setImageTags(String imageTags) {
        this.imageTags = imageTags;
    }

    public String getSfsPath() {
        return sfsPath;
    }

    public void setSfsPath(String sfsPath) {
        this.sfsPath = sfsPath;
    }
}
