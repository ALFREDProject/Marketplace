package com.tempos21.mymarket.sdk.util;

import eu.alfred.api.market.MarketPlace;

public class MarketPlaceHelper2 {

    private static MarketPlaceHelper2 instance;
    public MarketPlace marketPlace;

    public static MarketPlaceHelper2 getInstance() {
        if (instance == null) {
            instance = new MarketPlaceHelper2();
        }
        return instance;
    }


    public void setMarketPlace(MarketPlace mPlace) {
        if (mPlace != null && this.marketPlace == null) {
            this.marketPlace = mPlace;
        }
    }
}
