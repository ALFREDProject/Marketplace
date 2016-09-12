package com.tempos21.market.device;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * This class indicates if there are connection to network
 *
 * @author A519130
 */
public class Connection {
    /**
     * This method manage if there are connection to network
     *
     * @param context Context of specific Activity
     * @return true if there are connection
     * false otherwise
     */
    public static boolean netConnect(Context context) {

        ConnectivityManager cm;
        NetworkInfo info = null;

        try {
            cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            info = cm.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (info != null) {
            return true;
        } else {
            return false;
        }
    }
}
