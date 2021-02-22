package com.jay.fs.controller;

import com.jay.fs.entity.UserEntity;
import com.jay.fs.service.UserService;
import com.jay.fs.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("/connection")
    public boolean testConnection(){
        return true;
    }

    @PostMapping("/login/{username}/{password}")
    public String login(@PathVariable("username") String username, @PathVariable("password") String password){
        UserEntity user = userService.getUserInfoByName(username);
        if(user!=null && password.equals(user.getPassword())){
            String token = null;
            try {
                token = TokenUtil.generateToken(user.getUserId(), username, password);
            } catch (Exception e) {
                return null;
            }
            return token;
        }
        else{
            return null;
        }
    }
}
