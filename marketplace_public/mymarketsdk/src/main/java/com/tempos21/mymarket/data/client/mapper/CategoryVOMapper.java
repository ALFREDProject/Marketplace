package com.tempos21.mymarket.data.client.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.category.CategoryListVO;
import com.tempos21.mymarket.data.client.response.category.CategoryVO;
import com.tempos21.mymarket.sdk.model.Category;

import java.lang.reflect.Type;

public class CategoryVOMapper extends BaseVOMapper<CategoryVO, Category> {

  public static ClientResponse<CategoryListVO> mapResponse(String json) {
    Type type = new TypeToken<ClientResponse<CategoryListVO>>() {}.getType();
    return new Gson().fromJson(json, type);
  }

  @Override
  public Category toModel(CategoryVO vo) {
    Category category = new Category();
    category.id = vo.id;
    category.name = vo.name;
    category.image = vo.image;
    return category;
  }

  @Override
  public CategoryVO toVO(Category model) {
    return null;
  }
}
