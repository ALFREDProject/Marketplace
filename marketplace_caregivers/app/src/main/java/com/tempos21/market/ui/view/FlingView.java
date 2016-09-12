package com.tempos21.market.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.widget.LinearLayout;

//import com.tempos21.market.util.TLog;

public class FlingView extends LinearLayout implements OnGestureListener {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    private OnFlingListener onFlingListener;

    public FlingView(Context context) {
        super(context);

        // TODO Auto-generated constructor stub
        init(context);

    }

    public FlingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);

    }

    public void init(Context context) {
        this.setScrollContainer(true);
        gestureDetector = new GestureDetector(context, this);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//		TLog.e("DispatchTouchEvent");
        boolean dispatchResult = super.dispatchTouchEvent(ev);
        boolean gestureResult = gestureDetector.onTouchEvent(ev);
        if (gestureResult) {
            return true;
        } else {
            return dispatchResult;
        }

    }

    // @Override
    // public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
    // float velocityY) {
    //
    //
    // TLog.e("VELOCITY: " + velocityX);
    //

    // return false;
    // }
    // }

    @Override
    public boolean onDown(MotionEvent e) {
//		TLog.e("onDown");
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
//		TLog.e("onShowPress");

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
//		TLog.e("onSingleTapUp");
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
//		TLog.e("onScroll");
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
//		TLog.e("onLongPress");

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
//		TLog.e("onFling");
        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//			TLog.e("SWIPE LEFT: " + (e1.getY() - e2.getY()));
            if (onFlingListener != null) {
                onFlingListener.onFlingLeft();
            }
            this.cancelLongPress();

            return true;

        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//			TLog.e("SWIPE RIGHT: " + (e2.getY() - e1.getY()));
            if (onFlingListener != null) {
                onFlingListener.onFlingRight();
            }
            this.cancelLongPress();

            return true;
        }
        return false;
    }

    public OnFlingListener getOnFlingListener() {
        return onFlingListener;
    }

    public void setOnFlingListener(OnFlingListener onFlingListener) {
        this.onFlingListener = onFlingListener;
    }

    public interface OnFlingListener {
        public void onFlingRight();

        public void onFlingLeft();
    }

}
