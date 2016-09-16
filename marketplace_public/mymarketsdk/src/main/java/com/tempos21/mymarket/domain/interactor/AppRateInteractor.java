package com.tempos21.mymarket.domain.interactor;

import android.content.Context;

import com.tempos21.mymarket.data.repository.client.AppRateListServiceRepository;
import com.tempos21.mymarket.domain.dto.request.AppRateListRequest;
import com.tempos21.mymarket.domain.dto.response.AppRateListResponse;
import com.tempos21.mymarket.sdk.model.AppRate;

import java.util.List;

public class AppRateInteractor extends Interactor<AppRateListRequest, AppRateListResponse> {

    public AppRateInteractor(Context context) {
        super(context);
    }

    @Override
    protected AppRateListResponse perform(AppRateListRequest input) throws Exception {
        return getAppRateList(input);
    }

    private AppRateListResponse getAppRateList(AppRateListRequest input) throws Exception {
        List<AppRate> appRateList;
        AppRateListServiceRepository serviceRepository = new AppRateListServiceRepository(getContext());
        AppRateListResponse mainResponse = new AppRateListResponse();
        appRateList = serviceRepository.get(input);
        mainResponse.appRateList = appRateList;
        return mainResponse;
    }
}