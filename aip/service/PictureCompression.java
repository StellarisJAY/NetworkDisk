package com.jay.aipservice.service;

import ch.qos.logback.core.encoder.ByteArrayUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class PictureCompression {

    private Logger logger = LoggerFactory.getLogger("图片压缩");

    public byte[] compressPicture(byte[] picture, long destSize){
        if(picture == null) return null;
        byte[] compressed = picture;
        double accuracy = getAccuracy(picture.length);
        long srcSize = picture.length;
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(picture);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(picture.length);

            while(compressed.length > destSize){
                Thumbnails.of(inputStream).scale(accuracy).outputQuality(accuracy).toOutputStream(outputStream);

                compressed = outputStream.toByteArray();
            }
            logger.info("图片压缩完成：原始大小=" + srcSize + "B ; 最终大小=" + compressed.length + "B");
        } catch (Exception e) {
            logger.error("图片压缩失败，异常：", e);
        }
        return compressed;
    }

    private static double getAccuracy(long size) {
        double accuracy;
        if (size < 900) {
            accuracy = 0.85;
        } else if (size < 2047) {
            accuracy = 0.6;
        } else if (size < 3275) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }
}
