package com.dukeai.manageloads.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipientsListModel implements Serializable {

    @SerializedName("status")
    public String status;

    @SerializedName("msg")
    public String message;

    @SerializedName("req_id")
    public String requestId;

    @SerializedName("data")
    public ArrayList<RecipientDataModel> recipientsList = new ArrayList<RecipientDataModel>();

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

    public ArrayList<RecipientDataModel> getRecipientsList() {
        return recipientsList;
    }

    public void setRecipientsList(ArrayList<RecipientDataModel> recipientsList) {
        this.recipientsList = recipientsList;
    }
}
