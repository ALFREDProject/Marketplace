package com.tempos21.market.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.tempos21.market.ui.LoginActivity;
import com.tempos21.market.util.TLog;
import com.worldline.alfredo.R;

public class GcmIntentService extends IntentService {

    private static final String CLASS_NAME = GcmIntentService.class.getSimpleName() + " ";
    private static final String PARAM_PAYLOAD = "payload";
    private static int HELLO_ID = 1;


    public GcmIntentService() {
        super("GcmIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        TLog.e(CLASS_NAME + "Got a message!");
        TLog.i(CLASS_NAME + intent.getExtras().get(PARAM_PAYLOAD).toString());

        createNotification(intent);

        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }


    private void createNotification(Intent intent) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, HELLO_ID, new Intent(this, LoginActivity.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle = bigTextStyle.bigText(intent.getExtras().get(PARAM_PAYLOAD).toString());
        builder.setContentIntent(pendingIntent);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle(getApplicationContext().getString(R.string.app_name));
        builder.setContentText(intent.getExtras().get(PARAM_PAYLOAD).toString());
        builder.setStyle(bigTextStyle);
        builder.setAutoCancel(true);

        notificationManager.notify(HELLO_ID, builder.build());
    }

}
