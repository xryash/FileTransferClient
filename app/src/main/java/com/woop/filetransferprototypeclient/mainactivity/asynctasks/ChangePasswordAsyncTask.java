package com.woop.filetransferprototypeclient.mainactivity.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.woop.filetransferprototypeclient.api.APIProvider;
import com.woop.filetransferprototypeclient.api.responses.ServiceErrorResponse;
import com.woop.filetransferprototypeclient.config.OptionsData;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;


/**
 * Created by NoID on 22.04.2018.
 */

public class ChangePasswordAsyncTask extends AsyncTask<Void,Void,Boolean>{

    private final String login;
    private final String token;
    private final String server;
    private final String newpassword;
    private final Context context;
    private final String TAG = ChangePasswordAsyncTask.class.getSimpleName();

    private static ChangePasswordAsyncTask instance;

    public static ChangePasswordAsyncTask getInstance(String newpassword,Context context) {
        if (instance != null)
            return null;
        return new ChangePasswordAsyncTask(newpassword, context);
    }

    private ChangePasswordAsyncTask(String newpassword, Context context) {
        this.context = context;
        this.login = OptionsData.getInstance().getLogin();
        this.token = OptionsData.getInstance().getToken();
        this.server = OptionsData.getInstance().getServer();
        this.newpassword = newpassword;
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        APIProvider apiProvider = new APIProvider(server);
        Response response = null;

        response = apiProvider.changePassword(login,token,newpassword);
        if (response == null) {
            Log.v(TAG, "Ответ пустой");
            return false;
        }
        if (response.isSuccessful())
            return true;
        else {
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
