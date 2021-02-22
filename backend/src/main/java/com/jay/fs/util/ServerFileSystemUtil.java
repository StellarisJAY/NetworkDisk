package com.jay.fs.util;

import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class ServerFileSystemUtil {
    @Value("${fileSystem.server.rootPath}")
    private static String rootPath = null;

    public boolean upload(byte[] fileBytes, String path) throws IOException {
        if(StringUtil.isNullOrEmpty(path) || StringUtil.isNullOrEmpty(rootPath)){
            return false;
        }
        File file = new File(rootPath + path);

        if(file.exists() || file.isDirectory()){
            return false;
        }

        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(fileBytes);
        outputStream.close();
        return true;
    }

    public boolean download(String path, OutputStream outputStream) throws IOException {
        if(StringUtil.isNullOrEmpty(path) || StringUtil.isNullOrEmpty(rootPath)){
            return false;
        }
        File file = new File(rootPath + "/" +  path);
        if(!file.exists() || file.isDirectory()){
            return false;
        }

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = fileInputStream.read(buffer, 0, 1024)) != -1){
            outputStream.write(buffer);
        }

        outputStream.close();
        fileInputStream.close();
        return true;
    }

    public boolean mkdir(String path){
        if(StringUtil.isNullOrEmpty(path) || StringUtil.isNullOrEmpty(rootPath)){
            return false;
        }
        File file = new File(path);
        if(file.isDirectory() || file.exists()){
            return false;
        }
        file.mkdirs();
        return true;
    }
}
