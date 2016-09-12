package com.tempos21.market.client.service.impl;

import android.content.Context;

import com.tempos21.market.client.bean.ServiceResponse;
import com.tempos21.market.client.bean.User;
import com.tempos21.market.client.service.impl.LoginService.OnLoginResponseListener;
import com.tempos21.market.client.service.input.Input;
import com.tempos21.market.client.service.input.LoginInput;
import com.tempos21.market.db.UserModel;
import com.tempos21.market.util.ShowError;

public abstract class DefaultSecureService extends DefaultService implements OnLoginResponseListener {

    Context context;
    LoginService loginService;
    DefaultServiceHandler serviceHandler;

    ;
    ServiceResponse emptyResponse = null;
    boolean loginExecuted = false;
    private User user;
    private ServiceType serviceType;
    private Input input;
    private int retry = 0;
    public DefaultSecureService(String url, ServiceType serviceType, Context context) {
        super(url);
        this.serviceType = serviceType;
        this.context = context;
    }

    private void launchLogin() {
        LoginInput input = new LoginInput(user);
        loginService = new LoginService(context);
        loginService.runService(input);
        loginService.setOnServiceResponseListener(this);
    }

    public void runService() {
        serviceHandler = new DefaultServiceHandler(context, this);
        serviceHandler.setOnServiceResponseListener(this);
        if (serviceType == ServiceType.POST) {
            serviceHandler.runPostServiceAsync();
        } else {
            serviceHandler.runGetServiceAsync();
        }
    }

    public void runService(Input input) {
        this.input = input;
        //setupParameters(input);
        serviceHandler = new DefaultServiceHandler(context, this);

        serviceHandler.setOnServiceResponseListener(this);
        if (serviceType == ServiceType.POST) {
            serviceHandler.runPostServiceAsync();
        } else {
            serviceHandler.runGetServiceAsync();
        }
    }

    public void setupParameters(Input input) {
        clearEntitiyParameters();


    }

    public void loadUser() {
        UserModel model = new UserModel(context);
        user = model.getUser();
    }

    @Override
    public void onServiceResponse(int responseCode, ServiceResponse response) {
        /* Login is only lauched WHEN:
		 * A) It hasn't been executed before in this run;
		 * B) Response has been received
		 * C) The response has a login error as error code. or a 403
		 * D) Lost session
		 */

        if (responseCode == ServiceErrorCodes.HTTP_403 && retry < 3) {
            ShowError.showReconnectionDialog(context);
            loadUser();
            launchLogin();
            retry++;
        } else {
            if (responseCode == ServiceErrorCodes.LOGIN_ERROR) {
                ShowError.hideReconnectionDialog();
                onSecureServiceResponse(responseCode, response);
            } else {
                if (response != null) {
                    if (response.getIntError_code() == ServiceErrorCodes.HTTP_403 && retry < 3) {
                        ShowError.showReconnectionDialog(context);
                        loadUser();
                        launchLogin();
                        retry++;
                    } else {
                        if (response.getIntError_code() == ServiceErrorCodes.LOGIN_ERROR) {
                            responseCode = ServiceErrorCodes.LOGIN_ERROR;
                        }
                        ShowError.hideReconnectionDialog();
                        onSecureServiceResponse(responseCode, response);
                        retry = 0;
                    }
                } else {
                    ShowError.hideReconnectionDialog();
                    onSecureServiceResponse(responseCode, response);
                    retry = 0;
                }
            }
        }
    }

    @Override
    public void onLoginResponse(int responseCode, User loggedUser) {
        if (responseCode == ServiceErrorCodes.HTTP_OK && loggedUser != null) {
            user.setApprover(loggedUser.isApprover());
            user.setIsTester(loggedUser.isIsTester());
            user.setName(loggedUser.getName());
            user.setId(loggedUser.getId());
            UserModel model = new UserModel(context);
            model.setUser(user);
            if (input != null) {
                runService(input);
            } else {
                runService();
            }
        } else {
            ShowError.hideReconnectionDialog();
            onSecureServiceResponse(responseCode, emptyResponse);
            retry = 0;
        }
    }

    public abstract void onSecureServiceResponse(int responseCode, ServiceResponse response);


    public enum ServiceType {GET, POST}


}
