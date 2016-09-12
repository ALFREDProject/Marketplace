package com.tempos21.mymarket.data.repository.database;

import android.content.Context;

import com.tempos21.mymarket.data.database.dao.CategoryDAO;
import com.tempos21.mymarket.data.repository.AbstractRepository;
import com.tempos21.mymarket.sdk.model.Category;

import java.util.List;

public class CategoryListDatabaseRepository extends AbstractRepository<Void, List<Category>> {

  public CategoryListDatabaseRepository(Context context) {
    super(context);
  }

  @Override
  public List<Category> get(Void input) throws Exception {
    CategoryDAO dao = new CategoryDAO(getContext());
    return dao.getList();

  }

  @Override
  public void store(List<Category> output) throws Exception {
    CategoryDAO dao = new CategoryDAO(getContext());
    dao.clearAll();
    dao.putList(output);
  }
}
