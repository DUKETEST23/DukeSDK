package com.dukeai.manageloads.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CreateLoadModel implements Serializable {

    @SerializedName("status")
    String status;

    @SerializedName("msg")
    String message;

    @SerializedName("req_id")
    String requestId;

    @SerializedName("data")
    String data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
