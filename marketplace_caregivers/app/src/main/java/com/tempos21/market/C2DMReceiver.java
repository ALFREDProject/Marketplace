package com.tempos21.market;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.c2dm.C2DMessaging;
import com.tempos21.market.c2dm.C2DMCurrent;
import com.tempos21.market.c2dm.MarketPushHarvester;
import com.tempos21.market.ui.MainActivity;
import com.tempos21.market.util.TLog;
import com.worldline.alfredo.R;

import java.util.Date;

public class C2DMReceiver extends es.tempos21.android.c2dmlib.C2DMReceiver {
    public static final String SENDER_EMAIL = "atosmarket.push@gmail.com";

    private static final String SHARED_PREFS_FILE = "C2DM";
    private static final String SHARED_PREFS_KEY = "RegistrationId";
    private static final String SHARED_PREFS_LASTDAY_NOTIFICATION = "LastDayNotification";

    private static final String LAST_REGISTRATION_ID = "LAST_REGISTRATION_ID";
    private static final String C2DM_SHARED_PREF = "c2dm_";

    private static final String PARAM_PAYLOAD = "payload";

    public C2DMReceiver() {
        super(SENDER_EMAIL);
    }

    public static void register(Context context) {
        TLog.d("Registration sending...");
        C2DMessaging.register(context, SENDER_EMAIL);

        TLog.i("Registration sent!");
    }

    public static boolean isPreviouslyRegistered(Context context) {
        TLog.d("isPreviouslyRegistered");
        return getRegistationId(context) != null;
    }

    public static String getRegistationId(Context context) {
        TLog.d("getRegistationId");
        return context.getSharedPreferences(SHARED_PREFS_FILE,
                Context.MODE_PRIVATE).getString(SHARED_PREFS_KEY, null);
    }

    public static void setRegistationId(Context context, String registrationId) {
        TLog.d("setRegistationId");
//		UserModel model = new UserModel(context);
        context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .edit().putString(SHARED_PREFS_KEY, registrationId).commit();
        MarketPushHarvester pushH = new MarketPushHarvester();
        pushH.pushRegister(context, getRegistationId(context));
        setLastC2DM(context, getRegistationId(context));
    }

    private static void setLastC2DM(Context context, String newRegistrationId) {
        TLog.d("setLastC2DM");
        SharedPreferences.Editor editor = context.getSharedPreferences(
                C2DM_SHARED_PREF, Context.MODE_PRIVATE).edit();
        editor.putString(LAST_REGISTRATION_ID, newRegistrationId);
        editor.commit();
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        TLog.i("Got a message!");
        String payload = intent.getExtras().get(PARAM_PAYLOAD).toString();
        String userId = intent.getExtras().get(C2DMParams.PARAM_USERID)
                .toString();
        String action = intent.getExtras().get(C2DMParams.PARAM_ACTION)
                .toString();
        TLog.i("PARAM_PAYLOAD = " + payload + " - PARAM_USERID = " + userId
                + " - PARAM_ACTION = " + action);

        long lastDayNotification = getLastDayNotification(context);
        TLog.d("Last day notification: " + lastDayNotification);
        long now = new Date().getTime();
        TLog.d("Now: " + now);

//		if (dayGreaterThan(now, lastDayNotification)) {
        setLastDayNotification(context, now);
        TLog.d("This is the first notification of today");
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();
        Notification notification = new Notification(icon, intent
                .getExtras().get(PARAM_PAYLOAD).toString(), when);
        // Context context = getApplicationContext();
        CharSequence contentTitle = context.getString(R.string.app_name);
        CharSequence contentText = payload;
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("notification", true);
        C2DMCurrent.setUserId(userId);
        C2DMCurrent.setAction(Integer.parseInt(action));
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);
        notification.flags = Notification.FLAG_SHOW_LIGHTS
                | Notification.DEFAULT_VIBRATE
                | Notification.FLAG_AUTO_CANCEL;
        notification.ledARGB = 0xff0000ff;
        notification.ledOnMS = 300;
        notification.ledOffMS = 1000;
        notification.defaults = Notification.DEFAULT_ALL;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(context, contentTitle, contentText,
                contentIntent);
        final int HELLO_ID = 1;
        mNotificationManager.notify(HELLO_ID, notification);
//		} else {
//			TLog.d("Today already have received a notification");
//		}
    }

    @Override
    public void onError(Context context, String errorId) {
        TLog.e("Got an error");
        TLog.i(errorId);
    }

    @Override
    public void onRegistered(Context context, String registrationId) {
        TLog.i("Registration successful!");
        TLog.i(registrationId);
        setRegistationId(context, registrationId);
    }

    @Override
    public void onUnregistered(Context context) {
        TLog.i("Unregistration successful!");
    }

//	private String getLastRegistrationId(Context context) {
//		TLog.d("getLastRegistrationId");
//		return context.getSharedPreferences(C2DM_SHARED_PREF,
//				Context.MODE_PRIVATE).getString(LAST_REGISTRATION_ID, "");
//	}

    private long getLastDayNotification(Context context) {
        long time = context.getSharedPreferences(SHARED_PREFS_FILE,
                Context.MODE_PRIVATE).getLong(
                SHARED_PREFS_LASTDAY_NOTIFICATION, -1);
        return time;
    }

    private void setLastDayNotification(Context context, long time) {
        context.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                .edit().putLong(SHARED_PREFS_LASTDAY_NOTIFICATION, time)
                .commit();
    }

    public class C2DMParams {
        public static final String PARAM_USERID = "userid";
        public static final String PARAM_ACTION = "action";
        public static final int ACTION_NEW_DEALS = 0;
    }

//	/**
//	 * Compare if a day of date done in millis is greater than other
//	 * 
//	 * @param first
//	 *            time in millis first parameter
//	 * @param second
//	 *            time in millins second parameter
//	 * @return true if first day is greater than second ay
//	 */
//	private boolean dayGreaterThan(long first, long second) {
//		boolean greater = false;
//		Calendar cal1 = Calendar.getInstance();
//		cal1.setTimeInMillis(first);
//		Calendar cal2 = Calendar.getInstance();
//		cal2.setTimeInMillis(second);
//		int year1 = cal1.get(Calendar.YEAR);
//		int year2 = cal2.get(Calendar.YEAR);
//		TLog.d("year of first param = " + year1 + " - year of second param = "
//				+ year2);
//		if (year1 > year2) {
//			greater = true;
//		} else if (first < second) {
//			greater = false;
//		} else { // same year
//			int day1 = cal1.get(Calendar.DAY_OF_YEAR);
//			int day2 = cal2.get(Calendar.DAY_OF_YEAR);
//			TLog.d("day of first param = " + day1 + " - day of second param = "
//					+ day2);
//			if (day1 > day2) {
//				greater = true;
//			} else {
//				greater = false;
//			}
//		}
//		return greater;
//	}

}