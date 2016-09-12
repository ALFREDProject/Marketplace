package com.tempos21.mymarket.sdk;

import android.content.Context;

import com.tempos21.mymarket.sdk.model.AppListType;
import com.tempos21.mymarket.sdk.model.app.App;

import java.util.List;

public abstract class AbstractAppList<C> {

  public enum State {

    LIST, MORE, REFRESH
  }

  protected State state = State.LIST;

  protected Context context;

  protected String name;
  protected int start;
  protected int from;
  protected int elements;
  protected String sorting;
  protected int categoryId;
  protected int countryId;
  protected int languageId;
  protected boolean hasPromoImage;
  protected AppListType appListType;
  protected String query;

  protected boolean finished;

  protected C config;

  AbstractAppList(Context context, C config) {
    this.context = context;
    this.config = config;
    applyConfig(config);
  }

  public abstract void applyConfig(C config);

  /**
   * Return the list of apps checking if exists a cache and saving or updating the cache if needed
   */
  public abstract void getAppList(final OnAppListListener listener);

  /**
   * Return the next list of apps when the request is paginated
   */
  public abstract void loadMoreResults(final OnAppListListener listener, int listSize);

  /**
   * Return the list of apps without checking if exists a cache but saving or updating the cache if needed
   */
  public abstract void getForcedAppList(final OnAppListListener listener);

  public Context getContext() {
    return context;
  }

  public State getState() {
    return state;
  }

  public interface OnAppListListener {

    void onAppListSuccess(AppListType type, List<App> response);

    void onAppListError(AppListType type, long id, Exception e);
  }
}
