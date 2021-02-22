package com.jay.fs.service;

public interface FolderService {
    boolean insertFolder(String folderName, Long path, Long userId) throws Exception;

    boolean deleteFolder(Long fileId, Long userId) throws Exception;
}
