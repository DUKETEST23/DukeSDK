package com.dukeai.manageloads.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class UserLoadsModel implements Serializable {

    /*@SerializedName("documents")
    private ArrayList<LoadDocumentModel> documents = new ArrayList<LoadDocumentModel>();

    @SerializedName("load_id")
    private String loadId;

    @SerializedName("updated")
    private String updatedDate;

    @SerializedName("status")
//    @SerializedName("status_")
    private String status;

    @SerializedName("amount")
    private String amount;

    @SerializedName("date_created")
    private String createdDate;

    @SerializedName("cust_id")
    private String custId;

    @SerializedName("load_uuid")
    private String loadUUID;

    public UserLoadsModel(ArrayList<LoadDocumentModel> documents, String loadId, String updatedDate, String status, String amount, String createdDate, String custId, String loadUUID) {
        this.documents = documents;
        this.loadId = loadId;
        this.updatedDate = updatedDate;
        this.status = status;
        this.amount = amount;
        this.createdDate = createdDate;
        this.custId = custId;
        this.loadUUID = loadUUID;
    }

    public UserLoadsModel() {

    }

    public ArrayList<LoadDocumentModel> getDocuments() {
        return documents;
    }

    public void setDocuments(ArrayList<LoadDocumentModel> documents) {
        this.documents = documents;
    }

    public String getLoadId() {
        return loadId;
    }

    public void setLoadId(String loadId) {
        this.loadId = loadId;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getLoadUUID() {
        return loadUUID;
    }

    public void setLoadUUID(String loadUUID) {
        this.loadUUID = loadUUID;
    }*/



    @SerializedName("status_")
    @Expose
    private Object status;
    @SerializedName("load_id")
    @Expose
    private String loadId;
    @SerializedName("updated")
    @Expose
    private String updated;
    @SerializedName("amount")
    @Expose
    private Object amount;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("cust_id")
    @Expose
    private String custId;
    @SerializedName("load_uuid")
    @Expose
    private String loadUuid;
    @SerializedName("req_id")
    @Expose
    private String reqId;
    @SerializedName("documents")
    @Expose
    private List<LoadDocumentModel> documents = null;

    private boolean isChecked ;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public String getLoadId() {
        return loadId;
    }

    public void setLoadId(String loadId) {
        this.loadId = loadId;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Object getAmount() {
        return amount;
    }

    public void setAmount(Object amount) {
        this.amount = amount;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getLoadUuid() {
        return loadUuid;
    }

    public void setLoadUuid(String loadUuid) {
        this.loadUuid = loadUuid;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public List<LoadDocumentModel> getDocuments() {
        return documents;
    }

    public void setDocuments(List<LoadDocumentModel> documents) {
        this.documents = documents;
    }

}
