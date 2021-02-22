package com.jay.fs.controller;

import com.jay.fs.service.UserService;
import com.jay.fs.util.TokenUtil;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService = null;
    @GetMapping("/usedSpace")
    public Long getUsedSpace(HttpServletRequest request){
        Long userId = TokenUtil.getUserId(request.getHeader("token"));
        if(userId == null) return null;
        return userService.getUsedSpace(userId);
    }

    @GetMapping("/maxSpace")
    public Long getMaxSpace(HttpServletRequest request){
        Long userId = TokenUtil.getUserId(request.getHeader("token"));
        if(userId == null) return null;
        return userService.getMaxSpace(userId);
    }

}
