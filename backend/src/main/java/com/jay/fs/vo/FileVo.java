package com.jay.fs.vo;

public class FileVo {
    private Long fileId;
    private String filename;
    private Long size;
    private String type;
    private int directory;
    private String updateDate;

    public FileVo() {
    }

    public FileVo(Long fileId, String filename, Long size, String type, int directory, String updateDate) {
        this.fileId = fileId;
        this.filename = filename;
        this.size = size;
        this.type = type;
        this.directory = directory;
        this.updateDate = updateDate;
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

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
