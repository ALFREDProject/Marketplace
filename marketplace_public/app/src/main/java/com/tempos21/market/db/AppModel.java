package com.tempos21.market.db;

//import android.annotation.SuppressLint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tempos21.market.util.Util;
import com.tempos21.mymarket.sdk.model.app.App;

import java.util.ArrayList;
import java.util.List;


/**
 * This class stores Alfredo Market apps installed in device
 *
 * @author A519130
 */
public class AppModel {

    public static final String TABLE_NAME = "Apps";
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String VERSION_NUMBER = "versionNumber";
    public static final String VERSION_ID = "versionId";
    public static final String SUPPORT_EMAIL = "supportEmail";
    public static final String PROMO_URL = "promo_url";
    public static final String PACKAGENAME = "packageName";
    public static final String NOTIFICATION_EMAIL = "notificationEmail";
    public static final String VERSION_STRING = "versionString";
    public static final String ICON_URL = "iconUrl";
    public static final String EXTERNAL_URL = "externalUrl";
    public static final String EXTERNAL_BINARY = "externalBinary";
    public static final String DATE = "date";
    public static final String AUTHOR = "author";
    public static final String ALLOWED = "allowed";
    public static final String RATING = "rating";
    public static final String CATEGORY_ID = "categoryId";

    Context context;
    private String categoryId = "0";


    public AppModel(Context context) {
        this.context = context;
    }


    /**
     * This method gets the Alfredo Market app
     *
     * @return app
     */
    public App getApp(String id) {
        App app = new App();
        Cursor cursor;

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        cursor = dbHelper.getReadableDatabase().query(TABLE_NAME, null, ID + "=" + id, null, null, null, null);

        if (cursor.moveToNext()) {
            app = readCursor(cursor);
        }

        cursor.close();
        dbHelper.close();

        return app;
    }


    /**
     * This method gets the Alfredo Market apps installed in device
     *
     * @return apps installed from device
     */
    public List<App> getApps() {
        List<App> apps = new ArrayList<App>();
        Cursor cursor;

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        cursor = dbHelper.getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            apps.add(readCursor(cursor));
        }

        cursor.close();
        dbHelper.close();

        return apps;
    }

    /**
     * This method sets the Alfredo market apps installed in device
     *
     * @param apps all Alfredo market apps
     */
    public void setApps(List<App> apps) {
        if (apps != null) {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(TABLE_NAME, null, null);

            for (App app : apps) {
                if (Util.isAppInstalled(context, app.packageName)) {
                    app.versionNumber = Util.getVersion(context, app.packageName);
                    ContentValues values = setValues(app);
                    db.insert(TABLE_NAME, null, values);
                }
            }
            db.close();
            dbHelper.close();
        }
    }

    /**
     * This method gets the Alfredo Market apps installed in device
     *
     * @return apps installed from device
     */
    public List<App> getAppsByCategory(String categoryId) {
        List<App> apps = new ArrayList<App>();
        Cursor cursor;

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        cursor = dbHelper.getReadableDatabase().query(TABLE_NAME, null, CATEGORY_ID + "=" + categoryId, null, null, null, null);

        while (cursor.moveToNext()) {
            apps.add(readCursor(cursor));
        }

        cursor.close();
        dbHelper.close();

        return apps;
    }

    private App readCursor(Cursor cursor) {
        App app = new App();
        app.id = cursor.getInt(cursor.getColumnIndex(ID));
        app.versionNumber = cursor.getInt(cursor.getColumnIndex(VERSION_NUMBER));
        app.versionId = cursor.getInt(cursor.getColumnIndex(VERSION_ID));
        app.supportEmails = cursor.getString(cursor.getColumnIndex(SUPPORT_EMAIL));
        app.rating = cursor.getFloat(cursor.getColumnIndex(RATING));
        app.promoUrl = cursor.getString(cursor.getColumnIndex(PROMO_URL));
        app.packageName = cursor.getString(cursor.getColumnIndex(PACKAGENAME));
        app.notificationEmails = cursor.getString(cursor.getColumnIndex(NOTIFICATION_EMAIL));
        app.name = cursor.getString(cursor.getColumnIndex(NAME));
        app.versionString = cursor.getString(cursor.getColumnIndex(VERSION_STRING));
        app.iconUrl = cursor.getString(cursor.getColumnIndex(ICON_URL));
        app.externalUrl = cursor.getString(cursor.getColumnIndex(EXTERNAL_URL));
        app.externalBinary = cursor.getString(cursor.getColumnIndex(EXTERNAL_BINARY)).equalsIgnoreCase("true");
        app.date = cursor.getString(cursor.getColumnIndex(DATE));
        app.author = cursor.getString(cursor.getColumnIndex(AUTHOR));
        app.allowed = cursor.getString(cursor.getColumnIndex(ALLOWED)).equalsIgnoreCase("true");

        return app;
    }

    //@SuppressLint("UseValueOf")
    private ContentValues setValues(App app) {
        ContentValues values = new ContentValues();
        values.put(ID, app.id);
        values.put(VERSION_NUMBER, app.versionNumber);
        values.put(VERSION_ID, app.versionId);
        values.put(SUPPORT_EMAIL, app.supportEmails);
        values.put(RATING, app.rating);
        values.put(PROMO_URL, app.promoUrl);
        values.put(PACKAGENAME, app.packageName);
        values.put(NOTIFICATION_EMAIL, app.notificationEmails);
        values.put(NAME, app.name);
        values.put(VERSION_STRING, app.versionString);
        values.put(ICON_URL, app.iconUrl);
        values.put(EXTERNAL_URL, app.externalUrl);
        values.put(EXTERNAL_BINARY, "" + app.externalBinary);
        values.put(DATE, app.date);
        values.put(AUTHOR, app.author);
        values.put(ALLOWED, "" + app.allowed);
        values.put(CATEGORY_ID, Integer.parseInt(categoryId));

        return values;
    }


    /**
     * This method indicates if the app is saved in Data Base
     *
     * @param app app to verify if is installed
     * @return true if the app is installed, false otherwise
     */
    public boolean isOnDB(App app) {
        Cursor cursor;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        cursor = dbHelper.getReadableDatabase().query(TABLE_NAME, null, ID + "=" + app.id, null, null, null, null);

        if (cursor.getCount() == 0) {
            dbHelper.close();
            dbHelper.close();
            return false;
        } else {
            dbHelper.close();
            dbHelper.close();
            return true;
        }
    }


    /**
     * This method deletes all table content
     */
    public void clearAppsTable() {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.getWritableDatabase().delete(TABLE_NAME, null, null);
        dbHelper.close();
    }


    /**
     * This method deletes a specific app of data base when is uninstalled
     *
     * @param app uninstalled app to delete
     */
    public void deleteApp(App app) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.getWritableDatabase().delete(TABLE_NAME, ID + "=" + app.id, null);
        dbHelper.close();
    }


    /**
     * This method inserts a specific app in data base when is installed
     *
     * @param app installed app to insert
     */
    public void insertApp(App app) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = setValues(app);
        db.insert(TABLE_NAME, null, values);

        db.close();
        dbHelper.close();
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isEmpty() {
        return getApps().isEmpty();
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

}
