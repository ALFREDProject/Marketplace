package com.tempos21.mymarket.sdk.manager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.tempos21.mymarket.R;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.MyMarketPreferences;
import com.tempos21.rampload.model.RampLoadDownload;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DownloadNotificationManager {

    public static final int NOTIFICATION_DOWNLOADING = 45875;
    public static final int NOTIFICATION_DOWNLOADED = 35487;
    private static final String KEY_LAST = "last";
    private static final long TIME_LIMIT = (15 * 60 * 1000);
    private static DownloadNotificationManager instance;
    private Context context;
    private NotificationCompat.Builder progressBuilder;
    private android.app.NotificationManager progressNotificationManager;
    private Map<Integer, RampLoadDownload> filesDownloading = new HashMap<Integer, RampLoadDownload>();
    private Map<Integer, RampLoadDownload> filesDownloaded = new HashMap<Integer, RampLoadDownload>();

    private DownloadNotificationManager(Context context) {
        this.context = context;
    }

    public static boolean init(Context context) {
        if (instance == null) {
            instance = new DownloadNotificationManager(context);
            return true;
        }
        return false;
    }

    public static DownloadNotificationManager getInstance() {
        if (instance == null) {
            throw new RuntimeException("NotificationManager must be initialized");
        }
        return instance;
    }

    private void showDownloadingNotification(int id, RampLoadDownload download) {
        progressBuilder = new NotificationCompat.Builder(context);
        String title = context.getString(R.string.app_downloading);
        String name = download.name;
        if (filesDownloading.size() > 1) {
            name = context.getString(R.string.app_download_dots);
        }
        progressBuilder.setNumber(filesDownloading.size());
        progressBuilder.setSmallIcon(android.R.drawable.stat_sys_download);
        progressBuilder.setProgress(0, 0, true);
        progressBuilder.setAutoCancel(true);
        progressBuilder.setTicker(context.getString(R.string.app_download_ticker));
        progressBuilder.setContentTitle(title);
        progressBuilder.setContentText(name);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        for (Integer integer : filesDownloading.keySet()) {
            inboxStyle.addLine(filesDownloading.get(integer).name);
        }
        inboxStyle.setSummaryText(filesDownloading.size() + context.getString(R.string.app_download_applications_downloading));
        inboxStyle.setBigContentTitle(title);
        progressBuilder.setStyle(inboxStyle);

        progressNotificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        progressNotificationManager.notify(NOTIFICATION_DOWNLOADING, progressBuilder.build());
    }

    private void showDownloadedNotification(int id, RampLoadDownload download) {
        long now = System.currentTimeMillis();
        long elapsed = now - MyMarketPreferences.getInstance(context).getLong(KEY_LAST, now);
        if (elapsed >= TIME_LIMIT) {
            filesDownloaded.clear();
            filesDownloaded.put(id, download);
        }
        MyMarketPreferences.getInstance(context).setLong(KEY_LAST, System.currentTimeMillis());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        String title = context.getString(R.string.app_downloaded);
        String name = download.name;
        filesDownloading.remove(id);
        if (filesDownloaded.size() > 1) {
            name = context.getString(R.string.app_download_dots);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, getIntent(download), 0);
        builder.setContentIntent(pendingIntent);
        builder.setNumber(filesDownloaded.size());
        builder.setSmallIcon(android.R.drawable.stat_sys_download_done);
        builder.setAutoCancel(true);
        builder.setTicker(context.getString(R.string.app_download_ticker));
        builder.setContentTitle(title);
        builder.setContentText(name);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        for (Integer integer : filesDownloaded.keySet()) {
            inboxStyle.addLine(filesDownloaded.get(integer).name);
        }
        inboxStyle.setSummaryText(filesDownloaded.size() + context.getString(R.string.app_download_applications_downloaded));
        inboxStyle.setBigContentTitle(title);
        builder.setStyle(inboxStyle);

        android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_DOWNLOADED, builder.build());
        if (filesDownloading.size() == 0) {
            notificationManager.cancel(NOTIFICATION_DOWNLOADING);
        }
    }

    private Intent getIntent(RampLoadDownload download) {
        String path = String.format(MyMarket.getInstance().getAppsDestinationFile(), download.id, download.name.trim().replaceAll("\\s+", ""));
        Uri packageUri = Uri.fromFile(new File(path));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(packageUri, context.getString(R.string.app_download_type));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public void notifyToManager(int notification, int max, int progress, boolean indeterminate) {
        progressBuilder.setProgress(max, progress, indeterminate);
        progressNotificationManager.notify(notification, progressBuilder.build());
    }

    public void addDownloading(int id, RampLoadDownload download) {
        filesDownloading.put(id, download);
        showDownloadingNotification(id, download);
    }

    public void removeDownloading(int id) {
        filesDownloading.remove(id);
        progressNotificationManager.cancel(DownloadNotificationManager.NOTIFICATION_DOWNLOADING);
    }

    public void addDownloaded(int id) {
        filesDownloaded.put(id, filesDownloading.get(id));
        filesDownloading.remove(id);
        showDownloadedNotification(id, filesDownloaded.get(id));
    }

    public void addDownloaded(int id, RampLoadDownload download) {
        filesDownloaded.put(id, download);
        filesDownloading.remove(id);
        showDownloadedNotification(id, download);
    }

    public boolean isAlreadyDownloaded(int id) {
        for (Integer i : filesDownloaded.keySet()) {
            if (id == filesDownloaded.get(i).id) {
                return true;
            }
        }
        return false;
    }
}
