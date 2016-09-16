package com.tempos21.market.ui.presenter;

import com.tempos21.market.ui.view.GenericView;
import com.tempos21.market.util.MarketPlaceHelper;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.Category;

import java.util.ArrayList;
import java.util.List;

import eu.alfred.api.market.responses.category.CategoryList;
import eu.alfred.api.market.responses.listener.GetCategoryListResponseListener;

public class CategoryListPresenterImpl implements CategoryListPresenter, MyMarket.OnMarketCategoryListener {

    private GenericView<List<Category>> categoryListView;

    public CategoryListPresenterImpl(GenericView<List<Category>> categoryListView) {
        this.categoryListView = categoryListView;
    }

    @Override
    public void getCategoryList() {
        categoryListView.showProgress();
        MarketPlaceHelper.getInstance().marketPlace.getCategoryList(new GetCategoryListResponseListener() {
            @Override
            public void onSuccess(CategoryList categoryList) {
                if (categoryList != null) {
                    List<Category> categories = new ArrayList<Category>();
                    Category category;
                    for (eu.alfred.api.market.responses.category.Category category1 : categoryList.categories) {
                        category = new Category();
                        category.id = category1.id != null ? category1.id : -1;
                        category.name = category1.name;
                        category.image = category1.image;
                        categories.add(category);
                    }
                    onCategoryListSuccess(categories);
                } else {
                    onCategoryListError(-1, new NullPointerException("categoryList parameter is null."));
                }
            }

            @Override
            public void onError(Exception e) {
                onCategoryListError(-1, e);
            }
        });
        //MyMarket.getInstance().getCategoryList(this);
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
