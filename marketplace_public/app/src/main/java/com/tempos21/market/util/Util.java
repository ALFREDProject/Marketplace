package com.tempos21.market.util;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tempos21.mymarket.sdk.MyMarket;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * This class indicates if there are connection to network
 *
 * @author A519130
 */
public class Util {
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

        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    public static int getVersion(Context context, String packageName) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            ApplicationInfo info = packageInfo.applicationInfo;
            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                if (info.packageName.equals(packageName)) {
                    return packageInfo.versionCode;
                }
            }
        }
        return 0;
    }


    public static boolean isAppInstalled(Context context, String packageName) {
        for (PackageInfo packageInfo : getInstalledApplicationsByTheUser(context)) {
            if (packageInfo.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }


    private static List<PackageInfo> getInstalledApplicationsByTheUser(Context context) {
        List<ApplicationInfo> apps = context.getPackageManager().getInstalledApplications(0);
        List<PackageInfo> userApps = new ArrayList<PackageInfo>();
        for (ApplicationInfo app : apps) {
            if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) {
                if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM) {
                    // User application
                    try {
                        userApps.add(context.getPackageManager().getPackageInfo(app.packageName, 0));
                    } catch (PackageManager.NameNotFoundException e) {
                        if (MyMarket.getInstance().isDebug()) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return userApps;
    }


    public static String getFormattedSize(double mb) {
        String size = "";

        if (mb >= 1) {
            size = "" + mb;
            size = size.substring(0, 4);
            size = size.concat(" MB");
        } else {
            if (mb == 0) {
                size = "0.0 MB";
            } else {
                mb = mb * 1000;
                size = "" + mb;
                size = size.substring(0, 6);
                size = size.concat(" KB");
            }
        }

        return size;
    }


    public static CharSequence getFormattedDate(String date) {
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("MMMM dd, yyyy");
        DateFormatSymbols usSymbols = new DateFormatSymbols(Locale.ENGLISH);
        targetFormat.setDateFormatSymbols(usSymbols);
        try {
            return targetFormat.format(sourceFormat.parse(date));

        } catch (ParseException e) {
            return date;
        }
    }

}
