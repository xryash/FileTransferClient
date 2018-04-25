package com.woop.filetransferprototypeclient.mainactivity.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.woop.filetransferprototypeclient.api.APIProvider;
import com.woop.filetransferprototypeclient.api.responses.ServiceErrorResponse;
import com.woop.filetransferprototypeclient.config.OptionsData;
import com.woop.filetransferprototypeclient.mainactivity.adapters.SwipedFileDownloadListAdapter;
import com.woop.filetransferprototypeclient.mainactivity.entity.FileListItemData;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by NoID on 22.04.2018.
 */

public class DeleteFileAsyncTask extends AsyncTask<Void, Void, Boolean>  {




    private final String login;
    private final String token;
    private final String server;
    private final int id;
    private final int position;
    private final String TAG = DeleteFileAsyncTask.class.getSimpleName();
    private final  Context context;
    private SwipedFileDownloadListAdapter adapter;
    private List<FileListItemData> dataList;


    private static DeleteFileAsyncTask instance;

    public synchronized static DeleteFileAsyncTask getInstance(int id, int position, Context context,List<FileListItemData> dataList,SwipedFileDownloadListAdapter adapter) {
        if (instance != null)
            return null;
        return new DeleteFileAsyncTask(id,position,context,dataList,adapter);
    }


    public DeleteFileAsyncTask(int id, int position, Context context,List<FileListItemData> dataList,SwipedFileDownloadListAdapter adapter) {
        this.login = OptionsData.getInstance().getLogin();
        this.token = OptionsData.getInstance().getToken();
        this.server = OptionsData.getInstance().getServer();
        this.id = id;
        this.context = context;
        this.dataList = dataList;
        this.adapter = adapter;
        this.position = position;
    }



    @Override
    protected Boolean doInBackground(Void... voids) {
        APIProvider apiProvider = new APIProvider(server);
        Response response = null;
        response = apiProvider.deleteFile(login,token,id);
        if (response == null) {
            Log.v(TAG, "Ответ пустой");
            return false;
        }

        if (response.isSuccessful()) {
            return true;
        } else {
            Converter<ResponseBody, ServiceErrorResponse> converter = apiProvider.getRetrofit().
                    responseBodyConverter(ServiceErrorResponse.class, new Annotation[0]);
            ServiceErrorResponse errorResponse = null;
            try {
                errorResponse = converter.convert(response.errorBody());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            Log.v(TAG, errorResponse.toString());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);
        instance = null;
        if (success) {
            Toast.makeText(context, "Всё ок", Toast.LENGTH_LONG).show();
            dataList.remove(position);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(context, "Не удалось выполнить операцию", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        instance = null;
    }
}
