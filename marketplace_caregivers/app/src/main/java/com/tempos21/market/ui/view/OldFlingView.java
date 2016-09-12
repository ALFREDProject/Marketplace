package com.tempos21.market.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.tempos21.market.util.TLog;

public class OldFlingView extends ViewGroup {


    /**
     * Indicates that the pager is in an idle, settled state. The current page
     * is fully in view and no animation is in progress.
     */
    public static final int SCROLL_STATE_IDLE = 0;
    /**
     * Indicates that the pager is currently being dragged by the user.
     */
    public static final int SCROLL_STATE_DRAGGING = 1;
    /**
     * Indicates that the pager is in the process of settling to a final
     * position.
     */
    public static final int SCROLL_STATE_SETTLING = 2;
    /**
     * Sentinel value for no current active pointer. Used by
     * {@link #activePointerId}.
     */
    private static final int INVALID_POINTER = -1;
    //private int minimumVelocity;
    /**
     * Position of the last motion event.
     */
    private float lastMotionX;
    /**
     * ID of the active pointer. This is used to retain consistency during
     * drags/flings if multiple pointers are used.
     */
    private int activePointerId = INVALID_POINTER;
    /**
     * Determines speed during touch scrolling
     */
    private VelocityTracker velocityTracker;
    private int maximumVelocity;
    private int scrollState = SCROLL_STATE_IDLE;
    private boolean isBeingDragged;
    private boolean isUnableToDrag;
    private float lastMotionY;
    private float touchSlop;
    private OnFlingListener onFlingListener;

    public OldFlingView(Context context) {
        super(context);

        // TODO Auto-generated constructor stub
        init(context);

    }

    public OldFlingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);

    }

    public void init(Context context) {
        ViewConfiguration.get(context);
        setWillNotDraw(false);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setFocusable(true);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        touchSlop = configuration.getScaledTouchSlop();
        // minimumVelocity = configuration.getScaledMinimumFlingVelocity();
        maximumVelocity = configuration.getScaledMaximumFlingVelocity();

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onMotionEvent will be called and we do the actual
         * scrolling there.
         */


        final int action = ev.getAction() & MotionEvent.ACTION_MASK;

        // Always take care of the touch gesture being complete.
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // Release the drag.
            TLog.v("onInterceptTouchEvent ACTION_UP");
            isBeingDragged = false;
            isUnableToDrag = false;
            activePointerId = INVALID_POINTER;
            return false;
        }

        // Nothing more to do here if we have decided whether or not we
        // are dragging.
        if (action != MotionEvent.ACTION_DOWN) {
            if (isBeingDragged) {
                TLog.v("Intercept returning true!");
                return true;
            }
            if (isUnableToDrag) {
                TLog.v("Intercept returning false!");
                return false;

            }
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                TLog.v("onInterceptTouchEvent ACTION_MOVE");

                /*
                 * mIsBeingDragged == false, otherwise the shortcut would have caught it. Check
                 * whether the user has moved far enough from his original down touch.
                 */

                /*
                * Locally do absolute value. mLastMotionY is set to the y value
                * of the down event.
                */
                if (activePointerId == INVALID_POINTER) {
                    // If we don't have a valid id, the touch down wasn't on content.
                    break;
                }

                final float x = ev.getX();
                final float dx = x - lastMotionX;
                final float xDiff = Math.abs(dx);
                final float y = ev.getY();
                final float yDiff = Math.abs(y - lastMotionY);
                getScrollX();

                if (canScroll(this, false, (int) dx, (int) x, (int) y)) {
                    // Nested view has scrollable area under this point. Let it be handled there.
                    lastMotionX = x;
                    lastMotionY = y;
                    return false;
                }
                if (xDiff > touchSlop && xDiff > yDiff) {
                    TLog.v("Starting drag!");
                    isBeingDragged = true;
                    setScrollState(SCROLL_STATE_DRAGGING);
                    lastMotionX = x;
                    //setScrollingCacheEnabled(true);
                } else {
                    if (yDiff > touchSlop) {
                        // The finger has moved enough in the vertical
                        // direction to be counted as a drag...  abort
                        // any attempt to drag horizontally, to work correctly
                        // with children that have scrolling containers.
                        TLog.v("Starting unable to drag!");
                        isUnableToDrag = true;
                    }
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                TLog.v("onInterceptTouchEvent ACTION_DOWN");

                /*
                 * Remember location of down touch.
                 * ACTION_DOWN always refers to pointer index 0.
                 */
                lastMotionX = ev.getX();
                lastMotionY = ev.getY();

                if (scrollState == SCROLL_STATE_SETTLING) {
                    // Let the user 'catch' the pager as it animates.
                    isBeingDragged = true;
                    isUnableToDrag = false;
                    setScrollState(SCROLL_STATE_DRAGGING);
                } else {
                    isBeingDragged = false;
                    isUnableToDrag = false;
                }

                TLog.v("Down at " + lastMotionX + "," + lastMotionY
                        + " mIsBeingDragged=" + isBeingDragged
                        + "mIsUnableToDrag=" + isUnableToDrag);
                break;
            }


        }

        /*
        * The only time we want to intercept motion events is if we are in the
        * drag mode.
        */
        return isBeingDragged;
    }
//	@Override
//	protected void onLayout(boolean changed, int l, int t, int r, int b) {
//		 final int count = getChildCount();
//		final int width = r-l;
//		for (int i = 0; i < count; i++) {
//			View child = getChildAt(i);
//			if (child.getVisibility() != GONE ) {
//				
//				int childLeft = getPaddingLeft()+width;
//				int childTop = getPaddingTop();
//				
//				child.layout(childLeft, childTop,
//						childLeft + child.getMeasuredWidth(),
//						childTop + child.getMeasuredHeight());
//			}
//		}
//	}

    /**
     * We only want the current page that is being shown to be focusable.
     */
    @Override
    protected boolean onRequestFocusInDescendants(int direction,
                                                  Rect previouslyFocusedRect) {
        int index;
        int increment;
        int end;
        int count = getChildCount();
        if ((direction & FOCUS_FORWARD) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }
        for (int i = index; i != end; i += increment) {
            View child = getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                if (child.requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.getLayoutParams();
            child.layout(l, t, l + child.getMeasuredWidth(), t + child.getMeasuredHeight());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(ev);

        final int action = ev.getAction();
        boolean needsInvalidate = false;

        //boolean isBeingDragged=false;
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {

                // Remember where the motion event started
                lastMotionX = ev.getX();
                break;
            }
            case MotionEvent.ACTION_MOVE:
                TLog.v("onTouchEvent ACTION_MOVE");

                if (!isBeingDragged) {

                    final float x = ev.getX();
                    final float xDiff = Math.abs(x - lastMotionX);
                    final float y = ev.getY();
                    final float yDiff = Math.abs(y - lastMotionY);
                    //TLog.v("Moved x to " + x + "," + y + " diff=" + xDiff + ","+ yDiff);
                    if (xDiff > touchSlop && xDiff > yDiff) {
                        TLog.v("Starting drag!");
                        isBeingDragged = true;
                        lastMotionX = x;
                        setScrollState(SCROLL_STATE_DRAGGING);
                    }
                }
                if (isBeingDragged) {
                    // Scroll to follow the motion event
                    TLog.v("Dragging!");
                    final float x = ev.getX();
                    lastMotionX = x;
                    getScrollX();
                    getWidth();


                }
                break;
            case MotionEvent.ACTION_UP:
                TLog.v("onTouchEvent ACTION_UP");
                if (isBeingDragged) {
                    velocityTracker.computeCurrentVelocity(1000, maximumVelocity);
                    float vel = velocityTracker.getXVelocity();
                    getWidth();
                    getScrollX();
                    activePointerId = INVALID_POINTER;
                    TLog.v("dragged!!!");
                    if (onFlingListener != null) {

                        onFlingListener.onFling((int) Math.signum(vel));
                    }
                    endDrag();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                TLog.v("onTouchEvent ACTION_CANCEL");
                if (isBeingDragged) {
                    activePointerId = INVALID_POINTER;
                    endDrag();

                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final float x = ev.getX();
                lastMotionX = x;
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:

                lastMotionX = ev.getX();
                break;
        }
        if (needsInvalidate) {
            invalidate();
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        boolean growHeight = widthMode != MeasureSpec.UNSPECIFIED;

        int width = 0;
        int height = getPaddingTop();

        int currentWidth = getPaddingLeft();
        int currentHeight = 0;

        boolean breakLine = false;
        boolean newLine = false;
        int spacing = 0;

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            child.getLayoutParams();


            if (growHeight && (breakLine || currentWidth + child.getMeasuredWidth() > widthSize)) {
                height += currentHeight;
                currentHeight = 0;
                width = Math.max(width, currentWidth - spacing);
                currentWidth = getPaddingLeft();
                newLine = true;
            } else {
                newLine = false;
            }

//			lp.x = currentWidth;
//			lp.y = height;

            currentWidth += child.getMeasuredWidth() + spacing;
            currentHeight = Math.max(currentHeight, child.getMeasuredHeight());

//			breakLine = lp.breakLine;
        }

        if (!newLine) {
            height += currentHeight;
            width = Math.max(width, currentWidth - spacing);
        }

        width += getPaddingRight();
        height += getPaddingBottom();

        setMeasuredDimension(resolveSize(width, widthMeasureSpec),
                resolveSize(height, heightMeasureSpec));
    }

    private void setScrollState(int newState) {
        if (scrollState == newState) {
            return;
        }
        scrollState = newState;

    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) v;
            final int scrollX = v.getScrollX();
            final int scrollY = v.getScrollY();
            final int count = group.getChildCount();
            // Count backwards - let topmost views consume scroll distance first.
            for (int i = count - 1; i >= 0; i--) {
                // TODO: Add versioned support here for transformed views.
                // This will not work for transformed views in Honeycomb+
                final View child = group.getChildAt(i);
                if (x + scrollX >= child.getLeft() && x + scrollX < child.getRight() &&
                        y + scrollY >= child.getTop() && y + scrollY < child.getBottom() &&
                        canScroll(child, true, dx, x + scrollX - child.getLeft(),
                                y + scrollY - child.getTop())) {
                    return true;
                }
            }
        }

        return true;
    }

    private void endDrag() {
        isBeingDragged = false;
        isUnableToDrag = false;

        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

    public OnFlingListener getOnFlingListener() {
        return onFlingListener;
    }

    public void setOnFlingListener(OnFlingListener onFlingListener) {
        this.onFlingListener = onFlingListener;
    }

    public interface OnFlingListener {
        public void onFling(int direction);
    }

}
