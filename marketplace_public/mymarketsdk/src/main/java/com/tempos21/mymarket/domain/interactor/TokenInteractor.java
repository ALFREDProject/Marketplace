package com.tempos21.mymarket.domain.interactor;

import android.content.Context;

import com.tempos21.mymarket.data.repository.client.TokenServiceRepository;
import com.tempos21.mymarket.domain.dto.request.TokenRequest;
import com.tempos21.mymarket.sdk.model.Token;

public class TokenInteractor extends Interactor<TokenRequest, Token> {

    public TokenInteractor(Context context) {
        super(context);
    }

    @Override
    protected Token perform(TokenRequest input) throws Exception {
        return setToken(input);
    }

    private Token setToken(TokenRequest input) throws Exception {
        TokenServiceRepository serviceRepository = new TokenServiceRepository(getContext());
        return serviceRepository.get(input);
    }
}