package com.tempos21.market.ui.presenter;

import com.tempos21.market.ui.view.GenericView;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.Category;

import java.util.List;

public class CategoryListPresenterImpl implements CategoryListPresenter, MyMarket.OnMarketCategoryListener {

  private GenericView<List<Category>> categoryListView;

  public CategoryListPresenterImpl(GenericView<List<Category>> categoryListView) {
    this.categoryListView = categoryListView;
  }

  @Override
  public void getCategoryList() {
    categoryListView.showProgress();
    MyMarket.getInstance().getCategoryList(this);
  }

  @Override
  public void onCategoryListSuccess(List<Category> categoryList) {
    categoryListView.onViewSuccess(categoryList);
    categoryListView.hideProgress();
  }

  @Override
  public void onCategoryListError(long id, Exception e) {
    categoryListView.onViewError(id, e);
    categoryListView.hideProgress();
  }
}
