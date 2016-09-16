package com.tempos21.market.util;


public class TLog {
    public static final boolean LOG_ENABLED = true;
    private static String logTag = null;

    public static String getLogTag() {
        if (logTag == null) {
            return (TLog.class.getPackage().getName());
        }
        return logTag;
    }

    public static void setLogTag(String logTag) {

        TLog.logTag = logTag;
    }


    public static void v(String verbose) {
        if (LOG_ENABLED) {
            android.util.Log.v(getLogTag(), verbose);
        }
    }

    public static void d(String debug) {
        if (LOG_ENABLED) {
            android.util.Log.d(getLogTag(), debug);
        }
    }


    public static void i(String info) {
        if (LOG_ENABLED) {
            android.util.Log.i(getLogTag(), info);
        }
    }

    public static void w(String warning) {
        if (LOG_ENABLED) {
            android.util.Log.w(getLogTag(), warning);
        }
    }

    public static void e(String error) {
        if (LOG_ENABLED) {
            android.util.Log.e(getLogTag(), error);
        }
    }

}
