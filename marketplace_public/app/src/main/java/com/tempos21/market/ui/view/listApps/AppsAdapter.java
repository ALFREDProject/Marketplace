package com.tempos21.market.ui.view.listApps;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.tempos21.market.Constants;
import com.tempos21.mymarket.sdk.model.app.App;
import com.worldline.alfredo.R;

import java.util.List;

public class AppsAdapter extends ArrayAdapter<App> {

    private DisplayImageOptions options;
    private List<App> apps;
    private Context context;


    public AppsAdapter(Context context, List<App> apps) {
        super(context, R.layout.item_apps_grid, apps);
        this.apps = apps;
        this.context = context;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_image) // resource or drawable
                .showImageForEmptyUri(R.drawable.no_image) // resource or drawable
                .showImageOnFail(R.drawable.no_image) // resource or drawable
                .resetViewBeforeLoading(true)  // default
                .delayBeforeLoading(0)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .considerExifParams(false) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new SimpleBitmapDisplayer()) // default
                .handler(new Handler()) // default
                .build();

    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        ViewHolder vh;
        v = buildView(convertView);
        vh = findViews(v);

        App app = apps.get(position);
        setData(vh, app);
        return v;
    }


    private View buildView(View convertView) {
        View v;
        if (convertView != null) {
            v = convertView;
        } else {
            LayoutInflater vi = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_apps_grid, null);
        }
        return v;
    }


    private ViewHolder findViews(View v) {
        ViewHolder vh;
        if (v.getTag() != null) {
            vh = (ViewHolder) v.getTag();
        } else {
            vh = new ViewHolder();
            vh.appIcon = (ImageView) v.findViewById(R.id.appIcon);
            vh.appTitle = (TextView) v.findViewById(R.id.appTitle);

            v.setTag(vh);
        }
        return vh;
    }


    private void setData(ViewHolder vh, App app) {
        vh.appTitle.setText(app.name);
        ImageLoader.getInstance().displayImage(Constants.GET_IMAGE_ICON + app.iconUrl, vh.appIcon, options);
        vh.id = "" + app.id;
    }


    private class ViewHolder {
        public ImageView appIcon;
        public TextView appTitle;
        public String id;
    }

}
