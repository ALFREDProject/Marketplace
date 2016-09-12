package com.tempos21.market.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.tempos21.ioutils.filecache.FileLoaderQueue;
import com.tempos21.ioutils.filecache.FileLoaderQueue.OnFileLoadedListener;
import com.tempos21.market.Constants;
import com.tempos21.market.client.bean.AppWithImage;
import com.tempos21.market.ui.fragment.HomeFragment;
import com.tempos21.market.ui.imageLoader.ViewHolder;
import com.worldline.alfredo.R;

import java.util.ArrayList;


public class AppsPromoAdapter extends ArrayAdapter<AppWithImage> implements OnFileLoadedListener {

    private ArrayList<AppWithImage> apps;
    private Activity context;
    private FileLoaderQueue fileLoader;
    private HomeFragment home;


    public AppsPromoAdapter(Activity context, ArrayList<AppWithImage> applications, HomeFragment home) {
        super(context, R.layout.item_app, applications);
        this.apps = applications;
        this.context = context;
        this.home = home;
        fileLoader = new FileLoaderQueue(context, context.getString(R.string.app_name));
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null || !(convertView.getTag() instanceof ViewHolder)) {
            holder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.item_app, null);

            holder.promoImage = (ImageView) convertView.findViewById(R.id.promoAppImage);
            holder.appProgress = (ProgressBar) convertView.findViewById(R.id.appProgress);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        loadBitmap(position, holder);

        return convertView;
    }


    public void loadBitmap(int position, ViewHolder holder) {
        AppWithImage app = apps.get(position);

        if (app.getPromo() != null) {
            holder.promoImage.setImageBitmap(app.getPromo());
            holder.promoImage.setVisibility(View.VISIBLE);
            holder.appProgress.setVisibility(View.GONE);

        } else {
            if (app.getPromo_url() != null) {
                fileLoader.setOnFileLoadedListener(this);
                fileLoader.loadFile(app.getId(), Constants.GET_IMAGE_PROMO + app.getPromo_url());
            } else {
                holder.promoImage.setImageResource(R.drawable.no_banner);
                holder.promoImage.setAdjustViewBounds(true);
                holder.promoImage.setVisibility(View.VISIBLE);
                holder.appProgress.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onFileLoaded(String arg0, Bitmap arg1) {
        if (arg1 != null) {
            for (AppWithImage app : apps) {
                if (app.getId().equals(arg0))
                    app.setPromo(arg1);
            }
        } else {
            for (AppWithImage app : apps) {
                if (app.getId().equals(arg0)) {
                    app.setPromo(null);
                    app.setPromo_url(null);
                }
            }
        }

        home.getAppsList().invalidateViews();
    }
}
