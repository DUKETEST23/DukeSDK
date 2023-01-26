package com.dukeai.manageloads.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class UploadFileResponseModel implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("msg")
    private String message;

    @SerializedName("req_id")
    private String requestId;

//    @SerializedName("fileStatus")
//    private FileUploadResponseModel fileUploadResponseModel;

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

//    public FileUploadResponseModel getFileUploadResponseModel() {
//        return fileUploadResponseModel;
//    }
//
//    public void setFileUploadResponseModel(FileUploadResponseModel fileUploadResponseModel) {
//        this.fileUploadResponseModel = fileUploadResponseModel;
//    }

    public class FileUploadResponseModel implements Serializable {

        @SerializedName("filename")
        private String filename;

        @SerializedName("is_success")
        private String isSuccess;

        @SerializedName("msg")
        private String message;

        @SerializedName("pages")
        private ArrayList<JsonObject> pages = new ArrayList<>();

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getIsSuccess() {
            return isSuccess;
        }

        public void setIsSuccess(String isSuccess) {
            this.isSuccess = isSuccess;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ArrayList<JsonObject> getPages() {
            return pages;
        }

        public void setPages(ArrayList<JsonObject> pages) {
            this.pages = pages;
        }
    }
}
