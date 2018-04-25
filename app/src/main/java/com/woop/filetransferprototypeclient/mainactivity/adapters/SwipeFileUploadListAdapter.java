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
import com.woop.filetransferprototypeclient.mainactivity.entity.FileUploadMetaData;

import java.util.List;

/**
 * Created by NoID on 09.04.2018.
 */



public class SwipeFileUploadListAdapter extends BaseSwipeAdapter {
    private Context context;
    private List<FileUploadMetaData> dataList;

    public SwipeFileUploadListAdapter(Context context, List<FileUploadMetaData> data) {
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
    public int getSwipeLayoutResourceId(int position) {
        return R.id.upload_swipe;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.item_upload_files_list, null);
        return view;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        final TextView nameView, pathView;
        nameView = convertView.findViewById(R.id.nameView);
        pathView = convertView.findViewById(R.id.pathView);
        final FileUploadMetaData fileUploadMetaData = dataList.get(position);
        nameView.setText(fileUploadMetaData.getName());
        pathView.setText(preparePath(fileUploadMetaData.getPath()));

        final SwipeLayout swipeLayout = convertView.findViewById(getSwipeLayoutResourceId(position));
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                //YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
                Toast.makeText(context, "onOpen", Toast.LENGTH_SHORT).show();
            }
        });



        swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
            @Override
            public void onDoubleClick(SwipeLayout layout, boolean surface) {
                Toast.makeText(context, "DoubleClick", Toast.LENGTH_SHORT).show();
            }
        });


        convertView.findViewById(R.id.button_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLayout.close();
                dataList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "click delete", Toast.LENGTH_SHORT).show();
            }
        });

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