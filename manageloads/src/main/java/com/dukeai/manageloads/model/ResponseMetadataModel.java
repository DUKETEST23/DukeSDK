package com.dukeai.manageloads.model;

import com.google.gson.annotations.SerializedName;

public class ResponseMetadataModel {
    @SerializedName("RequestId")
    String RequestId;
    @SerializedName("HTTPStatusCode")
    int HTTPStatusCode;
    @SerializedName("HTTPHeaders")
    HttpHeadersModel httpHeadersModel;
    @SerializedName("RetryAttempts")
    int RetryAttempts;

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public int getHTTPStatusCode() {
        return HTTPStatusCode;
    }

    public void setHTTPStatusCode(int HTTPStatusCode) {
        this.HTTPStatusCode = HTTPStatusCode;
    }

    public HttpHeadersModel getHttpHeadersModel() {
        return httpHeadersModel;
    }

    public void setHttpHeadersModel(HttpHeadersModel httpHeadersModel) {
        this.httpHeadersModel = httpHeadersModel;
    }

    public int getRetryAttempts() {
        return RetryAttempts;
    }

    public void setRetryAttempts(int retryAttempts) {
        RetryAttempts = retryAttempts;
    }

    public class HttpHeadersModel {
        @SerializedName("server")
        String server;
        @SerializedName("date")
        String date;
        @SerializedName("content-type")
        String contentType;
        @SerializedName("content-length")
        String contentLength;
        @SerializedName("connection")
        String connection;
        @SerializedName("x-amzn-requestid")
        String amznRequestId;
        @SerializedName("x-amz-crc32")
        String amznCrc;

        public String getServer() {
            return server;
        }

        public void setServer(String server) {
            this.server = server;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getContentLength() {
            return contentLength;
        }

        public void setContentLength(String contentLength) {
            this.contentLength = contentLength;
        }

        public String getConnection() {
            return connection;
        }

        public void setConnection(String connection) {
            this.connection = connection;
        }

        public String getAmznRequestId() {
            return amznRequestId;
        }

        public void setAmznRequestId(String amznRequestId) {
            this.amznRequestId = amznRequestId;
        }

        public String getAmznCrc() {
            return amznCrc;
        }

        public void setAmznCrc(String amznCrc) {
            this.amznCrc = amznCrc;
        }
    }
}
