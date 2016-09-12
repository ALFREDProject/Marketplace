package com.tempos21.mymarket.domain.interactor;

import android.content.Context;

import com.tempos21.mymarket.data.repository.client.AppListServiceRepository;
import com.tempos21.mymarket.data.database.dao.CacheDAO;
import com.tempos21.mymarket.data.repository.database.AppListDatabaseRepository;
import com.tempos21.mymarket.domain.dto.request.AppListRequest;
import com.tempos21.mymarket.domain.dto.response.AppListResponse;
import com.tempos21.mymarket.sdk.AbstractAppList;
import com.tempos21.mymarket.sdk.model.AppList;
import com.tempos21.mymarket.sdk.model.AppListType;
import com.tempos21.mymarket.sdk.model.app.App;

import java.util.ArrayList;
import java.util.List;

public class AppInteractor extends Interactor<AppListRequest, AppListResponse> {

  private final AbstractAppList list;
  private final AppListType appListType;

  public AppInteractor(Context context, AbstractAppList list, AppListType appListType) {
    super(context);
    this.list = list;
    this.appListType = appListType;
  }

  @Override
  protected AppListResponse perform(AppListRequest input) throws Exception {
    return getAppList(input);
  }

  private AppListResponse getAppList(AppListRequest input) throws Exception {
    List<App> appList = new ArrayList<App>();
    AppListServiceRepository serviceRepository = new AppListServiceRepository(getContext(), appListType);
    AppListResponse mainResponse = new AppListResponse();
    switch (appListType) {
      case SEARCH:
        appList = serviceRepository.get(input);
        break;
      default:
        AppListDatabaseRepository databaseRepository = new AppListDatabaseRepository(getContext(), list);
        CacheDAO cacheDAO = new CacheDAO(getContext());
        switch (list.getState()) {
          case MORE:
          case REFRESH:
            appList = serviceRepository.get(input);
            databaseRepository.store(new AppList(input.name, appList));
            if (cacheDAO.existsItemInCache(input.name)) {
              cacheDAO.updateCacheItem(input.name, System.currentTimeMillis());
            } else {
              cacheDAO.putItemIntoCache(input.name, System.currentTimeMillis());
            }
            break;
          case LIST:
            if (cacheDAO.existsItemInCache(input.name)) {
              if (cacheDAO.itemHasExpired(input.name)) {
                appList = serviceRepository.get(input);
                databaseRepository.store(new AppList(input.name, appList));
                cacheDAO.updateCacheItem(input.name, System.currentTimeMillis());
              } else {
                AppList appApp = databaseRepository.get(input);
                appList = appApp.appList;
              }
            } else {
              appList = serviceRepository.get(input);
              databaseRepository.store(new AppList(input.name, appList));
              cacheDAO.putItemIntoCache(input.name, System.currentTimeMillis());
            }
            break;
        }
        break;
    }
    mainResponse.appList = appList;
    return mainResponse;
  }
}