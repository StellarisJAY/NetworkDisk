package com.jay.fs;

import com.jay.fs.entity.FileEntity;
import com.jay.fs.util.aip.AuthService;
import com.jay.fs.util.aip.Base64Util;
import com.jay.fs.util.aip.FileUtil;
import com.jay.fs.util.aip.HttpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
* 测试类
*
*
*/
@SpringBootTest
class NetworkDiskApplicationTests {

    @Autowired
    JavaMailSender javaMailSender;

    @Test
    public void testMail(){
        SimpleMailMessage message = new SimpleMailMessage();


        javaMailSender.send(message);
    }


}
