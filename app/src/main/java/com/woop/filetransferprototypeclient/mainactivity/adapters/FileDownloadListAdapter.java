package com.woop.filetransferprototypeclient.mainactivity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.woop.filetransferprototypeclient.R;
import com.woop.filetransferprototypeclient.mainactivity.entity.FileListItemData;

import java.util.List;

/**
 * Created by NoID on 27.03.2018.
 */

public class FileDownloadListAdapter  extends BaseAdapter {
    private Context context;
    private List<FileListItemData> dataList;

    public FileDownloadListAdapter(Context context, List<FileListItemData> data) {
        this.dataList = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public FileListItemData getItem(int position) {
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
                    inflate(R.layout.item_download_files_list, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final FileListItemData fileListItemData = dataList.get(position);
        viewHolder.nameView.setText(fileListItemData.getSubmittedFileName());


        return convertView;
    }

    private class ViewHolder {
        final TextView nameView;

        ViewHolder(View view){
            nameView = view.findViewById(R.id.nameView);
        }

    }



}
