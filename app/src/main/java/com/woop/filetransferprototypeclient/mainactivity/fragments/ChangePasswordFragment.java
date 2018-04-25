package com.woop.filetransferprototypeclient.mainactivity.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.woop.filetransferprototypeclient.R;
import com.woop.filetransferprototypeclient.mainactivity.asynctasks.ChangePasswordAsyncTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {

    private View progressView;
    private View passFormView;

    private EditText onePasswordView;
    private EditText twoPasswordView;

    private final String TAG = ChangePasswordFragment.class.getSimpleName();
    private boolean isConfirmed = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_change_password, container, false);

        progressView = rootView.findViewById(R.id.pass_progress);
        passFormView = rootView.findViewById(R.id.pass_form);

        onePasswordView = rootView.findViewById(R.id.password_one);
        twoPasswordView = rootView.findViewById(R.id.password_two);

        CheckBox checkBox = rootView.findViewById(R.id.confirm);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isConfirmed = b;
            }
        });

        Button changePasswordButton = rootView.findViewById(R.id.change_button);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordAttempt();
            }
        });


        return rootView;
    }

    private boolean isPasswordValid(String password) {return password.length() > 6;}

    private void changePasswordAttempt() {
        onePasswordView.setError(null);
        twoPasswordView.setError(null);

        String onePassword = onePasswordView.getText().toString();
        String twoPassword = twoPasswordView.getText().toString();

        boolean cancel =  false;
        View focusView = null;

        if (!isConfirmed){
            Toast.makeText(getContext(), "Подтвердите операцию", Toast.LENGTH_LONG).show();
            return;
        }

        if (!onePassword.equals(twoPassword)) {
            onePasswordView.setError(getString(R.string.error_different_passwords));
            twoPasswordView.setError(getString(R.string.error_different_passwords));
            focusView = twoPasswordView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(onePassword) && !isPasswordValid(onePassword)) {
            onePasswordView.setError(getString(R.string.error_invalid_password));
            focusView = onePasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(onePassword)) {
            onePasswordView.setError(getString(R.string.error_field_required));
            focusView = onePasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(twoPassword)) {
            twoPasswordView.setError(getString(R.string.error_field_required));
            focusView = twoPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        }
        else {
            showProgress(true);
            final ChangePasswordAsyncTask asyncTask = ChangePasswordAsyncTask.getInstance(onePassword,getContext());
            if (asyncTask == null)
                return;

            asyncTask.execute();

            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    while (asyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            continue;
                        }
                    }
                    showProgress(false);
                }
            });
        }

    }




    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            passFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            passFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    passFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
        } else {
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            passFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
