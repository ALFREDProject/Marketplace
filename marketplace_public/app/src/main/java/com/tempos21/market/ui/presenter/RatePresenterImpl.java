package com.tempos21.market.ui.presenter;

import com.tempos21.mymarket.domain.dto.request.RateRequest;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.Rate;


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
    MyMarket.getInstance().setRate(this, rateRequest);
  }


  public interface OnRateListener{
    void OnRateListenerSucces(Rate rate);
    void OnRateListenerError(Exception e);
  }

}
