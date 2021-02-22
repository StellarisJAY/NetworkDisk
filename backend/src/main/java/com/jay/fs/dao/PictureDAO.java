package com.jay.fs.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface PictureDAO {

    @Select("SELECT f.hdfsPath FROM tb_file AS f, tb_user_file AS uf WHERE " +
            "uf.userId=#{userId} AND " +
            "uf.fileId=f.fileId AND " +
            "f.fileId=#{fileId} AND " +
            "f.type in (\"jpg\",\"png\",\"gif\",\"bmp\")")
    String getPictureHdfsPath(@Param("fileId") Long fileId, @Param("userId") Long userId);
}
