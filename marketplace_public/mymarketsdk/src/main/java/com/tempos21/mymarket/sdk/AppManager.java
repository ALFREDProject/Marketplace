package com.tempos21.mymarket.sdk;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.gson.Gson;
import com.tempos21.mymarket.R;
import com.tempos21.mymarket.sdk.constant.MyMarketConstants;
import com.tempos21.mymarket.sdk.manager.DownloadNotificationManager;
import com.tempos21.mymarket.sdk.model.app.AppDetail;
import com.tempos21.mymarket.sdk.util.Logger;
import com.tempos21.rampload.RampLoad;
import com.tempos21.rampload.exception.RampLoadInitException;
import com.tempos21.rampload.manager.RampLoadDownloadManager;
import com.tempos21.rampload.model.RampLoadDownload;
import com.tempos21.rampload.util.Prefs;

import java.io.File;
import java.util.List;

public class AppManager implements RampLoad.DownloadListener, RampLoad.StatusListener {

    private static final int TYPE_INSTALLED_DOWNLOADED = 0;
    private static final int TYPE_DOWNLOADED_SERVER = 1;

    private static AppManager instance;
    private Context context;
    private State state = State.DOWNLOAD;

    private AppManager(Context context) {
        this.context = context;
        DownloadNotificationManager.init(context);
        RampLoad.getInstance().addDownloadListener(this);
        RampLoad.getInstance().addStatusListener(this);
    }

    public static AppManager init(Context context) {
        if (instance == null) {
            instance = new AppManager(context);
        }
        return instance;
    }

    public static AppManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("AppManager not initialized");
        }
        return instance;
    }

    public void addTestIdles(RampLoadDownloadManager.RampLoadQueueType key, RampLoadDownload rampLoadDownload) {
        RampLoadDownloadManager.getInstance().addTestIdles(key, rampLoadDownload);
    }

    void showQueues() {
        Prefs.init(context);

        String idleRequest = Prefs.getString(RampLoadDownloadManager.RampLoadQueueType.QUEUE_IDLE.value, null);
        RampLoadDownload[] idle = new Gson().fromJson(idleRequest, RampLoadDownload[].class);
        for (RampLoadDownload r : idle) {
            Logger.logE("<QUEUE_IDLE id=\"" + r.id + "\" name=\"" + r.name + "\" url=\"" + r.url + "\" path=\"" + r.path + "\" />");
        }

        String downloadingRequest = Prefs.getString(RampLoadDownloadManager.RampLoadQueueType.QUEUE_DOWNLOADING.value, null);
        RampLoadDownload[] downloading = new Gson().fromJson(downloadingRequest, RampLoadDownload[].class);
        for (RampLoadDownload r : downloading) {
            Logger.logE("<QUEUE_DOWNLOADING id=\"" + r.id + "\" name=\"" + r.name + "\" url=\"" + r.url + "\" path=\"" + r.path + "\" />");
        }

    }

    public void startIdle() {
        showQueues();
        try {
            if (RampLoad.getInstance().getDownloading().size() == 0) {
                List<RampLoadDownload> list = RampLoad.getInstance().getIdle();
                if (list.size() > 0) {
                    RampLoad.getInstance().download(list.get(0));
                }
            }
        } catch (Exception e) {
            if (MyMarket.getInstance().isDebug()) {
                e.printStackTrace();
            }
        }
    }

    private RampLoadDownload createRampLoadDownloadFromAppDetail(AppDetail appDetail) {
        String url = String.format(MyMarketConstants.APP_DOWNLOAD_URL, appDetail.versionId);
        String destination = String.format(MyMarket.getInstance().getAppsDestinationFile(), appDetail.versionId, appDetail.name.trim().replaceAll("\\s+", ""));
        return new RampLoadDownload(appDetail.versionId, appDetail.name, url, destination);
    }

    void download(AppDetail appDetail) {
        state = State.DOWNLOADING;
        try {
            if (!appDetail.externalBinary) {
                RampLoad.getInstance().download(createRampLoadDownloadFromAppDetail(appDetail));
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(appDetail.externalUrl));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } catch (RampLoadInitException e) {
            if (MyMarket.getInstance().isDebug()) {
                e.printStackTrace();
            }
        }
    }

    void install(AppDetail appDetail) {
        context.startActivity(getIntent(createRampLoadDownloadFromAppDetail(appDetail)));
    }

    void uninstall(AppDetail appDetail) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse(context.getString(R.string.app_download_package) + appDetail.packageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    void update(AppDetail appDetail) {
        String path = String.format(MyMarket.getInstance().getAppsDestinationFile(), appDetail.versionId, appDetail.name.trim().replaceAll("\\s+", ""));
        Uri packageUri = Uri.fromFile(new File(path));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(packageUri, context.getString(com.tempos21.mymarket.R.string.app_download_type));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private Intent getIntent(RampLoadDownload download) {
        String path = String.format(MyMarket.getInstance().getAppsDestinationFile(), download.id, download.name.trim().replaceAll("\\s+", ""));
        Uri packageUri = Uri.fromFile(new File(path));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(packageUri, context.getString(R.string.app_download_type));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    boolean isAppInQueue(int id) {
        List<RampLoadDownload> list = RampLoad.getInstance().getIdle();
        for (RampLoadDownload d : list) {
            if (id == d.id) {
                return true;
            }
        }
        return false;
    }

    State getAppState(AppDetail appDetail) {
        checkAppState(appDetail);
        return state;
    }

    void checkAppState(AppDetail appDetail) {
        if (appDetail != null) {
            if (DeviceManager.getInstance().isAppInstalled(appDetail.packageName)) { // installed
                if (DeviceManager.getInstance().isAppDownloaded(appDetail)) { // installed, downloaded
                    if (DeviceManager.getInstance().getIfDownloadedAppHasSameSizeAsInServer(appDetail)) { // installed, downloaded, same size as in server
                        state = State.INSTALL;
                        int installedAppVersionCode = DeviceManager.getInstance().getInstalledAppVersionCode(appDetail);
                        int downloadedAppVersionCode = DeviceManager.getInstance().getDownloadedAppVersionCode(appDetail);
                        checkVersionCode(appDetail, TYPE_INSTALLED_DOWNLOADED, installedAppVersionCode, downloadedAppVersionCode);
                    } else { // installed, downloaded, not same size as in server
                        state = State.DOWNLOADING;
                        try {
                            int installedAppVersionCode = DeviceManager.getInstance().getInstalledAppVersionCode(appDetail);
                            int downloadedAppVersionCode = DeviceManager.getInstance().getDownloadedAppVersionCode(appDetail);
                            checkVersionCode(appDetail, TYPE_INSTALLED_DOWNLOADED, installedAppVersionCode, downloadedAppVersionCode);
                        } catch (Exception e) {
                            if (MyMarket.getInstance().isDebug()) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else { // installed, not downloaded
                    if (DeviceManager.getInstance().isAppUpdatable(appDetail)) { // installed, not downloaded, updatable
                        state = State.DOWNLOAD_UPDATE;
                    } else if (isAppInQueue(appDetail.id)) { // installed, not downloaded, not updatable, in queue
                        state = State.DOWNLOADING;
                    } else { // installed, not downloaded, not updatable, not in queue
                        state = State.UNINSTALL;
                    }
                }
            } else if (DeviceManager.getInstance().isAppDownloaded(appDetail)) { // not installed, downloaded
                if (DeviceManager.getInstance().getIfDownloadedAppHasSameSizeAsInServer(appDetail)) { // not installed, downloaded, same size as in server
                    state = State.INSTALL;
                } else { // not installed, downloaded, not same size as in server
                    state = State.DOWNLOADING;
                    try {
                        int downloadedAppVersionCode = DeviceManager.getInstance().getDownloadedAppVersionCode(appDetail);
                        int serverAppVersionCode = appDetail.versionNumber;
                        checkVersionCode(appDetail, TYPE_DOWNLOADED_SERVER, downloadedAppVersionCode, serverAppVersionCode);
                    } catch (Exception e) {
                        if (MyMarket.getInstance().isDebug()) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (isAppInQueue(appDetail.id)) { // not installed, not downloaded, in queue
                state = State.DOWNLOADING;
            } else { // not installed, not downloaded, not in queue
                state = State.DOWNLOAD;
            }
        } else {
            state = State.NULL;
        }
    }

    private void checkVersionCode(AppDetail appDetail, int type, int vc1, int vc2) {
        if (vc1 < vc2) {
            /**
             * version installed older than the downloaded
             * or version downloaded older than the one in the server
             */
            switch (type) {
                case TYPE_INSTALLED_DOWNLOADED:
                    state = State.UPDATE_FROM_DISK;
                    break;
                case TYPE_DOWNLOADED_SERVER:
                    state = State.DOWNLOAD;
                    break;
            }
        } else if (vc1 == vc2) {
            if (DeviceManager.getInstance().isAppUpdatable(appDetail)) {
                /**
                 * same version installed and downloaded and updatable
                 * or version downloaded same as the one in the server and updatable
                 */
                switch (type) {
                    case TYPE_INSTALLED_DOWNLOADED:
                        state = State.DOWNLOAD_UPDATE;
                        break;
                    case TYPE_DOWNLOADED_SERVER:
                        state = State.INSTALL;
                        break;
                }
            } else {
                /**
                 * same version installed and downloaded
                 * or version downloaded same as the one in the server
                 */
                switch (type) {
                    case TYPE_INSTALLED_DOWNLOADED:
                        state = State.UNINSTALL;
                        break;
                    case TYPE_DOWNLOADED_SERVER:
                        state = State.UNINSTALL;
                        break;
                }
            }
        } else if (vc1 > vc2) {
            /**
             * version installed newer than the downloaded
             * or version downloaded same newer than the one in the server
             */
            switch (type) {
                case TYPE_INSTALLED_DOWNLOADED:
                    state = State.UNINSTALL;
                    break;
                case TYPE_DOWNLOADED_SERVER:
                    state = State.DOWNLOAD;
                    break;
            }
        }
    }

    @Override
    public void onDownloadStart(int id, String url, String path) {
        List<RampLoadDownload> list = RampLoad.getInstance().getDownloading();
        for (RampLoadDownload d : list) {
            if (id == d.id) {
                DownloadNotificationManager.getInstance().addDownloading(id, d);
                break;
            }
        }
    }

    @Override
    public void onDownloadProgress(int id, String url, String path, final float progress) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                DownloadNotificationManager.getInstance().notifyToManager(DownloadNotificationManager.NOTIFICATION_DOWNLOADING, 100, (int) (progress * 100), false);
            }
        }).start();
    }

    @Override
    public void onDownloadFinish(int id, String url, String path) {
        if (!DownloadNotificationManager.getInstance().isAlreadyDownloaded(id)) {
            DownloadNotificationManager.getInstance().addDownloaded(id);
        }
    }

    @Override
    public void onDownloadFailed(int id, String url, String path, Exception exception) {
        DownloadNotificationManager.getInstance().removeDownloading(id);
    }

    @Override
    public void onStatusIdle() {

    }

    @Override
    public void onStatusDownloading() {

    }

    public enum State {
        NULL, DOWNLOAD, DOWNLOAD_UPDATE, DOWNLOADING, UPDATE_FROM_DISK, INSTALL, UNINSTALL
    }
}
