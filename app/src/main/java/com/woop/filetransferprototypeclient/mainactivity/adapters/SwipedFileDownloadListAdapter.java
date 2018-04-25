package com.woop.filetransferprototypeclient.mainactivity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.woop.filetransferprototypeclient.R;
import com.woop.filetransferprototypeclient.mainactivity.asynctasks.DeleteFileAsyncTask;
import com.woop.filetransferprototypeclient.mainactivity.asynctasks.DownloadFileAsyncTask;
import com.woop.filetransferprototypeclient.mainactivity.entity.FileListItemData;

import java.util.List;

/**
 * Created by NoID on 16.04.2018.
 */

public class SwipedFileDownloadListAdapter extends BaseSwipeAdapter {
    private Context context;
    private List<FileListItemData> dataList;

    public SwipedFileDownloadListAdapter(Context context, List<FileListItemData> data) {
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
    public int getSwipeLayoutResourceId(int position) {
        return R.id.download_swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.item_download_files_list, null);
        return view;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        final TextView nameView, pathView;
        nameView = convertView.findViewById(R.id.nameView);
        pathView = convertView.findViewById(R.id.pathView);
        final FileListItemData fileListItemData = dataList.get(position);
        nameView.setText(fileListItemData.getSubmittedFileName());
        pathView.setText(preparePath(fileListItemData.getDirectory()));

        final SwipeLayout swipeLayout = convertView.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, convertView.findViewWithTag("Bottom1"));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                //YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });

        convertView.findViewById(R.id.load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close();
                downloadFile(position);
            }
        });

        convertView.findViewById(R.id.trash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close();
                deleteFile(position);
            }
        });
    }

    private void downloadFile(int position) {
        FileListItemData item = dataList.get(position);
        DownloadFileAsyncTask asyncTask = DownloadFileAsyncTask.getInstance(item,context);
        if (asyncTask == null) {
            Toast.makeText(context, "Предыдущая операция не завершена", Toast.LENGTH_SHORT).show();
            return;
        }
        asyncTask.execute();
    }

    private void deleteFile(int position) {
        int id = dataList.get(position).getId();
        DeleteFileAsyncTask asyncTask = DeleteFileAsyncTask.getInstance(id,position,context,dataList,this);
        if (asyncTask == null) {
            Toast.makeText(context, "Предыдущая операция не завершена", Toast.LENGTH_SHORT).show();
            return;
        }
        asyncTask.execute();
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

}
