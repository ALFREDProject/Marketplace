package com.tempos21.market;

public final class Constants {

    public static final boolean DEBUG = true;

    /***
     * Services URL
     */
    public static final String BASE_URL = "http://devel1.tempos21.com/alfredo-dev/rest/market";                // ALFRED MARKET

    public static final String MY_MARKET_PREFERENCE_KEY_USER_NAME = "userName";
    public static final String MY_MARKET_PREFERENCE_KEY_USER_PASSWORD = "userPassword";
    public static final String MY_MARKET_PREFERENCE_KEY_NAME = "name";
    public static final String MY_MARKET_PREFERENCE_KEY_USER_ID = "userId";
    public static final String MY_MARKET_PREFERENCE_KEY_TOKEN = "token";

    public static final String GET_IMAGE_ICON = BASE_URL + "/image/icon/";
    public static final String GET_IMAGE_MEDIA = BASE_URL + "/image/media/";

    public static final String WRONG_CREDENTIALS = "Wrong credential";
    public static final String GCM_SENDER_ID = "11904307683";

    /***
     * EXPIRATION DATES
     */
    public static final int COUNTRIES_EXPIRATION = 1000 * 60 * 60 * 24; // 24
    // hours
    public static final int CATEGORIES_EXPIRATION = 1000 * 60 * 60 * 24; // 24


    public static final String KEY_PROFILE_NAME = "KEY_PROFILE_NAME";
    public static final String KEY_PROFILE_EMAIL = "KEY_PROFILE_EMAIL";

    // hours

}
