package com.woop.filetransferprototypeclient.api;




import com.woop.filetransferprototypeclient.api.responses.FileUploadResponse;
import com.woop.filetransferprototypeclient.api.responses.NewAccountResponse;
import com.woop.filetransferprototypeclient.api.responses.VerifyAccountResponse;
import com.woop.filetransferprototypeclient.mainactivity.entity.FileListItemData;

import java.io.InputStream;
import java.util.List;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;


/**
 * Created by NoID on 11.02.2018.
 */

public interface FileTransferAPI {

    @Multipart
    @POST("upload/1.0/")
    Call<FileUploadResponse> uploadFile(@Header("Authorization") String authorization,
                                               @Part MultipartBody.Part file,
                                               @Part("directory") RequestBody directory,
                                               @Part("size") RequestBody size);

    @GET("account/verify/1.0")
    Call<VerifyAccountResponse> verifyAccount(@Header("Authorization") String authorization);

    @GET("account/new/1.0")
    Call<NewAccountResponse> newAccount(@Header("AccData") String accData);


    @GET("download/list/1.0")
    Call<List<FileListItemData>> getFileList(@Header("Authorization") String authorization);


    @GET("download/1.0")
    @Streaming
    Call<ResponseBody> downloadFile(@Header("Authorization") String Authorization,
                                    @Query("id") int id);

    @GET("delete/1.0/")
    Call<ResponseBody> deleteFile (@Header("Authorization") String Authorization,
                                       @Query("id") int id);

    @GET("account/password/1.0/")
    Call<ResponseBody> changePassword(@Header("Authorization") String Authorization,
                        @Query("new") String newpassword);

}
