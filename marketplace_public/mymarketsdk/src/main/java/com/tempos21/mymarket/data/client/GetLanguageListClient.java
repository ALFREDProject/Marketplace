package com.tempos21.mymarket.data.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.mapper.LanguageVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.language.LanguageListVO;
import com.tempos21.mymarket.sdk.util.ErrorHandler;
import com.tempos21.mymarket.sdk.util.Strings;

import retrofit.client.Response;

public class GetLanguageListClient extends Client<Void, ClientResponse<LanguageListVO>> {

    public GetLanguageListClient(Context context) {
        super(context);
    }

    @Override
    protected ClientResponse<LanguageListVO> request(Void request) throws Exception {
        MarketClientService service = MarketClientAdapter.getInstance(getContext()).getService();
        Response serviceResponse = service.getLanguageList();
        ClientResponse<LanguageListVO> response = LanguageVOMapper.mapResponse(Strings.convertStreamToString(serviceResponse.getBody().in(), "UTF-8"));
        if ("OK".equals(response.status)) {
            return response;
        } else {
            ErrorHandler.getInstance(getContext()).checkClientResponseError(response);
            throw new Exception("Language list loading fail");
        }
    }
}
