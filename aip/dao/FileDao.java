package com.jay.aipservice.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface FileDao {

    @Update("UPDATE tb_file SET imageTags=#{tags} WHERE fileId=#{fileId}")
    Integer updateImageTag(@Param("tags") String tags, @Param("fileId") Long fileId);
}
