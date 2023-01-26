package com.dukeai.manageloads.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoadsTransmitModel implements Serializable {
    /*@SerializedName("status")
    public Object status;

    @SerializedName("msg")
    public Object message;

    @SerializedName("req_id")
    public Object requestId;

    @SerializedName("data")
    public Object data;

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getRequestId() {
        return requestId;
    }

    public void setRequestId(Object requestId) {
        this.requestId = requestId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }*/

    @SerializedName("status")
    public Object status;

    @SerializedName("msg")
    public Object message;

    @SerializedName("req_id")
    public Object requestId;

    @SerializedName("data")
    public Object data;

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public Object getRequestId() {
        return requestId;
    }

    public void setRequestId(Object requestId) {
        this.requestId = requestId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
