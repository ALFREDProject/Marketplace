package com.tempos21.market.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tempos21.market.client.bean.App;
import com.tempos21.market.client.bean.AppWithImage;
import com.tempos21.market.client.bean.Apps;
import com.tempos21.market.client.bean.AppsWithImage;
import com.tempos21.market.client.bean.OS;
import com.tempos21.market.client.bean.Platform;
import com.tempos21.market.client.bean.Platforms;
import com.tempos21.market.db.AppModel;
import com.tempos21.market.device.Connection;
import com.tempos21.market.ui.StartUpActivity;
import com.tempos21.market.ui.adapter.AppsPromoAdapter;
import com.tempos21.market.util.MarketPlaceHelper;
import com.worldline.alfredo.R;

import java.util.ArrayList;
import java.util.Collections;

import eu.alfred.api.market.AppListType;
import eu.alfred.api.market.responses.apps.AppList;
import eu.alfred.api.market.responses.listener.GetAppListResponseListener;
import eu.alfred.ui.AppActivity;

//import android.annotation.SuppressLint;


//@SuppressLint("ValidFragment")
public class HomeFragment extends Fragment implements OnClickListener, OnItemClickListener {

    public static final String LOGIN_ERROR = "login_error";
    public static Apps installedApps;
    private View fragmentView;
    private GridView appsList;
    private ArrayList<AppWithImage> apps;
    private ToggleButton latest;
    private ToggleButton ratings;
    private AppActivity context;
    //	private boolean orderAsc = true;
    private View homeProgress;
    private View appsListView;
    private TextView homeNotFound;



    public void setContext(AppActivity c) {
        context = c;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.home, null, true);

        findViews();
        setInitialView();
        setListeners();

        if (Connection.netConnect(context)) {
            homeNotFound.setVisibility(View.INVISIBLE);
            getApps();
        } else {
            homeProgress.setVisibility(View.INVISIBLE);
            appsListView.setVisibility(View.INVISIBLE);
            homeNotFound.setVisibility(View.VISIBLE);
            homeNotFound.setText(R.string.not_connection);
        }

        return fragmentView;
    }

    private void findViews() {
        appsList = (GridView) fragmentView.findViewById(R.id.appsListView);
        latest = (ToggleButton) fragmentView.findViewById(R.id.latest);
        ratings = (ToggleButton) fragmentView.findViewById(R.id.ratings);
        homeProgress = fragmentView.findViewById(R.id.homeProgress);
        appsListView = fragmentView.findViewById(R.id.appsListView);
        homeNotFound = (TextView) fragmentView.findViewById(R.id.homeNotFound);

    }

    private void setInitialView() {
        homeProgress.setVisibility(View.VISIBLE);
        appsListView.setVisibility(View.INVISIBLE);
        homeNotFound.setVisibility(View.INVISIBLE);
    }

    private void setListeners() {
        appsList.setOnItemClickListener(this);
        latest.setOnClickListener(this);
        ratings.setOnClickListener(this);
    }

    private void getApps() {
        // TODO replace service
        AppListType appListType = AppListType.DEFAULT;
        String words = "";
        String name = "";
        int start = 0;
        int elements = 10000;
        String sorting = "";
        int countryId = 0;
        int categoryId = 0;
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
                            onGetAppsServiceResponse(true, apps);
                        } else {
                            onGetAppsServiceResponse(false, new Apps());
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        onGetAppsServiceResponse(false, new Apps());
                    }
                });
        /*GetAppsService service = new GetAppsService(Constants.GETAPPS_SERVICE, getActivity());
        service.setOnGetAppsServiceResponseListener(this);
		service.runService();*/
    }

    public void onGetAppsServiceResponse(boolean success, Apps apps) {
        if (!success) {
            Intent intent = new Intent(context, StartUpActivity.class);
            intent.putExtra(LOGIN_ERROR, true);
            startActivity(intent);
            context.finish();
        } else {
            this.apps = setAppsWithImage(apps);
            sortByDate();
            loadAdapter(success);

            AppModel model = new AppModel(context);
            model.clearAppsTable();
            model.setApps(apps);
            installedApps = apps;
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


    private void loadAdapter(boolean success) {
        if (apps != null && apps.size() > 0) {
            AppsPromoAdapter appsAdapter = new AppsPromoAdapter(context, apps, this);
            appsList.setAdapter(appsAdapter);
            homeProgress.setVisibility(View.INVISIBLE);
            appsListView.setVisibility(View.VISIBLE);
            homeNotFound.setVisibility(View.INVISIBLE);
        } else {
            homeProgress.setVisibility(View.INVISIBLE);
            appsListView.setVisibility(View.INVISIBLE);
            if (success) {
                homeNotFound.setVisibility(View.VISIBLE);
            } else {
                homeNotFound.setVisibility(View.VISIBLE);
                homeNotFound.setText(R.string.server_error);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.latest:
                latest.setChecked(true);
                latest.setBackgroundResource(R.drawable.btn_latest_down);
                ratings.setBackgroundResource(R.drawable.btn_ratings_up);
                ratings.setChecked(false);
                sortByDate();
                break;

            case R.id.ratings:
                latest.setBackgroundResource(R.drawable.btn_latest_up);
                ratings.setBackgroundResource(R.drawable.btn_ratings_down);

                ratings.setChecked(true);
                latest.setChecked(false);
                sortByRate();
                break;
        }
    }

    private void sortByDate() {
//		if (App.getSortType() == App.SORT_BY_DATE) {
//			orderAsc = !orderAsc;
//		} else {
//			orderAsc = true;
        App.setSortType(App.SORT_BY_DATE);
//		}
        if (apps != null) {
            Collections.sort(apps);
//			if (!orderAsc)
//				Collections.reverse(apps);

            appsList.invalidateViews();
        }
    }

    private void sortByRate() {
//		if (App.getSortType() == App.SORT_BY_RATE) {
//			orderAsc = !orderAsc;
//		} else {
//			orderAsc = true;
        App.setSortType(App.SORT_BY_RATE);
//		}
        if (apps != null) {
            Collections.sort(apps);
//			if (!orderAsc)
//				Collections.reverse(apps);
//
            appsList.invalidateViews();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        Intent i = new Intent(getActivity(), com.tempos21.market.ui.AppActivity.class);
        i.putExtra(com.tempos21.market.ui.AppActivity.EXTRA_APP_ID, apps.get(position).getId());
        startActivity(i);
    }

    public GridView getAppsList() {
        return appsList;
    }

    public void setAppsList(GridView appsList) {
        this.appsList = appsList;
    }

}
