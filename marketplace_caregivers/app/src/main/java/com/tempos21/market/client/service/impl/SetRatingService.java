package com.tempos21.market.client.service.impl;

import android.content.Context;

import com.tempos21.market.client.bean.ServiceResponse;
import com.tempos21.market.client.service.input.Input;

public class SetRatingService extends DefaultSecureService {

    OnSetRatingServiceResponseListener onSetRatingServiceResponseListener;
    DefaultServiceHandler serviceHandler;


    public SetRatingService(String url, Context context) {
        super(url, ServiceType.POST, context);
    }

    public OnSetRatingServiceResponseListener getOnServiceResponseListener() {
        return onSetRatingServiceResponseListener;
    }

    public void setOnSetRatingServiceResponseListener(OnSetRatingServiceResponseListener onServiceResponseListener) {
        this.onSetRatingServiceResponseListener = onServiceResponseListener;
    }

    public DefaultServiceHandler getServiceHandler() {
        return serviceHandler;
    }

    public void setServiceHandler(DefaultServiceHandler serviceHandler) {
        this.serviceHandler = serviceHandler;
    }

    @Override
    public void runService(Input input) {
        setupParameters(input);
        super.runService(input);
    }

    @Override
    public void onSecureServiceResponse(int responseCode, ServiceResponse response) {
        int ErrorCode = responseCode;


        if (onSetRatingServiceResponseListener != null) {
            onSetRatingServiceResponseListener.onSetRatingServiceResponse(ErrorCode, response.getIntError_code());
        }
    }

    public void setupParameters(String id, String title, String body, int rate, int userId, int versionNumber) {
        clearParameters();
        addEntityParameter("id", id);
        addEntityParameter("title", title);
        addEntityParameter("rate", Integer.toString(rate));
        addEntityParameter("author", Integer.toString(userId));
        addEntityParameter("body", body);
        addEntityParameter("versionNumber", Integer.toString(versionNumber));
    }


    public interface OnSetRatingServiceResponseListener {
        public void onSetRatingServiceResponse(int responseCode, int error_code);
    }

}
