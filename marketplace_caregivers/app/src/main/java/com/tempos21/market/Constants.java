package com.tempos21.market;

public final class Constants {

    public static final boolean DEBUG = true;

    /***
     * Services URL
     */
    public static final String BASE_URL = "http://devel1.tempos21.com/alfredo-dev/rest/market";                // ALFRED MARKET


    public static final String LOGIN_SERVICE = BASE_URL + "/login";
    public static final String GETCOUNTRIES_SERVICE = BASE_URL + "/countries";
    public static final String GETAPPS_SERVICE = BASE_URL + "/apps";
    public static final String GET_TESTING_APPS_SERVICE = BASE_URL + "/apps/testing";
    public static final String SEARCHAPPS_SERVICE = BASE_URL + "/apps/search";
    public static final String GETAPP_SERVICE = BASE_URL + "/app";
    public static final String GETCATEGORIES_SERVICE = BASE_URL + "/categories";
    public static final String GET_INSTALLED_APP_SERVICE = BASE_URL + "/installedapps";
    public static final String TRANSACTIONS_SERVICE = BASE_URL + "/installedapps/set/";
    public static final String GET_IMAGE_ICON = BASE_URL + "/image/icon/";
    public static final String GET_IMAGE_PROMO = BASE_URL + "/image/promo/";
    public static final String GET_IMAGE_MEDIA = BASE_URL + "/image/media/";
    public static final String GET_RATINGS_SERVICE = BASE_URL + "/ratings";
    public static final String SET_RATING_SERVICE = BASE_URL + "/ratings/set";
    public static final String GET_BINARY = BASE_URL + "/binary/";
    public static final String GET_UPDATED_APPS = BASE_URL + "/apps/updates/";

    /***
     * EXPIRATION DATES
     */
    public static final int COUNTRIES_EXPIRATION = 1000 * 60 * 60 * 24; // 24
    // hours
    public static final int CATEGORIES_EXPIRATION = 1000 * 60 * 60 * 24; // 24
    // hours

}
