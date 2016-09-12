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

import com.tempos21.market.Constants;
import com.tempos21.market.client.bean.App;
import com.tempos21.market.client.bean.AppWithImage;
import com.tempos21.market.client.bean.Apps;
import com.tempos21.market.client.bean.AppsWithImage;
import com.tempos21.market.client.service.impl.GetAppsService;
import com.tempos21.market.client.service.impl.GetAppsService.OnGetAppsServiceResponseListener;
import com.tempos21.market.client.service.impl.ServiceErrorCodes;
import com.tempos21.market.client.service.input.GetAppsInput;
import com.tempos21.market.device.Connection;
import com.tempos21.market.ui.AppActivity;
import com.tempos21.market.ui.StartUpActivity;
import com.tempos21.market.ui.view.listApps.AppsModeView;
import com.tempos21.market.ui.view.listApps.AppsModeView.OnModeChangedListener;
import com.tempos21.market.ui.view.listApps.ListAppsView;
import com.tempos21.market.ui.view.listApps.ListAppsView.OnItemClickListener;
import com.tempos21.market.ui.view.listApps.ListAppsView.OnLoadingStatusListener;
import com.tempos21.market.util.TLog;
import com.worldline.alfredo.R;

//@SuppressLint({ "ValidFragment", "ValidFragment" })
public final class UpdatedAppsFragment extends Fragment implements OnModeChangedListener, OnLoadingStatusListener, OnGetAppsServiceResponseListener, OnItemClickListener {
    private View me;
    private ListAppsView listAppsView;
    private AppsModeView appsModeView;
    private GetAppsService searchAppsService;
    private String countryId = null;
    private String categoryId = null;
    private int appsCount = 0;
    private AppsWithImage allApps;
    private View notConnection;
    private Activity context;
    private TextView sectionTitle;


    public UpdatedAppsFragment() {

    }

    public UpdatedAppsFragment(Activity context) {
        this.context = context;
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
            notConnection.setVisibility(View.VISIBLE);
            listAppsView.setVisibility(View.INVISIBLE);
        }

        TLog.v("oncreateview");

        return me;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TLog.v("onactivitycreated");
    }

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
        sectionTitle.setText(getString(R.string.menuUpdated));
        searchAppsService = new GetAppsService(Constants.GETAPPS_SERVICE, getActivity());
        searchAppsService.setOnGetAppsServiceResponseListener(this);
        GetAppsInput input = new GetAppsInput();
        input.setSorting(GetAppsInput.SORTING_APPS_DATE);
        input.setStart(appsCount);
        input.setCountry(countryId);
        input.setCategory(categoryId);
        searchAppsService.runService(input);

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

    @Override
    public void onGetAppsServiceResponse(int responseCode, Apps apps) {
        if (responseCode == ServiceErrorCodes.OK && apps != null && apps.size() > 0) {
            if (appsCount > 0) {
                listAppsView.addApps(setAppsWithImage(apps));
            } else {
                listAppsView.setApps(setAppsWithImage(apps));
            }
            allApps.addAll(setAppsWithImage(apps));
        } else {
            if (responseCode == ServiceErrorCodes.LOGIN_ERROR) {
                Intent intent = new Intent(context, StartUpActivity.class);
                intent.putExtra(HomeFragment.LOGIN_ERROR, true);
                startActivity(intent);
                context.finish();
            } else {
                listAppsView.setFinished();
            }
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
        startActivity(i);
    }

}
