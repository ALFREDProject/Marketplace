package com.tempos21.mymarket.domain.dto.request;

import com.tempos21.mymarket.sdk.constant.MyMarketConstants;

public class AppListRequest {

  public String words = "";
  public String name;
  public int start = MyMarketConstants.APP_LIST_START;
  public int elements = MyMarketConstants.APP_LIST_ELEMENTS;
  public String sorting = MyMarketConstants.APP_LIST_SORTING;
  public int countryId = 0;
  public int categoryId = 0;
  public int languageId = 0;
  public boolean hasPromoImage = false;
}
