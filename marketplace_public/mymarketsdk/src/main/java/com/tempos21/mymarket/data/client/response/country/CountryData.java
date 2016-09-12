package com.tempos21.mymarket.data.client.response.country;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryData {

  @Expose
  @SerializedName("countryResponse")
  public List<CountryVO> countryVO;
}
