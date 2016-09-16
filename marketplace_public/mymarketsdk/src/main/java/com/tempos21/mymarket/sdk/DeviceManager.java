package com.tempos21.mymarket.sdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tempos21.mymarket.sdk.model.InstalledApp;
import com.tempos21.mymarket.sdk.model.app.App;
import com.tempos21.mymarket.sdk.model.app.AppDetail;
import com.tempos21.mymarket.sdk.util.MarketPlaceHelper2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import eu.alfred.api.market.request.Change;
import eu.alfred.api.market.request.Changes;
import eu.alfred.api.market.responses.listener.SetInstalledAppsResponseListener;
import eu.alfred.api.market.responses.set_installed_apps.Datum;

class DeviceManager {

    private static DeviceManager instance;

    private PackageManager packageManager;

    private Context context;

    private long bytes = 0;

    private DeviceManager(Context context) {
        this.context = context;
        packageManager = context.getPackageManager();
    }

    static DeviceManager init(Context context) {
        if (instance == null) {
            instance = new DeviceManager(context);
        }
        return instance;
    }

    static DeviceManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("Device not initialized");
        }
        return instance;
    }

    List<PackageInfo> getInstalledApplicationsByTheUser() {
        List<ApplicationInfo> apps = context.getPackageManager().getInstalledApplications(0);
        List<PackageInfo> userApps = new ArrayList<PackageInfo>();
        for (ApplicationInfo app : apps) {
            if ((app.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) {
                if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM) {
                    // User application
                    try {
                        userApps.add(packageManager.getPackageInfo(app.packageName, 0));
                    } catch (PackageManager.NameNotFoundException e) {
                        if (MyMarket.getInstance().isDebug()) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return userApps;
    }

    boolean isAppUpdatable(AppDetail appDetail) {
        for (PackageInfo packageInfo : getInstalledApplicationsByTheUser()) {
            if (packageInfo.packageName.equals(appDetail.packageName)) {
                if (packageInfo.versionCode < appDetail.versionNumber) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean isAppInstalled(String packageName) {
        for (PackageInfo packageInfo : getInstalledApplicationsByTheUser()) {
            if (packageInfo.packageName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    boolean isAppDownloaded(AppDetail appDetail) {
        File file = new File(String.format(MyMarket.getInstance().getAppsDestinationFile(), appDetail.id, appDetail.name.trim().replaceAll("\\s+", "")));
        if (file.exists()) {
            bytes = file.length();
        }
        return file.exists();
    }

    boolean getIfDownloadedAppHasSameSizeAsInServer(AppDetail appDetail) {
        return round(appDetail.size) == round((bytes / 1024f) / 1024f);
    }

    double round(double number) {
        return (double) Math.round(number * 1000) / 1000l;
    }

    void checkInstalledApps(final MyMarket.OnMarketSetInstalledAppsListener listener, List<App> serverAppList, final List<App> registeredInServerAppList) {
        List<AppInstalledInTheDevice> appListFromServerInstalledInTheDevice = getAppListFromServerInstalledInTheDevice(serverAppList);
        List<App> registeredInServerButNotInstalledAppList = getAppListRegisteredInServerButNotInstalled(registeredInServerAppList, appListFromServerInstalledInTheDevice);

        List<InstalledApp> installedAppListChanges = new ArrayList<InstalledApp>();

        if (appListFromServerInstalledInTheDevice.size() > 0) {
            for (AppInstalledInTheDevice app : appListFromServerInstalledInTheDevice) {
                InstalledApp installedAppRequest = new InstalledApp();
                installedAppRequest.id = app.id;
                installedAppRequest.type = InstalledApp.INSTALL;
                installedAppRequest.version = app.versionCode;
                installedAppListChanges.add(installedAppRequest);
            }
        }

        if (registeredInServerButNotInstalledAppList.size() > 0) {
            for (App app : registeredInServerButNotInstalledAppList) {
                InstalledApp installedAppRequest = new InstalledApp();
                installedAppRequest.id = app.id;
                installedAppRequest.type = InstalledApp.DELETE;
                installedAppRequest.version = app.versionNumber;
                installedAppListChanges.add(installedAppRequest);
            }
        }

        if (installedAppListChanges.size() > 0) {

            Changes changes = new Changes();
            List<Change> changeList = new ArrayList<Change>();


            Change change;

            for (InstalledApp installedAppListChange : installedAppListChanges) {
                change = new Change();
                change.setId((int) installedAppListChange.id);
                change.setType(installedAppListChange.type);
                change.setVersion(installedAppListChange.version);
                changeList.add(change);
            }
            changes.setChange(changeList);


            MarketPlaceHelper2.getInstance().marketPlace.setInstalledApps(changes, new SetInstalledAppsResponseListener() {
                @Override
                public void onSuccess(Datum datum) {
                    listener.onSetInstalledAppsSuccess();
                }

                @Override
                public void onError(Exception e) {
                    listener.onSetInstalledAppsError(-1, e);
                }
            });

            //MyMarket.getInstance().setInstalledApps(listener, installedAppListChanges);
        }
    }

    int getDownloadedAppVersionCode(AppDetail appDetail) {
        return context.getPackageManager().getPackageArchiveInfo(String.format(MyMarket.getInstance().getAppsDestinationFile(), appDetail.id, appDetail.name.trim().replaceAll("\\s+", "")), 0).versionCode;
    }

    int getInstalledAppVersionCode(AppDetail appDetail) {
        List<PackageInfo> deviceAppsList = getInstalledApplicationsByTheUser();
        for (PackageInfo info : deviceAppsList) {
            if (info.packageName.equals(appDetail.packageName)) {
                return info.versionCode;
            }
        }
        return 0;
    }

    List<AppInstalledInTheDevice> getAppListFromServerInstalledInTheDevice(List<App> serverAppList) {
        List<PackageInfo> deviceAppsList = getInstalledApplicationsByTheUser();
        List<AppInstalledInTheDevice> appInstalledInTheDeviceList = new ArrayList<AppInstalledInTheDevice>();
        for (App app : serverAppList) {
            for (PackageInfo info : deviceAppsList) {
                if (app.packageName.equals(info.packageName)) {
                    appInstalledInTheDeviceList.add(new AppInstalledInTheDevice(app.id, info.versionCode, info.packageName));
                }
            }
        }
        return appInstalledInTheDeviceList;
    }

    List<App> getAppListRegisteredInServerButNotInstalled(List<App> registeredInServerAppList, List<AppInstalledInTheDevice> appListFromServerInstalledInTheDevice) {
        List<App> notInstalledApps = new ArrayList<App>();
        for (App appRegistered : registeredInServerAppList) {
            boolean installedInTheDevice = false;
            for (AppInstalledInTheDevice appInstalledInTheDevice : appListFromServerInstalledInTheDevice) {
                if (appRegistered.packageName.equals(appInstalledInTheDevice.packageName)) {
                    installedInTheDevice = true;
                }
            }
            if (!installedInTheDevice) {
                notInstalledApps.add(appRegistered);
            }
        }
        return notInstalledApps;
    }

    void checkInstalledApps(final MyMarket.OnMarketSetInstalledAppsListener listener, final List<App> registeredInServerAppList) {
        List<PackageInfo> deviceAppsList = getInstalledApplicationsByTheUser();

        List<App> listOfRegisteredAppsInServerButNotInstalled = getListOfRegisteredAppsInServerButNotInstalled(registeredInServerAppList, deviceAppsList);

        List<InstalledApp> installedAppListChanges = new ArrayList<InstalledApp>();
        if (listOfRegisteredAppsInServerButNotInstalled.size() > 0) {
            for (App app : listOfRegisteredAppsInServerButNotInstalled) {
                InstalledApp installedAppRequest = new InstalledApp();
                installedAppRequest.id = app.id;
                installedAppRequest.type = InstalledApp.DELETE;
                installedAppRequest.version = app.versionNumber;
                installedAppListChanges.add(installedAppRequest);
            }
        }

        if (installedAppListChanges.size() > 0) {
            Changes changes = new Changes();
            List<Change> changeList = new ArrayList<Change>();


            Change change;

            for (InstalledApp installedAppListChange : installedAppListChanges) {
                change = new Change();
                change.setId((int) installedAppListChange.id);
                change.setType(installedAppListChange.type);
                change.setVersion(installedAppListChange.version);
                changeList.add(change);
            }
            changes.setChange(changeList);


            MarketPlaceHelper2.getInstance().marketPlace.setInstalledApps(changes, new SetInstalledAppsResponseListener() {
                @Override
                public void onSuccess(Datum datum) {
                    listener.onSetInstalledAppsSuccess();
                }

                @Override
                public void onError(Exception e) {
                    listener.onSetInstalledAppsError(-1, e);
                }
            });

            //MyMarket.getInstance().setInstalledApps(listener, installedAppListChanges);
        }
    }

    List<App> getListOfRegisteredAppsInServerButNotInstalled(List<App> registeredInServerAppList, List<PackageInfo> deviceAppsList) {
        List<App> notInstalledApps = new ArrayList<App>();
        for (App appRegistered : registeredInServerAppList) {
            boolean installedInTheDevice = false;
            for (PackageInfo packageInfo : deviceAppsList) {
                if (appRegistered.packageName.equals(packageInfo.packageName)) {
                    installedInTheDevice = true;
                }
            }
            if (!installedInTheDevice) {
                notInstalledApps.add(appRegistered);
            }
        }
        return notInstalledApps;
    }

    class AppInstalledInTheDevice {

        public long id;
        public int versionCode;
        public String packageName;

        public AppInstalledInTheDevice(long id, int versionCode, String packageName) {
            this.id = id;
            this.versionCode = versionCode;
            this.packageName = packageName;
        }
    }
}
