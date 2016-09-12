package com.tempos21.market.ui.presenter;

public interface FilteredAppListPresenter {

  void loadMore(int listSize);

  void refreshList(String query);

  void getAppList(String query);
}