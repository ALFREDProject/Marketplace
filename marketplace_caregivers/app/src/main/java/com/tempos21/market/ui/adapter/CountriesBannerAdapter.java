package com.tempos21.market.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tempos21.market.client.bean.Countries;
import com.tempos21.market.client.bean.Country;
import com.tempos21.market.ui.imageLoader.ViewHolder;
import com.tempos21.market.util.TLog;
import com.worldline.alfredo.R;

public class CountriesBannerAdapter extends ArrayAdapter<Country> {

    private Countries countries;
    private Activity context;
    private int selected = -1;

    public CountriesBannerAdapter(Activity context, Countries countries) {
        super(context, R.layout.item_country, countries);
        this.context = context;
        this.countries = countries;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null
                || !(convertView.getTag() instanceof ViewHolder)) {
            holder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_country_banner, null);

            holder.nameCountry = (TextView) convertView
                    .findViewById(R.id.nameCountry);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameCountry.setText(countries.get(position).getName());
        TLog.e("Position: " + position + "\tSelected: " + selected);

        return convertView;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

}
