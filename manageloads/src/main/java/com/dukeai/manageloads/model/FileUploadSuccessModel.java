package com.dukeai.manageloads.model;

import com.google.gson.annotations.SerializedName;

public class FileUploadSuccessModel extends ResponseModel {
    @SerializedName("infile_sha1")
    String infileSha1;
    @SerializedName("db_response")
    DbResponseModel dbResponseModel;
    @SerializedName("sqs_response")
    SqsResponse sqsResponse;

    public String getInfileSha1() {
        return infileSha1;
    }

    public void setInfileSha1(String infileSha1) {
        this.infileSha1 = infileSha1;
    }

    public DbResponseModel getDbResponseModel() {
        return dbResponseModel;
    }

    public void setDbResponseModel(DbResponseModel dbResponseModel) {
        this.dbResponseModel = dbResponseModel;
    }

    public SqsResponse getSqsResponse() {
        return sqsResponse;
    }

    public void setSqsResponse(SqsResponse sqsResponse) {
        this.sqsResponse = sqsResponse;
    }

    public class DbResponseModel {
        @SerializedName("ResponseMetadata")
        ResponseMetadataModel responseMetadataModel;

        public ResponseMetadataModel getResponseMetadataModel() {
            return responseMetadataModel;
        }

        public void setResponseMetadataModel(ResponseMetadataModel responseMetadataModel) {
            this.responseMetadataModel = responseMetadataModel;
        }
    }

    public class SqsResponse {
        @SerializedName("MD5OfMessageBody")
        String MD5OfMessageBody;
        @SerializedName("MessageId")
        String messageId;
        @SerializedName("ResponseMetadata")
        ResponseMetadataModel responseMetadataModel;

        public String getMD5OfMessageBody() {
            return MD5OfMessageBody;
        }

        public void setMD5OfMessageBody(String MD5OfMessageBody) {
            this.MD5OfMessageBody = MD5OfMessageBody;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public ResponseMetadataModel getResponseMetadataModel() {
            return responseMetadataModel;
        }

        public void setResponseMetadataModel(ResponseMetadataModel responseMetadataModel) {
            this.responseMetadataModel = responseMetadataModel;
        }
    }
}
