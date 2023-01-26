package com.dukeai.manageloads.model;

import com.google.gson.annotations.SerializedName;

public class ResponseModel {
    @SerializedName("Code")
    String code;
    @SerializedName("Message")
    String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
