package com.tempos21.mymarket.sdk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tempos21.mymarket.sdk.AbstractAppList;
import com.tempos21.mymarket.sdk.AppList;
import com.tempos21.mymarket.sdk.AppListConfig;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.AppListType;
import com.tempos21.mymarket.sdk.model.app.App;

import java.util.ArrayList;
import java.util.List;

public class AppNotificationReceiver extends BroadcastReceiver implements AbstractAppList.OnAppListListener, MyMarket.OnMarketSetInstalledAppsListener {

    private Context context;
    private AppList appList;
    private AppList installedAppList;
    private List<App> serverAppList;

    /**
     * This method receives a message for any application notification (Install/Uninstall/Update)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        appList = MyMarket.getInstance().getAppList(new AppListConfig.Builder() // AppListConfig
                .setName("Receiver AppList") // Name
                .setElements(0).build()); // Number of elements per page

        installedAppList = MyMarket.getInstance().getInstalledAppList(new AppListConfig.Builder() // AppListConfig
                .setName("Receiver Installed AppList") // Name
                .setElements(0).build()); // Number of elements per page

        if (!intent.getData().getEncodedSchemeSpecificPart().equals(context.getPackageName())) {
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) { // "PACKAGE INSTALLED: " + intent.getData().getEncodedSchemeSpecificPart()
                checkApps();
            } else if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) { // "PACKAGE REMOVED: " + intent.getData().getEncodedSchemeSpecificPart()
                checkApps();
            } else if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) { // "PACKAGE REPLACED: " + intent.getData().getEncodedSchemeSpecificPart()
                checkApps();
            }
        }
    }

    private void checkApps() {
        appList.getForcedAppList(this);
    }

    @Override
    public void onAppListSuccess(AppListType type, List<App> apps) {
        switch (type) {
            case DEFAULT:
                serverAppList = new ArrayList<App>();
                for (App app : apps) {
                    serverAppList.add(app);
                }
                installedAppList.getForcedAppList(this);
                break;
            case INSTALLED:
                MyMarket.getInstance().checkInstalledApps(this, serverAppList, apps);
                break;
        }
    }

    @Override
    public void onAppListError(AppListType type, long id, Exception e) {
        switch (type) {
            case DEFAULT:
                break;
            case INSTALLED:
                break;
        }
    }

    @Override
    public void onSetInstalledAppsSuccess() {

    }

    @Override
    public void onSetInstalledAppsError(long id, Exception e) {

    }
}
