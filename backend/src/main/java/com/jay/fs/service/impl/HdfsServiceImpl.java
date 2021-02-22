package com.jay.fs.service.impl;

import com.jay.fs.service.HdfsService;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;

@Service
public class HdfsServiceImpl implements HdfsService {

    @Value("${fileSystem.hdfs.namenode}")
    private String NAME_NODE;

    @Value("${fileSystem.tempPath}")
    private String fileSystemTempPath;

    private FileSystem hdfs = null;
    @Override
    public FileSystem getHdfs() throws IOException {
        if(this.hdfs != null) return hdfs;
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", NAME_NODE);

        FileSystem fs = FileSystem.get(conf);
        this.hdfs = fs;
        return this.hdfs;
    }

    /**
     * 判断文件是否存在
     * @param path
     * @return
     * @throws IOException
     */
    @Override
    public boolean exists(String path) throws IOException {
        FileSystem hdfs = this.getHdfs();
        if(hdfs.exists(new Path(path))){
            return true;
        }
        return false;
    }

    /**
     * 从hdfs下载文件
     * @param path        文件路径
     * @param os          下载输出流
     * @return
     * @throws IOException
     */
    @Override
    public boolean download(Path path, OutputStream os) throws IOException {
        FileSystem hdfs = getHdfs();
        if(!hdfs.exists(path)) return false;

        FSDataInputStream inputStream = hdfs.open(path); // 打开hdfs文件输入流

        // 读取文件，并写入下载输出流
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = inputStream.read(buffer, 0, 1024)) != -1){
            os.write(buffer, 0, len);
        }
        os.close();
        inputStream.close();
        return true;
    }

    /**
     * 上传文件到hdfs
     * @param path         上传路径
     * @return
     * @throws IOException
     */
    @Override
    public boolean upload(Path path, byte[] fileBytes) throws IOException {
        FileSystem hdfs = getHdfs();
        if(hdfs.exists(path)) return false;       // 判断路径是否已经被占用

        FSDataOutputStream outputStream = hdfs.create(path); // 打开hdfs文件输出流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileBytes); // 文件字节输入流
        // 读取文件上传输入流，写入到hdfs文件输出流
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = inputStream.read(buffer, 0, 1024)) != -1){
            outputStream.write(buffer, 0, len);
        }

        outputStream.close();
        inputStream.close();
        return true;
    }

    @Override
    @Transactional
    public boolean delete(Path path, boolean isFolder) throws IOException {
        FileSystem hdfs = getHdfs();
        return hdfs.delete(path, isFolder);
    }

    @Override
    public boolean mkDir(Path path) throws IOException{
        FileSystem hdfs = getHdfs();
        if(!hdfs.exists(path)){
            return hdfs.mkdirs(path);
        }
        return false;
    }

    @Override
    public File downloadToFileSystem(Path path) {
        FileOutputStream fileOutputStream = null;
        FSDataInputStream inputStream = null;
        try{
            FileSystem hdfs = getHdfs();
            // 创建文件对象
            File file = new File(fileSystemTempPath + path.toString());
            // 检查父文件夹是否存在，不存在就创建
            File parent = file.getParentFile();
            if(!parent.exists()){
                parent.mkdirs();
            }
            // 打开文件输出流和hdfs输入流
            fileOutputStream = new FileOutputStream(file);
            inputStream = hdfs.open(path);

            // 从hdfs写入文件
            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = inputStream.read(buffer, 0, 1024)) != -1){
                fileOutputStream.write(buffer);
            }
            inputStream.close();
            fileOutputStream.close();
            // 最终返回文件对象在
            return file;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
