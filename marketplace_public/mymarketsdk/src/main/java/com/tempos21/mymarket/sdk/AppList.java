package com.tempos21.mymarket.sdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import com.tempos21.mymarket.domain.dto.request.AppListRequest;
import com.tempos21.mymarket.domain.dto.response.AppListResponse;
import com.tempos21.mymarket.sdk.model.AppListType;
import com.tempos21.mymarket.sdk.model.app.App;
import com.tempos21.mymarket.sdk.model.app.Os;
import com.tempos21.mymarket.sdk.model.app.Platform;
import com.tempos21.mymarket.sdk.util.MarketPlaceHelper2;

import java.util.ArrayList;
import java.util.List;

import eu.alfred.api.market.responses.listener.GetAppListResponseListener;

public class AppList extends AbstractAppList<AppListConfig> {

    AppList(Context context, AppListConfig config, AppListType appListType) {
        super(context, config);
        this.appListType = appListType;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                if (packageInfo.applicationInfo.packageName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAppUpdateable(Context context, App app) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                if (packageInfo.applicationInfo.packageName.equals(app.packageName)) {
                    if(packageInfo.versionCode < app.versionNumber){
                        return true;
                    }
                }
            }
        }
        return false;
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

    @Override
    public void getForcedAppList(final OnAppListListener listener) {
        applyConfig(config);
        state = State.REFRESH;
        list(listener);
    }

    private void list(final OnAppListListener listener) {
        AppListRequest appListRequest = getRequest();
        appListRequest.elements = 1000;

        eu.alfred.api.market.AppListType lType = eu.alfred.api.market.AppListType.DEFAULT;
        if (eu.alfred.api.market.AppListType.DEFAULT.getIndex() == appListType.getIndex()) {
            lType = eu.alfred.api.market.AppListType.DEFAULT;
        } else if (eu.alfred.api.market.AppListType.INSTALLED.getIndex() == appListType.getIndex()) {
            lType = eu.alfred.api.market.AppListType.INSTALLED;
        } else if (eu.alfred.api.market.AppListType.LATEST.getIndex() == appListType.getIndex()) {
            lType = eu.alfred.api.market.AppListType.LATEST;
        } else if (eu.alfred.api.market.AppListType.MOST_POPULAR.getIndex() == appListType.getIndex()) {
            lType = eu.alfred.api.market.AppListType.MOST_POPULAR;
        } else if (eu.alfred.api.market.AppListType.SEARCH.getIndex() == appListType.getIndex()) {
            lType = eu.alfred.api.market.AppListType.SEARCH;
        } else if (eu.alfred.api.market.AppListType.UPDATABLE.getIndex() == appListType.getIndex()) {
            lType = eu.alfred.api.market.AppListType.UPDATABLE;
        } else {
            lType = eu.alfred.api.market.AppListType.DEFAULT;
        }

        if (appListRequest.words == null) {
            appListRequest.words = "";
        }

        final eu.alfred.api.market.AppListType finalLType = lType;

        // TODo
        eu.alfred.api.market.AppListType lType1 = lType == eu.alfred.api.market.AppListType.INSTALLED ? eu.alfred.api.market.AppListType.DEFAULT : lType;
        lType1 = lType == eu.alfred.api.market.AppListType.UPDATABLE ? eu.alfred.api.market.AppListType.DEFAULT : lType1;

        MarketPlaceHelper2.getInstance().marketPlace.getAppList(
                lType1,
                appListRequest.words, appListRequest.name, appListRequest.start,
                appListRequest.elements, appListRequest.sorting, appListRequest.countryId,
                appListRequest.categoryId, appListRequest.languageId, appListRequest.hasPromoImage,
                new GetAppListResponseListener() {
                    @Override
                    public void onSuccess(eu.alfred.api.market.responses.apps.AppList appList) {
                        if (appList != null) {

                            AppListResponse result = new AppListResponse();
                            result.appList = new ArrayList<App>();

                            for (eu.alfred.api.market.responses.apps.App app : appList.apps) {
                                App app1 = new App();

                                app1.id = app.id;
                                app1.name = app.name;
                                app1.versionNumber = app.versionNumber;
                                app1.allowed = app.allowed;
                                app1.supportEmails = app.supportEmails;
                                app1.versionString = app.versionString;
                                app1.iconUrl = app.iconUrl;
                                app1.rating = app.rating;
                                app1.promoUrl = app.promoUrl;
                                app1.author = app.author;
                                app1.externalUrl = app.externalUrl;
                                app1.versionId = app.versionId;
                                app1.externalBinary = app.externalBinary;
                                app1.notificationEmails = app.notificationEmails;
                                app1.packageName = app.packageName;
                                app1.date = app.date;

                                app1.platform = new ArrayList<Platform>();

                                for (eu.alfred.api.market.responses.apps.Platform platform : app.platform) {
                                    Platform platform1 = new Platform();
                                    platform1.id = platform.id;
                                    platform1.name = platform.name;
                                    platform1.os = new Os();
                                    platform1.os.id = platform.os.id;
                                    platform1.os.name = platform.os.name;
                                    platform1.os.extension = platform.os.extension;

                                    app1.platform.add(platform1);
                                }


                                result.appList.add(app1);
                            }


                            if (result.appList.size() == 0 || result.appList.size() < config.getElements()) {
                                finished = true;
                            }
                            if (finalLType == eu.alfred.api.market.AppListType.INSTALLED) {
                                List<App> appsInstalled = new ArrayList<>();
                                for (App app : result.appList) {
                                    if (isAppInstalled(context, app.packageName)) {
                                        appsInstalled.add(app);
                                    }
                                }
                                listener.onAppListSuccess(appListType, appsInstalled);
                            }else if (finalLType == eu.alfred.api.market.AppListType.UPDATABLE) {
                                List<App> appsUpdateable = new ArrayList<>();
                                for (App app : result.appList) {
                                    if (isAppUpdateable(context, app)) {
                                        appsUpdateable.add(app);
                                    }
                                }
                                listener.onAppListSuccess(appListType, appsUpdateable);
                            } else {
                                listener.onAppListSuccess(appListType, result.appList);
                            }

                        } else {
                            listener.onAppListError(appListType, -1, new NullPointerException());
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        listener.onAppListError(appListType, -1, e);
                    }
                });


        /*final AppInteractor appInteractor = new AppInteractor(context, this, appListType);
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
                listener.onAppListError(appListType, id, e);
            }
        });
        appInteractor.executeAsync(getRequest());*/
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
