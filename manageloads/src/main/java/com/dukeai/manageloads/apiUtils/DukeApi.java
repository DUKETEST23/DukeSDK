package com.dukeai.manageloads.apiUtils;

import com.dukeai.manageloads.model.ArrayResponseModel;
import com.dukeai.manageloads.model.AuthModel;
import com.dukeai.manageloads.model.CreateLoadModel;
import com.dukeai.manageloads.model.DocumentDetailsModel;
import com.dukeai.manageloads.model.FileDeleteSuccessModel;
import com.dukeai.manageloads.model.FileStatusModel;
import com.dukeai.manageloads.model.FileUploadSuccessModel;
import com.dukeai.manageloads.model.GenericResponseWithJSONModel;
import com.dukeai.manageloads.model.LoadsListModel;
import com.dukeai.manageloads.model.LoadsTransmitModel;
import com.dukeai.manageloads.model.RecipientsListModel;
import com.dukeai.manageloads.model.SingleLoadModel;
import com.dukeai.manageloads.model.UpdatePaymentModel;
import com.dukeai.manageloads.model.UploadFileResponseModel;
import com.dukeai.manageloads.viewmodel.GenericResponseModel;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DukeApi {
    @POST(ApiUrls.Loads.USER_LOADS)
    Call<LoadsListModel> getUserLoads(@Header("Authorization") String auth, @Path("cust_id") String customerId, @Body JsonObject jsonObject);

    @GET(ApiUrls.Loads.GET_LOAD_DETAIL)
    Call<SingleLoadModel> getLoadDetail(@Header("Authorization") String auth, @Path("cust_id") String customerId, @Path("loadUUID") String loadUUID);


    @POST(ApiUrls.FileStatus.FILE_STATUS)
    Call<FileStatusModel> getFileStatus(@Header("Authorization") String auth, @Path("cust_id") String customerId, @Body JsonObject jsonObject);

    @GET(ApiUrls.DocumentDetails.URL)
    Call<DocumentDetailsModel> fetchDocumentDetails(@Header("Authorization") String auth, @Path("cust_id") String customerId, @Path("sha1") String sha1);

    @POST(ApiUrls.DocumentSignatureUpdate.URL)
    Call<UpdatePaymentModel> updateDocumentSignature(@Header("Authorization") String auth, @Path("cust_id") String customerId, @Body JsonObject jsonObject);

    @GET(ApiUrls.FileStatus.FILE_DOWNLOAD)
    Call<ResponseBody> downloadFile(@Header("Authorization") String auth, @Header("Content-Type") String content_type, @Header("Accept") String accept, @Path("cust_id") String customerId, @Path("filename") String filename);

    @PUT(ApiUrls.Loads.DELETE_LOAD_OBJECT)
    Call<GenericResponseModel> deleteLoadObject(@Header("Authorization") String auth, @Path("cust_id") String customerId, @Path("loadUUID") String loadUUID);

    @PUT(ApiUrls.Loads.UPDATE_RECIPIENT)
    Call<GenericResponseModel> updateRecipientDetail(@Header("Authorization") String auth, @Path("cust_id") String customerId, @Body JsonObject jsonObject);

    @POST(ApiUrls.Loads.RECIPIENTS_LIST)
    Call<RecipientsListModel> getRecipientsList(@Header("Authorization") String auth, @Path("cust_id") String customerId, @Body JsonObject jsonObject);

    @PUT(ApiUrls.Loads.DELETE_FROM_LOAD)
    Call<GenericResponseModel> deleteFromLoad(@Header("Authorization") String auth, @Path("cust_id") String customerId, @Path("loadUUID") String loadUUID, @Body JsonObject jsonObject);

    @DELETE(ApiUrls.FileStatus.DELETE_FILE)
    Call<FileDeleteSuccessModel> deleteFile(@Header("Authorization") String auth, @Path("cust_id") String customerId, @Path("sha1") String sha1);

    @POST(ApiUrls.Loads.TRANSMIT_LOADS)
    Call<LoadsTransmitModel> transmitLoads(@Header("Authorization") String auth,
                                           @Path("cust_id") String customerId,
                                           @Body Map<String, Object> jsonObject);

    @Multipart
    @POST(ApiUrls.MultiFileUpload.NEW_MULTI_UPLOAD)
    Call<UploadFileResponseModel> upload(@Header("Authorization") String auth, @Path("cust_id") String customerId,
                                         @Part MultipartBody.Part address, @Part MultipartBody.Part signature,
                                         @Part MultipartBody.Part is_scan, @Part MultipartBody.Part load_flag,
                                         @Part MultipartBody.Part coordinates,
                                         @Part MultipartBody.Part manifest, @Part MultipartBody.Part[] files,
                                         @Part MultipartBody.Part[] pdfFiles);

    @POST(ApiUrls.Loads.CREATE_LOAD)
    Call<CreateLoadModel> createLoad(@Header("Authorization") String auth, @Path("cust_id") String customerId, @Body JsonObject jsonObject);


    @GET(ApiUrls.MultiFileUpload.GENERATE_MANIFEST)
    Call<GenericResponseWithJSONModel> generateManifest(@Header("Authorization") String auth, @Path("cust_id") String customerId);

    @Multipart
    @POST(ApiUrls.MultiFileUpload.MULTI_FILE_UPLOAD)
    Call<FileUploadSuccessModel> uploadMultipleFilesWithCount(@Header("Authorization") String auth, @Path("cust_id") String customerId, @Part MultipartBody.Part[] files, @Part MultipartBody.Part fileCount, @Part MultipartBody.Part address);

    @FormUrlEncoded
    @POST(ApiUrls.Authentication.AUTH)
    Call<AuthModel> authentication(@Field("client_id") String client_id,
                                   @Field("api_key") String api_key,
                                   @Field("cust_id") String cust_id,
                                   @Field("idToken") String idToken,
                                   @Field("accessToken") String accessToken,
                                   @Field("refreshToken") String refreshToken);

    @GET(ApiUrls.UserRegistration.FORGOT_PASSWORD)
    Call<ArrayResponseModel> forgotPassword(@Path("cust_id") String customerId);


}
