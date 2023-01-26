package com.dukeai.manageloads.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class LoadsListModel implements Serializable {
    private String loadId;
    private String uploadDate;
    private String amount;

    public LoadsListModel(String loadId, String uploadDate, String amount) {
        this.loadId = loadId;
        this.uploadDate = uploadDate;
        this.amount = amount;
    }

    public String getLoadId() {
        return loadId;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public String getAmount() {
        return amount;
    }

//    @SerializedName("user_loads")
//    private ArrayList<UserLoadsModel> userLoads = new ArrayList<UserLoadsModel>();
//
//    @SerializedName("req_id")
//    private String requestId;

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String message;

    @SerializedName("req_id")
    public String requestId;

    @SerializedName("data")
    public ArrayList<UserLoadsModel> userLoadsModels = new ArrayList<UserLoadsModel>();

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

    public ArrayList<UserLoadsModel> getRecipientsList() {
        return userLoadsModels;
    }

    public void setRecipientsList(ArrayList<UserLoadsModel> recipientsList) {
        this.userLoadsModels = recipientsList;
    }
}
