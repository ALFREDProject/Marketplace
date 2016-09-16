package com.tempos21.mymarket.data.repository.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.GetAppRateListClient;
import com.tempos21.mymarket.data.client.mapper.AppRateVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.app_rate.AppRateListVO;
import com.tempos21.mymarket.data.repository.AbstractRepository;
import com.tempos21.mymarket.domain.dto.request.AppRateListRequest;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.AppRate;

import java.util.List;

public class AppRateListServiceRepository extends AbstractRepository<AppRateListRequest, List<AppRate>> {

    public AppRateListServiceRepository(Context context) {
        super(context);
    }

    @Override
    public List<AppRate> get(AppRateListRequest input) throws Exception {
        try {
            GetAppRateListClient client = new GetAppRateListClient(getContext());
            ClientResponse<AppRateListVO> response = client.execute(input);
            AppRateVOMapper mapper = new AppRateVOMapper();
            return mapper.toModel(response.data.appRateVO);
        } catch (Exception e) {
            if (MyMarket.getInstance().isDebug()) {
                e.printStackTrace();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void store(List<AppRate> output) throws Exception {
        throw new UnsupportedOperationException();
    }
}
