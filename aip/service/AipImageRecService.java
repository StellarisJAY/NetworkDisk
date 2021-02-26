package com.jay.aipservice.service;

import com.jay.aipservice.util.AuthService;
import com.jay.aipservice.util.Base64Util;
import com.jay.aipservice.util.FileUtil;
import com.jay.aipservice.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class AipImageRecService {

    private static Logger logger = LoggerFactory.getLogger("ImageRec");

    public static String imageRec(String imageParam){
        String url = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general";
        try {
            // 接口参数
            String param = "image=" + imageParam;

            // 获取接口access-token
            String accessToken = "[" + AuthService.getAuth() + "]";

            // 获取返回json
            String result = HttpUtil.post(url, accessToken, param);
            JSONObject jsonResult = new JSONObject(result);
            JSONArray resultArray = jsonResult.getJSONArray("result");

            logger.info("image rec result: " + result);
            //List<String> tags = new ArrayList<>();
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
            return tags.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
