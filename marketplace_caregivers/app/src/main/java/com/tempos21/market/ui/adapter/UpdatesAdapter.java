package com.tempos21.market.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tempos21.ioutils.filecache.FileLoaderQueue;
import com.tempos21.ioutils.filecache.FileLoaderQueue.OnFileLoadedListener;
import com.tempos21.market.Constants;
import com.tempos21.market.client.bean.AppWithImage;
import com.tempos21.market.client.bean.AppsWithImage;
import com.tempos21.market.ui.view.RoundeCornersImage;
import com.worldline.alfredo.R;


public class UpdatesAdapter extends ArrayAdapter<AppWithImage> implements OnFileLoadedListener {

    private static final int ITEM_VIEW = R.layout.item_update;

    private AppsWithImage items;
    private Context context;

    private ViewHolder vh;

    private Bitmap noImageBitmap;

    private FileLoaderQueue bitmapQueue;

    public UpdatesAdapter(Context context, AppsWithImage apps) {
        super(context, ITEM_VIEW, apps);
        this.items = apps;
        this.context = context;

        noImageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image);
        bitmapQueue = new FileLoaderQueue(context,
                context.getString(R.string.app_name));
        bitmapQueue.setOnFileLoadedListener(this);
    }


    public View getView(int position, View v, ViewGroup parent) {
        if (v == null) {
            v = inflateView();
        }
        vh = getViewHolder(v);
        setData(position);
        return v;

    }


    private void setData(int position) {
        AppWithImage item = items.get(position);
        vh.appTitle.setText(item.getName());
        if (item.getIcon() != null) {
            vh.appIcon.setImage(item.getIcon());
        } else {
            vh.appIcon.setImage(noImageBitmap);
            bitmapQueue.loadFile(item.getId(), Constants.GET_IMAGE_ICON + item.getIcon_url());
        }
        vh.appUpdate.setTag(vh);
        vh.appUpdateProgress.setVisibility(View.INVISIBLE);
        vh.position = position;

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
        for (AppWithImage app : items) {
            if (app.getId().equals(id)) {
                app.setIcon(bitmap);
                return;
            }
        }
    }

    private ViewHolder getViewHolder(View v) {
        ViewHolder vh;
        if (v.getTag() == null) {
            vh = new ViewHolder();
            findViews(v, vh);
            v.setTag(vh);
            vh.appUpdate.setTag(vh);


        } else {
            vh = (ViewHolder) v.getTag();
        }
        return vh;
    }


    private void findViews(View v, ViewHolder vh) {
        vh.appTitle = (TextView) v.findViewById(R.id.appTitle);
        vh.appIcon = (RoundeCornersImage) v.findViewById(R.id.appIcon);
        vh.appUpdate = (View) v.findViewById(R.id.appUpdate);
        vh.appUpdateProgress = v.findViewById(R.id.appUpdateProgress);

    }


    private View inflateView() {
        View v;
        LayoutInflater mInflater = LayoutInflater.from(context);
        v = mInflater.inflate(ITEM_VIEW, null);
        return v;
    }

    class ViewHolder {
        View appUpdateProgress;
        TextView appTitle;
        RoundeCornersImage appIcon;
        View appUpdate;
        int position;
    }


}
