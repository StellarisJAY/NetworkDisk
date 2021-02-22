package com.jay.fs.service;

import java.io.OutputStream;

public interface PictureService {

    boolean getThumbnail(Long fileId, Long userId, OutputStream outputStream);
}
