package com.tempos21.market.ui.presenter;

import com.tempos21.market.util.MarketPlaceHelper;
import com.tempos21.mymarket.domain.dto.request.RateRequest;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.Rate;
import com.tempos21.mymarket.sdk.model.User;

import eu.alfred.api.market.responses.listener.SetAppRateResponseListener;
import eu.alfred.api.market.responses.set_app_rate.SetAppRateResponse;


public class RatePresenterImpl implements RatePresenter, MyMarket.OnMarketRateListener {

    private final OnRateListener onRateListener;


    public RatePresenterImpl(OnRateListener onRateListener) {
        this.onRateListener = onRateListener;
    }


    @Override
    public void onRateSuccess(Rate rate) {
        onRateListener.OnRateListenerSucces(rate);
    }


    @Override
    public void onRateError(long id, Exception e) {
        onRateListener.OnRateListenerError(e);
    }


    @Override
    public void setRate(RateRequest rateRequest) {
        MarketPlaceHelper.getInstance().marketPlace.setAppRate(rateRequest.id, rateRequest.title, rateRequest.rate, rateRequest.author, rateRequest.body, rateRequest.versionNumber, new SetAppRateResponseListener() {
            @Override
            public void onSuccess(SetAppRateResponse setAppRateResponse) {
                if (setAppRateResponse != null) {
                    Rate rate = new Rate();
                    rate.versionString = setAppRateResponse.versionString;
                    rate.score = setAppRateResponse.score;
                    rate.subject = setAppRateResponse.subject;
                    rate.date = setAppRateResponse.date;
                    rate.id = (int) setAppRateResponse.id;
                    rate.comment = setAppRateResponse.comment;

                    rate.users = new User();
                    rate.users.id = (int) setAppRateResponse.users.id;
                    rate.users.name = setAppRateResponse.users.name;
                    rate.users.dasUser = setAppRateResponse.users.dasUser;
                    rate.users.authToken = setAppRateResponse.users.authToken;
                    rate.users.isTester = setAppRateResponse.users.tester;
                    rate.users.isApprover = setAppRateResponse.users.approver;

                    onRateSuccess(rate);
                } else {
                    onRateError(-1, new NullPointerException("setAppRateResponse parameter is null."));
                }
            }

            @Override
            public void onError(Exception e) {
                onRateError(-1, e);
            }
        });
    }


    public interface OnRateListener {
        void OnRateListenerSucces(Rate rate);

        void OnRateListenerError(Exception e);
    }

}
