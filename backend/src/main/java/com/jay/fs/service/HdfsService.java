package com.jay.fs.service;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public interface HdfsService {

    FileSystem getHdfs() throws IOException;

    boolean exists(String path) throws IOException;

    boolean download(Path path, OutputStream os) throws IOException;

    boolean upload(Path path, byte[] fileBytes) throws IOException;

    boolean delete(Path path, boolean isFolder) throws IOException;

    boolean mkDir(Path path) throws IOException;

    File downloadToFileSystem(Path path);
}
