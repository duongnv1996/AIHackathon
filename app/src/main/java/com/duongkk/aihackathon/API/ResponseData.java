package com.duongkk.aihackathon.API;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DuongKK on 5/18/2017.
 */

public class ResponseData {
    @SerializedName("msg")
    String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
