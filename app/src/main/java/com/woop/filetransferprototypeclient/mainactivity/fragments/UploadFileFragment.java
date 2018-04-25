package com.woop.filetransferprototypeclient.mainactivity.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.woop.filetransferprototypeclient.R;
import com.woop.filetransferprototypeclient.api.APIProvider;
import com.woop.filetransferprototypeclient.api.responses.FileUploadResponse;
import com.woop.filetransferprototypeclient.api.responses.ServiceErrorResponse;
import com.woop.filetransferprototypeclient.config.OptionsData;
import com.woop.filetransferprototypeclient.mainactivity.entity.FileUploadMetaData;
import com.woop.filetransferprototypeclient.mainactivity.adapters.SwipeFileUploadListAdapter;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;


/**
 * Created by NoID on 04.03.2018.
 */

public class UploadFileFragment extends Fragment {

    private final int REQUEST_CHOOSER = 114;

    public Context context;


    private final String TAG = UploadFileFragment.class.getSimpleName();
    private List<FileUploadMetaData> dataList = new ArrayList<>();
    private ListView listView;
    private View progressView;
    private View formView;
    private TextView progressTextView;

    private UploadAsyncTask uploadAsyncTask = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_upload_file, container, false);
        context = rootView.getContext();

        progressTextView = rootView.findViewById(R.id.upload_text_progress);
        listView = rootView.findViewById(R.id.upload_files_list);
        progressView = rootView.findViewById(R.id.upload_progress);
        formView = rootView.findViewById(R.id.upload_form);

        Button uploadButton = (Button) rootView.findViewById(R.id.button_upload);
        Button chooseButton = (Button) rootView.findViewById(R.id.button_choose);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((SwipeLayout)(listView.getChildAt(position - listView.getFirstVisiblePosition()))).open(true);
            }
        });

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Выбор файла", Toast.LENGTH_SHORT).show();
                Intent getContentIntent = FileUtils.createGetContentIntent();
                Intent intent = Intent.createChooser(getContentIntent, "Выбор файла");

                startActivityForResult(intent, REQUEST_CHOOSER);

            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showDialog();
                attemptUpload();
            }

        });


                return rootView;
    }

    private void attemptUpload() {

        if (dataList.size() == 0){
            Toast.makeText(context, "Список файлов пуст", Toast.LENGTH_SHORT).show();
            return;
        }

        if (uploadAsyncTask != null)
            return;
        Toast.makeText(context, "Загрузка файлов", Toast.LENGTH_SHORT).show();
        String login = OptionsData.getInstance().getLogin();
        String token = OptionsData.getInstance().getToken();
        String server = OptionsData.getInstance().getServer();
        uploadAsyncTask = new UploadAsyncTask(login,token,server);
        uploadAsyncTask.execute((Void) null);


    }

    private void showDialog() {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Загрузка файлов")
                .setContentText("Вы уверены что хотите начать загрузку?")
                .setConfirmText("Да")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        attemptUpload();
                    }
                })
                .show();
    }

    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            formView.setVisibility(show ? View.GONE : View.VISIBLE);
            formView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    formView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            listView.setVisibility(show ? View.GONE : View.VISIBLE);
            listView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    listView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
            progressTextView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressTextView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressTextView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressTextView.setVisibility(show ? View.VISIBLE : View.GONE);
            formView.setVisibility(show ? View.GONE : View.VISIBLE);
            listView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    public synchronized void onActivityResult(final int requestCode, int resultCode, final Intent data) {

        if (resultCode == Activity.RESULT_OK) {

            final Uri uri = data.getData();
            String path = FileUtils.getPath(this.getContext(), uri);
            String name = FileUtils.getFile(this.getContext(), uri).getName();
            System.out.println(path);
            System.out.println(name);
            dataList.add(new FileUploadMetaData(name, path, uri));
            SwipeFileUploadListAdapter adapter = new SwipeFileUploadListAdapter(context, dataList);
            listView.setAdapter(adapter);
            adapter.setMode(Attributes.Mode.Single);


        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e(TAG, "Файл не выбран");
        }

    }

    public class UploadAsyncTask extends AsyncTask<Void, Integer, Integer> {

        private final String login;
        private final String token;
        private final String server;

        public UploadAsyncTask(String login, String token, String server) {
            this.login = login;
            this.token = token;
            this.server = server;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
            progressTextView.setText(String.format("Загружено 0 из %s файлов",dataList.size()));
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressTextView.setText(String.format("Загружено %s из %s файлов",values[0],dataList.size()));

        }

        @Override
        protected Integer doInBackground(Void... voids) {
            APIProvider apiProvider = new APIProvider(server);
            Response<FileUploadResponse> response = null;
            int count = 0;
            for (FileUploadMetaData fileUploadMetaData : dataList) {
                File file = FileUtils.getFile(getContext(), fileUploadMetaData.getUri());
                response = apiProvider.uploadFile(login, token, file);

                if (response == null) {
                    Log.v(TAG, "Ответ пустой");
                    continue;
                }
                if (response.isSuccessful()) {
                    count++;
                    publishProgress(count);
                    Log.v(TAG, fileUploadMetaData.getName() + " загружен");
                }
                else {
                    Converter<ResponseBody,ServiceErrorResponse> converter = apiProvider.getRetrofit().
                            responseBodyConverter(ServiceErrorResponse.class,new Annotation[0]);
                    ServiceErrorResponse errorResponse = null;
                    try {
                        errorResponse = converter.convert(response.errorBody());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.v(TAG, fileUploadMetaData.getName() + errorResponse.toString());

                }

            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return count;

        }

        @Override
        protected void onPostExecute(Integer integer) {
            showProgress(false);
            uploadAsyncTask = null;
            if (integer > 0)
                Toast.makeText(getContext(), String.format("Загружено %s из %s файлов",integer,dataList.size()), Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getContext(), "Не удалось выполнить операцию", Toast.LENGTH_LONG).show();
            dataList.clear();

        }

        @Override
        protected void onCancelled() {
            showProgress(false);
            uploadAsyncTask = null;
        }


    }

}
