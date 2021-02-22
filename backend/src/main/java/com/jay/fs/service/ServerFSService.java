package com.jay.fs.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ServerFSService {
    boolean upload(InputStream inputStream, String path) throws IOException;

    boolean download(OutputStream outputStream, String path) throws IOException;

    boolean mkdir(String path);

    boolean delete(String path);
}
