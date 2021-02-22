package com.jay.fs.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.jay.fs.entity.UserEntity;
import com.jay.fs.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger("拦截器");
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        int unAuthorized = 401;
        logger.info(request.getMethod() + " : " + request.getRequestURL() + " : " + token);
        /*
         * 前端在发送请求前会先发送一个OPTIONS请求，OPTIONS 成功后才会发送原请求
         * 所以这里不拦截OPTIONS请求，放行并等待前端的正式请求
         */
        if(request.getMethod().equals("OPTIONS")){
            return true;
        }
        if(!(handler instanceof HandlerMethod)){
            response.setStatus(unAuthorized);
            return false;
        }
        if(token == null){
            response.setStatus(unAuthorized);
            logger.info("token 为空");
            return false;
        }

        /*
            判断是否超时
            超时时间：30 * 60 * 1000 = 30min
         */
        Long timestamp = JWT.decode(token).getClaim("timestamp").asLong();
        Long currentTimeMillis = System.currentTimeMillis();
        if(currentTimeMillis - timestamp >= 30 * 60 * 1000){
            logger.info("token超时");
            response.setStatus(unAuthorized);
            return false;
        }
        // 判断该token的用户是否存在
        Long userId = Long.valueOf(JWT.decode(token).getAudience().get(0));
        UserEntity user = userService.getUserById(userId);
        if(user == null){
            response.setStatus(unAuthorized);
            logger.info("用户不存在");
            return false;
        }

        // 验证token是否有效
        try{
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getPassword())).build();
            jwtVerifier.verify(token);
        }catch (JWTVerificationException e){
            response.setStatus(unAuthorized);
            logger.info("token无法解析");
            return false;
        }

        return true;
    }


}
