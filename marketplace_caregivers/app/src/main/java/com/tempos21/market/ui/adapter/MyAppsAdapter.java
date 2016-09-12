package com.tempos21.market.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tempos21.market.client.bean.MyAppInfo;
import com.tempos21.market.ui.imageLoader.ViewHolder;
import com.worldline.alfredo.R;

import java.util.ArrayList;


public class MyAppsAdapter extends ArrayAdapter<MyAppInfo> {

    private ArrayList<MyAppInfo> myApps;
    private Activity context;

    public MyAppsAdapter(Activity context, ArrayList<MyAppInfo> apps) {
        super(context, R.layout.item_myapps, apps);
        this.myApps = apps;
        this.context = context;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            holder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_myapps, null);

            holder.nameApp = (TextView) convertView.findViewById(R.id.nameApp);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.nameApp.setText(myApps.get(position).getAppName());

        return convertView;

    }

}
