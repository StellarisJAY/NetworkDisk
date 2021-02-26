package com.jay.aipservice.service;

import com.jay.aipservice.dao.FileDao;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private FileDao fileDao;
    @Autowired
    private PictureCompression pictureCompression;

    /**
     * 监听AIP.image队列，接收消息后拆分出id和图像数据
     * @param message
     */
    @RabbitListener(queues = "AIP.image")
    public void listenImageRecQueue(Message message){
        String messageBody = new String(message.getBody());
        // 从消息体获取文件id
        Long fileId = getFileId(messageBody);
        // 图片字节数组
        //byte[] pictureBytes = ("data:image/*;base64," + messageBody.substring(messageBody.indexOf("}") + 1)).getBytes();
        // 获取图像识别结果（图片标签列表）
        String tags = AipImageRecService.imageRec(messageBody.substring(messageBody.indexOf("}") + 1));
        if(tags != null){
            // 写入数据库
            fileDao.updateImageTag(tags, fileId);
        }
    }

    /**
     * 从message获取图像文件id
     * @param messageBody
     * @return
     */
    private Long getFileId(String messageBody){
        int idPrefix = messageBody.indexOf('{');
        int idSuffix = messageBody.indexOf('}');
        return Long.valueOf(messageBody.substring(idPrefix + 1, idSuffix));
    }

}
