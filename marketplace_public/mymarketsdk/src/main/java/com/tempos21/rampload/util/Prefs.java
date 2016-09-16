package com.tempos21.rampload.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * This class is used to simplify an access to the {@link SharedPreferences}.
 */
public class Prefs {

    public static final String DEFAULT_PREFS_NAME = "prefs";
    public static final int PREFS_MODE = Context.MODE_PRIVATE;
    private static Context ctx;
    private static String prefsName;

    /**
     * Initializes the values for the {@link SharedPreferences}.
     * The name of the {@link SharedPreferences} will be {@link Prefs#DEFAULT_PREFS_NAME}.
     *
     * @param context
     */
    public static void init(Context context) {
        ctx = context;
        prefsName = DEFAULT_PREFS_NAME;
    }

    /**
     * Initializes the values for the {@link SharedPreferences}.
     *
     * @param context
     * @param name    The name of the {@link SharedPreferences}.
     * @throws Exception Throws an {@link Exception} If the name is null or empty.
     */
    public static void init(Context context, String name) throws Exception {
        if (TextUtils.isEmpty(name)) {
            throw new Exception("Parameter name can't be null or empty.");
        }
        ctx = context;
        prefsName = name;
    }

    /**
     * Retrieves an instance of {@link SharedPreferences}.
     *
     * @return The instance of {@link SharedPreferences}.
     */
    private static SharedPreferences getPrefs() {
        return ctx.getSharedPreferences(DEFAULT_PREFS_NAME, PREFS_MODE);
    }

    /**
     * Retrieves a {@link String} from the {@link SharedPreferences}.
     * Returns the defaultValue parameter if the value does not exist.
     *
     * @param key          The key of the value.
     * @param defaultValue It will return this value if the value is not found.
     * @return The value found with the key or defaultValue if the value is not found.
     */
    public static String getString(String key, String defaultValue) {
        return getPrefs().getString(key, defaultValue);
    }

    /**
     * Sets a value for the key parameter into the {@link SharedPreferences}.
     *
     * @param key   The key used to find the value.
     * @param value The value to store and pair with the key.
     */
    public static void setString(String key, String value) {
        getPrefs().edit().putString(key, value).commit();
    }

    /**
     * Retrieves an int from the {@link SharedPreferences}.
     * Returns the defaultValue parameter if the value does not exist.
     *
     * @param key          The key of the value.
     * @param defaultValue It will return this value if the value is not found.
     * @return The value found with the key or defaultValue if the value is not found.
     */
    public static int getInt(String key, int defaultValue) {
        return getPrefs().getInt(key, defaultValue);
    }

    /**
     * Sets a value for the key parameter into the {@link SharedPreferences}.
     *
     * @param key   The key used to find the value.
     * @param value The value to store and pair with the key.
     */
    public static void setInt(String key, int value) {
        getPrefs().edit().putInt(key, value).commit();
    }

    /**
     * Retrieves an float from the {@link SharedPreferences}.
     * Returns the defaultValue parameter if the value does not exist.
     *
     * @param key          The key of the value.
     * @param defaultValue It will return this value if the value is not found.
     * @return The value found with the key or defaultValue if the value is not found.
     */
    public static float getFloat(String key, float defaultValue) {
        return getPrefs().getFloat(key, defaultValue);
    }

    /**
     * Sets a value for the key parameter into the {@link SharedPreferences}.
     *
     * @param key   The key used to find the value.
     * @param value The value to store and pair with the key.
     */
    public static void setFloat(String key, float value) {
        getPrefs().edit().putFloat(key, value).commit();
    }

    /**
     * Retrieves a long from the {@link SharedPreferences}.
     * Returns the defaultValue parameter if the value does not exist.
     *
     * @param key          The key of the value.
     * @param defaultValue It will return this value if the value is not found.
     * @return The value found with the key or defaultValue if the value is not found.
     */
    public static long getLong(String key, long defaultValue) {
        return getPrefs().getLong(key, defaultValue);
    }

    /**
     * Sets a value for the key parameter into the {@link SharedPreferences}.
     *
     * @param key   The key used to find the value.
     * @param value The value to store and pair with the key.
     */
    public static void setLong(String key, long value) {
        getPrefs().edit().putLong(key, value).commit();
    }

    /**
     * Retrieves a boolean from the {@link SharedPreferences}.
     * Returns the defaultValue parameter if the value does not exist.
     *
     * @param key          The key of the value.
     * @param defaultValue It will return this value if the value is not found.
     * @return The value found with the key or defaultValue if the value is not found.
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return getPrefs().getBoolean(key, defaultValue);
    }

    /**
     * Sets a value for the key parameter into the {@link SharedPreferences}.
     *
     * @param key   The key used to find the value.
     * @param value The value to store and pair with the key.
     */
    public static void setBoolean(String key, boolean value) {
        getPrefs().edit().putBoolean(key, value).commit();
    }
}