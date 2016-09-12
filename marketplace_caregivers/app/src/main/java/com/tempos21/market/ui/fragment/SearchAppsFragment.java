package com.tempos21.market.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tempos21.market.Constants;
import com.tempos21.market.client.bean.App;
import com.tempos21.market.client.bean.AppWithImage;
import com.tempos21.market.client.bean.Apps;
import com.tempos21.market.client.bean.AppsWithImage;
import com.tempos21.market.client.bean.OS;
import com.tempos21.market.client.bean.Platform;
import com.tempos21.market.client.bean.Platforms;
import com.tempos21.market.client.service.impl.SearchAppsService;
import com.tempos21.market.client.service.impl.SearchAppsService.OnSearchAppsServiceResponseListener;
import com.tempos21.market.client.service.impl.ServiceErrorCodes;
import com.tempos21.market.client.service.input.SearchInput;
import com.tempos21.market.device.Connection;
import com.tempos21.market.ui.AppActivity;
import com.tempos21.market.ui.StartUpActivity;
import com.tempos21.market.ui.view.listApps.AppsModeView;
import com.tempos21.market.ui.view.listApps.AppsModeView.OnModeChangedListener;
import com.tempos21.market.ui.view.listApps.ListAppsView;
import com.tempos21.market.ui.view.listApps.ListAppsView.OnItemClickListener;
import com.tempos21.market.ui.view.listApps.ListAppsView.OnLoadingStatusListener;
import com.tempos21.market.util.MarketPlaceHelper;
import com.tempos21.market.util.TLog;
import com.worldline.alfredo.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import eu.alfred.api.market.AppListType;
import eu.alfred.api.market.responses.apps.AppList;
import eu.alfred.api.market.responses.listener.GetAppListResponseListener;

//import android.annotation.SuppressLint;

//@SuppressLint("ValidFragment")
public final class SearchAppsFragment extends Fragment implements OnModeChangedListener, OnLoadingStatusListener, OnItemClickListener {
    private View me;
    private ListAppsView listAppsView;
    private AppsModeView appsModeView;
    private SearchAppsService searchAppsService;
    private String countryId = null;
    private String categoryId = null;
    private int appsCount = 0;
    private AppsWithImage allApps;

    private String query;
    private Activity context;
    private TextView notConnection;
    private TextView sectionTitle;

    public SearchAppsFragment(Activity context, String query) {
        this.query = query;
        this.context = context;

        TLog.i("Query arrived:" + query);
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
        String uri = null;
        try {
            // TODO search service?
            uri = Constants.SEARCHAPPS_SERVICE + "/" + URLEncoder.encode(query, "UTF-8");
            sectionTitle.setText(getString(R.string.results) + " " + query);
            SearchInput input = new SearchInput();
            input.setSorting(SearchInput.SORTING_APPS_DATE);
            input.setStart(appsCount);
            input.setCountry(countryId);
            input.setCategory(categoryId);

            AppListType appListType = AppListType.SEARCH;
            String words = query;
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
                                onSearchAppsServiceResponse(true, apps);
                            } else {
                                onSearchAppsServiceResponse(false, new Apps());
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            onSearchAppsServiceResponse(false, new Apps());
                        }
                    });

            /*searchAppsService = new SearchAppsService(uri, getActivity());
            searchAppsService.setOnSearchAppsServiceResponseListener(this);
            searchAppsService.runService(input);*/
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onModeChanged(boolean listMode) {
        listAppsView.swapMode();

    }

    @Override
    public void onLoadingStatus(int itemCount) {
        appsCount = itemCount;
        getData();

    }

    public void onSearchAppsServiceResponse(boolean success, Apps apps) {
        if (success && apps != null && apps.size() > 0) {
            if (appsCount > 0) {
                listAppsView.addApps(setAppsWithImage(apps));
            } else {
                listAppsView.setApps(setAppsWithImage(apps));
            }
            allApps.addAll(setAppsWithImage(apps));
        } else {
            /*if (responseCode == ServiceErrorCodes.LOGIN_ERROR) {
                Intent intent = new Intent(context, StartUpActivity.class);
                intent.putExtra(HomeFragment.LOGIN_ERROR, true);
                startActivity(intent);
                context.finish();
            } else {*/
                if (allApps.size() == 0) {
                    notConnection.setVisibility(View.VISIBLE);
                    if (success) {
                        notConnection.setText(R.string.not_found);
                    } else {
                        notConnection.setText(R.string.server_error);
                    }
                }
                listAppsView.setFinished();
            //}

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

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public void onItemClick(App app) {
        Intent i = new Intent(getActivity(), AppActivity.class);
        i.putExtra(AppActivity.EXTRA_APP_ID, app.getId());
        startActivityForResult(i, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        me = inflater.inflate(R.layout.latest_apps, null, true);
        findViews();
        setListeners();

        if (Connection.netConnect(context)) {
            notConnection.setVisibility(View.INVISIBLE);
            if (allApps != null) {
                listAppsView.setApps(allApps);
            } else {
                allApps = new AppsWithImage();
                getData();
            }
        } else {
            sectionTitle.setText(R.string.results);
            listAppsView.setVisibility(View.INVISIBLE);
            notConnection.setVisibility(View.VISIBLE);
        }

        TLog.v("oncreateview");

        return me;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TLog.v("onactivitycreated");
    }

    //	@SuppressLint("ValidFragment")
    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        TLog.v("Starting latest apps fragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        TLog.v("onpause");

    }

    @Override
    public void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        TLog.v("Stopping latest apps fragment");
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
