package com.tempos21.mymarket.domain.interactor;

import android.content.Context;

import com.tempos21.mymarket.data.repository.client.RateServiceRepository;
import com.tempos21.mymarket.domain.dto.request.RateRequest;
import com.tempos21.mymarket.sdk.model.Rate;

public class RateInteractor extends Interactor<RateRequest, Rate> {

  public RateInteractor(Context context) {
    super(context);
  }

  @Override
  protected Rate perform(RateRequest input) throws Exception {
    return setRate(input);
  }

  private Rate setRate(RateRequest input) throws Exception {
    RateServiceRepository serviceRepository = new RateServiceRepository(getContext());
    return serviceRepository.get(input);
  }
}