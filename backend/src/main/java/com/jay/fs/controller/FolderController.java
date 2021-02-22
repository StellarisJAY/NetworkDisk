package com.jay.fs.controller;

import com.jay.fs.common.CommonResult;
import com.jay.fs.service.FolderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/folder")
@CrossOrigin
public class FolderController {
    @Autowired
    private FolderService folderService = null;

    private Logger logger = LoggerFactory.getLogger("FolderOpts");
    /**
     * 新建文件夹接口
     * @param folderName
     * @param path
     * @return
     */
    @PostMapping
    public CommonResult createFolder(@RequestParam("folderName") String folderName, @RequestParam("path") Long path){
        // 获取用户id
        Long userId = 1L;
        try{
            boolean status = folderService.insertFolder(folderName, path, userId);
            return new CommonResult(status==true?"创建成功":"创建失败：hdfs路径错误").add("status", status);
        }catch(Exception e){
            logger.error("创建文件夹失败:    " + e.getMessage());
            e.printStackTrace();
            return new CommonResult("创建失败，出现异常").add("status", false);
        }
    }

    @DeleteMapping("/{fileId}")
    public CommonResult deleteFolder(@PathVariable("fileId") Long fileId){
        Long userId = 1L;
        try{
            boolean status = folderService.deleteFolder(fileId, userId);
            return new CommonResult(status == true ? "删除成功" : "删除失败：hdfs路径错误").add("status", status);
        }
        catch(Exception e){
            logger.error("删除文件夹失败:    " + e.getMessage());
            e.printStackTrace();
            return new CommonResult("删除失败，出现异常").add("status", false);
        }
    }
}
