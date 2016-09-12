package com.tempos21.market;

import com.tempos21.market.configuration.Configuration;

public class AlfredoMarketConfiguration implements Configuration {

    @Override
    public String getBaseUrl() {
        return Constants.BASE_URL;
    }


}
