package com.tempos21.mymarket.sdk.constant;

import android.os.Environment;

import com.tempos21.mymarket.sdk.model.AppListSorting;

import java.io.File;

public class MyMarketConstants {

  public static final String TAG = "MY MARKET";
  public static final String PS = File.separator;

  public static final long EXPIRATION_DELTA = 30 * (60 * 1000); // 30 minutes
  public static final int APP_LIST_START = 0; // Default value of 0
  public static final int APP_LIST_ELEMENTS = 20; // Default value of 20
  public static final String APP_LIST_SORTING = AppListSorting.RATING.getName(); // Default value is "rating"

  public static final String HOST_NAME = "devel1.tempos21.com";
  public static final String SERVER = "https://" + HOST_NAME;
  public static final String URI = "/alfredo-dev/rest/market";

  public static final String APP_IMAGE = "/image";
  public static final String APP_IMAGE_ICON = SERVER + URI + APP_IMAGE + "/icon";
  public static final String APP_IMAGE_MEDIA = SERVER + URI + APP_IMAGE + "/media";
  public static final String APP_PROMO_IMAGE = SERVER + URI + APP_IMAGE + "/promo";
  public static final String APP_DOWNLOAD_URL = SERVER + URI + "/binary/%s/%s"; // appId, versionNumber
  public static final String APPS_PATH = Environment.getExternalStorageDirectory().getPath() + PS + "market" + PS + "apps" + PS;
  public static final String APP_EXTENSION = ".apk";
  public static final String APPS_DESTINATION_PATH = PS + "market" + PS + "apps";
  public static final String APPS_DESTINATION_FILE = APPS_DESTINATION_PATH + PS + "%s" + PS + "%s" + APP_EXTENSION; // apps_path, appId, appName
  public static final float DISK_QUOTA = 500f;
}