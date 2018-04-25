package com.woop.filetransferprototypeclient.mainactivity.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.woop.filetransferprototypeclient.R;
import com.woop.filetransferprototypeclient.api.APIProvider;
import com.woop.filetransferprototypeclient.api.responses.ServiceErrorResponse;
import com.woop.filetransferprototypeclient.config.OptionsData;
import com.woop.filetransferprototypeclient.mainactivity.asynctasks.LoadStreamAsyncTask;
import com.woop.filetransferprototypeclient.mainactivity.entity.FileListItemData;
import com.woop.filetransferprototypeclient.mainactivity.adapters.SwipedFileDownloadListAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;


/**
 * Created by NoID on 04.03.2018.
 */

public class DownloadFileFragment extends Fragment {

    private ListLoadAsyncTask mLoadListTask = null;

    private final static String TAG = DownloadFileFragment.class.getSimpleName();
    private ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_download_file, container, false);
        listView = rootView.findViewById(R.id.files_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout)(listView.getChildAt(position - listView.getFirstVisiblePosition()))).open(true);
            }
        });

        Button button = (Button) rootView.findViewById(R.id.button_update);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadlist();
            }
        });

        return rootView;
    }

    private void loadlist() {
        mLoadListTask = new ListLoadAsyncTask();
        mLoadListTask.execute((Void) null);
    }

    private class ListLoadAsyncTask extends AsyncTask<Void, Void, Integer> {
        private final String login;
        private final String token;
        private final String server;
        private List<FileListItemData> data = null;

        public ListLoadAsyncTask() {
            this.login = OptionsData.getInstance().getLogin();
            this.token = OptionsData.getInstance().getToken();
            this.server = OptionsData.getInstance().getServer();
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            APIProvider apiProvider = new APIProvider(server);
            Response<List<FileListItemData>> response = null;
            response = apiProvider.getFileList(login, token);

            if (response == null) {
                Log.v(TAG, "Ответ пустой");
                return 0;
            }
            if (response.isSuccessful()) {
                data = response.body();
                if (data == null) {
                    Log.v(TAG, "Лист не существует");
                    return 0;
                } else if (data.isEmpty()) {
                    Log.v(TAG, "Лист пустой");
                    return 1;
                }
                return 2;
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
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer value) {
            mLoadListTask = null;
            switch (value) {
                case 0:
                    Toast.makeText(getContext(), "Не удалось выполнить операцию", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(getContext(), "У данного пользователя нет файлов", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    SwipedFileDownloadListAdapter adapter = new SwipedFileDownloadListAdapter(getContext(), data);
                    listView.setAdapter(adapter);
                    break;

            }
        }

        @Override
        protected void onCancelled() {
            mLoadListTask = null;
        }
    }

    /**
    private class DownloadFileAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private final String login;
        private final String token;
        private final String server;
        private final FileListItemData itemData;
        private InputStream stream = null;

        public DownloadFileAsyncTask(FileListItemData itemData) {
            this.login = OptionsData.getInstance().getLogin();
            this.token = OptionsData.getInstance().getToken();
            this.server = OptionsData.getInstance().getServer();
            this.itemData = itemData;
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
            mDownloadFileAsyncTask = null;
            if (success && writeFile()) {
                Toast.makeText(getContext(), "Всё ок", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Не удалось выполнить операцию", Toast.LENGTH_LONG).show();
            }
        }

        private boolean writeFile() {
            LoadStreamAsyncTask loadStreamAsyncTask = new LoadStreamAsyncTask(itemData.getSubmittedFileName(), TAG);
            loadStreamAsyncTask.execute(stream);
            return true;
        }

        @Override
        protected void onCancelled() {
            mDownloadFileAsyncTask = null;
        }
    }
    **/

}
