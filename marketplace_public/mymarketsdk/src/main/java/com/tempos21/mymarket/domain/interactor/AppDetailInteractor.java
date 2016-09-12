package com.tempos21.mymarket.domain.interactor;

import android.content.Context;

import com.tempos21.mymarket.data.repository.client.AppDetailServiceRepository;
import com.tempos21.mymarket.domain.dto.request.AppDetailRequest;
import com.tempos21.mymarket.domain.dto.response.AppDetailResponse;

public class AppDetailInteractor extends Interactor<AppDetailRequest, AppDetailResponse> {

  public AppDetailInteractor(Context context) {
    super(context);
  }

  @Override
  protected AppDetailResponse perform(AppDetailRequest input) throws Exception {
    return getAppDetail(input);
  }

  private AppDetailResponse getAppDetail(AppDetailRequest input) throws Exception {
    AppDetailResponse mainResponse = new AppDetailResponse();
    AppDetailServiceRepository serviceRepository = new AppDetailServiceRepository(getContext());
    mainResponse.appDetail = serviceRepository.get(input);
    return mainResponse;
  }
}