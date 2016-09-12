package com.tempos21.mymarket.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyMarketPreferences {

  public static final String MY_MARKET_PREFERENCE_KEY_USER_NAME = "userName";
  public static final String MY_MARKET_PREFERENCE_KEY_USER_PASSWORD = "userPassword";
  public static final String MY_MARKET_PREFERENCE_KEY_SESSION_ID = "sessionId";

  private static MyMarketPreferences instance;
  private Context context;

  private MyMarketPreferences(Context context) {
    this.context = context;
  }

  public static MyMarketPreferences getInstance(Context context) {
    if (instance == null) {
      instance = new MyMarketPreferences(context);
    }
    return instance;
  }

  private SharedPreferences getPreferences() {
    return PreferenceManager.getDefaultSharedPreferences(context);
  }

  public void setString(String key, String value) {
    getPreferences().edit().putString(key, value).commit();
  }

  public String getString(String key, String defaultValue) {
    return getPreferences().getString(key, defaultValue);
  }

  public void setBoolean(String key, boolean value) {
    getPreferences().edit().putBoolean(key, value).commit();
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    return getPreferences().getBoolean(key, defaultValue);
  }

  public void setInt(String key, int value) {
    getPreferences().edit().putInt(key, value).commit();
  }

  public int getInt(String key, int defaultValue) {
    return getPreferences().getInt(key, defaultValue);
  }

  public void setLong(String key, long value) {
    getPreferences().edit().putLong(key, value).commit();
  }

  public long getLong(String key, long defaultValue) {
    return getPreferences().getLong(key, defaultValue);
  }

  public void setFloat(String key, float value) {
    getPreferences().edit().putFloat(key, value).commit();
  }

  public float getFloat(String key, float defaultValue) {
    return getPreferences().getFloat(key, defaultValue);
  }

  public void deleteKey(String key) {
    if (getPreferences().contains(key)) {
      getPreferences().edit().remove(key).commit();
    }
  }
}