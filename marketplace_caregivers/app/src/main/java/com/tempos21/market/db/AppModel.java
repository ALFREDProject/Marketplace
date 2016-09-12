package com.tempos21.market.db;

//import android.annotation.SuppressLint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tempos21.market.client.bean.App;
import com.tempos21.market.client.bean.Apps;
import com.tempos21.market.device.BinaryInstaller;


/**
 * This class stores Alfredo Market apps installed in device
 *
 * @author A519130
 */
public class AppModel {

    public static final String TABLE_NAME = "Apps";
    public static final String ID = "_id";
    public static final String PACKAGE = "package";
    public static final String VERSION = "version";
    public static final String NAME = "name";
    public static final String IMG = "img";
    public static final String RATING = "rating";
    Context context;


    public AppModel(Context context) {
        this.context = context;
    }


    /**
     * This method gets the Alfredo Market apps installed in device
     *
     * @return apps installed from device
     */
    public Apps getApps() {
        Apps apps = new Apps();
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
    public void setApps(Apps apps) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);

        for (App app : apps) {
            if (BinaryInstaller.isAppInstalled(context, app.getPackageName())) {
                app.setVersion_number(BinaryInstaller.getVersion(context, app.getPackageName()));
                ContentValues values = setValues(app);
                db.insert(TABLE_NAME, null, values);
            }
        }
        db.close();
        dbHelper.close();
    }

    private App readCursor(Cursor cursor) {
        App app = new App();
        app.setId("" + cursor.getInt(cursor.getColumnIndex(ID)));
        app.setPackageName(cursor.getString(cursor.getColumnIndex(PACKAGE)));
        app.setVersion_number(cursor.getInt(cursor.getColumnIndex(VERSION)));
        app.setName(cursor.getString(cursor.getColumnIndex(NAME)));
        app.setIcon_url(cursor.getString(cursor.getColumnIndex(IMG)));
        app.setRating(cursor.getDouble(cursor.getColumnIndex(RATING)));

        return app;
    }

    //@SuppressLint("UseValueOf")
    private ContentValues setValues(App app) {
        ContentValues values = new ContentValues();
        values.put(ID, new Integer(app.getId()).intValue());
        values.put(PACKAGE, app.getPackageName());
        values.put(VERSION, app.getVersion_number());
        values.put(NAME, app.getName());
        values.put(IMG, app.getIcon_url());
        values.put(RATING, app.getRating());

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
        cursor = dbHelper.getReadableDatabase().query(TABLE_NAME, null, ID + "=" + app.getId(), null, null, null, null);

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
        dbHelper.getWritableDatabase().delete(TABLE_NAME, ID + "=" + app.getId(), null);
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
}
