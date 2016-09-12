package com.tempos21.mymarket.domain.dto.request;

public class InstalledAppRequest {

  private String jsonArray;

  public String getJsonArray() {
    return jsonArray;
  }

  public void setJsonArray(String jsonArray) {
    this.jsonArray = "{\"change\":" + jsonArray + "}";
  }
}