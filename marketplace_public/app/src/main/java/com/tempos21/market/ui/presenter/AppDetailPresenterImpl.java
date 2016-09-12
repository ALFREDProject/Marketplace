package com.tempos21.market.ui.presenter;

import com.tempos21.market.ui.view.GenericViewSubRequest;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.app.AppDetail;
import com.tempos21.mymarket.sdk.model.AppRate;

import java.util.List;

public class AppDetailPresenterImpl implements AppDetailPresenter, MyMarket.OnMarketAppDetailListener, MyMarket.OnMarketAppRateListener {

  private GenericViewSubRequest<AppDetail, List<AppRate>> view;
  private long id;

  public AppDetailPresenterImpl(GenericViewSubRequest<AppDetail, List<AppRate>> view) {
    this.view = view;
  }

  @Override
  public void getAppDetail(long id) {
    view.showProgress();
    MyMarket.getInstance().getAppDetail(this, id);
    this.id = id;
  }

  @Override
  public void onAppDetailSuccess(AppDetail appDetail) {
    view.onViewSuccess(appDetail);
    view.hideProgress();
    MyMarket.getInstance().getAppRateList(this, id);
  }

  @Override
  public void onAppDetailError(long id, Exception e) {
    view.onViewError(id, e);
    view.hideProgress();
  }

  @Override
  public void onAppRateListSuccess(List<AppRate> appRateList) {
    view.onViewSuccessSubRequest(appRateList);
  }

  @Override
  public void onAppRateListError(long id, Exception e) {
    view.onViewErrorSubRequest(id, e);
  }
}
