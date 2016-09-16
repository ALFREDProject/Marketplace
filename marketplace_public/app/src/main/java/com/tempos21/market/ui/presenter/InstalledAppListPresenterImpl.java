package com.tempos21.market.ui.presenter;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tempos21.market.ui.view.GenericView;
import com.tempos21.mymarket.sdk.AbstractAppList;
import com.tempos21.mymarket.sdk.AppList;
import com.tempos21.mymarket.sdk.AppListConfig;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.AppListSorting;
import com.tempos21.mymarket.sdk.model.AppListType;
import com.tempos21.mymarket.sdk.model.app.App;

import java.util.ArrayList;
import java.util.List;

public class InstalledAppListPresenterImpl implements AppListPresenter, AppList.OnAppListListener {

    private GenericView<List<App>> listView;
    private AbstractAppList list;

    public InstalledAppListPresenterImpl(GenericView<List<App>> listView) {
        this.listView = listView;
        this.list = MyMarket.getInstance().getInstalledAppList(new AppListConfig.Builder() // AppListConfig
                .setName("Installed Applications") // Name
                .setStart(0) // Start element
                .setElements(1000) // Number of elements per page
                .setSorting(AppListSorting.RATING.getName()).build());
    }

    @Override
    public void loadMore(int listSize) {
        listView.showProgress();
        list.loadMoreResults(this, listSize);
    }

    @Override
    public void refreshList() {
        listView.showProgress();
        list.getForcedAppList(this);
    }

    @Override
    public void getAppList() {
        listView.showProgress();
        //list.getAppList(this);
        list.getForcedAppList(this);
    }

    @Override
    public void onAppListSuccess(AppListType type, List<App> appList) {
        listView.onViewSuccess(appList);
        listView.hideProgress();
    }

    @Override
    public void onAppListError(AppListType type, long id, Exception e) {
        listView.onViewError(id, e);
        listView.hideProgress();
    }
}
