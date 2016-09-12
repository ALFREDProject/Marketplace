package com.tempos21.market.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.worldline.alfredo.R;

/**
 * TODO: document your custom view class.
 */
public class CategoriesRowView extends LinearLayout {

    private static final int ROW_WITH = 440;


    public CategoriesRowView(Context context) {
        super(context);
        init();
    }

    public CategoriesRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CategoriesRowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init(){
        setOrientation(this.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ROW_WITH, 1f);
        setLayoutParams(lp);
    }


}
