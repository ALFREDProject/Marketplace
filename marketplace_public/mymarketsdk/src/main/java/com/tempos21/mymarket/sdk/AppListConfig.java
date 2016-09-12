package com.tempos21.mymarket.sdk;

import com.tempos21.mymarket.sdk.constant.MyMarketConstants;
import com.tempos21.mymarket.sdk.model.AppListSorting;

public class AppListConfig {

  private String name;
  private int start;
  private int elements;
  private String sorting;
  private int categoryId;
  private int countryId;
  private int languageId;
  private boolean hasPromoImage;
  private String words;

  private AppListConfig(Builder builder) {
    name = builder.name;
    start = builder.start;
    elements = builder.elements;
    sorting = builder.sorting;
    categoryId = builder.categoryId;
    countryId = builder.countryId;
    languageId = builder.languageId;
    hasPromoImage = builder.hasPromoImage;
    words = builder.words;
  }

  public String getName() {
    return name;
  }

  public int getStart() {
    return start;
  }

  public int getElements() {
    return elements;
  }

  public String getSorting() {
    return sorting;
  }

  public int getCategoryId() {
    return categoryId;
  }

  public int getCountryId() {
    return countryId;
  }

  public int getLanguageId() {
    return languageId;
  }

  public boolean hasPromoImage() {
    return hasPromoImage;
  }

  public String getSearchQuery() {
    return words;
  }

  public static class Builder {

    private String name;
    private int start = MyMarketConstants.APP_LIST_START; // Starting element
    private int elements = MyMarketConstants.APP_LIST_ELEMENTS; // Number of elements per page
    private String sorting = AppListSorting.RATING.getName();
    private int categoryId = 0;
    private int countryId = 0;
    private int languageId = 0;
    private boolean hasPromoImage = false;
    private String words;

    public Builder() {}

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setStart(int start) {
      this.start = start;
      return this;
    }

    public Builder setElements(int elements) {
      this.elements = elements;
      return this;
    }

    public Builder setSorting(String sorting) {
      this.sorting = sorting;
      return this;
    }

    public Builder setCategoryId(int categoryId) {
      this.categoryId = categoryId;
      return this;
    }

    public Builder setCountryId(int countryId) {
      this.countryId = countryId;
      return this;
    }

    public Builder setLanguageId(int languageId) {
      this.languageId = languageId;
      return this;
    }

    public Builder setHasPromoImage(boolean hasPromoImage) {
      this.hasPromoImage = hasPromoImage;
      return this;
    }

    public Builder setSearchQuery(String query) {
      this.words = query;
      return this;
    }

    public AppListConfig build() {
      if (name == null) {
        throw new RuntimeException("AppList name is mandatory");
      }
      return new AppListConfig(this);
    }
  }
}
