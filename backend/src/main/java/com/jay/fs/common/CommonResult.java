package com.jay.fs.common;

import java.util.HashMap;
import java.util.Map;

public class CommonResult {
    private String message;
    private Map<String, Object> data;

    public CommonResult(String message) {
        this.message = message;
        this.data = null;
    }

    public CommonResult data(HashMap<String, Object> data){
        this.data = data;
        return this;
    }

    public CommonResult message(String message){
        this.message = message;
        return this;
    }

    public CommonResult add(String name, Object value){
        if(this.data == null){
            this.data = new HashMap<>();
        }
        this.data.put(name, value);
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
