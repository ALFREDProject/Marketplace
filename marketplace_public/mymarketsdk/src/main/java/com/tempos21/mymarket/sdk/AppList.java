package com.tempos21.mymarket.sdk;

import android.content.Context;

import com.tempos21.mymarket.domain.interactor.AppInteractor;
import com.tempos21.mymarket.domain.interactor.Interactor;
import com.tempos21.mymarket.domain.dto.request.AppListRequest;
import com.tempos21.mymarket.domain.dto.response.AppListResponse;
import com.tempos21.mymarket.sdk.model.AppListType;
import com.tempos21.mymarket.sdk.model.User;
import com.tempos21.mymarket.sdk.model.app.App;

import java.util.ArrayList;

public class AppList extends AbstractAppList<AppListConfig> {

  AppList(Context context, AppListConfig config, AppListType appListType) {
    super(context, config);
    this.appListType = appListType;
  }

  @Override
  public void applyConfig(AppListConfig config) {
    this.finished = false;
    this.name = config.getName();
    this.start = config.getStart();
    this.from = this.start;
    this.elements = config.getElements();
    this.sorting = config.getSorting();
    this.categoryId = config.getCategoryId();
    this.countryId = config.getCountryId();
    this.languageId = config.getLanguageId();
    this.hasPromoImage = config.hasPromoImage();
    this.query = config.getSearchQuery();
  }

  @Override
  public void getAppList(final OnAppListListener listener) {
    state = State.LIST;
    list(listener);
  }

  @Override
  public void getForcedAppList(final OnAppListListener listener) {
    applyConfig(config);
    state = State.REFRESH;
    list(listener);
  }

  @Override
  public void loadMoreResults(final OnAppListListener listener, int listSize) {
    if (!finished) {
      if (listSize > 0) {
        start = from + listSize;
      } else {
        start += config.getElements();
      }
      state = State.MORE;
      list(listener);
    } else {
      listener.onAppListSuccess(appListType, new ArrayList<App>());
    }
  }

  private void list(final OnAppListListener listener) {
    final AppInteractor appInteractor = new AppInteractor(context, this, appListType);
    appInteractor.setClientResultListener(new Interactor.OnTaskResultListener<AppListResponse>() {

      @Override
      public void onTaskSuccess(long id, AppListResponse result) {
        if (result.appList.size() == 0 || result.appList.size() < config.getElements()) {
          finished = true;
        }
        listener.onAppListSuccess(appListType, result.appList);
      }

      @Override
      public void onTaskFailure(long id, Exception e) {
        if (e.getMessage() != null && e.getMessage().equals(MyMarket.SERVER_ERROR_FORBIDDEN)) {
          MyMarket.getInstance().reLogin(new MyMarket.OnMarketReLoginListener() {

            @Override
            public void onReLoginSuccess(User user) {
              appInteractor.executeAsync(getRequest());
            }

            @Override
            public void onReLoginError(long id, Exception e) {

            }
          });
        } else {
          listener.onAppListError(appListType, id, e);
        }
      }
    });
    appInteractor.executeAsync(getRequest());
  }

  private AppListRequest getRequest() {
    AppListRequest appListRequest = new AppListRequest();
    appListRequest.name = name;
    appListRequest.start = start;
    appListRequest.elements = elements;
    appListRequest.sorting = sorting;
    appListRequest.categoryId = categoryId;
    appListRequest.countryId = countryId;
    appListRequest.languageId = languageId;
    appListRequest.hasPromoImage = hasPromoImage;
    appListRequest.words = query;
    return appListRequest;
  }
}
