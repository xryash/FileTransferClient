package com.woop.filetransferprototypeclient.mainactivity.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.woop.filetransferprototypeclient.api.APIProvider;
import com.woop.filetransferprototypeclient.api.responses.ServiceErrorResponse;
import com.woop.filetransferprototypeclient.config.OptionsData;
import com.woop.filetransferprototypeclient.mainactivity.entity.FileListItemData;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by NoID on 22.04.2018.
 */

public class DownloadFileAsyncTask extends AsyncTask<Void, Void, Boolean> {

    private final String login;
    private final String token;
    private final String server;
    private final String TAG = DeleteFileAsyncTask.class.getSimpleName();
    private final Context context;
    private final FileListItemData itemData;
    private InputStream stream = null;


    private static DownloadFileAsyncTask instance;

    public static DownloadFileAsyncTask getInstance(FileListItemData itemData,Context context) {
        if (instance != null)
            return null;
        return new DownloadFileAsyncTask(itemData,context);
    }

    public DownloadFileAsyncTask(FileListItemData itemData,Context context) {
        this.login = OptionsData.getInstance().getLogin();
        this.token = OptionsData.getInstance().getToken();
        this.server = OptionsData.getInstance().getServer();
        this.itemData = itemData;
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        APIProvider apiProvider = new APIProvider(server);
        Response<ResponseBody> response = null;

        response = apiProvider.downloadFile(login, token, itemData.getId());
        if (response == null) {
            Log.v(TAG, "Ответ пустой");
            return false;
        }

        if (response.isSuccessful()) {
            stream = response.body().byteStream();
            if (stream == null) {
                Log.v(TAG, "Поток пустой");
                return false;
            }
            return true;
        } else {
            Converter<ResponseBody, ServiceErrorResponse> converter = apiProvider.getRetrofit().
                    responseBodyConverter(ServiceErrorResponse.class, new Annotation[0]);
            ServiceErrorResponse errorResponse = null;
            try {
                errorResponse = converter.convert(response.errorBody());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.v(TAG, errorResponse.toString());
            return false;
        }

    }


    @Override
    protected void onPostExecute(Boolean success) {
        instance = null;
        if (success && writeFile()) {
            Toast.makeText(context, "Всё ок", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Не удалось выполнить операцию", Toast.LENGTH_LONG).show();
        }
    }

    private boolean writeFile() {
        LoadStreamAsyncTask loadStreamAsyncTask = new LoadStreamAsyncTask(itemData.getSubmittedFileName(), TAG);
        loadStreamAsyncTask.execute(stream);
        return true;
    }

    @Override
    protected void onCancelled() {
        instance = null;
    }
}
