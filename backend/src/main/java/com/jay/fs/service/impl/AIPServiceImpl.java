package com.jay.fs.service.impl;

import com.jay.fs.dao.FileDAO;
import com.jay.fs.service.AIPService;
import com.jay.fs.util.aip.AuthService;
import com.jay.fs.util.aip.HttpUtil;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.*;

@Service
public class AIPServiceImpl implements AIPService {
    @Autowired
    private FileDAO fileDAO;
    // 线程池
    private final ExecutorService threadPool;

    public AIPServiceImpl(){
        this.threadPool = new ThreadPoolExecutor(3, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>());
    }

    @Override
    public void imageRec(Long fileId, String image) {
        // 线程池中获取一个线程调用图像识别接口
        this.threadPool.execute(()->{
            String url = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general";
            try {
                // 接口参数
                String param = "image=" + image;

                // 获取接口access-token
                String accessToken = "[" + AuthService.getAuth() + "]";

                // 获取返回json
                String result = HttpUtil.post(url, accessToken, param);
                JSONObject jsonResult = new JSONObject(result);
                JSONArray resultArray = jsonResult.getJSONArray("result");

                StringBuilder tags = new StringBuilder();
                // 解析json结果，转换成tag列表
                for(int i = 0; i < resultArray.length(); i++){
                    JSONObject tempObject = resultArray.getJSONObject(i);
                    if(!tempObject.getString("root").equals("")){
                        tags.append(tempObject.getString("root"));
                        break;
                    }
                }
                for(int i = 0; i < resultArray.length(); i++){
                    JSONObject arrayObject = resultArray.getJSONObject(i);
                    if(arrayObject.getDouble("score") >= 0.2){
                        tags.append(arrayObject.getString("keyword"));
                        if(i != resultArray.length()){
                            tags.append(",");
                        }
                    }
                }
                // 将识别结果写入数据库
                String tagString = tags.toString();
                fileDAO.updateImageTag(tagString, fileId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
