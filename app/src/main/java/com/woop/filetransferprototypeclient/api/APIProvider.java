package com.woop.filetransferprototypeclient.api;







import android.util.Base64;

import com.woop.filetransferprototypeclient.api.responses.FileUploadResponse;
import com.woop.filetransferprototypeclient.api.responses.NewAccountResponse;
import com.woop.filetransferprototypeclient.api.responses.VerifyAccountResponse;
import com.woop.filetransferprototypeclient.mainactivity.entity.FileListItemData;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by NoID on 11.02.2018.
 */

public class APIProvider {

    private final FileTransferAPI fileTransferAPI;
    private final String BASE_URI;
    private static final String FILE_PART_NAME = "file";
    private final Retrofit retrofit;

    public APIProvider(String BASE_URI) {
        this.BASE_URI = BASE_URI;
        retrofit = new Retrofit.Builder().
                baseUrl(BASE_URI).
                addConverterFactory(JacksonConverterFactory.create()).

                build();
        this.fileTransferAPI = retrofit.create(FileTransferAPI.class);
    }

    public FileTransferAPI getFileTransferAPI() {
        return fileTransferAPI;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public String getBASE_URI() {
        return BASE_URI;
    }

    private RequestBody createStringPart(String string) {
        return RequestBody.create(
                MediaType.parse("multipart/form-data"), string);
    }

    private MultipartBody.Part createFilePart(File file) {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData(FILE_PART_NAME, file.getName(), requestFile);
    }

    private String createHeaderAuthString(String login, String token) {
        String loginAndToken = String.format("%s:%s",login,token);
        String encoded ="Basic " +  Base64.encodeToString(loginAndToken.getBytes(), Base64.NO_WRAP);
        return encoded;
    }

    public Response<FileUploadResponse> uploadFile(String login, String token,File file)    {
        System.out.println("uploadFile");
        String authHeader = createHeaderAuthString(login,token);
        RequestBody directory = createStringPart(file.getAbsolutePath());
        MultipartBody.Part filePart = createFilePart(file);
        RequestBody size = createStringPart(String.valueOf(file.length()));
        Response<FileUploadResponse> response = null;

        try {
            response = fileTransferAPI.uploadFile(authHeader, filePart, directory,size).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    public Response<NewAccountResponse> newAccount(String login, String password) {
        System.out.println("newAccount");
        String authHeader = createHeaderAuthString(login,password);
        Response<NewAccountResponse> response = null;
        try {
            response = fileTransferAPI.newAccount(authHeader).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }


    public Response<VerifyAccountResponse> verifyAccount(String login, String password) {
        System.out.println("verifyAccount");
        String authHeader = createHeaderAuthString(login,password);
        Response<VerifyAccountResponse> response = null;
        try {
            response = fileTransferAPI.verifyAccount(authHeader).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    public Response<List<FileListItemData>> getFileList(String login, String token) {
        System.out.println("getFileList");
        String authHeader = createHeaderAuthString(login,token);
        Response<List<FileListItemData>> response = null;

        try {
            response = fileTransferAPI.getFileList(authHeader).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    public Response<ResponseBody> downloadFile(String login, String token, int id) {
        System.out.println("downloadFile");
        String authHeader = createHeaderAuthString(login,token);
        Response<ResponseBody> response = null;

        try {
            response = fileTransferAPI.downloadFile(authHeader,id).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    public Response deleteFile(String login, String token, int id) {
        System.out.println("deleteFile");
        String authHeader = createHeaderAuthString(login,token);
        Response response = null;

        try {
            response = fileTransferAPI.deleteFile(authHeader,id).execute();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    public Response changePassword(String login, String token, String newpassword) {
        System.out.println("changePassword");
        String authHeader = createHeaderAuthString(login,token);
        String encoded = Base64.encodeToString(newpassword.getBytes(),Base64.NO_WRAP);
        Response response = null;

        try {
            response = fileTransferAPI.changePassword(authHeader,encoded).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

}
