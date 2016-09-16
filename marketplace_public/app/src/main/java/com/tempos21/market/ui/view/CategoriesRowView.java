package com.tempos21.market.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

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


    private void init() {
        setOrientation(this.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, ROW_WITH, 1f);
        setLayoutParams(lp);
    }


}
