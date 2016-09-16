package com.tempos21.market.util;

import eu.alfred.api.market.MarketPlace;

public class MarketPlaceHelper {

    private static MarketPlaceHelper instance;
    public MarketPlace marketPlace;

    public static MarketPlaceHelper getInstance() {
        if (instance == null) {
            instance = new MarketPlaceHelper();
        }
        return instance;
    }


    public void setMarketPlace(MarketPlace mPlace) {
        if (mPlace != null && this.marketPlace == null) {
            this.marketPlace = mPlace;
        }
    }
}
