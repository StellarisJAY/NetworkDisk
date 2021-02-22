package com.jay.fs.dao;

import com.jay.fs.entity.FileEntity;
import com.jay.fs.vo.FileVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FileDAO {

    /**
     * 查询某个文件的hdfs路径
     * @param fileId
     * @return
     */
    @Select("SELECT hdfsPath FROM tb_file WHERE fileId=#{fileId}")
    String getHdfsPath(@Param("fileId") Long fileId);


    @Select("SELECT filename FROM tb_file WHERE fileId=#{fileId}")
    String getFilename(@Param("fileId") Long fileId);

    /**
     * 查询文件夹下的所有文件
     * @param directory
     * @param userId
     * @return
     */
    @Select("SELECT f.* FROM tb_file as f, tb_user_file as uf " +
            "WHERE f.fileId=uf.fileId AND uf.userId=#{userId} AND uf.path=#{directoryId} " +
            "ORDER BY f.directory DESC, f.type ASC")
    List<FileEntity> getFilesInDirectory(@Param("directoryId") Long directory, @Param("userId") Long userId);


    @Select("SELECT f.* FROM tb_file AS f, tb_user_file AS uf WHERE " +
            "f.fileId=uf.fileId AND uf.path=#{directoryId} AND uf.userId=#{userId} " +
            "ORDER BY f.directory DESC, f.type ASC")
    List<FileVo> getFileVosInDirectory(@Param("directoryId") Long directory, @Param("userId") Long userId);
    /**
     * 添加新文件到文件表
     * @param file
     * @return
     */
    @Insert("INSERT INTO tb_file VALUES(#{file.fileId}, #{file.filename}, " +
            "#{file.size}, #{file.type}, #{file.directory}, " +
            "#{file.hdfsPath}, #{file.updateDate}, #{file.uploader}, #{file.imageTags})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insertFile(@Param("file") FileEntity file);

    /**
     * 添加新文件与用户所属关系
     * @param userId
     * @param fileId
     * @param path
     * @return
     */
    @Insert("INSERT INTO tb_user_file VALUES(#{userId}, #{fileId}, #{path})")
    Integer insertUserFile(@Param("userId") Long userId, @Param("fileId") Long fileId, @Param("path") Long path);

    @Select("SELECT CONCAT(filename, \".\", type) FROM tb_file WHERE fileId=#{fileId}")
    String getFileFullName(@Param("fileId")Long fileId);

    @Delete("DELETE FROM tb_file WHERE fileId=#{fileId}")
    int deleteFile(@Param("fileId") Long fileId);

    @Delete("DELETE FROM tb_user_file WHERE userId=#{userId} AND fileId=#{fileId}")
    int deleteUserFile(@Param("fileId") Long fileId, @Param("userId") Long userId);

    @Select("SELECT size FROM tb_file WHERE fileId=#{fileId}")
    Long getFileSize(@Param("fileId") Long fileId);


    @Select("SELECT f.hdfsPath FROM tb_file AS f, tb_user_file AS uf WHERE uf.fileId=f.fileId AND f.fileId in #{fileIds}")
    List<String> getHdfsPaths(@Param("fileIds")Long[] fileIds, @Param("userId") Long userId);

    @Select("SELECT f.* FROM tb_file AS f, tb_user_file AS uf " +
            "WHERE f.fileId=uf.fileId AND uf.userId=#{userId} AND " +
            "f.type IN ('pdf', 'docx', 'doc', 'txt', 'md', 'xls', 'xlsx', 'pptx', 'ppt')")
    List<FileVo> getAllDocuments(@Param("userId") Long userId);

    @Select("SELECT f.* FROM tb_file AS f, tb_user_file AS uf " +
            "WHERE f.fileId=uf.fileId AND uf.userId=#{userId} AND " +
            "f.type IN ('mp3', 'wav', 'flac')")
    List<FileVo> getAllMusics(@Param("userId") Long userId);


    @Select("SELECT f.* FROM tb_file AS f, tb_user_file AS uf " +
            "WHERE uf.fileId=f.fileId AND " +
            "uf.userId=#{userId} AND " +
            "f.filename LIKE #{input}")
    List<FileVo> searchFiles(@Param("input") String input, @Param("userId") Long userId);

    @Select("SELECT f.* FROM tb_file AS f, tb_user_file AS uf " +
            "WHERE uf.fileId=f.fileId AND " +
            "uf.userId=#{userId} AND " +
            "f.filename LIKE #{input} AND " +
            "f.type IN ('pdf', 'docx', 'doc', 'txt', 'md', 'xls', 'xlsx', 'pptx', 'ppt')")
    List<FileVo> searchDocuments(@Param("input") String input, @Param("userId") Long userId);

    @Select("SELECT f.* FROM tb_file AS f, tb_user_file AS uf " +
            "WHERE uf.fileId=f.fileId AND " +
            "uf.userId=#{userId} AND " +
            "f.filename LIKE #{input} AND " +
            "f.type IN ('jpg', 'png', 'gif', 'bmp', 'svg')")
    List<FileVo> searchPictures(@Param("input") String input, @Param("userId") Long userId);


    @Select("SELECT f.* FROM tb_file AS f, tb_user_file AS uf " +
            "WHERE f.fileId=uf.fileId AND uf.userId=#{userId} AND " +
            "f.filename LIKE #{input} AND " +
            "f.type IN ('mp3', 'wav', 'flac')")
    List<FileVo> searchMusics(@Param("input") String input, @Param("userId") Long userId);
}
