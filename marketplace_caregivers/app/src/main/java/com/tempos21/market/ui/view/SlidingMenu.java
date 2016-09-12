package com.tempos21.market.ui.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.tempos21.market.ui.view.MenuButtons.FoldingListener;
import com.worldline.alfredo.R;

public class SlidingMenu extends LinearLayout implements AnimationListener, FoldingListener {
    public final static int COLLAPSED = 0;
    public final static int NORMAL = 1;
    public final static int EXPANDED = 2;
    private final static int COLLAPSED_SIZE = 20;
    private final static int NORMAL_SIZE = 75;
    private final static int A = TranslateAnimation.ABSOLUTE;
    private final static float fromY = 0;
    private final static float toY = 0;
    private static final long FOLDING_DURATION = 400;
    GestureDetector gestures;
    private int mode = NORMAL;
    private int finalMargin;
    private boolean first = true;
    private int collapsedMargin;
    private int normalMargin;
    private int expandedMargin;
    private boolean onAnimation = false;
    private Context context;
    private int newMargin;
    private float density;
    private int collapsedSize;
    private int normalSize;
    private MenuButtons menuButtons;
    private boolean startClosed = false;
    private boolean startNormal = true;

    public SlidingMenu(Context context) {
        super(context);
        this.context = context;
        init();

    }

    public SlidingMenu(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        init();
    }


    private void init() {
        //gestures = new GestureDetector(context, this);
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        density = metrics.density;
        collapsedSize = (int) (COLLAPSED_SIZE * density);
        normalSize = (int) (NORMAL_SIZE * density);
//		menuButtons=(MenuButtons)this.findViewById(R.id.menuButtons);
//		menuButtons.setFoldingListener(this);
    }

    // @Override
    // public boolean onInterceptTouchEvent(MotionEvent ev) {
    // return (gestures.onTouchEvent(ev));
    // }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
        collapsedMargin = -w + collapsedSize;
        normalMargin = -w + normalSize;
        expandedMargin = 0;
        if (startClosed) {
            autoClose();
            startClosed = false;
        }
        if (startNormal) {
            autoNormal();
            startNormal = false;
        }
    }

    public void switchMode() {
        if (!onAnimation) {
            onAnimation = true;
            if (mode == COLLAPSED) {
                setMode(NORMAL);
            } else {
                setMode(COLLAPSED);
            }
        }
    }

    public void unfold() {
        if (!onAnimation) {
            if (mode == COLLAPSED) {
                setMode(NORMAL);
            } else if (mode == NORMAL) {
                setMode(EXPANDED);
            }
        }
    }

    public void fold() {

        if (menuButtons != null) {
            menuButtons.hideDoSearch();
        }
        if (!onAnimation) {
            if (mode == EXPANDED) {
                setMode(NORMAL);
            } else if (mode == NORMAL) {
                setMode(COLLAPSED);
            }
        }
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int newMode) {
        if (menuButtons == null) {
            menuButtons = (MenuButtons) this.findViewById(R.id.menuButtons);
            //menuButtons.setFoldingListener(this);
        }
        if (newMode != mode) {

            float fromX = ((LinearLayout.LayoutParams) this.getLayoutParams()).leftMargin;

            setLeftMargin(0);

            mode = newMode;
            if (newMode == COLLAPSED) {
                setNewMargin(collapsedSize);
                finalMargin = collapsedMargin;
                collapseBar(fromX);
            } else if (newMode == NORMAL) {
                setNewMargin(normalSize);
                normalBar(fromX);
                finalMargin = normalMargin;
            } else if (newMode == EXPANDED) {
                setNewMargin(getWidth() - 1);
                expandBar(fromX);
                finalMargin = expandedMargin;
            }

        }

    }

    private void setLeftMargin(int margin) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this
                .getLayoutParams();
        params.setMargins(margin, params.topMargin, params.rightMargin,
                params.bottomMargin);
        // The first time clearing the animation makes a weird flickering,
        // so we avoid doing it. Bazinga!
        if (!first) {
            clearAnimation();
        } else {
            first = false;
        }
        this.setLayoutParams(params);
    }

    private void collapseBar(float fromX) {
        final float toX = collapsedMargin;
        animateBar(fromX, toX);
    }

    private void normalBar(float fromX) {
        final float toX = normalMargin;

        animateBar(fromX, toX);
    }

    private void expandBar(float fromX) {
        animateBar(fromX, 0);
    }

    private void animateBar(float fromX, float toX) {

        TranslateAnimation collapse = new TranslateAnimation(A, fromX, A, toX,
                A, fromY, A, toY);
        collapse.setDuration(FOLDING_DURATION);
        collapse.setAnimationListener(this);
        collapse.setInterpolator(new BounceInterpolator());
        collapse.setFillAfter(true);
        this.startAnimation(collapse);

    }

    public void autoClose() {
        setLeftMargin(collapsedMargin);
    }

    public void autoNormal() {
        setLeftMargin(normalMargin);
    }

    @Override
    public void onAnimationStart(Animation animation) {
        onAnimation = true;

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        onAnimation = false;
        setLeftMargin(finalMargin);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    public int getNewMargin() {
        return newMargin;
    }

    public void setNewMargin(int newMargin) {
        this.newMargin = newMargin;
    }

    public boolean isStartClosed() {
        return startClosed;
    }

    public void setStartClosed(boolean startClosed) {
        this.startClosed = startClosed;
    }

    @Override
    public void onFolding() {
        //fold();

    }

    @Override
    public void onUnfolding() {
        //unfold();

    }


}
