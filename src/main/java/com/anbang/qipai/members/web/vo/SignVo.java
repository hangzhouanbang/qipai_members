package com.anbang.qipai.members.web.vo;

import java.util.HashMap;
import java.util.Map;

public class SignVo {

    private boolean success = true;

    private String msg;

    private Map<String, Object> data = new HashMap<>();

    public SignVo(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public SignVo setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public SignVo setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void addData(String key, Object data) {
        this.data.put(key, data);
    }

}
