package com.tempos21.market.ui.presenter;

import com.tempos21.market.ui.view.GenericView;
import com.tempos21.mymarket.sdk.AppList;
import com.tempos21.mymarket.sdk.AppListConfig;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.AppListType;
import com.tempos21.mymarket.sdk.model.app.App;

import java.util.List;

public class AppListPresenterImpl implements AppListPresenter, AppList.OnAppListListener {

  private GenericView<List<App>> appListView;
  private AppList appList01;

  public AppListPresenterImpl(GenericView<List<App>> appListView) {
    this.appListView = appListView;

    AppListConfig.Builder builder = new AppListConfig.Builder();
    builder.setName("Market Applications");// Name
    builder.setElements(0);// Number of elements per page

    this.appList01 = MyMarket.getInstance().getAppList(builder.build()); // AppListConfig
  }

  @Override
  public void loadMore(int numElements) {
    appListView.showProgress();
    // appList01.loadMore(this);
  }

  @Override
  public void refreshList() {
    appListView.showProgress();
    appList01.getForcedAppList(this);
  }

  @Override
  public void getAppList() {
    appListView.showProgress();
    //appList01.getAppList(this);
    appList01.getForcedAppList(this);
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
