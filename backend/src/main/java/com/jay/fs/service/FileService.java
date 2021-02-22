package com.jay.fs.service;

import com.github.pagehelper.PageInfo;
import com.jay.fs.entity.FileEntity;
import com.jay.fs.vo.FileVo;
import org.springframework.amqp.core.Message;

import java.io.*;
import java.util.List;

public interface FileService {
    List<FileVo> getFilesInDirectory(Long directoryId, Long userId);

    String getHdfsPath(Long fileId);

    boolean uploadFile(FileEntity file, Long userId, Long path, InputStream inputStream) throws Exception;

    boolean download(Long fileId, Long userId, OutputStream outputStream);

    String getFilename(Long fileId);

    String getFileFullName(Long fileId);

    boolean deleteFile(Long fileId, Long userId) throws Exception;

    List<FileVo> searchFiles(Long userId, String input, String fileType);

    boolean downloadPackage(Long[] fileIds, Long userId, OutputStream downloadOS) throws IOException;

    void updateUsedSpace(Message message) throws UnsupportedEncodingException;

    PageInfo<FileVo> getAllDocuments(Long userId, int pageNum, int pageSize);

    PageInfo<FileVo> getAllMusics(Long userId, int pageNum, int pageSize);
}
