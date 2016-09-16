package com.tempos21.market.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tempos21.mymarket.sdk.model.AppRate;

import java.util.ArrayList;
import java.util.List;


/**
 * This class stores Alfredo Market review installed in device
 *
 * @author A519130
 */
public class ReviewModel {

    public static final String TABLE_NAME = "review";
    public static final String ID = "_id";
    public static final String USER_ID = "userId";
    public static final String VERSION_STRING = "versionString";
    public static final String TEXT = "text";
    public static final String DATE_CREATION = "dateCreation";
    public static final String USER_FULL_NAME = "userFullName";
    public static final String TITLE = "title";
    public static final String RATE = "rate";
    public static final String USER_NAME = "userName";
    public static final String APP_ID = "appId";

    Context context;


    public ReviewModel(Context context) {
        this.context = context;
    }


    /**
     * This method gets the Alfredo Market review installed in device
     *
     * @return review installed from device
     */
    public List<AppRate> getReviews(String id) {
        List<AppRate> reviews = new ArrayList<AppRate>();
        Cursor cursor;

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        cursor = dbHelper.getReadableDatabase().query(TABLE_NAME, null, APP_ID + "=" + id, null, null, null, null);

        while (cursor.moveToNext()) {
            reviews.add(readCursor(cursor));
        }

        cursor.close();
        dbHelper.close();

        return reviews;
    }


    private AppRate readCursor(Cursor cursor) {
        AppRate review = new AppRate();
        review.id = cursor.getInt(cursor.getColumnIndex(ID));
        review.userId = cursor.getLong(cursor.getColumnIndex(USER_ID));
        review.versionString = cursor.getString(cursor.getColumnIndex(VERSION_STRING));
        review.text = cursor.getString(cursor.getColumnIndex(TEXT));
        review.dateCreation = cursor.getString(cursor.getColumnIndex(DATE_CREATION));
        review.userFullName = cursor.getString(cursor.getColumnIndex(USER_FULL_NAME));
        review.title = cursor.getString(cursor.getColumnIndex(TITLE));
        review.rate = cursor.getInt(cursor.getColumnIndex(RATE));
        review.userName = cursor.getString(cursor.getColumnIndex(USER_NAME));

        return review;
    }


    /**
     * This method sets the Alfredo market review
     *
     * @param review review to store
     */
    public void setReview(AppRate review, String appId) {
        if (review != null) {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = setValues(review, appId);
            if (!isOnDB("" + review.id)) {
                db.insert(TABLE_NAME, null, values);
            } else {
                db.delete(TABLE_NAME, ID + "=" + review.id, null);
                db.insert(TABLE_NAME, null, values);
            }

            db.close();
            dbHelper.close();
        }
    }


    /**
     * This method indicates if the review is saved in Data Base
     *
     * @param reviewId to verify if is stored
     * @return true if the review is stored, false otherwise
     */
    public boolean isOnDB(String reviewId) {
        Cursor cursor;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        cursor = dbHelper.getReadableDatabase().query(TABLE_NAME, null, ID + "=" + reviewId, null, null, null, null);

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
     * This method sets the Alfredo market review installed in device
     *
     * @param reviews all Alfredo market categories
     */
    public void setReviews(List<AppRate> reviews, String appId) {
        if (reviews != null) {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(TABLE_NAME, null, null);

            for (AppRate review : reviews) {
                ContentValues values = setValues(review, appId);
                db.insert(TABLE_NAME, null, values);
            }

            db.close();
            dbHelper.close();
        }
    }


    //@SuppressLint("UseValueOf")
    private ContentValues setValues(AppRate review, String appId) {
        ContentValues values = new ContentValues();
        values.put(ID, review.id);
        values.put(APP_ID, appId);
        values.put(USER_ID, review.userId);
        values.put(VERSION_STRING, review.versionString);
        values.put(TEXT, review.text);
        values.put(DATE_CREATION, review.dateCreation);
        values.put(USER_FULL_NAME, review.userFullName);
        values.put(TITLE, review.title);
        values.put(RATE, review.rate);
        values.put(USER_NAME, review.userName);

        return values;
    }


    public Context getContext() {
        return context;
    }


    public void setContext(Context context) {
        this.context = context;
    }


    public boolean isEmpty(String appId) {
        return getReviews(appId).isEmpty();
    }

}
