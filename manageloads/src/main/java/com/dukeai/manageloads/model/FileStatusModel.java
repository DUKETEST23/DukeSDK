package com.dukeai.manageloads.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class FileStatusModel extends ResponseModel implements Serializable {
    @SerializedName("member_status")
    String memberStatus;
    @SerializedName("upload_count")
    double uploadCount;
    @SerializedName("upload_limit")
    double uploadLimit;

    @SerializedName("request")
    requestModel request;

    @SerializedName("unsubscribeID")
    String uniqueUserId;

    @SerializedName("processed")
    ArrayList<ProcessedDocumentsModel> processedDocumentsModels = new ArrayList<>();
    @SerializedName("POD_processed")
    ArrayList<ProcessedDocumentsModel> processedPODDocuments = new ArrayList<>();
    @SerializedName("in_process")
    ArrayList<InProcessDocumentsModel> inProcessDocumentsModels = new ArrayList<>();
    @SerializedName("rejected")
    ArrayList<RejectedDocumentsModel> rejectedDocumentsModels = new ArrayList<>();
    ArrayList<ProcessedDocumentsModel> podProccessedDocument = new ArrayList<>();

    public FileStatusModel() {
        podProccessedDocument = getAllProcessedDocuments();
    }

    public double getUploadCount() {
        return uploadCount;
    }

    public void setUploadCount(double uploadCount) {
        this.uploadCount = uploadCount;
    }

    public double getUploadLimit() {
        return uploadLimit;
    }

    public void setUploadLimit(double uploadLimit) {
        this.uploadLimit = uploadLimit;
    }

    public String getUniqueUserId() {
        return uniqueUserId;
    }

    public void setUniqueUserId(String uniqueUserId) {
        this.uniqueUserId = uniqueUserId;
    }

    public ArrayList<ProcessedDocumentsModel> getPodProccessedData() {
        return podProccessedDocument;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public requestModel getRequest() {
        return request;
    }

    public void setRequest(requestModel request) {
        this.request = request;
    }

    public ArrayList<ProcessedDocumentsModel> getProcessedDocumentsModels() {
        return processedDocumentsModels;
    }

    public void setProcessedDocumentsModels(ArrayList<ProcessedDocumentsModel> processedDocumentsModels) {
        this.processedDocumentsModels = processedDocumentsModels;
    }

    public ArrayList<ProcessedDocumentsModel> getAllProcessedDocuments() {
        ArrayList<ProcessedDocumentsModel> temp = new ArrayList<>(processedPODDocuments);
        temp.addAll(processedDocumentsModels);
        return temp;
    }

    public ArrayList<ProcessedDocumentsModel> getPodProcessedDocuments() {
        ArrayList<ProcessedDocumentsModel> temp = new ArrayList<>(processedPODDocuments);
        temp.addAll(processedPODDocuments);
        return temp;
    }

    public ArrayList<InProcessDocumentsModel> getInProcessDocumentsModels() {
        return inProcessDocumentsModels;
    }

    public void setInProcessDocumentsModels(ArrayList<InProcessDocumentsModel> inProcessDocumentsModels) {
        this.inProcessDocumentsModels = inProcessDocumentsModels;
    }

    public ArrayList<RejectedDocumentsModel> getRejectedDocumentsModels() {
        return rejectedDocumentsModels;
    }

    public void setRejectedDocumentsModels(ArrayList<RejectedDocumentsModel> rejectedDocumentsModels) {
        this.rejectedDocumentsModels = rejectedDocumentsModels;
    }

    public class requestModel {
        @SerializedName("cust_id")
        String customerId;
        @SerializedName("caller")
        String caller;
        @SerializedName("from")
        String form;
        @SerializedName("to")
        String to;
        @SerializedName("num_docs")
        String numberOfDocs;

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCaller() {
            return caller;
        }

        public void setCaller(String caller) {
            this.caller = caller;
        }

        public String getForm() {
            return form;
        }

        public void setForm(String form) {
            this.form = form;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getNumberOfDocs() {
            return numberOfDocs;
        }

        public void setNumberOfDocs(String numberOfDocs) {
            this.numberOfDocs = numberOfDocs;
        }
    }
}
