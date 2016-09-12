package com.tempos21.market.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tempos21.market.client.bean.Countries;
import com.tempos21.market.client.bean.Country;
import com.tempos21.market.ui.imageLoader.ViewHolder;
import com.worldline.alfredo.R;


public class CountriesAdapter extends ArrayAdapter<Country> {

    private Countries countries;
    private Activity context;


    public CountriesAdapter(Activity context, Countries countries) {
        super(context, R.layout.item_country, countries);
        this.context = context;
        this.countries = countries;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            holder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_country, null);

            holder.linearTitle = (LinearLayout) convertView.findViewById(R.id.linearTitle);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.linearCountry = (LinearLayout) convertView.findViewById(R.id.linearCountry);
            holder.nameCountry = (TextView) convertView.findViewById(R.id.nameCountry);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String myLetter = countries.get(position).getName().substring(0, 1).toUpperCase();
        String otherLetter = "";
        if (position > 0) {
            otherLetter = countries.get(position - 1).getName().substring(0, 1).toUpperCase();
        }

        if (position == 0 || otherLetter.compareTo(myLetter) != 0) {
            holder.linearTitle.setVisibility(View.VISIBLE);
            holder.title.setText(myLetter);
            holder.nameCountry.setText(countries.get(position).getName());

        } else {
            holder.linearTitle.setVisibility(View.GONE);
            holder.nameCountry.setText(countries.get(position).getName());
        }

        return convertView;
    }

}
