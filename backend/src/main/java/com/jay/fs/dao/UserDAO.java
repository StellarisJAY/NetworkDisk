package com.jay.fs.dao;

import com.jay.fs.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserDAO {

    @Select("SELECT usedSpace FROM tb_user WHERE userId=#{userId}")
    Long getUsedSpace(@Param("userId") Long userId);

    @Update("UPDATE tb_user SET usedSpace=#{usedSpace} WHERE userId=#{userId}")
    Integer updateUsedSpace(@Param("userId") Long userId, @Param("usedSpace") Long usedSpace);

    @Update("UPDATE tb_user SET usedSpace=usedSpace+#{size} WHERE userId=#{userId}")
    Integer increaseUsedSpace(@Param("userId") Long userId, @Param("size") Long size);

    @Select("SELECT maxSpace FROM tb_user WHERE userId=#{userId}")
    Long getMaxSpace(@Param("userId") Long userId);

    @Select("SELECT * FROM tb_user WHERE username=#{username}")
    UserEntity getUserByUsername(@Param("username") String username);

    @Select("SELECT * FROM tb_user WHERE userId=#{userId}")
    UserEntity getUserById(@Param("userId")Long userId);

    @Select("SELECT role FROM tb_user_role WHERE userId=#{userId}")
    List<String> getUserRoleById(@Param("userId") Long userId);
}
