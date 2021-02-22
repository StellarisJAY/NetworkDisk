package com.jay.fs.service.impl;

import com.jay.fs.dao.UserDAO;
import com.jay.fs.entity.UserEntity;
import com.jay.fs.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO = null;

    private Logger logger = LoggerFactory.getLogger("UserServiceImpl");

    /**
     * 获取用户已使用容量大小
     * @param userId
     * @return
     */
    @Override
    //@Cacheable(cacheNames = "redisCache", key = "#userId+'_UsedSpace'")
    public Long getUsedSpace(Long userId) {
        return userDAO.getUsedSpace(userId);
    }

    /**
     * 获取该用户最大可用空间
     * @param userId
     * @return
     */
    @Override
    //@Cacheable(cacheNames = "redisCache", key = "#userId+'_MaxSpace'")
    public Long getMaxSpace(Long userId) {
        return userDAO.getMaxSpace(userId);
    }

    @Override
    @CachePut(cacheNames = "redisCache", key = "#userId+'_UsedSpace'", condition = "#result!=-1L")
    @Transactional
    public Long increaseUsedSpace(Long userId, Long size){
        int status = userDAO.increaseUsedSpace(userId, size);
        if(status != 0){
            return userDAO.getUsedSpace(userId);
        }
        return -1L;
    }

    @Override
    @CachePut(cacheNames = "redisCache", key = "#userId+'_UsedSpace'", condition="#result != -1L")
    public Long decreaseUsedSpace(Long userId, Long size) {
        int status = userDAO.increaseUsedSpace(userId, -size);
        if(status != 0){
            return userDAO.getUsedSpace(userId);
        }
        logger.warn("已用空间修改出错");
        return -1L;
    }

    /**
     * 通过用户名获取用户信息
     * @param username
     * @return
     */
    @Override
    @CachePut(cacheNames = "redisCache", key = "'user' + #result.getUserId()", condition = "#result != null")
    public UserEntity getUserInfoByName(String username) {
        return userDAO.getUserByUsername(username);
    }

    /**
     * 通过用户id获取用户信息
     * @param userId
     * @return
     */
    @Override
    @Cacheable(cacheNames = "redisCache", key = "'user'+#userId")
    public UserEntity getUserById(Long userId) {
        return userDAO.getUserById(userId);
    }
}
