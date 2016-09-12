package com.tempos21.market.ui.presenter;

import android.support.annotation.Nullable;

import com.tempos21.market.ui.view.GenericView;
import com.tempos21.mymarket.sdk.AbstractAppList;
import com.tempos21.mymarket.sdk.AppList;
import com.tempos21.mymarket.sdk.AppListConfig;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.AppListSorting;
import com.tempos21.mymarket.sdk.model.AppListType;
import com.tempos21.mymarket.sdk.model.app.App;

import java.util.List;

public class FilteredAppListPresenterImpl implements FilteredAppListPresenter, AppList.OnAppListListener {

  private GenericView<List<App>> view;
  private AbstractAppList list;

  public FilteredAppListPresenterImpl(GenericView<List<App>> view) {
    this.view = view;
  }

  @Override
  public void loadMore(int listSize) {
    view.showProgress();
    if (list != null) {
      list.loadMoreResults(this, listSize);
    } else {
      view.hideProgress();
    }
  }

  @Override
  public void refreshList(String query) {
    view.showProgress();
    list = MyMarket.getInstance().getSearchAppList(getConfig(query));
    list.getForcedAppList(this);
  }

  @Override
  public void getAppList(String query) {
    view.showProgress();
    list = MyMarket.getInstance().getSearchAppList(getConfig(query));
    list.getForcedAppList(this);
  }

  private AppListConfig getConfig(@Nullable String query) {
    AppListConfig.Builder builder = new AppListConfig.Builder() // AppListConfig
      .setName("Queried Applications") // Name
      .setStart(0) // Start element
      .setElements(0)  // Number of elements per page
      .setSorting(AppListSorting.LATEST.getName());
    if (query != null) {
      builder.setSearchQuery(query);
    }
    return builder.build();
  }

  @Override
  public void onAppListSuccess(AppListType type, List<App> appList) {
    view.onViewSuccess(appList);
    view.hideProgress();
  }

  @Override
  public void onAppListError(AppListType type, long id, Exception e) {
    view.onViewError(id, e);
    view.hideProgress();
  }
}
