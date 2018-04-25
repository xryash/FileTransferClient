package com.woop.filetransferprototypeclient.tabactivity.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import java.lang.annotation.Annotation;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.woop.filetransferprototypeclient.R;
import com.woop.filetransferprototypeclient.api.APIProvider;
import com.woop.filetransferprototypeclient.config.OptionsData;
import com.woop.filetransferprototypeclient.config.OptionsDataLoader;
import com.woop.filetransferprototypeclient.api.responses.ServiceErrorResponse;
import com.woop.filetransferprototypeclient.api.responses.VerifyAccountResponse;
import com.woop.filetransferprototypeclient.mainactivity.MainActivity;


import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by NoID on 24.02.2018.
 */

public class FragmentTabLogin extends Fragment {

    private AutoCompleteTextView loginView;
    private AutoCompleteTextView serverView;
    private EditText passwordView;
    private View mProgressView;
    private View mLoginFormView;

    private final String TAG = FragmentTabLogin.class.getSimpleName();
    private UserLoginTask mAuthTask = null;
    private boolean isRemembered = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tab_login, container, false);
        loginView = rootView.findViewById(R.id.login);
        serverView = rootView.findViewById(R.id.server);
        passwordView = rootView.findViewById(R.id.password);
        mLoginFormView = rootView.findViewById(R.id.login_form);
        mProgressView = rootView.findViewById(R.id.login_progress);

        isSharedDataExist();

        CheckBox checkBox = rootView.findViewById(R.id.remember);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isRemembered = b;
            }
        });

        Button signInButton = (Button) rootView.findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        return rootView;

    }



    private void isSharedDataExist() {
        final Activity activity = this.getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgress(true);
                SharedPreferences preferences = activity.getSharedPreferences(OptionsDataLoader.APP_PREFERENCES, Context.MODE_PRIVATE);
                OptionsDataLoader loader = new OptionsDataLoader(preferences);
                if (loader.loadData()) {
                    Toast.makeText(getContext(), "Данные есть", Toast.LENGTH_LONG).show();
                    goToNextActivity();
                }
                else
                    Toast.makeText(getContext(), "Данных нет", Toast.LENGTH_LONG).show();

                showProgress(false);
            }
        });


    }

    private String formatServerAddress(String server) {
        if (server.contains(":"))
            return String.format("http://%s/app/",server);
        else
            return String.format("http://%s:8080/app/",server);
    }

    private boolean isPasswordValid(String password) {return password.length() > 6;}

    private void attemptLogin() {

        loginView.setError(null);
        passwordView.setError(null);
        serverView.setError(null);

        String login = loginView.getText().toString();
        String onePassword = passwordView.getText().toString();
        String server = serverView.getText().toString();

        boolean cancel =  false;
        View focusView = null;

        if (!TextUtils.isEmpty(onePassword) && !isPasswordValid(onePassword)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(login)) {
            loginView.setError(getString(R.string.error_field_required));
            focusView = loginView;
            cancel = true;
        }

        if (TextUtils.isEmpty(server)) {
            serverView.setError(getString(R.string.error_field_required));
            focusView = serverView;
            cancel = true;
        }

        if (cancel) {

            focusView.requestFocus();
        }
        else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(login, onePassword,formatServerAddress(server));
            mAuthTask.execute((Void) null);
        }
    }



    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void setDataWithSaving(String login,String token,String server) {
        SharedPreferences preferences = this.getActivity().getSharedPreferences(OptionsDataLoader.APP_PREFERENCES, Context.MODE_PRIVATE);
        OptionsDataLoader loader = new OptionsDataLoader(preferences);
        loader.saveData(login,token,server);
        OptionsData.getInstance(login,token,server);
    }

    private void setDataWithoutSaving(String login,String token,String server) {
        OptionsData.getInstance(login,token,server);
    }

    private void goToNextActivity() {
        Intent intent = new Intent(this.getContext(), MainActivity.class);
        startActivity(intent);
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String login;
        private final String password;
        private final String server;
        private String token;

        UserLoginTask(String login, String password, String server) {
            this.login = login;
            this.password = password;
            this.server = server;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            APIProvider apiProvider = new APIProvider(server);

            Response<VerifyAccountResponse> response = null;
            try {
                response = apiProvider.verifyAccount(login,password);
                if (response == null) {
                    Log.v(TAG, "Ответ пустой");
                    return false;
                }
                Log.v(TAG, String.valueOf(response.code()));

                if (response.isSuccessful()) {
                    Log.v(TAG, response.body().toString());
                    String data  = response.body().getData();
                    String decodedData =
                    new String(Base64.decode(data,Base64.NO_WRAP), "UTF-8");
                    Log.v(TAG, decodedData);
                    token = decodedData;
                }
                else {
                    Converter<ResponseBody,ServiceErrorResponse> converter = apiProvider.getRetrofit().
                            responseBodyConverter(ServiceErrorResponse.class,new Annotation[0]);
                    ServiceErrorResponse errorResponse = converter.convert(response.errorBody());
                    Log.v(TAG, errorResponse.toString());
                    return false;
                }

            } catch ( IOException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            showProgress(false);
            mAuthTask = null;
            if (success){
                Toast.makeText(getContext(), "Всё ок", Toast.LENGTH_LONG).show();
                if (isRemembered)
                    setDataWithSaving(login,token,server);
                else
                    setDataWithoutSaving(login,token,server);
                goToNextActivity();
            }
            else
                Toast.makeText(getContext(), "Не удалось выполнить операцию", Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
            mAuthTask = null;
        }
    }

}
