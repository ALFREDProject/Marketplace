package com.tempos21.mymarket.data.client.response.set_installed_apps;

import com.google.gson.annotations.Expose;

public class DatumVO {

  @Expose
  private Integer id;
  @Expose
  private String result;

  /**
   * @return The id
   */
  public Integer getId() {
    return id;
  }

  /**
   * @param id The id
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * @return The result
   */
  public String getResult() {
    return result;
  }

  /**
   * @param result The result
   */
  public void setResult(String result) {
    this.result = result;
  }

}
