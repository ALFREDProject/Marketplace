package com.tempos21.market.ui.presenter;

import com.tempos21.market.ui.view.GenericViewSubRequest;
import com.tempos21.market.util.MarketPlaceHelper;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.AppRate;
import com.tempos21.mymarket.sdk.model.app.AppDetail;
import com.tempos21.mymarket.sdk.model.app.Os;
import com.tempos21.mymarket.sdk.model.app.Platform;

import java.util.ArrayList;
import java.util.List;

import eu.alfred.api.market.responses.app_rate.AppRateList;
import eu.alfred.api.market.responses.listener.GetAppDetailsResponseListener;
import eu.alfred.api.market.responses.listener.GetAppRateListResponseListener;

public class AppDetailPresenterImpl implements AppDetailPresenter, MyMarket.OnMarketAppDetailListener, MyMarket.OnMarketAppRateListener {

    private GenericViewSubRequest<AppDetail, List<AppRate>> view;
    private long id;

    public AppDetailPresenterImpl(GenericViewSubRequest<AppDetail, List<AppRate>> view) {
        this.view = view;
    }

    @Override
    public void getAppDetail(long id) {
        view.showProgress();
        MarketPlaceHelper.getInstance().marketPlace.getAppDetails("" + id, new GetAppDetailsResponseListener() {
            @Override
            public void onSuccess(eu.alfred.api.market.responses.apps.AppDetail appDetail) {
                if (appDetail != null) {
                    AppDetail appDetail1 = new AppDetail();

                    appDetail1.id = appDetail.id;
                    appDetail1.name = appDetail.name;
                    appDetail1.description = appDetail.description;
                    appDetail1.screenshots = appDetail.screenshots;
                    appDetail1.versionString = appDetail.versionString;
                    appDetail1.iconUrl = appDetail.iconUrl;
                    appDetail1.rating = appDetail.rating;
                    appDetail1.promoUrl = appDetail.promoUrl;
                    appDetail1.versionNumber = appDetail.versionNumber;
                    appDetail1.date = appDetail.date;
                    appDetail1.size = appDetail.size;
                    appDetail1.notificationEmails = appDetail.notificationEmails;
                    appDetail1.allowed = appDetail.allowed;
                    appDetail1.supportEmails = appDetail.supportEmails;
                    appDetail1.author = appDetail.author;
                    appDetail1.externalUrl = appDetail.externalUrl;
                    appDetail1.versionId = appDetail.versionId;
                    appDetail1.externalBinary = appDetail.externalBinary;
                    appDetail1.packageName = appDetail.packageName;

                    List<Platform> platforms = new ArrayList<Platform>();

                    Platform pl;
                    for (eu.alfred.api.market.responses.apps.Platform platform : appDetail.platform) {
                        pl = new Platform();
                        pl.id = platform.id;
                        pl.name = platform.name;
                        pl.os = new Os();
                        pl.os.id = platform.os.id;
                        pl.os.name = platform.os.name;
                        pl.os.extension = platform.os.extension;
                        platforms.add(pl);
                    }

                    appDetail1.platform = platforms;


                    onAppDetailSuccess(appDetail1);
                } else {
                    onAppDetailError(-1, new NullPointerException());
                }
            }

            @Override
            public void onError(Exception e) {
                onAppDetailError(-1, e);
            }
        });
        //MyMarket.getInstance().getAppDetail(this, id);
        this.id = id;
    }

    @Override
    public void onAppDetailSuccess(AppDetail appDetail) {
        view.onViewSuccess(appDetail);
        view.hideProgress();

        MarketPlaceHelper.getInstance().marketPlace.getAppRateList("" + id, new GetAppRateListResponseListener() {
            @Override
            public void onSuccess(AppRateList appRateList) {
                if (appRateList != null) {
                    List<AppRate> appRateList1 = new ArrayList<AppRate>();

                    AppRate appRate;
                    for (eu.alfred.api.market.responses.app_rate.AppRate rate : appRateList.appRates) {
                        appRate = new AppRate();
                        appRate.userId = rate.userId;
                        appRate.versionString = rate.versionString;
                        appRate.text = rate.text;
                        appRate.dateCreation = rate.dateCreation;
                        appRate.userFullName = rate.userFullName;
                        appRate.title = rate.title;
                        appRate.rate = rate.rate;
                        appRate.userName = rate.userName;
                        appRate.id = rate.id;


                        appRateList1.add(appRate);
                    }


                    onAppRateListSuccess(appRateList1);
                } else {
                    onAppRateListError(-1, new NullPointerException());
                }
            }

            @Override
            public void onError(Exception e) {
                onAppRateListError(-1, e);
            }
        });
        //MyMarket.getInstance().getAppRateList(this, id);
    }

    @Override
    public void onAppDetailError(long id, Exception e) {
        view.onViewError(id, e);
        view.hideProgress();
    }

    @Override
    public void onAppRateListSuccess(List<AppRate> appRateList) {
        view.onViewSuccessSubRequest(appRateList);
    }

    @Override
    public void onAppRateListError(long id, Exception e) {
        view.onViewErrorSubRequest(id, e);
    }
}
