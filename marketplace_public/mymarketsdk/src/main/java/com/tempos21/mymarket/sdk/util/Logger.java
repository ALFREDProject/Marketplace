package com.tempos21.mymarket.sdk.util;

import android.util.Log;

import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.constant.MyMarketConstants;

public class Logger {

    /**
     * Shows a log with the passed string
     *
     * @param string string to show in the log
     */
    public static void log(String string) {
        if (MyMarket.getInstance().isDebug()) {
            Log.i(MyMarketConstants.TAG, string);
        }
    }

    /**
     * Shows a log with the passed string
     *
     * @param string string to show in the log
     */
    public static void logE(String string) {
        if (MyMarket.getInstance().isDebug()) {
            Log.e(MyMarketConstants.TAG, string);
        }
    }
}