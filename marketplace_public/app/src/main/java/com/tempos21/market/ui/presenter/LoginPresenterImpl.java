package com.tempos21.market.ui.presenter;

import android.content.Context;

import com.tempos21.market.Constants;
import com.tempos21.market.ui.view.GenericView;
import com.tempos21.market.util.MarketPlaceHelper;
import com.tempos21.mymarket.sdk.AppList;
import com.tempos21.mymarket.sdk.AppListConfig;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.AppListType;
import com.tempos21.mymarket.sdk.model.User;
import com.tempos21.mymarket.sdk.model.app.App;
import com.tempos21.rampload.util.Prefs;

import java.util.ArrayList;
import java.util.List;

import eu.alfred.api.market.responses.listener.LoginResponseListener;

public class LoginPresenterImpl implements LoginPresenter, AppList.OnAppListListener, MyMarket.OnMarketSetInstalledAppsListener {

    private Context context;
    private GenericView<User> view;
    private ArrayList<App> serverAppList;
    private AppList appList;
    private AppList installedAppList;


    public LoginPresenterImpl(Context context, GenericView<User> view) {
        this.view = view;
        this.context = context;
    }

    /*public void autoLogin() {
        view.showProgress();
        if (MyMarket.getInstance().areThereSavedCredentials()) {
            MyMarket.getInstance().autoLogin(this);
        } else {
            view.hideProgress();
        }
    }*/

    @Override
    public void login(final String username, final String password) {
        view.showProgress();
        MarketPlaceHelper.getInstance().marketPlace.login(username, password, new LoginResponseListener() {
            @Override
            public void onSuccess(eu.alfred.api.market.responses.login.User user) {
                if (user != null) {
                    User u = new User();
                    u.id = user.id;
                    u.name = user.name;
                    u.dasUser = user.dasUser;
                    u.authToken = user.authToken;
                    u.isTester = user.tester;
                    u.isApprover = user.approver;
                    Prefs.setString(Constants.MY_MARKET_PREFERENCE_KEY_USER_NAME, username);
                    Prefs.setString(Constants.MY_MARKET_PREFERENCE_KEY_USER_PASSWORD, password);
                    onLoginSuccess(u);
                } else {
                    onLoginError(-1, new NullPointerException("The retrieved user is null."));
                }
            }

            @Override
            public void onError(Exception e) {
                onLoginError(-1, e);
            }
        });
    }

    public void onLoginSuccess(User user) {
        appList = MyMarket.getInstance().getAppList(new AppListConfig.Builder() // AppListConfig
                .setName("Login AppList") // Name
                .setElements(0).build()); // Number of elements per page

        installedAppList = MyMarket.getInstance().getInstalledAppList(new AppListConfig.Builder() // AppListConfig
                .setName("Login Installed AppList") // Name
                .setElements(0).build()); // Number of elements per page

        appList.getForcedAppList(this);
        view.onViewSuccess(user);
        view.hideProgress();
    }

    public void onLoginError(long id, Exception e) {
        view.onViewError(id, e);
        view.hideProgress();
    }

    @Override
    public void onAppListSuccess(AppListType type, List<App> registeredInServerApps) {
        switch (type) {
            case DEFAULT:
                serverAppList = new ArrayList<App>();
                for (App app : registeredInServerApps) {
                    serverAppList.add(app);
                }
                installedAppList.getForcedAppList(this);
                break;
            case INSTALLED:
                MyMarket.getInstance().checkInstalledApps(this, serverAppList, registeredInServerApps);
                break;
        }
    }

    @Override
    public void onAppListError(AppListType type, long id, Exception e) {

    }

    @Override
    public void onSetInstalledAppsSuccess() {

    }

    @Override
    public void onSetInstalledAppsError(long id, Exception e) {

    }
}
