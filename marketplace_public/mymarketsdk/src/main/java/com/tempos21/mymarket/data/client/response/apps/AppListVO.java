package com.tempos21.mymarket.data.client.response.apps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppListVO {

    @Expose
    @SerializedName("app")
    public List<AppVO> appVO;
}
