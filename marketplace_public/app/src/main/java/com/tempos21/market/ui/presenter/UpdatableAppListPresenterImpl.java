package com.tempos21.market.ui.presenter;

import com.tempos21.market.ui.view.GenericView;
import com.tempos21.mymarket.sdk.AppList;
import com.tempos21.mymarket.sdk.AppListConfig;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.AppListSorting;
import com.tempos21.mymarket.sdk.model.AppListType;
import com.tempos21.mymarket.sdk.model.app.App;

import java.util.List;


public class UpdatableAppListPresenterImpl implements AppListPresenter, AppList.OnAppListListener {

  private GenericView<List<App>> appListView;
  private AppList appList;

  public UpdatableAppListPresenterImpl(GenericView<List<App>> appListView) {
    this.appListView = appListView;
    this.appList = MyMarket.getInstance().getUpdatableAppList(new AppListConfig.Builder() // AppListConfig
      .setName("Updatable AppList") // Name
      .setStart(0) // Start element
      .setElements(0) // Number of elements per page
      .setSorting(AppListSorting.RATING.getName()).build());
  }

  @Override
  public void loadMore(int elements) {
    appListView.showProgress();
    appList.loadMoreResults(this, elements);
  }

  @Override
  public void refreshList() {
    appListView.showProgress();
    appList.getForcedAppList(this);
  }

  @Override
  public void getAppList() {
    appListView.showProgress();
    //appList.getAppList(this);
    appList.getForcedAppList(this);
  }

  @Override
  public void onAppListSuccess(AppListType type, List<App> appList) {
    appListView.onViewSuccess(appList);
    appListView.hideProgress();
  }

  @Override
  public void onAppListError(AppListType type, long id, Exception e) {
    appListView.onViewError(id, e);
    appListView.hideProgress();
  }
}
