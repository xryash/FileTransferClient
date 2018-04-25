package com.woop.filetransferprototypeclient.mainactivity.entity;

import android.net.Uri;

/**
 * Created by NoID on 23.03.2018.
 */

public class FileUploadMetaData {
    private final String name;
    private final String path;
    private final Uri uri;

    public FileUploadMetaData(String name, String path, Uri uri) {
        this.name = name;
        this.path = path;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Uri getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "FileUploadMetaData{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", uri=" + uri +
                '}';
    }
}
