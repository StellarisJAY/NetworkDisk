package com.jay.fs.controller;


import com.github.pagehelper.PageInfo;
import com.jay.fs.common.CommonResult;
import com.jay.fs.entity.FileEntity;
import com.jay.fs.service.FileService;
import com.jay.fs.util.TokenUtil;
import com.jay.fs.vo.FileVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {
    @Autowired
    @Qualifier(value = "fileServiceImpl")
    private FileService fileService = null;

    private Logger logger = LoggerFactory.getLogger("fileOpts");
    @PostMapping("/upload/{path}")
    public CommonResult upload(@RequestParam("file") MultipartFile file, @PathVariable("path") Long path, HttpServletRequest request){
        Long userId = TokenUtil.getUserId(request.getHeader("token"));
        try{
            FileEntity fileEntity = multiPartToFileEntity(file);
            boolean status = fileService.uploadFile(fileEntity, userId, path, file.getInputStream());
            return new CommonResult(status==true?"上传成功":"上传失败").add("status", status);
        } catch (Exception e) {
            logger.error("上传文件失败---出现异常：    " + e.getMessage());
            return new CommonResult("上传出现异常").add("status", false);
        }

    }


    @GetMapping("/download/{fileId}")
    public CommonResult download(@PathVariable("fileId") Long fileId, HttpServletResponse response, HttpServletRequest request){
        Long userId = TokenUtil.getUserId(request.getHeader("token"));

        try {
            String filename = fileService.getFileFullName(fileId);
            response.setContentType("application/blob");
            response.setHeader("Content-Disposition", "attachment;");
            boolean status = fileService.download(fileId, userId, response.getOutputStream());
            return new CommonResult(status==true?"下载成功":"下载失败").add("status", status);
        } catch (IOException e) {
            logger.error("下载文件失败---出现异常：    " + e.getMessage());
            return new CommonResult("下载出现异常").add("status", false);
        }
    }

    @DeleteMapping("/{fileId}")
    public CommonResult deleteFile(@PathVariable("fileId")Long fileId, HttpServletRequest request){
        Long userId = TokenUtil.getUserId(request.getHeader("token"));
        try{
            boolean status =  fileService.deleteFile(fileId, userId);
            return new CommonResult(status==true ? "删除成功":"删除失败").add("status", status);
        }catch (Exception e){
            logger.error("删除文件失败---出现异常：    " + e.getMessage());
            return new CommonResult("删除失败，出现异常").add("status", false);
        }
    }

    @GetMapping("/page")
    public ModelAndView uploadPage(){
        return new ModelAndView("testUpload");
    }


    @GetMapping("/dir/{path}")
    public List<FileVo> getFilesInDirectory(@PathVariable("path") Long path, HttpServletRequest request){
        Long userId = TokenUtil.getUserId(request.getHeader("token"));
        return fileService.getFilesInDirectory(path, userId);
    }

    /**
     * 转换MultiPartFile为数据库对象FileEntity
     * @param file
     * @return
     */
    private FileEntity multiPartToFileEntity(MultipartFile file){
        FileEntity fileEntity = new FileEntity();
        if(file.getOriginalFilename().lastIndexOf('.') != -1){
            fileEntity.setFilename(file.getOriginalFilename());
            fileEntity.setType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1));
        }
        else{
            fileEntity.setFilename(file.getOriginalFilename());
            fileEntity.setType("file");
        }
        fileEntity.setSize(file.getSize());
        fileEntity.setDirectory(0);
        fileEntity.setUpdateDate(LocalDateTime.now().toString());
        return fileEntity;
    }

    @GetMapping("/search/{input}/{fileType}")
    public List<FileVo> searchFiles(@PathVariable("input")String input, @PathVariable(value="fileType", required = false) String fileType, HttpServletRequest request){
        Long userId = TokenUtil.getUserId(request.getHeader("token"));

        List<FileVo> result = fileService.searchFiles(userId, input, fileType);
        return result;
    }

    @GetMapping("/download/packed")
    public CommonResult downloadPackage(Long[] fileIds, HttpServletResponse response, HttpServletRequest request){
        try {
            Long userId = TokenUtil.getUserId(request.getHeader("token"));

            OutputStream downloadOS = response.getOutputStream();

            fileService.downloadPackage(fileIds, userId, downloadOS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new CommonResult("success");
    }

    /**
     * 获取所有文档文件，分页返回
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @param request
     * @return
     */
    @GetMapping("/documents/{pageNum}/{pageSize}")
    public PageInfo<FileVo> getAllDocuments(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize,
                                            HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = TokenUtil.getUserId(token);
        return fileService.getAllDocuments(userId, pageNum, pageSize);
    }

    @GetMapping("/musics/{pageNum}/{pageSize}")
    public PageInfo<FileVo> getAllMusics(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize,
                                         HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = TokenUtil.getUserId(token);

        return fileService.getAllMusics(userId, pageNum, pageSize);
    }

}
