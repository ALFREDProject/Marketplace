package com.tempos21.mymarket.data.repository.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.GetCountryListClient;
import com.tempos21.mymarket.data.client.mapper.CountryVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.country.CountryData;
import com.tempos21.mymarket.data.repository.AbstractRepository;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.Country;

import java.util.List;

public class CountryListServiceRepository extends AbstractRepository<Void, List<Country>> {

    public CountryListServiceRepository(Context context) {
        super(context);
    }

    @Override
    public List<Country> get(Void input) throws Exception {
        try {
            GetCountryListClient client = new GetCountryListClient(getContext());
            ClientResponse<CountryData> response = client.execute(null);
            CountryVOMapper mapper = new CountryVOMapper();
            return mapper.toModel(response.data.countryVO);
        } catch (Exception e) {
            if (MyMarket.getInstance().isDebug()) {
                e.printStackTrace();
            }
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void store(List<Country> output) throws Exception {
        throw new UnsupportedOperationException();
    }
}
