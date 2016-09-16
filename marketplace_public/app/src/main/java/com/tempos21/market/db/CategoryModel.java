package com.tempos21.market.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tempos21.mymarket.sdk.model.Category;

import java.util.ArrayList;
import java.util.List;


/**
 * This class stores Alfredo Market categories installed in device
 *
 * @author A519130
 */
public class CategoryModel {

    public static final String TABLE_NAME = "categories";
    public static final String ID = "_id";
    public static final String NAME = "name";

    Context context;


    public CategoryModel(Context context) {
        this.context = context;
    }


    /**
     * This method gets the Alfredo Market category
     *
     * @return category
     */
    public Category getCategory(String id) {
        Category category = new Category();
        Cursor cursor;

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        cursor = dbHelper.getReadableDatabase().query(TABLE_NAME, null, ID + "=" + id, null, null, null, null);

        if (cursor.moveToNext()) {
            category = readCursor(cursor);
        }

        cursor.close();
        dbHelper.close();

        return category;
    }


    /**
     * This method gets the Alfredo Market categories installed in device
     *
     * @return categories installed from device
     */
    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<Category>();
        Cursor cursor;

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        cursor = dbHelper.getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            categories.add(readCursor(cursor));
        }

        cursor.close();
        dbHelper.close();

        return categories;
    }

    /**
     * This method sets the Alfredo market categories installed in device
     *
     * @param categories all Alfredo market categories
     */
    public void setCategories(List<Category> categories) {
        if (categories != null) {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(TABLE_NAME, null, null);

            for (Category category : categories) {
                ContentValues values = setValues(category);
                db.insert(TABLE_NAME, null, values);
            }

            db.close();
            dbHelper.close();
        }
    }

    private Category readCursor(Cursor cursor) {
        Category category = new Category();
        category.id = cursor.getInt(cursor.getColumnIndex(ID));
        category.name = cursor.getString(cursor.getColumnIndex(NAME));

        return category;
    }

    //@SuppressLint("UseValueOf")
    private ContentValues setValues(Category category) {
        ContentValues values = new ContentValues();
        values.put(ID, category.id);
        values.put(NAME, category.name);

        return values;
    }


    /**
     * This method indicates if the category is saved in Data Base
     *
     * @param category category to verify if is installed
     * @return true if the category is installed, false otherwise
     */
    public boolean isOnDB(Category category) {
        Cursor cursor;
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        cursor = dbHelper.getReadableDatabase().query(TABLE_NAME, null, ID + "=" + category.id, null, null, null, null);

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
    public void clearCategoriesTable() {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.getWritableDatabase().delete(TABLE_NAME, null, null);
        dbHelper.close();
    }


    /**
     * This method deletes a specific category of data base when is uninstalled
     *
     * @param category uninstalled category to delete
     */
    public void deleteCategory(Category category) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.getWritableDatabase().delete(TABLE_NAME, ID + "=" + category.id, null);
        dbHelper.close();
    }


    /**
     * This method inserts a specific category in data base when is installed
     *
     * @param category installed category to insert
     */
    public void insertCategory(Category category) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = setValues(category);
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
        return getCategories().isEmpty();
    }
}
