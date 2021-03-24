package com.jay.fs.service.impl;

import cn.hutool.core.util.ZipUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jay.fs.dao.FileDAO;
import com.jay.fs.entity.FileEntity;
import com.jay.fs.service.*;
import com.jay.fs.util.aip.Base64Util;
import com.jay.fs.vo.FileVo;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileDAO fileDAO = null;
    @Autowired
    private HdfsService hdfsService = null;
    @Autowired
    private UserService userService = null;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private ServerFSService serverFSService = null;
    @Autowired
    private AIPService aipService = null;

    /*
        服务器文件系统临时文件夹，用于批量下载等业务
     */
    @Value("${fileSystem.tempPath}")
    private String fileSystemTempPath;
    /*
        服务器文件系统根目录
     */
    @Value("${fileSystem.server.rootPath}")
    private String sfsRootPath;

    Logger logger = LoggerFactory.getLogger("FileService");
    @Override
    public List<FileVo> getFilesInDirectory(Long directoryId, Long userId) {
        return fileDAO.getFileVosInDirectory(directoryId, userId);
    }

    /**
     * 获取文件在hdfs的地址
     * @param fileId
     * @return
     */
    @Override
    @Cacheable(cacheNames = "redisCache", key = "#fileId+'_hdfs_path'")
    public String getHdfsPath(Long fileId) {
        return fileDAO.getHdfsPath(fileId);
    }


    /**
     * 上传文件
     * @param file   文件数据库实体
     * @param userId 用户id
     * @param path   上传路径
     * @param inputStream 上传文件输入流
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean uploadFile(FileEntity file, Long userId, Long path, InputStream inputStream) throws Exception {
        if(file == null || userId == null || path == null || inputStream == null)return false;
        // 检查用户剩余空间大小
        long maxSpace = userService.getMaxSpace(userId);
        long usedSpace = userService.getUsedSpace(userId);
        if(file.getSize() + usedSpace > maxSpace) return false;


        // 生成hdfs保存路径
        String hdfsPath = generateHdfsPath(path, userId, file.getFilename());
        if(hdfsPath == null) return false;


        // 装配数据库实体
        file.setHdfsPath(hdfsPath);
        file.setUploader(userId);

        // 向数据库写入新文件信息
        fileDAO.insertFile(file);
        fileDAO.insertUserFile(userId, file.getFileId(), path);

        /*
            将文件的大小发送到消息队列，监听程序接收到后求和并更新用户表
            避免批量上传的高并发场景下数据库死锁
         */
        rabbitTemplate.convertAndSend("DbExchange.user", "userInfoKey", userId + ":" + file.getSize());

        // 获取文件的字节数组
        byte[] fileBytes = getFileBytes(inputStream);
        /*
            将图像文件发送到消息队列，由图像识别服务异步完成图片分类
         */
        if(isImage(file) == true){
            // 获取图像的base64字符串
            String imageParam = getImageParam(fileBytes);
            // 图像识别服务
            aipService.imageRec(file.getFileId(), imageParam);
        }

        // 上传到hdfs集群
        boolean status = hdfsService.upload(new Path(file.getHdfsPath()), fileBytes);
        if(status == false){
            throw new RuntimeException();
        }
        return true;
    }

    /**
     * 从消息队列获取需要更新的用户空间大小
     * @param message
     */
    @Override
    @RabbitListener(queues = "DbQueue.user")
    public void updateUsedSpace(Message message) {
        String messageBody = new String(message.getBody());
        Long userId = Long.valueOf(messageBody.substring(1, messageBody.indexOf(':')));
        Long size = Long.valueOf(messageBody.substring(messageBody.indexOf(':') + 1, messageBody.length() - 1));
        userService.increaseUsedSpace(userId, size);
    }

    /**
     * 下载文件
     * @param fileId  文件id
     * @param userId  用户id（用于检查文件所属）
     * @param outputStream 下载输出流
     * @return
     */
    @Override
    public boolean download(Long fileId, Long userId, OutputStream outputStream) {
        String hdfsPath = fileDAO.getHdfsPath(fileId);
        try {
            return hdfsService.download(new Path(hdfsPath), outputStream);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String getFilename(Long fileId) {
        return fileDAO.getFilename(fileId);
    }

    @Override
    public String getFileFullName(Long fileId) {
        return fileDAO.getFileFullName(fileId);
    }

    /**
     * 生成hdfs路径字符串
     * @param path
     * @param userId
     * @param filename
     * @return
     */
    // /cloud/{userId}/.../filename
    @Transactional(rollbackFor = RuntimeException.class)
    protected String generateHdfsPath(Long path, Long userId, String filename){
        String hdfsPath = "/cloud/"+userId+"/";
        if(path == 1L){
            return hdfsPath + filename;
        }
        String temp = fileDAO.getHdfsPath(path);
        if(temp != null){
            return temp + "/" + filename;
        }
        return null;
    }

    /**
     * 删除文件
     * @param fileId
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFile(Long fileId, Long userId) throws Exception {
        String hdfsPath = fileDAO.getHdfsPath(fileId);
        if(hdfsPath==null) return false;

        Long size = fileDAO.getFileSize(fileId);
        // 数据库中删除记录
        int s1 = fileDAO.deleteUserFile(fileId, userId);
        int s2 = fileDAO.deleteFile(fileId);
        // 减少已用空间大小
        userService.decreaseUsedSpace(userId, size);
        // hdfs删除文件
        boolean hdfsStatus = hdfsService.delete(new Path(hdfsPath), false);
        if(hdfsStatus == false){
            throw new RuntimeException();
        }
        return true;
    }

    @Override
    public List<FileVo> searchFiles(Long userId, String input, String fileType) {
        input = "%"+input+"%";
        if(fileType.equals("documents")){
            return fileDAO.searchDocuments(input, userId);
        } else if (fileType.equals("pictures")) {
            return fileDAO.searchPictures(input, userId);
        } else if (fileType.equals("musics")) {
            return fileDAO.searchMusics(input, userId);
        }
        return fileDAO.searchFiles(input, userId);
    }

    /**
     * 判断文件是否是图片类型
     * @param fileEntity
     * @return
     */
    private boolean isImage(FileEntity fileEntity){
        String type = fileEntity.getType();
        if(type.equalsIgnoreCase("PNG") || type.equalsIgnoreCase("JPG")
                || type.equalsIgnoreCase("JPEG")){
            return true;
        }
        return false;
    }

    /**
     * 获取文件的字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    private byte[] getFileBytes(InputStream inputStream) throws IOException{
        ByteArrayOutputStream byteOS = new ByteArrayOutputStream();

        int len = 0;
        byte[] buffer = new byte[1024];
        while((len = inputStream.read(buffer, 0, 1024)) != -1){
            byteOS.write(buffer);
        }
        return byteOS.toByteArray();
    }

    /**
     * 转化字节数组为图片BASE64字符串
     * @param imageBytes
     * @return
     * @throws IOException
     */
    private String getImageParam(byte[] imageBytes) throws IOException{
        String imgStr = Base64Util.encode(imageBytes);
        String imgParam = URLEncoder.encode(imgStr, "UTF-8");
        return imgParam;
    }

    /**
     * 打包下载所选的文件：
     * 1、首先从数据库获取这些文件的hdfs路径，然后将它们从hdfs下载到服务器临时文件夹
     * 2、创建目标压缩文件，使用ZipUtil的方法将这些临时文件压缩到目标文件中
     * 3、将最终压缩文件写入下载流，删除临时文件
     * @param fileIds 所选的文件id数组
     * @param userId 用户id，用于检查文件归属权
     * @param downloadOS 下载流
     * @return
     * @throws IOException
     */
    @Override
    public boolean downloadPackage(Long[] fileIds, Long userId, OutputStream downloadOS) throws IOException {
        // 从数据库获取所有要下载文件的路径
        List<String> paths = new ArrayList<>();
        for(int i = 0; i < fileIds.length; i++){
            paths.add(fileDAO.getHdfsPath(fileIds[i]));
        }

        /*
            将所有文件从hdfs转移到服务器文件系统准备打包
         */
        File[] zippingFiles = new File[paths.size()];
        int index = 0;
        for(String path : paths) {
            File tempFile = hdfsService.downloadToFileSystem(new Path(path));
            if (tempFile != null) {
                zippingFiles[index] = tempFile;
                index++;
            }
        }
        /*
            创建zip文件，将所有文件压缩
         */
        File zipFile = new File(fileSystemTempPath + "/[批量下载]" + System.currentTimeMillis() + ".zip");
        ZipUtil.zip(zipFile, false, zippingFiles);

        // 将压缩包写入下载输出流
        FileInputStream inputStream = new FileInputStream(zipFile);
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = inputStream.read(buffer, 0, 1024)) != -1){
            downloadOS.write(buffer);
        }
        downloadOS.close();
        inputStream.close();

        /*
            删除临时文件
         */
        zipFile.delete();
        for(File file : zippingFiles){
            file.delete();
        }
        return true;
    }

    @Override
    public PageInfo<FileVo> getAllDocuments(Long userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FileVo> result = fileDAO.getAllDocuments(userId);
        PageInfo<FileVo> pageInfo = new PageInfo<>(result);

        return pageInfo;
    }

    @Override
    public PageInfo<FileVo> getAllMusics(Long userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<FileVo> result = fileDAO.getAllMusics(userId);
        PageInfo<FileVo> pageInfo = new PageInfo<>(result);

        return pageInfo;
    }
}
