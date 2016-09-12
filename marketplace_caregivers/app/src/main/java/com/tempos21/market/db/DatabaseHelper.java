package com.tempos21.market.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tempos21.market.util.TLog;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String CLASS_NAME = DatabaseHelper.class
            .getSimpleName() + " ";
    private static final String DATABASE_NAME = "market.db";
    private static final int DATABASE_VERSION = 2;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        createUserTable(db);
        createAppsTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        TLog.w(CLASS_NAME + "Upgrading database from version " + oldVersion
                + " to " + newVersion + ", which will destroy all old data");

        if (oldVersion == 1) {
            createAppsTable(db);
            oldVersion = 2;
        }
    }

    private void createUserTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + UserModel.TABLE_NAME + " ("
                + UserModel.ID + " INTEGER PRIMARY KEY , "
                + UserModel.DAS_USER + " VARCHAR(45) , "
                + UserModel.PASSWORD + " VARCHAR(45), "
                + UserModel.NAME + " VARCHAR(256) , "
                + UserModel.AUTH_TOKEN + " VARCHAR(256), "
                + UserModel.DEVICE_ID + " VARCHAR(256), "
                + UserModel.PLATFORM + " VARCHAR(256), "
                + UserModel.VERSION + " VARCHAR(256), "
                + UserModel.IS_APPROVER + " BOOLEAN, "
                + UserModel.IS_TESTER + " BOOLEAN "
                + ");");
    }

    private void createAppsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + AppModel.TABLE_NAME + " ("
                + AppModel.ID + " INTEGER PRIMARY KEY , "
                + AppModel.PACKAGE + " VARCHAR(50) , "
                + AppModel.VERSION + " INTEGER ,"
                + AppModel.NAME + " VARCHAR(50) ,"
                + AppModel.IMG + " VARCHAR(100) ,"
                + AppModel.RATING + " DOUBLE "
                + ");");
    }

}

