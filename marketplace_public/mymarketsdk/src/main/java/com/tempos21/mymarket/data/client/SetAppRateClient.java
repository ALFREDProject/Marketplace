package com.tempos21.mymarket.data.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.mapper.RateVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.app_rate.RateVO;
import com.tempos21.mymarket.domain.dto.request.RateRequest;
import com.tempos21.mymarket.sdk.util.Strings;

import retrofit.client.Response;

public class SetAppRateClient extends Client<RateRequest, ClientResponse<RateVO>> {

    public SetAppRateClient(Context context) {
        super(context);
    }

    @Override
    protected ClientResponse<RateVO> request(RateRequest request) throws Exception {
        MarketClientService service = MarketClientAdapter.getInstance(getContext()).getService();
        Response serviceResponse = service.setRating(request.id, request.title, request.rate, request.author, request.body, request.versionNumber);
        ClientResponse<RateVO> response = RateVOMapper.mapResponse(Strings.convertStreamToString(serviceResponse.getBody().in(), "UTF-8"));
        if ("OK".equals(response.status)) {
            return response;
        } else {
            throw new Exception("Rate App fail");
        }
    }
}
