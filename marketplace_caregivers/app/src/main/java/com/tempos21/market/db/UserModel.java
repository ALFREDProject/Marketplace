package com.tempos21.market.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tempos21.market.client.bean.User;

public class UserModel {

    public static final String TABLE_NAME = "Users";
    public static final String ID = "_id";
    public static final String DAS_USER = "das_user";
    public static final String NAME = "name";
    public static final String AUTH_TOKEN = "auth_token";
    public static final String PLATFORM = "platform";
    public static final String VERSION = "version";
    public static final String IS_APPROVER = "is_approver";
    public static final String IS_TESTER = "is_tester";
    public static final String PASSWORD = "password";
    public static final String DEVICE_ID = "device_id";
    Context context;


    public UserModel(Context context) {
        this.context = context;
    }


    public User getUser() {
        User user = null;
        Cursor cursor;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        cursor = dbHelper.getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            user = readCursor(cursor);
        }
        cursor.close();
        dbHelper.close();
        return user;
    }

    public void setUser(User user) {
        ContentValues values = setValues(user);

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.insert(TABLE_NAME, null, values);
        db.close();
        dbHelper.close();
    }

    public void clearUser() {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.getWritableDatabase().delete(TABLE_NAME, null, null);
        dbHelper.close();
    }

    public User readCursor(Cursor cursor) {
        User user = new User();
        user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndex(ID)));
        user.setName(cursor.getString(cursor.getColumnIndex(NAME)));
        user.setDasUser(cursor.getString(cursor.getColumnIndex(DAS_USER)));
        user.setJ_username(cursor.getString(cursor.getColumnIndex(DAS_USER)));
        user.setAuthToken(cursor.getString(cursor.getColumnIndex(AUTH_TOKEN)));
        user.setDevice_id(cursor.getString(cursor.getColumnIndex(DEVICE_ID)));
        user.setPlatform(cursor.getString(cursor.getColumnIndex(PLATFORM)));
        user.setVersion(cursor.getString(cursor.getColumnIndex(VERSION)));
        user.setApprover(cursor.getInt(cursor.getColumnIndex(IS_APPROVER)) > 0);
        user.setIsTester(cursor.getInt(cursor.getColumnIndex(IS_TESTER)) > 0);
        user.setJ_password(cursor.getString(cursor.getColumnIndex(PASSWORD)));

        return user;
    }

    public ContentValues setValues(User user) {
        ContentValues values = new ContentValues();
        values.put(ID, user.getId());
        values.put(DAS_USER, user.getDasUser());
        values.put(NAME, user.getName());
        values.put(AUTH_TOKEN, user.getAuthToken());
        values.put(DEVICE_ID, user.getDevice_id());
        values.put(PLATFORM, user.getPlatform());
        values.put(VERSION, user.getVersion());
        values.put(IS_APPROVER, user.isApprover());
        values.put(IS_TESTER, user.isIsTester());
        values.put(PASSWORD, user.getJ_password());
        return values;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
