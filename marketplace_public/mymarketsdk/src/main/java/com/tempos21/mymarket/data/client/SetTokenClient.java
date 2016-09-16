package com.tempos21.mymarket.data.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.mapper.TokenVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.push.TokenVO;
import com.tempos21.mymarket.domain.dto.request.TokenRequest;
import com.tempos21.mymarket.sdk.util.Strings;

import retrofit.client.Response;

public class SetTokenClient extends Client<TokenRequest, ClientResponse<TokenVO>> {

    public SetTokenClient(Context context) {
        super(context);
    }

    @Override
    protected ClientResponse<TokenVO> request(TokenRequest request) throws Exception {
        MarketClientService service = MarketClientAdapter.getInstance(getContext()).getService();
        Response serviceResponse = service.setToken(request.token);
        ClientResponse<TokenVO> response = TokenVOMapper.mapResponse(Strings.convertStreamToString(serviceResponse.getBody().in(), "UTF-8"));
        if ("OK".equals(response.status)) {
            return response;
        } else {
            throw new Exception("Set Token fail");
        }
    }
}
