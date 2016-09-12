package com.tempos21.mymarket.data.repository.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.GetCategoryListClient;
import com.tempos21.mymarket.data.client.mapper.CategoryVOMapper;
import com.tempos21.mymarket.data.client.response.ClientResponse;
import com.tempos21.mymarket.data.client.response.category.CategoryListVO;
import com.tempos21.mymarket.data.repository.AbstractRepository;
import com.tempos21.mymarket.sdk.MyMarket;
import com.tempos21.mymarket.sdk.model.Category;

import java.util.List;

public class CategoryListServiceRepository extends AbstractRepository<Void, List<Category>> {

  public CategoryListServiceRepository(Context context) {
    super(context);
  }

  @Override
  public List<Category> get(Void input) throws Exception {
    try {
      GetCategoryListClient client = new GetCategoryListClient(getContext());
      ClientResponse<CategoryListVO> response = client.execute(null);
      CategoryVOMapper mapper = new CategoryVOMapper();
      return mapper.toModel(response.data.categoryVO);
    } catch (Exception e) {
      if (MyMarket.getInstance().isDebug()) {
        e.printStackTrace();
      }
      throw new Exception(e.getMessage());

    }
  }

  @Override
  public void store(List<Category> output) throws Exception {
    throw new UnsupportedOperationException();
  }
}
