package com.tempos21.market.ui.presenter;

import com.tempos21.mymarket.domain.dto.request.TokenRequest;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.Token;


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
    MyMarket.getInstance().setToken(this, tokenRequest);
  }


  public interface OnTokenListener{
    void OnTokenListenerSucces(Token token);
    void OnTokenListenerError(Exception e);
  }

}
