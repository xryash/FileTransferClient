package com.woop.filetransferprototypeclient.mainactivity.asynctasks;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by NoID on 06.04.2018.
 */

public class LoadStreamAsyncTask extends AsyncTask<InputStream, Void, Boolean> {
    final String appDirectoryName = "FileTransferClient";
    final File root = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS), appDirectoryName);
    final String fileName;
    final String TAG;

    public LoadStreamAsyncTask(String fileName, String TAG) {
        this.fileName = fileName;
        this.TAG = TAG;
    }


    @Override
    protected Boolean doInBackground(InputStream... params) {
        InputStream inputStream = params[0];
        root.mkdirs();
        File file = new File(root, fileName);
        OutputStream output = null;

        try {
            output = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read;
            Log.d(TAG, "Attempting to write to: " + root + "/" + fileName);
            while ((read = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, read);
                Log.v(TAG, "Writing to buffer to output stream.");
            }
            Log.d(TAG, "Flushing output stream.");
            output.flush();
            Log.d(TAG, "Output flushed.");
        } catch (IOException e) {
            Log.e(TAG, "IO Exception: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                if (output != null) {
                    output.close();
                    Log.d(TAG, "Output stream closed sucessfully.");
                }
                else{
                    Log.d(TAG, "Output stream is null");
                }
            } catch (IOException e){
                Log.e(TAG, "Couldn't close output stream: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        Log.d(TAG, "Download success: " + result);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}
