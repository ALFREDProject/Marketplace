package com.tempos21.mymarket.sdk;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.tempos21.mymarket.sdk.model.Platform;

import java.util.UUID;

public class MyMarketConfig {

  private Platform platform;
  private String deviceId;
  private int platformVersion;
  private String baseUrl;

  private MyMarketConfig(Builder builder) {
    platform = builder.platform;
    deviceId = builder.deviceId;
    platformVersion = builder.platformVersion;
    baseUrl = builder.baseUrl;
  }

  public Platform getPlatform() {
    return platform;
  }

  public String getDeviceId() {
    return deviceId;
  }

  public int getPlatformVersion() {
    return platformVersion;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public static class Builder {

    private final Context context;
    private String deviceId;
    private Platform platform = Platform.ANDROID;
    private int platformVersion = Build.VERSION.SDK_INT;
    private String baseUrl;

    public Builder(Context context) {
      this.context = context;
      this.deviceId = "7e77dd2b-653d-3018-8f49-dc81fa673244"; // TODO HARDCODED FOR TESTING PURPOSES getDeviceId()
      this.platformVersion = 17; // TODO HARDCODED FOR TESTING PURPOSES getPlatformVersion()
    }

    private String getDeviceId() {
      final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

      final String tmDevice, tmSerial, androidId;
      tmDevice = "" + tm.getDeviceId();
      tmSerial = "" + tm.getSimSerialNumber();
      androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

      UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
      return deviceUuid.toString();
    }

    private int getPlatformVersion() {
      return Build.VERSION.SDK_INT;
    }

    /**
     * This parameter will be used to serve applications of an specific platform. Default
     * value will return Android applications, but other platform could be forced.
     *
     * @param platform The platform to be used. Default is Platform.ANDROID
     */
    public Builder setPlatform(Platform platform) {
      this.platform = platform;
      return this;
    }

    public Builder setDeviceId(String deviceId) {
      this.deviceId = deviceId;
      return this;
    }

    /**
     * The version of the device is used to determine which application can be or not
     * installed in the device.
     *
     * @param platformVersion The version to be notified. Default is android.os.Build.VERSION.SDK_INT
     */
    public Builder setPlatformVersion(int platformVersion) {
      this.platformVersion = platformVersion;
      return this;
    }

    /**
     * This is the url where the MyMarket API is located. This is a mandatory parameter. Not
     * providing this parameter will raise an exception on the init method.
     *
     * @param url The url to use
     */
    public Builder setBaseUrl(String url) {
      this.baseUrl = url;
      return this;
    }

    public MyMarketConfig build() {
      return new MyMarketConfig(this);
    }
  }
}
