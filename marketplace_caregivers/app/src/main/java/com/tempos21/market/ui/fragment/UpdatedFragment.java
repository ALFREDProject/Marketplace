package com.tempos21.market.ui.fragment;


//import android.annotation.SuppressLint;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tempos21.market.Constants;
import com.tempos21.market.client.bean.App;
import com.tempos21.market.client.bean.AppWithImage;
import com.tempos21.market.client.bean.Apps;
import com.tempos21.market.client.bean.AppsWithImage;
import com.tempos21.market.client.bean.OS;
import com.tempos21.market.client.bean.Platform;
import com.tempos21.market.client.bean.Platforms;
import com.tempos21.market.client.bean.ReportsUpdates;
import com.tempos21.market.client.bean.Transaction;
import com.tempos21.market.client.bean.Transactions;
import com.tempos21.market.client.service.impl.ServiceErrorCodes;
import com.tempos21.market.client.service.impl.TransactionsService;
import com.tempos21.market.client.service.impl.TransactionsService.OnModifyInstalledAppsServiceResponseListener;
import com.tempos21.market.client.service.input.GetAppsInput;
import com.tempos21.market.client.service.input.TransactionsInput;
import com.tempos21.market.device.BinaryInstaller;
import com.tempos21.market.device.Connection;
import com.tempos21.market.ui.adapter.UpdatesAdapter;
import com.tempos21.market.util.AppsStateControl;
import com.tempos21.market.util.MarketPlaceHelper;
import com.tempos21.market.util.TLog;
import com.worldline.alfredo.R;

import java.util.ArrayList;
import java.util.List;

import eu.alfred.api.market.AppListType;
import eu.alfred.api.market.request.Change;
import eu.alfred.api.market.request.Changes;
import eu.alfred.api.market.responses.apps.AppList;
import eu.alfred.api.market.responses.listener.GetAppListResponseListener;
import eu.alfred.api.market.responses.listener.SetInstalledAppsResponseListener;
import eu.alfred.api.market.responses.set_installed_apps.Datum;


//@SuppressLint("ValidFragment")
public class UpdatedFragment extends Fragment implements OnItemClickListener, OnModifyInstalledAppsServiceResponseListener {//, OnClickListener {

    private View fragmentView;
    private Activity context;
    //private TextView updateAll;
    private ListView updatesList;
    private AppsWithImage apps = new AppsWithImage();
    private ProgressBar loading;
    //	private static ArrayList<Update> pendingUpdates = new ArrayList<Update>();
    private View updatingLayout;
    private View listLayout;
    private TextView updateText;
    private TextView notConnection;
    private int appsCount = 0;
    private int elements = 10;
    private App app;
    private int reply = 0;


    public UpdatedFragment() {
    }


    public UpdatedFragment(Activity context) {
        this.context = context;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.updated, null, true);

        findViews();
        setListeners();
        setInitialData();

        if (Connection.netConnect(context)) {
            notConnection.setVisibility(View.INVISIBLE);
            getData();
        } else {
            updatesList.setVisibility(View.INVISIBLE);
            loading.setVisibility(View.INVISIBLE);
            updateText.setVisibility(View.VISIBLE);
            notConnection.setText(R.string.not_connection);
        }

        TLog.v("onCreateView");
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        AppsStateControl control = new AppsStateControl(context);
        control.getInstalledApps();

        if (app == null) {
            return;
        }

        if (BinaryInstaller.isAppInstalled(context, app.getPackageName())) {
            int versionNumber = BinaryInstaller.getVersion(context, app.getPackageName());
            App delete = null;

            if (versionNumber == app.getVersion_number()) {

                for (App myApp : apps) {
                    if (myApp.getId().equals(app.getId())) {
                        delete = myApp;
                    }
                }

                if (delete != null) {
                    apps.remove(delete);
                    setData(true);
                }

                String type = "U";

                Changes changes = new Changes();
                List<Change> changeList = new ArrayList<Change>();
                Change change = new Change();
                try {
                    change.setId(Integer.parseInt(app.getId()));
                } catch (Exception ignored) {
                }
                change.setType(type);
                change.setVersion(versionNumber);
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
                /*AsyncNotifyInstall notifyInstall = new AsyncNotifyInstall(context, app.getId(), type, versionNumber);
                notifyInstall.execute();*/
            }

            hideUpdatingLayout();
        }
    }

    private void findViews() {
        //updateAll = (TextView) fragmentView.findViewById(R.id.updateAll);
        updatesList = (ListView) fragmentView.findViewById(R.id.updateList);
        loading = (ProgressBar) fragmentView.findViewById(R.id.loading);
        updatingLayout = fragmentView.findViewById(R.id.updatingLayout);
        listLayout = fragmentView.findViewById(R.id.listLayout);
        updateText = (TextView) fragmentView.findViewById(R.id.updatingApp);
        notConnection = (TextView) fragmentView.findViewById(R.id.notConnection);
    }

    private void setListeners() {
        //updateAll.setOnClickListener(this);
        updatesList.setOnItemClickListener(this);
    }

    /***
     * Establish all the initial data to be shown while data is retrieved
     */
    private void setInitialData() {
        loading.setVisibility(View.VISIBLE);
        //updateAll.setVisibility(View.INVISIBLE);
        updatesList.setVisibility(View.INVISIBLE);
        showListLayout();
    }

    private void showListLayout() {
        listLayout.setVisibility(View.VISIBLE);
        updatingLayout.setVisibility(View.INVISIBLE);
    }

    private void getData() {
        GetAppsInput input = new GetAppsInput();
        input.setSorting(GetAppsInput.SORTING_APPS_DATE);
        input.setStart(appsCount);
        input.setElements(elements);


        AppListType appListType = AppListType.UPDATABLE;
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
        GetUpdatableAppsService service = new GetUpdatableAppsService(Constants.GET_UPDATED_APPS, getActivity());
        service.setOnGetAppsServiceResponseListener(this);
        service.runService(input);
        */
    }

    public void onGetAppsServiceResponse(boolean success, Apps apps) {
        this.apps = setAppsWithImage(apps);
        setData(success);

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

    private void setData(boolean success) {
        loading.setVisibility(View.INVISIBLE);
        if (apps.size() > 0) {
            updatesList.setVisibility(View.VISIBLE);
            loadAdapter();
            //updateAll.setVisibility(View.VISIBLE);
        } else {
            notConnection.setVisibility(View.VISIBLE);
            if (success) {
                notConnection.setText(R.string.not_apps);
            } else {
                notConnection.setText(R.string.server_error);
            }
            //updateAll.setVisibility(View.GONE);
        }
    }

    private void loadAdapter() {
        UpdatesAdapter updatesAdapter = new UpdatesAdapter(context, apps);
        if (apps != null && apps.size() > 0) {
            updatesList.setAdapter(updatesAdapter);
            updatesList.invalidateViews();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        app = apps.get(position);
        showUpdatingLayout(app.getName());
//		pendingUpdates.add(new Update(app.getId(), app.getVersion_number(), app.isExternalBinary(), app.getExternalUrl()));
//		DownloadApp asyncDown = new DownloadApp(context);
//		asyncDown.execute();

        final AlertDialog.Builder dialog = new Builder(context);
        dialog.setTitle(R.string.update_app_title);
        dialog.setMessage(R.string.update_app_ask);
        dialog.setPositiveButton(R.string.ok, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                try {
                    downloadApp(new Update(app.getId(), app.getVersion_number(), app.isExternalBinary(), app.getExternalUrl()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        dialog.setNegativeButton(R.string.cancel, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                hideUpdatingLayout();
            }
        });
        dialog.show();

    }

    private void showUpdatingLayout(String appName) {
        listLayout.setVisibility(View.INVISIBLE);
        updatingLayout.setVisibility(View.VISIBLE);
        updateText.setText(String.format(context.getString(R.string.updatingApp), appName));
    }

    private void downloadApp(Update update) throws Exception {
        if (update.isExternal) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(update.url));
            startActivity(intent);
        } else {
            BinaryInstaller.installApplication(context, update.appId, "", update.appVersion, 0);
        }
    }

    private void hideUpdatingLayout() {
        listLayout.setVisibility(View.VISIBLE);
        updatingLayout.setVisibility(View.GONE);
        updateText.setVisibility(View.GONE);
    }

    @Override
    public void onModifyInstalledAppsServiceResponse(int responseCode, ReportsUpdates reports) {
        if (!(responseCode == ServiceErrorCodes.OK && reports.get(0).getResult().equals("ok")) && reply < 3) {
            AsyncNotifyInstall notifyInstall = new AsyncNotifyInstall(context, app.getId(), "U", BinaryInstaller.getVersion(context, app.getPackageName()));
            notifyInstall.execute();
            reply++;
        } else {
            reply = 0;
        }
    }

    public ListView getUpdatesList() {
        return updatesList;
    }

    public void setUpdatesList(ListView updatesList) {
        this.updatesList = updatesList;
    }

    private class AsyncNotifyInstall extends AsyncTask<Void, Void, Void> {
        Transaction transaction;
        private Context context;

        public AsyncNotifyInstall(Context context, String id, String type, int version) {
            this.transaction = new Transaction(id, type, version);
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Transactions change = new Transactions();
            change.add(transaction);
            TransactionsInput input = new TransactionsInput();
            input.setTransactions(change);

            TransactionsService service = new TransactionsService(Constants.TRANSACTIONS_SERVICE, context);
            service.setOnModifyInstalledAppsServiceResponseListener(UpdatedFragment.this);
            service.setupParameters(input);
            service.runService();

            return null;
        }
    }

    private class Update {
        String appId;
        int appVersion;
        //		boolean started = false;
        boolean isExternal = false;
        String url = "";

        public Update(String appId, int appVersion, boolean isExternal, String url) {
            super();
            this.appId = appId;
            this.appVersion = appVersion;
            this.isExternal = isExternal;
            this.url = url;
        }
    }


//	@Override
//	public void onStart() {
//		super.onStart();
//		if (pendingUpdates.size() > 0 && pendingUpdates.get(0).started) {
//			// TODO: NOTIFY SERVER
//			pendingUpdates.remove(0);
//			TLog.v("onStart");
//		}
//		if (pendingUpdates.size()==0) {
//			showListLayout();
//		}
//	}


//	@Override
//	public void onClick(View v) {
//		if (v.getId()==updateAll.getId()) {
//			for (App app:apps) {
//				pendingUpdates.add(new Update(app.getId(), app.getVersion_number(), app.isExternalBinary(), app.getExternalUrl()));
//			}
//		}
//		DownloadApp asyncDown = new DownloadApp(context);
//		asyncDown.execute();
//	}


//	private class DownloadApp extends AsyncTask<Void, Void, Integer> {
//	private final static int INSTALL_ERROR = 0;
//	private final static int INSTALL_OK = 1;
//
//	private Context context;
//
//	public DownloadApp(Context context) {
//		this.context = context;
//
//	}
//
//	@Override
//	protected Integer doInBackground(Void... params) {
//		try {
//			Update update = pendingUpdates.get(0);
//			update.started = true;
//			BinaryInstaller.installApplication(context, update.appId, update.appVersion);
//		} catch (Exception e) {
//			return INSTALL_ERROR;
//			
//		}
//		return INSTALL_OK;
//	}
//
//	@Override
//	protected void onPostExecute(Integer result) {
//		super.onPostExecute(result);
//		if (result == INSTALL_ERROR) {
//			showListLayout();
//			MToast.makeText(context, context.getString(R.string.updateError),Toast.LENGTH_LONG).show();
//		}
//	}
//}

}
