package com.dukeai.manageloads.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ProcessedDocumentsModel implements Serializable {
    @SerializedName("filename")
    String fileName;
    @SerializedName("upload_date")
    String uploadDate;
    @SerializedName("processed_date")
    String processedDate;
    @SerializedName("thumbnail")
    String thumbnail;
    @SerializedName("pages")
    ArrayList<String> pages;
    @SerializedName("sha1")
    String sha1;
    @SerializedName("signature")
    String signature;
    @SerializedName("data")
    ProcessedDocumentsDataModel processedData;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(String processedDate) {
        this.processedDate = processedDate;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ArrayList<String> getPages() {
        return pages;
    }

    public void setPages(ArrayList<String> pages) {
        this.pages = pages;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public ProcessedDocumentsDataModel getProcessedData() {
        return processedData;
    }

    public void setProcessedData(ProcessedDocumentsDataModel processedData) {
        this.processedData = processedData;
    }

    public class ProcessedDocumentsDataModel implements Serializable {
        @SerializedName("title")
        String title;
        @SerializedName("doc_type")
        String docType;
        @SerializedName("doc_date")
        String docDate;
        @SerializedName("net")
        Double net;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDocType() {
            return docType;
        }

        public void setDocType(String docType) {
            this.docType = docType;
        }

        public String getDocDate() {
            return docDate;
        }

        public void setDocDate(String docDate) {
            this.docDate = docDate;
        }

        public Double getNet() {
            return net;
        }

        public void setNet(Double net) {
            this.net = net;
        }
    }
}
