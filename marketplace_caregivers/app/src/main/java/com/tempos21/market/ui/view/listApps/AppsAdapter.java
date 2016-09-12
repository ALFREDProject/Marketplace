package com.tempos21.market.ui.view.listApps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tempos21.ioutils.filecache.FileLoaderQueue;
import com.tempos21.ioutils.filecache.FileLoaderQueue.OnFileLoadedListener;
import com.tempos21.market.Constants;
import com.tempos21.market.client.bean.AppWithImage;
import com.tempos21.market.client.bean.AppsWithImage;
import com.tempos21.market.ui.view.RoundeCornersImage;
import com.worldline.alfredo.R;

public class AppsAdapter extends ArrayAdapter<AppWithImage> implements
        OnFileLoadedListener {

    private AppsWithImage apps;
    private Context context;
    private boolean listMode;
    private FileLoaderQueue bitmapQueue;
    private Bitmap noImageBitmap;
    private ViewGroup parent;


    public AppsAdapter(Context context, AppsWithImage apps, boolean listMode) {
        super(context, (listMode) ? R.layout.item_apps_list
                : R.layout.item_apps_grid, apps);
        this.listMode = listMode;
        this.apps = apps;
        this.context = context;
        noImageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image);
        bitmapQueue = new FileLoaderQueue(context,
                context.getString(R.string.app_name));
        bitmapQueue.setOnFileLoadedListener(this);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        this.parent = parent;
        View v;
        ViewHolder vh;
        v = buildView(convertView);
        vh = findViews(v);

        AppWithImage app = apps.get(position);
        setData(vh, app);
        return v;
    }

    private View buildView(View convertView) {
        View v;
        if (convertView != null) {
            v = convertView;
        } else {
            LayoutInflater vi = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate((listMode) ? R.layout.item_apps_list
                    : R.layout.item_apps_grid, null);
        }
        return v;
    }

    private ViewHolder findViews(View v) {
        ViewHolder vh;
        if (v.getTag() != null) {
            vh = (ViewHolder) v.getTag();
        } else {
            vh = new ViewHolder();
            vh.appIcon = (RoundeCornersImage) v.findViewById(R.id.appIcon);
            vh.appTitle = (TextView) v.findViewById(R.id.appTitle);
            vh.appRating = (RatingBar) v.findViewById(R.id.appRating);

            v.setTag(vh);
        }
        return vh;
    }

    private void setData(ViewHolder vh, AppWithImage app) {
        vh.appTitle.setText(app.getName());
        vh.appRating.setRating((float) app.getRating());
        if (app.getIcon() != null) {
            vh.appIcon.setImage(app.getIcon());
        } else {
            vh.appIcon.setImage(noImageBitmap);
            bitmapQueue.loadFile(app.getId(), Constants.GET_IMAGE_ICON + app.getIcon_url());
        }
        vh.appIcon.setHasShadow(!listMode);
        vh.id = app.getId();

    }

    @Override
    public void onFileLoaded(String id, Bitmap bitmap) {
        if (bitmap == null) {
            bitmap = noImageBitmap;
        }
        storeIcon(id, bitmap);

        showImage(id, bitmap);
    }

    private void storeIcon(String id, Bitmap bitmap) {
        for (AppWithImage app : apps) {
            if (app.getId().equals(id)) {
                app.setIcon(bitmap);

                return;
            }
        }
    }

    private void showImage(String id, Bitmap bitmap) {
        if (parent == null || bitmap == null) {
            return;
        }
        View v;
        ViewHolder vh;
        for (int i = 0; i < parent.getChildCount(); i++) {
            v = parent.getChildAt(i);
            if ((v.getTag()) instanceof ViewHolder) {
                vh = (ViewHolder) v.getTag();
                if (id.equals(vh.id)) {
                    vh.appIcon.setImage(bitmap);
                }
            }
        }

    }

    private class ViewHolder {
        public RoundeCornersImage appIcon;
        public TextView appTitle;
        public RatingBar appRating;
        public String id;
    }

}
