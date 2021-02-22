package com.jay.fs.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MessageService {

    @RabbitListener(queues = "test.news")
    public void receive(Map<String, Object> o){
        System.out.println(o.getClass());
        System.out.println(o.toString());
    }
}
