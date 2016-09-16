package com.tempos21.mymarket.data.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.mapper.CountryVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.country.CountryData;
import com.tempos21.mymarket.sdk.util.Strings;

import retrofit.client.Response;

public class GetCountryListClient extends Client<Void, ClientResponse<CountryData>> {

    public GetCountryListClient(Context context) {
        super(context);
    }

    @Override
    protected ClientResponse<CountryData> request(Void request) throws Exception {
        MarketClientService service = MarketClientAdapter.getInstance(getContext()).getService();
        Response serviceResponse = service.getCountryList();
        ClientResponse<CountryData> response = CountryVOMapper.mapResponse(Strings.convertStreamToString(serviceResponse.getBody().in(), "UTF-8"));
        if ("OK".equals(response.status)) {
            return response;
        } else {
            throw new Exception("Country list loading fail");
        }
    }
}
