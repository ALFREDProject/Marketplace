package com.tempos21.market.ui.presenter;

import com.tempos21.market.util.MarketPlaceHelper;
import com.tempos21.mymarket.domain.dto.request.TokenRequest;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.Token;
import com.tempos21.mymarket.sdk.model.User;
import com.tempos21.mymarket.sdk.model.app.Os;
import com.tempos21.mymarket.sdk.model.app.Platform;

import eu.alfred.api.market.responses.listener.SetTokenResponseListener;
import eu.alfred.api.market.responses.push.SetTokenResponse;


public class TokenPresenterImpl implements TokenPresenter, MyMarket.OnMarketTokenListener {

    private final OnTokenListener onTokenListener;


    public TokenPresenterImpl(OnTokenListener onTokenListener) {
        this.onTokenListener = onTokenListener;
    }


    @Override
    public void onTokenSuccess(Token token) {
        onTokenListener.OnTokenListenerSucces(token);
    }


    @Override
    public void onTokenError(long id, Exception e) {
        onTokenListener.OnTokenListenerError(e);
    }


    @Override
    public void setToken(TokenRequest tokenRequest) {
        MarketPlaceHelper.getInstance().marketPlace.setToken(tokenRequest.token, new SetTokenResponseListener() {
            @Override
            public void onSuccess(SetTokenResponse setTokenResponse) {
                if (setTokenResponse != null) {
                    Token token = new Token();

                    token.uuid = setTokenResponse.uuid;
                    token.version = (int) setTokenResponse.version;
                    token.id = (int) setTokenResponse.id;

                    Platform platform = new Platform();
                    platform.id = token.platforms.id;
                    platform.name = token.platforms.name;
                    platform.os = new Os();
                    platform.os.id = token.platforms.os.id;
                    platform.os.name = token.platforms.os.name;
                    platform.os.extension = token.platforms.os.extension;
                    token.platforms = platform;

                    User user = new User();
                    user.id = setTokenResponse.users.id;
                    user.name = setTokenResponse.users.name;
                    user.dasUser = setTokenResponse.users.dasUser;
                    user.authToken = setTokenResponse.users.authToken;
                    user.isTester = setTokenResponse.users.tester;
                    user.isApprover = setTokenResponse.users.approver;
                    token.users = user;

                    onTokenSuccess(token);
                } else {
                    onTokenError(-1, new NullPointerException());
                }
            }

            @Override
            public void onError(Exception e) {
                onTokenError(-1, e);
            }
        });
        //MyMarket.getInstance().setToken(this, tokenRequest);
    }


    public interface OnTokenListener {
        void OnTokenListenerSucces(Token token);

        void OnTokenListenerError(Exception e);
    }

}
