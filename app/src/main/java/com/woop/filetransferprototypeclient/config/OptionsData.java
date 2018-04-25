package com.woop.filetransferprototypeclient.config;

/**
 * Created by NoID on 17.04.2018.
 */

public class OptionsData {
    private static OptionsData instance;


    private final String login;
    private final String token;
    private final String server;

    private OptionsData(String login, String token, String server) {
        this.login = login;
        this.token = token;
        this.server = server;
    }

    public static synchronized OptionsData getInstance(String login, String token, String server) {
        if (instance == null)
            instance = new OptionsData(login,token,server);
        return instance;
    }

    public static synchronized OptionsData getInstance() {
        return instance;
    }

    public static synchronized void removeInstance() {
        instance = null;
    }

    public String getLogin() {
        return login;
    }

    public String getToken() {
        return token;
    }

    public String getServer() {
        return server;
    }
}
