package com.tempos21.market.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.tempos21.ioutils.filecache.FileLoaderQueue;
import com.tempos21.ioutils.filecache.FileLoaderQueue.OnFileLoadedListener;
import com.tempos21.market.Constants;
import com.tempos21.market.util.TLog;
import com.worldline.alfredo.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ScreenshotsAdapter extends ArrayAdapter<String> implements
        OnFileLoadedListener {

    private ArrayList<String> screenshots;
    private HashMap<String, Bitmap> bitmaps;
    private Context context;

    private FileLoaderQueue bitmapQueue;

    public ScreenshotsAdapter(Context context, ArrayList<String> screenshots) {
        super(context, R.layout.item_screenshot, screenshots);
        this.screenshots = screenshots;
        this.context = context;
        this.bitmaps = new HashMap<String, Bitmap>();
        bitmapQueue = new FileLoaderQueue(context,
                context.getString(R.string.app_name));
        bitmapQueue.setOnFileLoadedListener(this);

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
            LayoutInflater vi = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            vh.screenshotProgress = (ProgressBar) v.findViewById(R.id.screenshotProgress);
            v.setTag(vh);
        }
        return vh;
    }

    private void setData(ViewHolder vh, String screenshot) {
        if (bitmaps.get(screenshot) != null) {
            vh.screenshotImage.setImageBitmap(bitmaps.get(screenshot));
            vh.screenshotImage.setVisibility(View.VISIBLE);
            vh.screenshotProgress.setVisibility(View.INVISIBLE);
        } else {
            bitmapQueue.loadFile(screenshot, Constants.GET_IMAGE_MEDIA + screenshot);
            TLog.v(screenshot);
            vh.screenshotImage.setVisibility(View.INVISIBLE);
            vh.screenshotProgress.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onFileLoaded(String id, Bitmap bitmap) {
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image);
        }
        storeIcon(id, bitmap);
        notifyDataSetChanged();
    }

    private void storeIcon(String id, Bitmap bitmap) {
        bitmaps.put(id, bitmap);
    }

    private class ViewHolder {
        public ImageView screenshotImage;
        public ProgressBar screenshotProgress;
    }

}
