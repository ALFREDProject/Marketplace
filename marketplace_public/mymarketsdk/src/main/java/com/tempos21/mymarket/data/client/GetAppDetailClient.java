package com.tempos21.mymarket.data.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.mapper.AppDetailVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.apps.AppDetailVO;
import com.tempos21.mymarket.domain.dto.request.AppDetailRequest;
import com.tempos21.mymarket.sdk.util.ErrorHandler;
import com.tempos21.mymarket.sdk.util.Strings;

import retrofit.client.Response;

public class GetAppDetailClient extends Client<AppDetailRequest, ClientResponse<AppDetailVO>> {

    public GetAppDetailClient(Context context) {
        super(context);
    }

    @Override
    protected ClientResponse<AppDetailVO> request(AppDetailRequest request) throws Exception {
        MarketClientService service = MarketClientAdapter.getInstance(getContext()).getService();
        Response serviceResponse = service.getAppById(request.id);
        ClientResponse<AppDetailVO> response = AppDetailVOMapper.mapResponse(Strings.convertStreamToString(serviceResponse.getBody().in(), "UTF-8"));
        if ("OK".equals(response.status)) {
            return response;
        } else {
            ErrorHandler.getInstance(getContext()).checkClientResponseError(response);
            throw new Exception("Application detail fail");
        }
    }
}
