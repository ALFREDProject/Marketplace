package com.tempos21.mymarket.data.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.mapper.AppRateVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.app_rate.AppRateListVO;
import com.tempos21.mymarket.domain.dto.request.AppRateListRequest;
import com.tempos21.mymarket.sdk.util.Strings;

import retrofit.client.Response;

public class GetAppRateListClient extends Client<AppRateListRequest, ClientResponse<AppRateListVO>> {

    public GetAppRateListClient(Context context) {
        super(context);
    }

    @Override
    protected ClientResponse<AppRateListVO> request(AppRateListRequest request) throws Exception {
        MarketClientService service = MarketClientAdapter.getInstance(getContext()).getService();
        Response serviceResponse = service.getAppRateList(request.id);
        ClientResponse<AppRateListVO> response = AppRateVOMapper.mapResponse(Strings.convertStreamToString(serviceResponse.getBody().in(), "UTF-8"));
        if ("OK".equals(response.status)) {
            return response;
        } else {
            throw new Exception("Application rate list fail");
        }
    }
}
