package com.tempos21.market.ui.fragment;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by a576023 on 18/02/2015.
 */
public final class KeyboardUtils {

    private KeyboardUtils() {

    }

    /**
     * @param context Context
     * @param view    View
     */
    public static void hideSoftKeyBoard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * @param context Context
     * @param view    View
     */
    public static void showSoftKeyboard(Context context, View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }
}
