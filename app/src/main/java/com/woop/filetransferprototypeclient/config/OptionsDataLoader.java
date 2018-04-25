package com.woop.filetransferprototypeclient.config;

import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by NoID on 17.04.2018.
 */

public class OptionsDataLoader {
    public static final String APP_PREFERENCES = "settings";
    private static final String APP_PREFERENCES_LOGIN = "login";
    private static final String APP_PREFERENCES_TOKEN = "token";
    private static final String APP_PREFERENCES_SERVER = "server";

    private SharedPreferences settings;

    public OptionsDataLoader(SharedPreferences settings) {
        this.settings = settings;
    }

    public void saveData(String login, String token, String server) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(APP_PREFERENCES_LOGIN, login);
        editor.putString(APP_PREFERENCES_TOKEN, token);
        editor.putString(APP_PREFERENCES_SERVER, server);
        editor.apply();
    }

    public boolean loadData() {
        String login = null,token = null,server = null;

        if (settings.contains(APP_PREFERENCES_LOGIN)) {
            login = settings.getString(APP_PREFERENCES_LOGIN, "");
        }

        if (settings.contains(APP_PREFERENCES_TOKEN)) {
            token = settings.getString(APP_PREFERENCES_TOKEN, "");
        }

        if (settings.contains(APP_PREFERENCES_SERVER)) {
            server = settings.getString(APP_PREFERENCES_SERVER, "");
        }

        if (login!= null && !TextUtils.isEmpty(login) && token!= null && !TextUtils.isEmpty(token) && server!= null && !TextUtils.isEmpty(server)) {
            OptionsData.getInstance(login,token,server);
            return true;
        }
        return  false;
    }

    public void deleteData() {
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(APP_PREFERENCES_LOGIN);
        editor.remove(APP_PREFERENCES_TOKEN);
        editor.remove(APP_PREFERENCES_SERVER);
        editor.apply();
    }
}
