package com.jay.fs.entity;

import java.io.Serializable;

public class UserEntity implements Serializable {
    private static final long serialVersionUID = -6938268106401610678L;
    private Long userId;
    private String username;
    private String password;
    private int available;
    private String email;
    private String phone;
    private Long maxSpace;
    private Long usedSpace;

    public UserEntity(Long userId, String username, String password, int available, String email, String phone, Long maxSpace, Long usedSpace) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.available = available;
        this.email = email;
        this.phone = phone;
        this.maxSpace = maxSpace;
        this.usedSpace = usedSpace;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getMaxSpace() {
        return maxSpace;
    }

    public void setMaxSpace(Long maxSpace) {
        this.maxSpace = maxSpace;
    }

    public Long getUsedSpace() {
        return usedSpace;
    }

    public void setUsedSpace(Long usedSpace) {
        this.usedSpace = usedSpace;
    }
}
