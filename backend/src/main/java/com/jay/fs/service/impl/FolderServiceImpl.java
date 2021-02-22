package com.jay.fs.service.impl;

import com.jay.fs.dao.FileDAO;
import com.jay.fs.entity.FileEntity;
import com.jay.fs.service.FolderService;
import com.jay.fs.service.HdfsService;
import com.jay.fs.service.UserService;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {
    @Autowired
    private HdfsService hdfsService = null;
    @Autowired
    private FileDAO fileDAO = null;
    @Autowired
    private UserService userService = null;

    Logger logger = LoggerFactory.getLogger("FolderService");

    /**
     * 新建文件夹
     * @param folderName
     * @param path
     * @param userId
     * @return
     * @throws RuntimeException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertFolder(String folderName, Long path, Long userId) throws Exception{
        // 生成hdfs的文件路径
        String hdfsPath = generateHdfsPath(folderName, path, userId);
        if(hdfsPath == null) return false;
        // 封装数据库实体
        FileEntity fileEntity = new FileEntity(null, folderName, 0L, "directory", 1, hdfsPath, LocalDateTime.now().toString(), userId, null, sfsPath);

        // 插入到文件表
        fileDAO.insertFile(fileEntity);
        // 插入用户文件关系
        fileDAO.insertUserFile(userId, fileEntity.getFileId(), path);
        // 在hdfs创建文件夹
        boolean hdfsStatus = hdfsService.mkDir(new Path(fileEntity.getHdfsPath()));
        // 创建失败，抛出RuntimeException，回滚事务
        if(hdfsStatus == false) {
            throw new RuntimeException();
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFolder(Long fileId, Long userId) throws Exception{
        // 1. delete user_file of the content
        // 2. delete user_file of this folder
        // 3. delete tb_file of this folder
        // 4. delete hdfs path
        String hdfsPath = fileDAO.getHdfsPath(fileId);
        if(hdfsPath == null) return false;
        // 删除数据库记录
        boolean s1 = deleteFolderInDB(fileId, userId);
        boolean s2 = hdfsService.delete(new Path(hdfsPath), true);
        // hdfs删除失败，回滚
        if(s2 == false){
            throw new RuntimeException();
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    protected boolean deleteFolderInDB(Long fileId, Long userId) throws Exception{
        // 获取该文件夹下的所有文件
        List<FileEntity> files = fileDAO.getFilesInDirectory(fileId, userId);
        if(files != null){
            // 遍历删除这些文件
            for(FileEntity file : files){
                // 如果是文件夹就调用删除文件夹的方法
                if(file.getDirectory() == 1){
                    deleteFolder(file.getFileId(), userId);
                }
                else{
                    // 普通文件，直接删除
                    fileDAO.deleteUserFile(file.getFileId(), userId);
                    fileDAO.deleteFile(file.getFileId());
                    //userService.decreaseUsedSpace(userId, file.getSize());
                }
            }
        }
        // 内容删除完成，删除自己
        fileDAO.deleteUserFile(fileId, userId);
        fileDAO.deleteFile(fileId);
        return true;
    }

    /**
     * 生成hdfs文件路径
     * @param folderName
     * @param path
     * @param userId
     * @return
     */
    @Transactional
    protected String generateHdfsPath(String folderName, Long path, Long userId){
        // 根目录
        if(path == 1){
            return "/cloud/"+userId+"/" + folderName;
        }
        // 非根目录
        String temp = fileDAO.getHdfsPath(path);
        return temp != null ? temp + "/" + folderName : null;
    }
}
