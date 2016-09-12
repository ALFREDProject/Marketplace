package com.tempos21.market.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tempos21.mymarket.sdk.model.Category;

import java.util.ArrayList;
import java.util.List;


/**
 * This class stores Alfredo Market screenshots apps in device
 *
 * @author A519130
 */
public class ScreenshotsModel {

    public static final String TABLE_NAME = "screenshots";
    public static final String NAME = "name";
    public static final String APP_ID = "appId";
    public static final String ID = "_id";

    Context context;


    public ScreenshotsModel(Context context) {
        this.context = context;
    }


    /**
     * This method gets the Alfredo Market screenshots installed in device
     *
     * @return screenshots installed from device
     */
    public ArrayList<String> getScreenshots(String appId) {
        ArrayList<String> screenshots = new ArrayList<String>();
        Cursor cursor;

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        cursor = dbHelper.getReadableDatabase().query(TABLE_NAME, null, APP_ID + "=" + appId, null, null, null, null);

        while (cursor.moveToNext()) {
            screenshots.add(readCursor(cursor));
        }

        cursor.close();
        dbHelper.close();

        return screenshots;
    }


    private String readCursor(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndex(NAME));
    }


    /**
     * This method inserts a specific screenshots in data base when is installed
     *
     * @param screenshots installed screenshots to insert
     */
    public void insertScreenshot(int appId, List<String> screenshots) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for(String name: screenshots) {
            Screenshot screenshot = new Screenshot();
            screenshot.name = name;
            screenshot.appId = appId;

            ContentValues values = setValues(screenshot);
            db.insert(TABLE_NAME, null, values);
        }
        db.close();
        dbHelper.close();
    }


    //@SuppressLint("UseValueOf")
    private ContentValues setValues(Screenshot screenshot) {
        ContentValues values = new ContentValues();
        values.put(NAME, screenshot.name);
        values.put(APP_ID, screenshot.appId);

        return values;
    }


    /**
     * This method indicates if the category is saved in Data Base
     *
     * @param appId screenshots to verify if is installed
     * @return true if the screenshots is installed, false otherwise
     */
    public boolean isOnDB(String appId) {
        Cursor cursor;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        cursor = dbHelper.getReadableDatabase().query(TABLE_NAME, null, APP_ID + "=" + appId, null, null, null, null);

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
     * This method deletes a specific screenshots of data base when is uninstalled
     *
     * @param appId uninstalled screenshots to delete
     */
    public void deleteScreenshot(String appId) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.getWritableDatabase().delete(TABLE_NAME, APP_ID + "=" + appId, null);
        dbHelper.close();
    }


    public Context getContext() {
        return context;
    }


    public void setContext(Context context) {
        this.context = context;
    }


    public class Screenshot{
        public String name;
        public int appId;
    }
}
