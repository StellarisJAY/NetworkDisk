package com.jay.fs.service.impl;

import com.jay.fs.dao.PictureDAO;
import com.jay.fs.service.HdfsService;
import com.jay.fs.service.PictureService;
import com.jay.fs.util.ThumbnailUtil;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 从文件业务中抽取出的图片相关操作的业务逻辑实现类：
 * 1、图片缩略图获取
 * 2、图片预览
 * 3、etc
 * @author Jay
 * @version 1.0
 */
@Service
public class PictureServiceImpl implements PictureService {

    @Autowired
    private HdfsService hdfsService;
    @Autowired
    private PictureDAO pictureDAO;

    // 服务器文件系统临时目录
    @Value("${fileSystem.tempPath}")
    private String fileSystemTemp;

    private static final int THUMBNAIL_WIDTH = 64;
    private static final int THUMBNAIL_HEIGHT = 64;

    Logger logger = LoggerFactory.getLogger("PictureService");

    /**
     * 获取图片缩略图
     * @param fileId 图片的文件id
     * @param userId 用户id（用于检查图片归属权）
     * @param outputStream 下载流
     * @return
     */
    @Override
    public boolean getThumbnail(Long fileId, Long userId, OutputStream outputStream) {
        // 获取该文件的hdfs路径
        String hdfsPath = pictureDAO.getPictureHdfsPath(fileId, userId);
        if(StringUtils.isEmpty(hdfsPath)){
            return false;
        }

        try{
            // 从hdfs下载到服务器临时文件中
            File src = hdfsService.downloadToFileSystem(new Path(hdfsPath));

            File thumbnail = new File(fileSystemTemp + hdfsPath.substring(0, hdfsPath.lastIndexOf('.') - 1) + "(thumb)" + hdfsPath.substring(hdfsPath.lastIndexOf('.')));
            if(!thumbnail.exists()){
                thumbnail.createNewFile();
            }
            // 生成缩略图，存在临时文件thumbnail中
            ThumbnailUtil.imageThumbnail(src, thumbnail);
            // 读取临时文件，写入下载输出流
            FileInputStream inputStream = new FileInputStream(thumbnail);
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = inputStream.read(buffer, 0, 1024)) != -1){
                outputStream.write(buffer, 0, 1024);
            }
            inputStream.close();
            outputStream.close();
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
