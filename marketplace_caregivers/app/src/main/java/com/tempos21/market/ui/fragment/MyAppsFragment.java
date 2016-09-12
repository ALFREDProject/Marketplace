package com.tempos21.market.ui.fragment;

//import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tempos21.market.client.bean.App;
import com.tempos21.market.client.bean.AppWithImage;
import com.tempos21.market.client.bean.Apps;
import com.tempos21.market.client.bean.AppsWithImage;
import com.tempos21.market.client.bean.OS;
import com.tempos21.market.client.bean.Platform;
import com.tempos21.market.client.bean.Platforms;
import com.tempos21.market.client.service.impl.GetInstalledAppsService;
import com.tempos21.market.client.service.input.GetAppsInput;
import com.tempos21.market.device.BinaryInstaller;
import com.tempos21.market.device.Connection;
import com.tempos21.market.ui.AppActivity;
import com.tempos21.market.ui.view.listApps.AppsModeView;
import com.tempos21.market.ui.view.listApps.AppsModeView.OnModeChangedListener;
import com.tempos21.market.ui.view.listApps.ListAppsView;
import com.tempos21.market.ui.view.listApps.ListAppsView.OnItemClickListener;
import com.tempos21.market.ui.view.listApps.ListAppsView.OnLoadingStatusListener;
import com.tempos21.market.util.AppsController;
import com.tempos21.market.util.AppsStateControl;
import com.tempos21.market.util.MarketPlaceHelper;
import com.tempos21.market.util.TLog;
import com.worldline.alfredo.R;

import eu.alfred.api.market.AppListType;
import eu.alfred.api.market.responses.apps.AppList;
import eu.alfred.api.market.responses.listener.GetAppListResponseListener;


//@SuppressLint("ValidFragment")
public final class MyAppsFragment extends Fragment implements OnModeChangedListener, OnLoadingStatusListener, OnItemClickListener {

    private View me;
    private ListAppsView listAppsView;
    private AppsModeView appsModeView;
    private GetInstalledAppsService searchAppsService;
    private int appsCount = 0;
    private int elements = 10;
    private AppsWithImage allApps;

    private Activity context;
    private TextView notConnection;
    private TextView sectionTitle;
    private App app;
    private boolean notGetData = true;


    public MyAppsFragment(Activity context) {
        this.context = context;
    }

    private void findViews() {
        notConnection = (TextView) me.findViewById(R.id.notConnection);
        listAppsView = (ListAppsView) me.findViewById(R.id.listAppsViews);
        appsModeView = (AppsModeView) me.findViewById(R.id.appsModeView);
        sectionTitle = (TextView) me.findViewById(R.id.sectionTitle);
    }

    private void setListeners() {
        appsModeView.setOnModeChangedListener(this);
        listAppsView.setOnLoadingStatusListener(this);
        listAppsView.setOnItemClickListener(this);
    }

    private void getData() {
        if (notGetData) {
            sectionTitle.setText(getString(R.string.menuMyApps));
            GetAppsInput input = new GetAppsInput();
            input.setSorting(GetAppsInput.SORTING_APPS_DATE);
            input.setStart(appsCount);
            input.setElements(elements);


            AppListType appListType = AppListType.INSTALLED;
            String words = "";
            String name = "";
            int start = input.getStart();
            int elements = input.getElements();
            String sorting = input.getSorting();
            int countryId = -1;
            try {
                countryId = Integer.parseInt(input.getCountry());
            } catch (Exception ignored) {
            }
            int categoryId = 0;
            try {
                categoryId = Integer.parseInt(input.getCategory());
            } catch (Exception ignored) {
            }
            int languageId = 0;
            boolean hasPromoImage = false;
            MarketPlaceHelper.getInstance().marketPlace.getAppList(
                    appListType,
                    words,
                    name,
                    start,
                    elements,
                    sorting,
                    countryId,
                    categoryId,
                    languageId,
                    hasPromoImage, new GetAppListResponseListener() {
                        @Override
                        public void onSuccess(AppList appList) {
                            if (appList != null) {
                                Apps apps = new Apps();
                                App app;
                                for (eu.alfred.api.market.responses.apps.App app1 : appList.apps) {
                                    app = new App();
                                    app.setId("" + (app1.id != null ? app1.id : -1));
                                    app.setName(app1.name);
                                    app.setDate(app1.date);
                                    app.setPackageName(app1.packageName);
                                    app.setNotificationEmails(app1.notificationEmails);
                                    app.setExternalBinary(app1.externalBinary != null ? app1.externalBinary : false);
                                    app.setVersion_number(app1.versionNumber != null ? app1.versionNumber : 1);
                                    app.setAllowed(app1.allowed != null ? app1.allowed : false);
                                    app.setSupportEmail(app1.supportEmails);
                                    app.setVersion_string(app1.versionString);
                                    app.setIcon_url(app1.iconUrl);
                                    app.setRating(app1.rating != null ? app1.rating : 1);
                                    app.setPromo_url(app1.promoUrl);
                                    app.setAuthor(app1.author);
                                    app.setExternalUrl(app1.externalUrl);
                                    app.setVersion_id("" + (app1.versionId != null ? app1.versionId : 1));
                                    Platforms platforms = new Platforms();
                                    Platform platform;
                                    for (eu.alfred.api.market.responses.apps.Platform platform1 : app1.platform) {
                                        platform = new Platform();
                                        platform.setId(platform1.id != null ? platform1.id : -1);
                                        platform.setName(platform1.name);

                                        com.tempos21.market.client.bean.OS os = new OS();
                                        os.setExtension(platform1.os.extension);
                                        os.setId(platform1.os.id != null ? platform1.os.id : -1);
                                        os.setName(platform1.os.name);

                                        platform.setOs(os);
                                        platforms.add(platform);
                                    }
                                    app.setPlatform(platforms);

                                    apps.add(app);
                                }
                                onGetInstalledAppsServiceResponse(true, apps);
                            } else {
                                onGetInstalledAppsServiceResponse(false, new Apps());
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            onGetInstalledAppsServiceResponse(false, new Apps());
                        }
                    });
            /*
            searchAppsService = new GetInstalledAppsService(Constants.GET_INSTALLED_APP_SERVICE, getActivity());
            searchAppsService.setOnGetInstalledAppsServiceResponseListener(this);
            searchAppsService.setupParameters(input);
            searchAppsService.runService();
            */
        }
    }

    public void onGetInstalledAppsServiceResponse(boolean success, Apps apps) {
        Apps revisedApps = AppsController.reviseApps(context, apps);

        if (success && revisedApps != null && revisedApps.size() > 0) {
            if (appsCount > 0) {
                listAppsView.addApps(setAppsWithImage(revisedApps));

            } else {
                listAppsView.setApps(setAppsWithImage(revisedApps));

            }
            if (revisedApps.size() < 10) {
                listAppsView.setFinished();
            }
            allApps.addAll(setAppsWithImage(revisedApps));

        } else {
            notConnection.setVisibility(View.VISIBLE);
            if (success) {
                notConnection.setText(R.string.not_install_apps);
            } else {
                notConnection.setText(R.string.server_error);
            }
            listAppsView.setFinished();

        }
    }

    private AppsWithImage setAppsWithImage(Apps apps) {
        AppsWithImage appsWithImage = new AppsWithImage();

        for (App app : apps) {
            AppWithImage appWithImage = new AppWithImage();
            appWithImage.setId(app.getId());
            appWithImage.setName(app.getName());
            appWithImage.setAuthor(app.getAuthor());
            appWithImage.setVersion_number(app.getVersion_number());
            appWithImage.setVersion_string(app.getVersion_string());
            appWithImage.setDate(app.getDate());
            appWithImage.setIcon_url(app.getIcon_url());
            appWithImage.setRating(app.getRating());
            appWithImage.setPromo_url(app.getPromo_url());
            appWithImage.setScreenshots(app.getScreenshots());
            appWithImage.setExternalBinary(app.isExternalBinary());
            appWithImage.setExternalUrl(app.getExternalUrl());
            appWithImage.setAllowed(app.isAllowed());
            appWithImage.setPlatform(app.getPlatform());
            appWithImage.setVersion_id(app.getVersion_id());
            appWithImage.setSize(app.getSize());
            appWithImage.setPackageName(app.getPackageName());
            appWithImage.setCategoryId(app.getCategoryId());
            appWithImage.setDescription(app.getDescription());
            appWithImage.setTarget(app.getTarget());
            appWithImage.setSupportUrl(app.getSupportUrl());
            appWithImage.setSupportEmail(app.getSupportEmail());
            appWithImage.setNotificationEmails(app.getNotificationEmails());

            appsWithImage.add(appWithImage);
        }

        return appsWithImage;
    }

    @Override
    public void onModeChanged(boolean listMode) {
        listAppsView.setMode(listMode);
    }

    @Override
    public void onLoadingStatus(int itemCount) {
        appsCount = itemCount;
        getData();
    }

    @Override
    public void onItemClick(App app) {
        this.app = app;

        Intent i = new Intent(getActivity(), AppActivity.class);
        i.putExtra(AppActivity.EXTRA_APP_ID, app.getId());
        startActivityForResult(i, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!BinaryInstaller.isAppInstalled(context, app.getPackageName())) {
            allApps.remove(app);
            listAppsView.setApps(allApps);
            listAppsView.setFinished();
            notGetData = false;
        } else {
            // Actualizamos rating
            if (data != null) {
                String id = data.getExtras().getString("id");
                double rating = data.getExtras().getDouble("rating");
                App myapp = new App();

                int i = 0;
                int size = allApps.size();
                while (i < size) {
                    myapp = allApps.get(i);
                    if (myapp.getId().equals(id)) {
                        i = size;
                        allApps.remove(myapp);
                    }
                    i++;
                }

                myapp.setRating(rating);
                allApps.add(setAppWithImage(myapp));
                listAppsView.setApps(allApps);
                listAppsView.setFinished();
                notGetData = false;
            }
        }

    }

    //	@SuppressLint("ValidFragment")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        me = inflater.inflate(R.layout.latest_apps, null, true);
        findViews();
        setListeners();

        if (!appsModeView.getMode()) {
            listAppsView.swapMode();
        }

        if (Connection.netConnect(context)) {
            notConnection.setVisibility(View.INVISIBLE);
            if (allApps != null) {
                listAppsView.setApps(allApps);
            } else {
                allApps = new AppsWithImage();
                getData();
            }
        } else {
            sectionTitle.setText(R.string.myApps);
            listAppsView.setVisibility(View.INVISIBLE);
            notConnection.setVisibility(View.VISIBLE);
        }

        TLog.v("oncreateview");

        return me;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppsStateControl control = new AppsStateControl(context);
        control.getInstalledApps();
//		appsCount=0;
//		allApps=new Apps();
//		listAppsView.setApps(allApps);
//		getData();
    }

    private AppWithImage setAppWithImage(App app) {
        AppWithImage appWithImage = new AppWithImage();
        appWithImage.setId(app.getId());
        appWithImage.setName(app.getName());
        appWithImage.setAuthor(app.getAuthor());
        appWithImage.setVersion_number(app.getVersion_number());
        appWithImage.setVersion_string(app.getVersion_string());
        appWithImage.setDate(app.getDate());
        appWithImage.setIcon_url(app.getIcon_url());
        appWithImage.setRating(app.getRating());
        appWithImage.setPromo_url(app.getPromo_url());
        appWithImage.setScreenshots(app.getScreenshots());
        appWithImage.setExternalBinary(app.isExternalBinary());
        appWithImage.setExternalUrl(app.getExternalUrl());
        appWithImage.setAllowed(app.isAllowed());
        appWithImage.setPlatform(app.getPlatform());
        appWithImage.setVersion_id(app.getVersion_id());
        appWithImage.setSize(app.getSize());
        appWithImage.setPackageName(app.getPackageName());
        appWithImage.setCategoryId(app.getCategoryId());
        appWithImage.setDescription(app.getDescription());
        appWithImage.setTarget(app.getTarget());
        appWithImage.setSupportUrl(app.getSupportUrl());
        appWithImage.setSupportEmail(app.getSupportEmail());
        appWithImage.setNotificationEmails(app.getNotificationEmails());

        return appWithImage;
    }

}
