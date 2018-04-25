package com.woop.filetransferprototypeclient.mainactivity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.woop.filetransferprototypeclient.R;
import com.woop.filetransferprototypeclient.mainactivity.entity.FileUploadMetaData;

import java.util.List;

/**
 * Created by NoID on 21.03.2018.
 */

public class FileUploadListAdapter extends BaseAdapter {
    private Context context;
    private List<FileUploadMetaData> dataList;

    public FileUploadListAdapter(Context context,List<FileUploadMetaData> data) {
        this.dataList = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public FileUploadMetaData getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.item_upload_files_list, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final FileUploadMetaData fileUploadMetaData = dataList.get(position);
        viewHolder.nameView.setText(fileUploadMetaData.getName());
        viewHolder.pathView.setText(preparePath(fileUploadMetaData.getPath()));


        return convertView;

    }

    private String preparePath(String str) {
        String[] array = str.split("/");
        String buff = "/";
        int step = 3;
        int i = array.length -2;
        while (step != 0) {
            buff = "/" + array[i] +buff;
            step--;
            i--;
        }
        buff = "..." + buff;
        System.out.println(buff);
        return buff;
    }


    private class ViewHolder {
        final TextView nameView, pathView;

        ViewHolder(View view){
            nameView = view.findViewById(R.id.nameView);
            pathView = view.findViewById(R.id.pathView);
        }

    }



}

