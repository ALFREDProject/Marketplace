package com.tempos21.mymarket.data.repository.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.SetAppRateClient;
import com.tempos21.mymarket.data.client.mapper.RateVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.app_rate.RateVO;
import com.tempos21.mymarket.data.repository.AbstractRepository;
import com.tempos21.mymarket.domain.dto.request.RateRequest;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.Rate;

public class RateServiceRepository extends AbstractRepository<RateRequest, Rate> {

    public RateServiceRepository(Context context) {
        super(context);
    }

    @Override
    public Rate get(RateRequest input) throws Exception {
        try {
            SetAppRateClient client = new SetAppRateClient(getContext());
            ClientResponse<RateVO> response = client.execute(input);
            RateVOMapper mapper = new RateVOMapper();
            return mapper.toModel(response.data);
        } catch (Exception e) {
            if (MyMarket.getInstance().isDebug()) {
                e.printStackTrace();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void store(Rate output) throws Exception {
        throw new UnsupportedOperationException();
    }
}
