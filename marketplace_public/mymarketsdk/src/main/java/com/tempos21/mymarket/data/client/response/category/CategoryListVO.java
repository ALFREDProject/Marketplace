package com.tempos21.mymarket.data.client.response.category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryListVO {

  @Expose
  @SerializedName("categoryResponse")
  public List<CategoryVO> categoryVO;
}
