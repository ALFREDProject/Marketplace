package com.tempos21.market.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tempos21.market.client.bean.Category;
import com.tempos21.market.ui.imageLoader.ViewHolder;
import com.worldline.alfredo.R;

import java.util.ArrayList;


public class CategoriesAdapter extends ArrayAdapter<Category> {

    private ArrayList<Category> categories;
    private Activity context;


    public CategoriesAdapter(Activity context, ArrayList<Category> cat) {
        super(context, R.layout.item_categories, cat);
        this.categories = cat;
        this.context = context;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            holder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_categories, null);

            holder.nameCategory = (TextView) convertView.findViewById(R.id.nameCategory);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameCategory.setText(categories.get(position).getName());

        return convertView;

    }

}
