package com.tempos21.market.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.tempos21.market.Constants;
import com.worldline.alfredo.R;

import java.util.List;


public class ScreenshotsAdapter extends ArrayAdapter<String> {

    private final DisplayImageOptions options;
    private List<String> screenshots;
    private Context context;


    public ScreenshotsAdapter(Context context, List<String> screenshots) {
        super(context, R.layout.item_screenshot, screenshots);
        this.screenshots = screenshots;
        this.context = context;

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.no_image) // resource or drawable
                .showImageForEmptyUri(R.drawable.no_image) // resource or drawable
                .showImageOnFail(R.drawable.no_image) // resource or drawable
                .resetViewBeforeLoading(false)  // default
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

        String screenshot = screenshots.get(position);
        setData(vh, screenshot);
        return v;
    }

    private View buildView(View convertView) {
        View v;
        if (convertView != null) {
            v = convertView;
        } else {
            LayoutInflater vi = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.item_screenshot, null);
        }
        return v;
    }

    private ViewHolder findViews(View v) {
        ViewHolder vh;
        if (v.getTag() != null) {
            vh = (ViewHolder) v.getTag();
        } else {
            vh = new ViewHolder();
            vh.screenshotImage = (ImageView) v.findViewById(R.id.screenshotImage);
            v.setTag(vh);
        }
        return vh;
    }

    private void setData(ViewHolder vh, String screenshot) {
        if (!screenshot.equalsIgnoreCase("")) {
            ImageLoader.getInstance().displayImage(Constants.GET_IMAGE_MEDIA + screenshot, vh.screenshotImage, options);
            vh.screenshotImage.setVisibility(View.VISIBLE);
        }
    }


    private class ViewHolder {
        public ImageView screenshotImage;
    }

}
