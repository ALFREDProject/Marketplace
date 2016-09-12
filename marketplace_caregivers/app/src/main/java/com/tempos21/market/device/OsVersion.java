package com.tempos21.market.device;

import android.content.Context;
import android.os.Build;

public class OsVersion {
    public static int getOsVersion(Context context) {
//			throws NameNotFoundException {
//		PackageInfo pinfo;
//		pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
//		return pinfo.versionCode;
        return Build.VERSION.SDK_INT;
//		ApplicationInfo ainfo;
//		ainfo = context.getApplicationInfo();
//		return ainfo.targetSdkVersion;

    }
}
