package com.jay.fs.util;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

/**
 * 图片缩略图工具类，获取指定大小的缩略图文件
 * @author Jay
 * @version 1.0
 */
public class ThumbnailUtil {

    private static final int DEFAULT_WIDTH = 64;
    private static final int DEFAULT_HEIGHT = 64;

    /**
     * 将原有图片压缩成width x height大小的图片
     * @param src 原图片文件
     * @param dest 目标文件
     * @param width 宽
     * @param height 高
     * @throws IOException
     */
    public static void imageThumbnail(File src, File dest, int width, int height) throws IOException {
        Thumbnails.of(src).size(width, height).toFile(dest);
    }

    /**
     * 将原有图片压缩成默认宽高的缩略图
     * @param src
     * @param dest
     * @throws IOException
     */
    public static void imageThumbnail(File src, File dest) throws IOException{
        Thumbnails.of(src).size(DEFAULT_WIDTH, DEFAULT_HEIGHT).toFile(dest);
    }
}
