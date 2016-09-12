package com.tempos21.market.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/** Size change listening TextView. */
public class SizeChangeNotifyingTextView extends TextView {
    /** Listener. */
    private OnSizeChangeListener m_listener;

    /**
     * Creates a new Layout-notifying TextView.
     * @param context   Context.
     * @param attrs     Attributes.
     */
    public SizeChangeNotifyingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Adds a size change listener.
     * @param listener  Listener.
     */
    public void setOnSizeChangedListener(OnSizeChangeListener listener) {
        m_listener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (m_listener != null) {
            m_listener.onSizeChanged(w, h, oldw, oldh);
        }
    }
    
    public interface OnSizeChangeListener {
    	public void onSizeChanged(int w, int h, int oldw, int oldh);
    }
}
