package com.jay.fs.controller;

import com.jay.fs.service.PictureService;
import com.jay.fs.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/picture")
@CrossOrigin
public class PictureController {

    @Autowired
    private PictureService pictureService;

    Logger logger = LoggerFactory.getLogger("PictureController");

    /**
     * 获取图片的缩略图
     * @param fileId 图片id
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/thumbnail/{fileId}")
    public boolean getThumbnail(@PathVariable("fileId") Long fileId, HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader("token");
        Long userId = TokenUtil.getUserId(token);
        try {
            response.setContentType("application/blob");
            boolean status = pictureService.getThumbnail(fileId, userId, response.getOutputStream());
            return status;
        } catch (IOException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}
