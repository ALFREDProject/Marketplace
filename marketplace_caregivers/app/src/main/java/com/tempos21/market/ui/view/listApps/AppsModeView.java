package com.tempos21.market.ui.view.listApps;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.worldline.alfredo.R;

public class AppsModeView extends FrameLayout implements OnClickListener {
    private static boolean listMode = true;
    private Context context;
    private View v;
    private ImageView listButton;
    private ImageView gridButton;
    private OnModeChangedListener onModeChangedListener;


    public AppsModeView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AppsModeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public AppsModeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        //if (!isInEditMode()) {
        inflateLayout();
        findViews();
        setListeners();
        setButtons();
        //}
    }

    public boolean getMode() {
        return listMode;
    }

    private void inflateLayout() {
        v = View.inflate(context, R.layout.apps_mode, null);
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.FILL_PARENT);
        v.setLayoutParams(params);
        this.addView(v);
    }

    private void findViews() {
        listButton = (ImageView) v.findViewById(R.id.list_view);
        gridButton = (ImageView) v.findViewById(R.id.grid_view);
    }

    private void setListeners() {
        listButton.setOnClickListener(this);
        gridButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        listMode = !listMode;
        if (onModeChangedListener != null) {
            onModeChangedListener.onModeChanged(listMode);
        }
        setButtons();
    }

    private void setButtons() {
        if (listMode) {
            listButton.setImageResource(R.drawable.btn_apps_list_down);
            gridButton.setImageResource(R.drawable.btn_apps_grid);
        } else {
            listButton.setImageResource(R.drawable.btn_apps_list);
            gridButton.setImageResource(R.drawable.btn_apps_grid_down);
        }

    }

    public OnModeChangedListener getOnModeChangedListener() {
        return onModeChangedListener;
    }

    public void setOnModeChangedListener(OnModeChangedListener onModeChangedListener) {
        this.onModeChangedListener = onModeChangedListener;
    }

    public interface OnModeChangedListener {
        public void onModeChanged(boolean listMode);
    }


}
