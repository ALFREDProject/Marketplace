package com.tempos21.market.ui.presenter;

import com.tempos21.market.ui.view.GenericView;
import com.tempos21.mymarket.sdk.AbstractAppList;
import com.tempos21.mymarket.sdk.AppList;
import com.tempos21.mymarket.sdk.AppListConfig;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.AppListSorting;
import com.tempos21.mymarket.sdk.model.AppListType;
import com.tempos21.mymarket.sdk.model.app.App;

import java.util.List;

public class AppListByCategoryPresenterImpl implements AppListPresenter, AppList.OnAppListListener {

  private GenericView<List<App>> view;
  private AbstractAppList list;

  public AppListByCategoryPresenterImpl(GenericView<List<App>> view, int category) {
    this.view = view;
    this.list = MyMarket.getInstance().getAppList(new AppListConfig.Builder() // AppListConfig
      .setName("Categorized Applications by " + category) // Name
      .setStart(0) // Start element
      .setCategoryId(category) // Category
      .setElements(2)  // Number of elements per page
      .setSorting(AppListSorting.RATING.getName()).build());
  }

  @Override
  public void loadMore(int listSize) {
    view.showProgress();
    list.loadMoreResults(this, listSize);
  }

  @Override
  public void refreshList() {
    view.showProgress();
    list.getForcedAppList(this);
  }

  @Override
  public void getAppList() {
    view.showProgress();
    //list.getAppList(this);
    list.getForcedAppList(this);
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
