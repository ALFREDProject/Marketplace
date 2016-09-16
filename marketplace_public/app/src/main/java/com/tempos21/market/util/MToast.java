package com.tempos21.market.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.worldline.alfredo.R;

public class MToast extends Toast {

    public MToast(Context context) {

        super(context);

    }

    /**
     * Make a custom toast that just contains a text view.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param text     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     */
    public static Toast makeText(Context context, CharSequence text, int duration) {

        Toast result = new Toast(context);

        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.toast, null);
        TextView tv = (TextView) v.findViewById(R.id.toastText);
        tv.setText(text);

        result.setView(v);
        result.setDuration(duration);


        return result;
    }

}