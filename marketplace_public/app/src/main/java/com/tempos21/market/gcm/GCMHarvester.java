package com.tempos21.market.gcm;

import com.tempos21.market.ui.presenter.TokenPresenterImpl;
import com.tempos21.market.util.TLog;
import com.tempos21.mymarket.domain.dto.request.TokenRequest;
import com.tempos21.mymarket.sdk.model.Token;


/**
 * Service to register token from gcm in our own backend
 */

public class GCMHarvester implements TokenPresenterImpl.OnTokenListener {

    public void pushRegister(String registrationId) {
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.token = registrationId;

        TokenPresenterImpl tokenPresenter = new TokenPresenterImpl(this);
        if (registrationId != null) {
            tokenPresenter.setToken(tokenRequest);
        }
    }


    @Override
    public void OnTokenListenerSucces(Token token) {
        TLog.i("PUSH REGISTERED ");
    }


    @Override
    public void OnTokenListenerError(Exception e) {
        TLog.i("PUSH NOT REGISTERED ");
    }

}
