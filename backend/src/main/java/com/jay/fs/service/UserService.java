package com.jay.fs.service;

import com.jay.fs.entity.UserEntity;

public interface UserService {
    Long getUsedSpace(Long userId);

    Long getMaxSpace(Long userId);

    Long increaseUsedSpace(Long userId, Long size);

    Long decreaseUsedSpace(Long userId, Long size);

    UserEntity getUserInfoByName(String username);

    UserEntity getUserById(Long userId);
}
