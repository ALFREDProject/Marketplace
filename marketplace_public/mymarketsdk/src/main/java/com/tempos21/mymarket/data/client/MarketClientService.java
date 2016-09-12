package com.tempos21.mymarket.data.client;

import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

public interface MarketClientService {

  /**
   * {{server}}/{{uri}}/login
   */
  @FormUrlEncoded
  @POST(MarketClientAdapter.URI_LOGIN)
  Response login(@Field("j_username") String j_username, // j_username
                 @Field("j_password") String j_password, // j_password
                 @Field("device_id") String device_id, // device_id
                 @Field("version") String version, // version
                 @Field("platform") String platform); //platform

  /**
   * {{server}}/{{uri}}/countries
   */
  @GET(MarketClientAdapter.URI_GET_COUNTRIES)
  Response getCountryList();

  /**
   * {{server}}/{{uri}}/categories
   */
  @GET(MarketClientAdapter.URI_GET_CATEGORIES)
  Response getCategoryList();

  /**
   * {{server}}/{{uri}}/languages
   */
  @GET(MarketClientAdapter.URI_GET_LANGUAGES)
  Response getLanguageList();

  /**
   * {{server}}/{{uri}}/ratings/{{app_id}}
   */
  @GET(MarketClientAdapter.URI_GET_RATINGS_OF_AN_APP)
  Response getAppRateList(@Path("app_id") Long app_id);

  @FormUrlEncoded
  @POST(MarketClientAdapter.URI_SET_RATING)
  Response setRating(@Field("id") Integer id, // id
                     @Field("title") String title, // title
                     @Field("rate") Integer rate, // rate
                     @Field("author") Integer author, // author
                     @Field("body") String body, // body
                     @Field("versionNumber") Integer versionNumber); // versionNumber

  @FormUrlEncoded
  @POST(MarketClientAdapter.URI_DELETE_RATING)
  Response deleteRating(@Field("id") String id);

  /**
   * {{server}}/{{uri}}/apps?start={{start}}&elements={{elements}}&sorting={{sorting}}
   */
  @GET(MarketClientAdapter.URI_GET_APPS)
  Response getAppList(@Query("start") Integer start, // start
                      @Query("elements") Integer elements, // elements
                      @Query("sorting") String sorting); // sorting

  /**
   * {{server}}/{{uri}}/apps?start={{start}}&elements={{elements}}&sorting={{sorting}}&country={{country}}&category={{category}}&language={{language}}&hasPromoImage={{hasPromoImage}}
   */
  @GET(MarketClientAdapter.URI_GET_APPS)
  Response getAppListMarket(@Query("start") Integer start, // start
                            @Query("elements") Integer elements, // elements
                            @Query("sorting") String sorting, // sorting
                            @Query("country") Integer country, // country
                            @Query("category") Integer category, // category
                            @Query("language") Integer language, // language
                            @Query("hasPromoImage") Boolean hasPromoImage); // hasPromoImage

  /**
   * {{server}}/{{uri}}/apps/search/{words}?start={{start}}&elements={{elements}}&sorting={{sorting}}
   */
  @GET(MarketClientAdapter.URI_SEARCH_APPS)
  Response getSearchAppListMarket(@Path("words") String words, // words
                                  @Query("start") Integer start, // start
                                  @Query("elements") Integer elements, // elements
                                  @Query("sorting") String sorting); // sorting

  /**
   * {{server}}/{{uri}}/installedapps?start={{start}}&elements={{elements}}&sorting={{sorting}}
   */
  @GET(MarketClientAdapter.URI_GET_INSTALLED_APPS)
  Response getInstalledAppList(@Query("start") Integer start, // start
                               @Query("elements") Integer elements, // elements
                               @Query("sorting") String sorting); // sorting

  /**
   * {{server}}/{{uri}}/apps/updates?start={{start}}&elements={{elements}}&sorting={{sorting}}
   */
  @GET(MarketClientAdapter.URI_GET_UPDATABLE_APPS)
  Response getUpdatableAppList(@Query("start") Integer start, // start
                               @Query("elements") Integer elements, // elements
                               @Query("sorting") String sorting); // sorting

  /**
   * {{server}}/{{uri}}/installedapps/set/
   */
  @FormUrlEncoded
  @POST(MarketClientAdapter.URI_SET_INSTALLED_APPS)
  Response setInstalledApps(@Field("changes") String string); // changes

  /**
   * {{server}}/{{uri}}/app/{{app_id}}
   */
  @GET(MarketClientAdapter.URI_GET_APP_BY_ID)
  Response getAppById(@Path("app_id") Long app_id);

  /**
   * {{server}}/{{uri}}/device/settoken/
   */
  @FormUrlEncoded
  @POST(MarketClientAdapter.URI_SET_TOKEN)
  Response setToken(@Field("token") String string); // registerId from gcm
}
