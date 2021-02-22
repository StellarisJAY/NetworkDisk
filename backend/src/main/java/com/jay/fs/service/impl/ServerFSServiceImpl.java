package com.jay.fs.service.impl;

import com.jay.fs.dao.FileDAO;
import com.jay.fs.dao.UserDAO;
import com.jay.fs.service.ServerFSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ServerFSServiceImpl implements ServerFSService {
    @Autowired
    private FileDAO fileDAO;
    @Autowired
    private UserDAO userDAO;

    @Override
    public boolean upload(InputStream inputStream, String path) throws IOException{
        File file = new File(path);
        if(!file.exists()){
            throw new FileNotFoundException();
        }

        FileOutputStream outputStream = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int length = 0;
        while((length = inputStream.read(buffer, 0, 1024)) != -1){
            outputStream.write(buffer);
        }
        inputStream.close();
        outputStream.close();
        return true;
    }

    @Override
    public boolean download(OutputStream outputStream, String path) throws IOException{
        File file = new File(path);
        if(!file.exists()){
            throw new FileNotFoundException();
        }
        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int length = 0;

        while((length = inputStream.read(buffer, 0, 1024)) != -1){
            outputStream.write(buffer);
        }
        inputStream.close();
        outputStream.close();
        return true;
    }

    @Override
    public boolean mkdir(String path) {
        File file = new File(path);
        if(file.exists() || file.isDirectory()){
            return false;
        }
        boolean status = file.mkdirs();
        return false;
    }

    @Override
    public boolean delete(String path) {
        File file = new File(path);
        if(!file.exists() || file.isDirectory()){
            return false;
        }

        boolean status = file.delete();
        return status;
    }
}
