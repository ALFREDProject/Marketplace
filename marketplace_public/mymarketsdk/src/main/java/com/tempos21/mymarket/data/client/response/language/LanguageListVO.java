package com.tempos21.mymarket.data.client.response.language;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LanguageListVO {

    @Expose
    @SerializedName("languageResponse")
    public List<LanguageVO> languageVO;
}
