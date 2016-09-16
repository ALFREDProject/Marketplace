package com.tempos21.mymarket.data.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.mapper.UserVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.login.UserVO;
import com.tempos21.mymarket.domain.dto.request.LoginRequest;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.MyMarketPreferences;
import com.tempos21.mymarket.sdk.util.ErrorHandler;
import com.tempos21.mymarket.sdk.util.Logger;
import com.tempos21.mymarket.sdk.util.Strings;

import retrofit.client.Header;
import retrofit.client.Response;

public class LoginClient extends Client<LoginRequest, ClientResponse<UserVO>> {

    public LoginClient(Context context) {
        super(context);
    }

    @Override
    protected ClientResponse<UserVO> request(LoginRequest request) throws Exception {
        MarketClientService service = MarketClientAdapter.getInstance(getContext()).getService();
        MyMarket market = MyMarket.getInstance();
        Response serviceResponse = service.login(request.userName, request.userPassword, market.getDeviceId(), String.valueOf(market.getPlatformVersion()), market.getPlatform().getName());
        if (MyMarket.getInstance().isDebug()) {
            logResponse(serviceResponse);
        }
        for (Header header : serviceResponse.getHeaders()) {
            if (header.getName() != null && header.getName().equals("Set-Cookie")) {
                MarketClientAdapter.getInstance(getContext()).setSessionId(header.getValue());
                MyMarketPreferences.getInstance(getContext()).setString(MyMarketPreferences.MY_MARKET_PREFERENCE_KEY_SESSION_ID, header.getValue());
                break;
            }
        }
        ClientResponse<UserVO> response = UserVOMapper.mapResponse(Strings.convertStreamToString(serviceResponse.getBody().in(), "UTF-8"));
        if ("OK".equals(response.status)) {
            return response;
        } else {
            ErrorHandler.getInstance(getContext()).checkClientResponseError(response);
            throw new Exception("Login fail");
        }
    }

    private void logResponse(Response response) throws Exception {
        Logger.logE("<RESPONSE status=\"" + response.getStatus() + "\">");
        Logger.logE("<RESPONSE reason=\"" + response.getReason() + "\">");
        Logger.logE("<RESPONSE url=\"" + response.getUrl() + "\">");
        for (Header header : response.getHeaders()) {
            Logger.logE("<HEADER name=\"" + header.getName() + "\" value=\"" + header.getValue() + "\">");
        }
        Logger.logE("<RESPONSE body=\"" + Strings.convertStreamToString(response.getBody().in(), "UTF-8") + "\">");
    }
}
