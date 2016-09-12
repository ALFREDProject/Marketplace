package com.tempos21.market.device;

import android.content.Context;
import android.util.DisplayMetrics;

import com.tempos21.market.util.TLog;

public class Device {

    public static boolean isTablet(Context context) {
        try {
            // Compute screen size
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            float screenWidth = dm.widthPixels / dm.xdpi;
            float screenHeight = dm.heightPixels / dm.ydpi;
            double size = Math.sqrt(Math.pow(screenWidth, 2)
                    + Math.pow(screenHeight, 2));
            // Tablet devices should have a screen size greater than 6 inches
            return size >= 6;
        } catch (Throwable t) {
            TLog.e("Failed to compute screen size");
            return false;
        }
    }

    //Alternative method found in http://stackoverflow.com/a/8222549/462615

//	private boolean isTabletDevice() {
//		  if (android.os.Build.VERSION.SDK_INT >= 11) { // honeycomb
//		    // test screen size, use reflection because isLayoutSizeAtLeast is
//		    // only available since 11
//		    Configuration con = getResources().getConfiguration();
//		    try {
//		      Method mIsLayoutSizeAtLeast = con.getClass().getMethod(
//		      "isLayoutSizeAtLeast", int.class);
//		      boolean r = (Boolean) mIsLayoutSizeAtLeast.invoke(con,
//		      0x00000004); // Configuration.SCREENLAYOUT_SIZE_XLARGE
//		      return r;
//		    } catch (Exception x) {
//		      x.printStackTrace();
//		      return false;
//		    }
//		  }
//		  return false;
//		}
}
