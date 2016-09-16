package com.tempos21.mymarket.data.client.response.app_rate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppRateListVO {

    @Expose
    @SerializedName("appRateResponse")
    public List<AppRateVO> appRateVO;
}

