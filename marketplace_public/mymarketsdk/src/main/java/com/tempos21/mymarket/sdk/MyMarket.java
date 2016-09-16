package com.tempos21.mymarket.sdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;

import com.tempos21.mymarket.domain.dto.request.LoginRequest;
import com.tempos21.mymarket.domain.dto.response.LoginResponse;
import com.tempos21.mymarket.domain.interactor.Interactor;
import com.tempos21.mymarket.domain.interactor.LoginInteractor;
import com.tempos21.mymarket.sdk.constant.MyMarketConstants;
import com.tempos21.mymarket.sdk.model.AppListType;
import com.tempos21.mymarket.sdk.model.AppRate;
import com.tempos21.mymarket.sdk.model.Category;
import com.tempos21.mymarket.sdk.model.Country;
import com.tempos21.mymarket.sdk.model.Language;
import com.tempos21.mymarket.sdk.model.Platform;
import com.tempos21.mymarket.sdk.model.Rate;
import com.tempos21.mymarket.sdk.model.Token;
import com.tempos21.mymarket.sdk.model.User;
import com.tempos21.mymarket.sdk.model.app.App;
import com.tempos21.mymarket.sdk.model.app.AppDetail;
import com.tempos21.mymarket.sdk.util.BuildVariant;
import com.tempos21.mymarket.sdk.util.Logger;
import com.tempos21.rampload.RampLoad;

import java.io.File;
import java.util.List;

public class MyMarket {

    public static final String SERVER_ERROR_GATEWAY_TIME_OUT = "504 Gateway Time-out";
    public static final String SERVER_ERROR_FORBIDDEN = "403 Forbidden";
    public static final String SERVER_ERROR_AUTHENTICATION_REQUIRED = "407 authenticationrequired";
    public static final String SERVER_ERROR_UNAVAILABLE = "503 Service Unavailable";
    public static final String SERVER_ERROR_NOT_FOUND = "404 Not Found";
    public static final String SERVER_ERROR_NO_HOST_NAME = "Unable to resolve host \"" + MyMarketConstants.HOST_NAME + "\": No address associated with hostname";
    private static MyMarket instance;
    private final String appsDestinationPath;
    private final String appsDestinationFile;
    private Context context;
    private MyMarketConfig config;
    private OnMarketLoginListener listener;

    private BuildVariant buildVariant = BuildVariant.DEBUG;

    private MyMarket() {
        appsDestinationPath = Environment.getExternalStorageDirectory().getPath() + MyMarketConstants.APPS_DESTINATION_PATH;
        appsDestinationFile = Environment.getExternalStorageDirectory().getPath() + MyMarketConstants.APPS_DESTINATION_FILE;
    }

    private MyMarket(Context context, MyMarketConfig config) {
        this.context = context;
        this.config = config;
        appsDestinationPath = Environment.getExternalStorageDirectory().getPath() + MyMarketConstants.APPS_DESTINATION_PATH;
        appsDestinationFile = Environment.getExternalStorageDirectory().getPath() + MyMarketConstants.APPS_DESTINATION_FILE;
        RampLoad.init(context);
        AppManager.init(context);
        DeviceManager.init(context);
    }

    /**
     * Initializes the MyMarket SDK. Any attempt to use the SDK without initialization
     * will generate a RuntimeException
     *
     * @param context The context to be used during execution.
     * @param config  The Configuration of the execution. This object can only be delivered
     *                on initialization, no further changes of configuration are allowed.
     */
    public static MyMarket init(Context context, MyMarketConfig config) {
        if (instance == null) {
            instance = new MyMarket(context, config);
        }
        return instance;
    }

    public static MyMarket getInstance() {
        if (instance == null) {
            throw new RuntimeException("Market not initialized");
        }
        return instance;
    }

    public static void addDownloadListener(RampLoad.DownloadListener listener) {
        RampLoad.getInstance().addDownloadListener(listener);
    }

    public static void addStatusListener(RampLoad.StatusListener listener) {
        RampLoad.getInstance().addStatusListener(listener);
    }

    public static void removeDownloadListener(RampLoad.DownloadListener listener) {
        RampLoad.getInstance().removeDownloadListener(listener);
    }

    public static void removeStatusListener(RampLoad.StatusListener listener) {
        RampLoad.getInstance().removeStatusListener(listener);
    }

    public boolean isDebug() {
        return buildVariant == BuildVariant.DEBUG;
    }

    /*
        public boolean areThereSavedCredentials() {
            String userName = MyMarketPreferences.getInstance(context).getString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_USER_NAME, "");
            String userPassword = MyMarketPreferences.getInstance(context).getString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_USER_PASSWORD, "");
            if (TextUtils.isEmpty(userName.trim())) {
                return false;
            } else if (TextUtils.isEmpty(userPassword.trim())) {
                return false;
            }
            return true;
        }

        public void autoLogin(final OnMarketLoginListener listener) {
            login(MyMarketPreferences.getInstance(context).getString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_USER_NAME, ""), MyMarketPreferences.getInstance(context).getString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_USER_PASSWORD, ""), listener);
        }
    */
    public void login(String userName, String userPassword, final OnMarketLoginListener listener) {
        this.listener = listener;
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.userName = userName;
        loginRequest.userPassword = userPassword;
        MyMarketPreferences.getInstance(context).setString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_USER_NAME, userName);
        MyMarketPreferences.getInstance(context).setString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_USER_PASSWORD, userPassword);
        LoginInteractor loginInteractor = new LoginInteractor(context);
        loginInteractor.setClientResultListener(new Interactor.OnTaskResultListener<LoginResponse>() {

            @Override
            public void onTaskSuccess(long id, LoginResponse result) {
                listener.onLoginSuccess(result.user);
            }

            @Override
            public void onTaskFailure(long id, Exception e) {
                listener.onLoginError(id, e);
            }
        });
        loginInteractor.executeAsync(loginRequest);
    }

    /*
        void reLogin(String userName, String userPassword) {
            Logger.logE("Server Error 403 Forbidden. Trying to re-login");
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.userName = userName;
            loginRequest.userPassword = userPassword;
            MyMarketPreferences.getInstance(context).setString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_USER_NAME, userName);
            MyMarketPreferences.getInstance(context).setString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_USER_PASSWORD, userPassword);
            LoginInteractor loginInteractor = new LoginInteractor(context);

            loginInteractor.setClientResultListener(new Interactor.OnTaskResultListener<LoginResponse>() {

                @Override
                public void onTaskSuccess(long id, LoginResponse result) {
                    if (listener != null) {
                        listener.onLoginSuccess(result.user);
                    }
                }

                @Override
                public void onTaskFailure(long id, Exception e) {
                    if (listener != null) {
                        listener.onLoginError(id, e);
                    }
                }
            });
            loginInteractor.executeAsync(loginRequest);
        }

        void reLogin(final OnMarketReLoginListener listener) {
            Logger.logE("Server Error 403 Forbidden. Trying to re-login");
            LoginRequest loginRequest = new LoginRequest();
            loginRequest.userName = MyMarketPreferences.getInstance(context).getString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_USER_NAME, "");
            loginRequest.userPassword = MyMarketPreferences.getInstance(context).getString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_USER_PASSWORD, "");
            LoginInteractor loginInteractor = new LoginInteractor(context);

            loginInteractor.setClientResultListener(new Interactor.OnTaskResultListener<LoginResponse>() {

                @Override
                public void onTaskSuccess(long id, LoginResponse result) {
                    if (listener != null) {
                        listener.onReLoginSuccess(result.user);
                    }
                }

                @Override
                public void onTaskFailure(long id, Exception e) {
                    if (listener != null) {
                        listener.onReLoginError(id, e);
                    }
                }
            });
            loginInteractor.executeAsync(loginRequest);
        }
        public void getCountryList(final OnMarketCountryListener listener) {
            CountryInteractor countryInteractor = new CountryInteractor(context);
            countryInteractor.setClientResultListener(new Interactor.OnTaskResultListener<CountryListResponse>() {

                @Override
                public void onTaskSuccess(long id, CountryListResponse result) {
                    listener.onCountryListSuccess(result.countryList);
                }

                @Override
                public void onTaskFailure(long id, Exception e) {
                    if (e.getMessage().equals(SERVER_ERROR_FORBIDDEN)) {
                        reLogin(MyMarketPreferences.getInstance(context).getString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_USER_NAME, ""), MyMarketPreferences.getInstance(context).getString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_USER_PASSWORD, ""));
                    }
                    listener.onCountryListError(id, e);
                }
            });
            countryInteractor.executeAsync(null);
        }

        public void getCategoryList(final OnMarketCategoryListener listener) {
            CategoryInteractor categoryInteractor = new CategoryInteractor(context);
            categoryInteractor.setClientResultListener(new Interactor.OnTaskResultListener<CategoryListResponse>() {

                @Override
                public void onTaskSuccess(long id, CategoryListResponse result) {
                    listener.onCategoryListSuccess(result.categoryList);
                }

                @Override
                public void onTaskFailure(long id, Exception e) {
                    if (e.getMessage().equals(SERVER_ERROR_FORBIDDEN)) {
                        reLogin(MyMarketPreferences.getInstance(context).getString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_USER_NAME, ""), MyMarketPreferences.getInstance(context).getString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_USER_PASSWORD, ""));
                    }
                    listener.onCategoryListError(id, e);
                }
            });
            categoryInteractor.executeAsync(null);
        }

        public void getLanguageList(final OnMarketLanguageListener listener) {
            LanguageInteractor languageInteractor = new LanguageInteractor(context);
            languageInteractor.setClientResultListener(new Interactor.OnTaskResultListener<LanguageListResponse>() {

                @Override
                public void onTaskSuccess(long id, LanguageListResponse result) {
                    listener.onLanguageListSuccess(result.languageList);
                }

                @Override
                public void onTaskFailure(long id, Exception e) {
                    if (e.getMessage().equals(SERVER_ERROR_FORBIDDEN)) {
                        reLogin(MyMarketPreferences.getInstance(context).getString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_USER_NAME, ""), MyMarketPreferences.getInstance(context).getString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_USER_PASSWORD, ""));
                    }
                    listener.onLanguageListError(id, e);
                }
            });
            languageInteractor.executeAsync(null);
        }
    */
    public Platform getPlatform() {
        return config.getPlatform();
    }

    public String getDeviceId() {
        return config.getDeviceId();
    }

    public int getPlatformVersion() {
        return config.getPlatformVersion();
    }

    public String getAppsDestinationFile() {
        return appsDestinationFile;
    }

    private void checkDiskQuota() {
        File file = new File(appsDestinationPath);
        float size = getFolderSize(file);
        Logger.logE("<DISK_QUOTA folder=\"" + appsDestinationPath + "\" size=\"" + size + "\" />");
        if (size > MyMarketConstants.DISK_QUOTA) {
            deleteApplicationsFolderContent(file);
        }
    }

    private float getFolderSize(File file) {
        long size = 0;
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.isFile()) {
                    size += f.length();
                } else {
                    size += getFolderSize(f);
                }
            }
            return (size / 1024f);
        }
        return 0;
    }

    private void deleteApplicationsFolderContent(File file) {
        File[] files = file.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f.isFile()) {
                    f.delete();
                } else {
                    deleteApplicationsFolderContent(f);
                }
            }
        }
    }

    public AppList getAppList(AppListConfig config, AppListType type) {
        if (instance == null) {
            throw new RuntimeException("Market not initialized");
        } else if (config == null) {
            throw new RuntimeException("AppListConfig must not be null");
        }
        return new AppList(context, config, type);
    }

    public AppList getAppList(AppListConfig config) {
        return getAppList(config, AppListType.DEFAULT);
    }

    public AppList getInstalledAppList(AppListConfig config) {
        return getAppList(config, AppListType.INSTALLED);
    }

    public AppList getMostPopularAppList(AppListConfig config) {
        return getAppList(config, AppListType.MOST_POPULAR);
    }

    public AppList getLatestAppList(AppListConfig config) {
        return getAppList(config, AppListType.LATEST);
    }

    public AppList getUpdatableAppList(AppListConfig config) {
        return getAppList(config, AppListType.UPDATABLE);
    }

    public AppList getSearchAppList(AppListConfig config) {
        return getAppList(config, AppListType.SEARCH);
    }
/*
    public void setInstalledApps(final OnMarketSetInstalledAppsListener listener, List<InstalledApp> installedAppList) {
        final InstalledAppRequest installedAppRequest = new InstalledAppRequest();
        if (installedAppList != null) {
            Gson gson = new GsonBuilder().create();
            JsonArray jsonArray = gson.toJsonTree(installedAppList).getAsJsonArray();
            installedAppRequest.setJsonArray(jsonArray.toString());
        }
        final SetInstalledAppsInteractor setInstalledAppsInteractor = new SetInstalledAppsInteractor(context);
        setInstalledAppsInteractor.setClientResultListener(new Interactor.OnTaskResultListener<Void>() {

            @Override
            public void onTaskSuccess(long id, Void result) {
                listener.onSetInstalledAppsSuccess();
            }

            @Override
            public void onTaskFailure(long id, Exception e) {
                if (e.getMessage().equals(SERVER_ERROR_FORBIDDEN)) {
                    MyMarket.getInstance().reLogin(new MyMarket.OnMarketReLoginListener() {

                        @Override
                        public void onReLoginSuccess(User user) {
                            setInstalledAppsInteractor.executeAsync(installedAppRequest);
                        }

                        @Override
                        public void onReLoginError(long id, Exception e) {

                        }
                    });
                } else {
                    listener.onSetInstalledAppsError(id, e);
                }
            }
        });
        setInstalledAppsInteractor.executeAsync(installedAppRequest);
    }
*/

  /* RampLoad */
/*
    public void getAppRateList(final OnMarketAppRateListener listener, long id) {
        final AppRateListRequest appRateListRequest = new AppRateListRequest();
        appRateListRequest.id = id;
        final AppRateInteractor appRateInteractor = new AppRateInteractor(context);
        appRateInteractor.setClientResultListener(new Interactor.OnTaskResultListener<AppRateListResponse>() {

            @Override
            public void onTaskSuccess(long id, AppRateListResponse result) {
                listener.onAppRateListSuccess(result.appRateList);
            }

            @Override
            public void onTaskFailure(long id, Exception e) {
                if (e.getMessage().equals(SERVER_ERROR_FORBIDDEN)) {
                    MyMarket.getInstance().reLogin(new MyMarket.OnMarketReLoginListener() {

                        @Override
                        public void onReLoginSuccess(User user) {
                            appRateInteractor.executeAsync(appRateListRequest);
                        }

                        @Override
                        public void onReLoginError(long id, Exception e) {

                        }
                    });
                } else {
                    listener.onAppRateListError(id, e);
                }
            }
        });
        appRateInteractor.executeAsync(appRateListRequest);
    }

    public void setRate(final OnMarketRateListener listener, final RateRequest rateRequest) {
        final RateInteractor rateInteractor = new RateInteractor(context);
        rateInteractor.setClientResultListener(new Interactor.OnTaskResultListener<Rate>() {

            @Override
            public void onTaskSuccess(long id, Rate result) {
                listener.onRateSuccess(result);
            }

            @Override
            public void onTaskFailure(long id, Exception e) {
                if (e.getMessage().equals(SERVER_ERROR_FORBIDDEN)) {
                    MyMarket.getInstance().reLogin(new MyMarket.OnMarketReLoginListener() {

                        @Override
                        public void onReLoginSuccess(User user) {
                            rateInteractor.executeAsync(rateRequest);
                        }

                        @Override
                        public void onReLoginError(long id, Exception e) {

                        }
                    });
                } else {
                    listener.onRateError(id, e);
                }
            }
        });
        rateInteractor.executeAsync(rateRequest);
    }

    public void getAppDetail(final OnMarketAppDetailListener listener, long id) {
        final AppDetailRequest appDetailRequest = new AppDetailRequest();
        appDetailRequest.id = id;
        final AppDetailInteractor appDetailInteractor = new AppDetailInteractor(context);
        appDetailInteractor.setClientResultListener(new Interactor.OnTaskResultListener<AppDetailResponse>() {

            @Override
            public void onTaskSuccess(long id, AppDetailResponse result) {
                listener.onAppDetailSuccess(result.appDetail);
            }

            @Override
            public void onTaskFailure(long id, Exception e) {
                if (e.getMessage() != null) {
                    MyMarket.getInstance().reLogin(new MyMarket.OnMarketReLoginListener() {

                        @Override
                        public void onReLoginSuccess(User user) {
                            appDetailInteractor.executeAsync(appDetailRequest);
                        }

                        @Override
                        public void onReLoginError(long id, Exception e) {

                        }
                    });
                } else {
                    listener.onAppDetailError(id, e);
                }
            }
        });
        appDetailInteractor.executeAsync(appDetailRequest);
    }

    public void setToken(final OnMarketTokenListener listener, final TokenRequest tokenRequest) {
        final TokenInteractor tokenInteractor = new TokenInteractor(context);
        tokenInteractor.setClientResultListener(new Interactor.OnTaskResultListener<Token>() {

            @Override
            public void onTaskSuccess(long id, Token result) {
                listener.onTokenSuccess(result);
            }

            @Override
            public void onTaskFailure(long id, Exception e) {
                if (e.getMessage().equals(SERVER_ERROR_FORBIDDEN)) {
                    MyMarket.getInstance().reLogin(new MyMarket.OnMarketReLoginListener() {

                        @Override
                        public void onReLoginSuccess(User user) {
                            tokenInteractor.executeAsync(tokenRequest);
                        }

                        @Override
                        public void onReLoginError(long id, Exception e) {

                        }
                    });
                } else {
                    listener.onTokenError(id, e);
                }
            }
        });
        tokenInteractor.executeAsync(tokenRequest);
    }
*/
  /* AppManager */

    public void download(AppDetail appDetail) {
        checkDiskQuota();
        AppManager.getInstance().download(appDetail);
    }

    public void install(AppDetail appDetail) {
        AppManager.getInstance().install(appDetail);
    }

    public void uninstall(AppDetail appDetail) {
        AppManager.getInstance().uninstall(appDetail);
    }

    public void update(AppDetail appDetail) {
        AppManager.getInstance().update(appDetail);
    }

    public AppManager.State getAppState(AppDetail appDetail) {
        return AppManager.getInstance().getAppState(appDetail);
    }

  /* DeviceManager */

    public void checkInstalledApps(final MyMarket.OnMarketSetInstalledAppsListener listener, List<App> serverAppList, final List<App> registeredInServerAppList) {
        DeviceManager.getInstance().checkInstalledApps(listener, serverAppList, registeredInServerAppList);
    }

    public List<PackageInfo> getInstalledApplicationsByUser() {
        return DeviceManager.getInstance().getInstalledApplicationsByTheUser();
    }

  /* Listeners */

    public interface OnMarketLoginListener {

        void onLoginSuccess(User user);

        void onLoginError(long id, Exception e);
    }

    public interface OnMarketReLoginListener {

        void onReLoginSuccess(User user);

        void onReLoginError(long id, Exception e);
    }

    public interface OnMarketCategoryListener {

        void onCategoryListSuccess(List<Category> categoryList);

        void onCategoryListError(long id, Exception e);
    }

    public interface OnMarketCountryListener {

        void onCountryListSuccess(List<Country> countryList);

        void onCountryListError(long id, Exception e);
    }

    public interface OnMarketLanguageListener {

        void onLanguageListSuccess(List<Language> languageList);

        void onLanguageListError(long id, Exception e);
    }

    public interface OnMarketSetInstalledAppsListener {

        void onSetInstalledAppsSuccess();

        void onSetInstalledAppsError(long id, Exception e);
    }

    public interface OnMarketAppRateListener {

        void onAppRateListSuccess(List<AppRate> appRateList);

        void onAppRateListError(long id, Exception e);
    }

    public interface OnMarketRateListener {

        void onRateSuccess(Rate rate);

        void onRateError(long id, Exception e);
    }

    public interface OnMarketAppDetailListener {

        void onAppDetailSuccess(AppDetail appDetail);

        void onAppDetailError(long id, Exception e);
    }

    public interface OnMarketTokenListener {

        void onTokenSuccess(Token token);

        void onTokenError(long id, Exception e);
    }

}
