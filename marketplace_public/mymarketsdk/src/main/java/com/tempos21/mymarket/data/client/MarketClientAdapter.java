package com.tempos21.mymarket.data.client;

import android.content.Context;

import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.MyMarketPreferences;
import com.tempos21.mymarket.sdk.constant.MyMarketConstants;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;

public class MarketClientAdapter {

  private static final String TAG = MyMarketConstants.TAG;
  static final String SERVER = MyMarketConstants.SERVER;
  static final String URI = MyMarketConstants.URI;

  static final String URI_LOGIN = URI + "/login";
  static final String URI_GET_COUNTRIES = URI + "/countries";
  static final String URI_GET_CATEGORIES = URI + "/categories";
  static final String URI_GET_LANGUAGES = URI + "/languages";
  static final String URI_GET_RATINGS_OF_AN_APP = URI + "/ratings/{app_id}";
  static final String URI_SET_RATING = URI + "/ratings/set";
  static final String URI_DELETE_RATING = URI + "/ratings/delete";
  static final String URI_GET_APPS = URI + "/apps";
  static final String URI_SEARCH_APPS = URI_GET_APPS + "/search/{words}";
  static final String URI_GET_APP_BY_ID = URI + "/app/{app_id}";
  static final String URI_GET_UPDATABLE_APPS = URI_GET_APPS + "/updates";
  static final String URI_GET_INSTALLED_APPS = URI + "/installedapps";
  static final String URI_SET_INSTALLED_APPS = URI_GET_INSTALLED_APPS + "/set";
  static final String URI_GET_IMAGE_ICON = URI + "/image/icon/";
  static final String URI_SET_TOKEN = URI + "/device/settoken";

  private static MarketClientAdapter instance;
  private final Context context;
  private MarketClientService service;
  private Interceptor interceptor;

  private MarketClientAdapter(Context context) {
    this.context = context;
  }

  public static MarketClientAdapter getInstance(Context context) {
    if (instance == null) {
      instance = new MarketClientAdapter(context);
    }
    return instance;
  }

  public MarketClientService getService() {
    if (service == null) {
      interceptor = new Interceptor();
      service = getBuilder().setRequestInterceptor(interceptor).build().create(MarketClientService.class);
    }
    return service;
  }

  private RestAdapter.Builder getBuilder() {
    RestAdapter.Builder builder;
    if (MyMarket.getInstance().isDebug()) {
      builder = new RestAdapter.Builder().setEndpoint(SERVER).setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog((TAG)));
    } else {
      builder = new RestAdapter.Builder().setEndpoint(SERVER);
    }
    return builder;
  }

  public void setSessionId(String sessionId) {
    interceptor.setSessionId(sessionId);
  }

  public void clearSessionId() {
    interceptor.clearSessionId();
  }

  private class Interceptor implements RequestInterceptor {

    private String sessionId;

    public void setSessionId(String sessionId) {
      this.sessionId = sessionId;
    }

    public void clearSessionId() {
      sessionId = null;
    }

    @Override
    public void intercept(RequestFacade request) {
      request.addHeader("Accept", "application/json");
      if (sessionId == null) {
        sessionId = MyMarketPreferences.getInstance(context).getString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_SESSION_ID, null);
      }
      if (sessionId != null) {
        request.addHeader("Cookie", sessionId);
      }
    }
  }
}
