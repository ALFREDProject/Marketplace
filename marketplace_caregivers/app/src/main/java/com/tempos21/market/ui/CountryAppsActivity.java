package com.tempos21.market.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tempos21.market.Constants;
import com.tempos21.market.client.bean.App;
import com.tempos21.market.client.bean.AppWithImage;
import com.tempos21.market.client.bean.Apps;
import com.tempos21.market.client.bean.AppsWithImage;
import com.tempos21.market.client.bean.Countries;
import com.tempos21.market.client.bean.Country;
import com.tempos21.market.client.bean.OS;
import com.tempos21.market.client.bean.Platform;
import com.tempos21.market.client.bean.Platforms;
import com.tempos21.market.client.service.impl.GetAppsService;
import com.tempos21.market.client.service.impl.GetAppsService.OnGetAppsServiceResponseListener;
import com.tempos21.market.client.service.impl.GetCountriesService;
import com.tempos21.market.client.service.impl.GetCountriesService.OnGetCountriesServiceResponseListener;
import com.tempos21.market.client.service.impl.ServiceErrorCodes;
import com.tempos21.market.client.service.input.GetAppsInput;
import com.tempos21.market.ui.adapter.CountriesBannerAdapter;
import com.tempos21.market.ui.fragment.HomeFragment;
import com.tempos21.market.ui.view.listApps.AppsModeView;
import com.tempos21.market.ui.view.listApps.AppsModeView.OnModeChangedListener;
import com.tempos21.market.ui.view.listApps.ListAppsView;
import com.tempos21.market.ui.view.listApps.ListAppsView.OnItemClickListener;
import com.tempos21.market.ui.view.listApps.ListAppsView.OnLoadingStatusListener;
import com.tempos21.market.util.MarketPlaceHelper;
import com.tempos21.market.util.TLog;
import com.worldline.alfredo.R;

import java.util.Collections;

import eu.alfred.api.market.AppListType;
import eu.alfred.api.market.responses.apps.AppList;
import eu.alfred.api.market.responses.country.CountryList;
import eu.alfred.api.market.responses.listener.GetAppListResponseListener;
import eu.alfred.api.market.responses.listener.GetCountryListResponseListener;

public final class CountryAppsActivity extends TActivity implements
        OnModeChangedListener, OnLoadingStatusListener,
        OnItemClickListener,
        OnItemSelectedListener {

    public static final String EXTRA_COUNTRY_ID = "countryId";

    private ListAppsView listAppsView;
    private AppsModeView appsModeView;
    private GetAppsService getAppsService;
    private String countryId = null;
    private int appsCount = 0;
    private Apps allApps = new Apps();
    private Countries countries;
    private Gallery countriesList;
    private ProgressBar countriesProgress;
    private CountriesBannerAdapter countriesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.country_apps);
        findViews();
        setListeners();
        countryId = this.getIntent().getExtras().getString(EXTRA_COUNTRY_ID);


        getCountries();
        if (!appsModeView.getMode()) {
            listAppsView.swapMode();

        }

    }

    private void findViews() {
        listAppsView = (ListAppsView) findViewById(R.id.listAppsViews);
        appsModeView = (AppsModeView) findViewById(R.id.appsModeView);
        countriesList = (Gallery) findViewById(R.id.countryList);
        countriesProgress = (ProgressBar) findViewById(R.id.countryProgress);

    }

    private void setListeners() {
        appsModeView.setOnModeChangedListener(this);
        listAppsView.setOnLoadingStatusListener(this);
        listAppsView.setOnItemClickListener(this);
        countriesList.setOnItemSelectedListener(this);
        countriesList.setCallbackDuringFling(false);
    }

    private void getData() {
        GetAppsInput input = new GetAppsInput();
        input.setSorting(GetAppsInput.SORTING_APPS_DATE);
        input.setStart(appsCount);
        input.setCountry(countryId);

        AppListType appListType = AppListType.DEFAULT;
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
        /*
        getAppsService = new GetAppsService(Constants.GETAPPS_SERVICE, this);
        getAppsService.setOnGetAppsServiceResponseListener(this);
        getAppsService.runService(input);
        */


    }

    private void getCountries() {

        countriesProgress.setVisibility(View.VISIBLE);
        loadCountries();

    }

    private void loadCountries() {
        /*
        GetCountriesService getCountries = new GetCountriesService(
                Constants.GETCOUNTRIES_SERVICE, this);
        getCountries.setOnGetCountriesServiceResponseListener(this);
        getCountries.runService();
        */

        MarketPlaceHelper.getInstance().marketPlace.getCountryList(new GetCountryListResponseListener() {
            @Override
            public void onSuccess(CountryList countryList) {
                Countries countries = new Countries();
                Country country;
                for (eu.alfred.api.market.responses.country.Country country1 : countryList.countries) {
                    country = new Country();
                    country.setId("" + (country1.id != null ? country1.id : -1));
                    country.setName(country1.name);
                    countries.add(country);
                }
                onGetCountriesResponse(true, countries);
            }

            @Override
            public void onError(Exception e) {
                onGetCountriesResponse(false, new Countries());
            }
        });
    }

    public void onGetCountriesResponse(boolean success, Countries countries) {
        Collections.sort(countries);
        this.countries = countries;

        countriesAdapter = new CountriesBannerAdapter(this, countries);
        countriesList.setAdapter(countriesAdapter);
        countriesProgress.setVisibility(View.INVISIBLE);
        countriesList.setSelection(countries.getCountryPosition(countryId), true);
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

    public void onGetAppsServiceResponse(boolean success, Apps apps) {
        if (success && apps != null
                && apps.size() > 0) {
            if (appsCount > 0) {
                listAppsView.addApps(setAppsWithImage(apps));
            } else {
                listAppsView.setApps(setAppsWithImage(apps));
            }
            allApps.addAll(apps);
        } else {
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
    public void onItemClick(App app) {
        Intent i = new Intent(this, AppActivity.class);
        i.putExtra(AppActivity.EXTRA_APP_ID, app.getId());
        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        for (int i = 0; i < countriesList.getChildCount(); i++) {
            View v = countriesList.getChildAt(i);
            TextView t = (TextView) v.findViewById(R.id.nameCountry);
            if (v.equals(view)) {
                t.setTypeface(null, Typeface.BOLD);
            } else {
                t.setTypeface(null, Typeface.NORMAL);
            }
        }

        countryId = countries.get(position).getId();
        TLog.e("Selected " + countries.get(position).getName());
        appsCount = 0;

        getData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }


}
