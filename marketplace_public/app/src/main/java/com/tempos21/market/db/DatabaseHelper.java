package com.tempos21.market.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tempos21.market.util.TLog;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String CLASS_NAME = DatabaseHelper.class.getSimpleName() + " ";
    private static final String DATABASE_NAME = "market.db";
    private static final int DATABASE_VERSION = 2;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createAppsTable(db);
        createCategoriessTable(db);
        createScreenshotsTable(db);
        createAppsDetailTable(db);
        createReviewTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TLog.w(CLASS_NAME + "Upgrading database from version " + oldVersion
                + " to " + newVersion + ", which will destroy all old data");

        if (oldVersion == 1) {
            createAppsTable(db);
            createCategoriessTable(db);
            createScreenshotsTable(db);
            createAppsDetailTable(db);
            createReviewTable(db);
            oldVersion = 2;
        }
    }

    private void createAppsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + AppModel.TABLE_NAME + " ("
                + AppModel.ID + " INTEGER PRIMARY KEY , "
                + AppModel.VERSION_NUMBER + " INTEGER , "
                + AppModel.VERSION_ID + " INTEGER ,"
                + AppModel.SUPPORT_EMAIL + " VARCHAR(100) ,"
                + AppModel.RATING + " DOUBLE ,"
                + AppModel.PROMO_URL + " VARCHAR(250) ,"
                + AppModel.PACKAGENAME + " VARCHAR(100) ,"
                + AppModel.NOTIFICATION_EMAIL + " VARCHAR(100) ,"
                + AppModel.NAME + " VARCHAR(100) ,"
                + AppModel.VERSION_STRING + " VARCHAR(100) ,"
                + AppModel.ICON_URL + " VARCHAR(250) ,"
                + AppModel.EXTERNAL_URL + " VARCHAR(100) ,"
                + AppModel.EXTERNAL_BINARY + " VARCHAR(100) ,"
                + AppModel.DATE + " VARCHAR(100) ,"
                + AppModel.AUTHOR + " VARCHAR(100) ,"
                + AppModel.ALLOWED + " VARCHAR(100) ,"
                + AppModel.CATEGORY_ID + " INTEGER "
                + ");");
    }

    private void createAppsDetailTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + AppModelDetail.TABLE_NAME + " ("
                + AppModelDetail.ID + " INTEGER PRIMARY KEY , "
                + AppModelDetail.VERSION_NUMBER + " INTEGER , "
                + AppModelDetail.VERSION_ID + " INTEGER ,"
                + AppModelDetail.SUPPORT_EMAIL + " VARCHAR(100) ,"
                + AppModelDetail.RATING + " DOUBLE ,"
                + AppModelDetail.PROMO_URL + " VARCHAR(250) ,"
                + AppModelDetail.PACKAGENAME + " VARCHAR(100) ,"
                + AppModelDetail.NOTIFICATION_EMAIL + " VARCHAR(100) ,"
                + AppModelDetail.NAME + " VARCHAR(100) ,"
                + AppModelDetail.VERSION_STRING + " VARCHAR(100) ,"
                + AppModelDetail.ICON_URL + " VARCHAR(250) ,"
                + AppModelDetail.EXTERNAL_URL + " VARCHAR(100) ,"
                + AppModelDetail.EXTERNAL_BINARY + " VARCHAR(100) ,"
                + AppModelDetail.DATE + " VARCHAR(100) ,"
                + AppModelDetail.AUTHOR + " VARCHAR(100) ,"
                + AppModelDetail.ALLOWED + " VARCHAR(100) , "
                + AppModelDetail.SIZE + " DOUBLE , "
                + AppModelDetail.DESCRIPTION + " VARCHAR(500) "
                + ");");
    }

    private void createCategoriessTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CategoryModel.TABLE_NAME + " ("
                + CategoryModel.ID + " INTEGER PRIMARY KEY , "
                + CategoryModel.NAME + " VARCHAR(100)"
                + ");");
    }

    private void createScreenshotsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ScreenshotsModel.TABLE_NAME + " ("
                + ScreenshotsModel.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL , "
                + ScreenshotsModel.NAME + " VARCHAR(500) , "
                + ScreenshotsModel.APP_ID + " INTEGER );");
    }

    private void createReviewTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ReviewModel.TABLE_NAME + " ("
                + ReviewModel.ID + " INTEGER PRIMARY KEY , "
                + ReviewModel.USER_ID + " DOUBLE ,"
                + ReviewModel.APP_ID + " VARCHAR(50), "
                + ReviewModel.VERSION_STRING + " VARCHAR(100) ,"
                + ReviewModel.TEXT + " VARCHAR(1000) ,"
                + ReviewModel.DATE_CREATION + " VARCHAR(100) ,"
                + ReviewModel.USER_FULL_NAME + " VARCHAR(100) ,"
                + ReviewModel.TITLE + " VARCHAR(100) ,"
                + ReviewModel.RATE + " INTEGER , "
                + ReviewModel.USER_NAME + " VARCHAR(200) "
                + ");");
    }

}

