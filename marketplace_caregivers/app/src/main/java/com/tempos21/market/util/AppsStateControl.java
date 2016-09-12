package com.tempos21.market.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tempos21.market.Constants;
import com.tempos21.market.client.bean.App;
import com.tempos21.market.client.bean.Apps;
import com.tempos21.market.client.bean.OS;
import com.tempos21.market.client.bean.Platform;
import com.tempos21.market.client.bean.Platforms;
import com.tempos21.market.client.bean.ReportsUpdates;
import com.tempos21.market.client.bean.Transaction;
import com.tempos21.market.client.bean.Transactions;
import com.tempos21.market.client.service.impl.GetInstalledAppsService;
import com.tempos21.market.client.service.impl.GetInstalledAppsService.OnGetInstalledAppsServiceResponseListener;
import com.tempos21.market.client.service.impl.ServiceErrorCodes;
import com.tempos21.market.client.service.impl.TransactionsService;
import com.tempos21.market.client.service.impl.TransactionsService.OnModifyInstalledAppsServiceResponseListener;
import com.tempos21.market.client.service.input.GetAppsInput;
import com.tempos21.market.client.service.input.TransactionsInput;
import com.tempos21.market.device.BinaryInstaller;

import java.util.ArrayList;
import java.util.List;

import eu.alfred.api.market.AppListType;
import eu.alfred.api.market.request.Change;
import eu.alfred.api.market.request.Changes;
import eu.alfred.api.market.responses.apps.AppList;
import eu.alfred.api.market.responses.listener.GetAppListResponseListener;
import eu.alfred.api.market.responses.listener.SetInstalledAppsResponseListener;
import eu.alfred.api.market.responses.set_installed_apps.Datum;


/**
 * This class controls if the installed user apps are the same of the apps installed on server
 *
 * @author A519130
 */
public class AppsStateControl implements OnModifyInstalledAppsServiceResponseListener {

    private Context context;
    private OnModifyInstalledAppsServiceResponseListener responseListener = this;
    private int reply = 0;
    private Apps serverApps;
    private App appInstalled;

    public AppsStateControl(Context context) {
        this.context = context;
    }

    public AppsStateControl(Context context, App app) {
        this.context = context;
        appInstalled = app;
    }

    /**
     * This method calls the service to request the installed apps on server
     */
    public void getInstalledApps() {
        GetAppsInput input = new GetAppsInput();
        input.setSorting(GetAppsInput.SORTING_APPS_DATE);
        input.setStart(0);
        input.setElements(10);


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
        GetInstalledAppsService installedAppsService = new GetInstalledAppsService(Constants.GET_INSTALLED_APP_SERVICE, context);
        installedAppsService.setOnGetInstalledAppsServiceResponseListener(this);
        installedAppsService.setupParameters(input);
        installedAppsService.runService();
        */
    }


    /**
     * This method receives the apps installed on server
     */
    public void onGetInstalledAppsServiceResponse(boolean success, Apps apps) {
        serverApps = apps;

        if (serverApps != null) {
            if (appInstalled != null) {
                insertAppOnServer();

            } else {
                for (App app : serverApps) {
                    String type = "";
                    int versionNumber = 0;

                    if (BinaryInstaller.isAppInstalled(context, app.getPackageName())) {
                        versionNumber = BinaryInstaller.getVersion(context, app.getPackageName());

                        if (versionNumber > app.getVersion_number()) {
                            type = "U";
                            selectState(app.getId(), type, versionNumber);
                        }
                    } else {
                        type = "D";
                        versionNumber = app.getVersion_number();
                        selectState(app.getId(), type, versionNumber);
                    }
                }
            }
        }
    }


    /**
     * This method insert de app installed on server
     *
     */
    public void insertAppOnServer() {
        boolean find = false;

        if (serverApps != null) {
            for (App appServer : serverApps) {
                if (appServer.getPackageName().equals(appInstalled.getPackageName())) {
                    find = true;
                }
            }

            if (!find) {
                selectState(appInstalled.getId(), "I", appInstalled.getVersion_number());
            }
        }
    }


    /**
     * This method selects the state of app to update the server ("D","U")
     *
     * @param id      app id
     * @param type    state to update
     * @param version app version
     */
    private void selectState(String id, String type, int version) {
        AsyncNotifyInstall notifyInstall;

        if (!type.equals("")) {


            Changes changes = new Changes();
            List<Change> changeList = new ArrayList<Change>();
            Change change = new Change();
            try {
                change.setId(Integer.parseInt(id));
            } catch (Exception ignored) {
            }
            change.setType(type);
            change.setVersion(version);
            changeList.add(change);
            changes.setChange(changeList);
            MarketPlaceHelper.getInstance().marketPlace.setInstalledApps(changes, new SetInstalledAppsResponseListener() {
                @Override
                public void onSuccess(Datum datum) {
                    Log.d("UpdatedFragment", "setInstalledApps onSuccess " + datum);
                }

                @Override
                public void onError(Exception e) {
                    Log.d("UpdatedFragment", "setInstalledApps onError " + e);
                }
            });









            /*notifyInstall = new AsyncNotifyInstall(context, id, type, version);
            notifyInstall.execute();*/
        }
    }

    /**
     * Receive the result of the update on server
     */
    @Override
    public void onModifyInstalledAppsServiceResponse(int responseCode, ReportsUpdates reports) {
        if (!(responseCode == ServiceErrorCodes.OK && reports.get(0).getResult().equals("ok")) && reply < 3) {
            getInstalledApps();
            reply++;
        } else {
            reply = 0;
        }
    }

    /**
     * This class manages the requests to update the apps state on server
     *
     * @author A519130
     */
    private class AsyncNotifyInstall extends AsyncTask<Void, Void, Void> {

        Transaction transaction;
        private Context context;

        public AsyncNotifyInstall(Context context, String id, String type, int version) {
            this.transaction = new Transaction(id, type, version);
            this.context = context;
        }

        /**
         * Send the actual apps state to server
         */
        @Override
        protected Void doInBackground(Void... params) {
            Transactions change = new Transactions();
            change.add(transaction);
            TransactionsInput input = new TransactionsInput();
            input.setTransactions(change);

            TransactionsService service = new TransactionsService(Constants.TRANSACTIONS_SERVICE, context);
            service.setOnModifyInstalledAppsServiceResponseListener(responseListener);
            service.setupParameters(input);
            service.runService();
            return null;
        }
    }

}
