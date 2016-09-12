package com.tempos21.market.ui.imageLoader;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ViewHolder {
    // AppsAdapter
    public ImageView promoImage;
    public ProgressBar appProgress;

    // UpdatesAdapter
    public ImageView iconImage;
    public TextView nameUpdate;
    public ProgressBar updateProgress;

    // MyAppsAdapter
    public TextView nameApp;

    // CountriesAdapter
    public LinearLayout linearTitle;
    public LinearLayout linearCountry;
    public TextView nameCountry;
    public TextView title;

    // CategoriesAdapter
    public TextView nameCategory;
}
