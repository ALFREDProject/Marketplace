package com.tempos21.mymarket.domain.interactor;

import android.content.Context;

import com.tempos21.mymarket.data.repository.client.CategoryListServiceRepository;
import com.tempos21.mymarket.data.database.dao.CacheDAO;
import com.tempos21.mymarket.data.repository.database.CategoryListDatabaseRepository;
import com.tempos21.mymarket.domain.dto.response.CategoryListResponse;
import com.tempos21.mymarket.sdk.model.CacheName;
import com.tempos21.mymarket.sdk.model.Category;

import java.util.List;

public class CategoryInteractor extends Interactor<Void, CategoryListResponse> {

  private static final String CACHE_NAME = CacheName.CATEGORY.getName();

  public CategoryInteractor(Context context) {
    super(context);
  }

  @Override
  protected CategoryListResponse perform(Void input) throws Exception {
    return getCategoryList();
  }

  private CategoryListResponse getCategoryList() throws Exception {
    List<Category> categoryList;
    CategoryListDatabaseRepository databaseRepository = new CategoryListDatabaseRepository(getContext());
    CategoryListServiceRepository serviceRepository = new CategoryListServiceRepository(getContext());
    CategoryListResponse mainResponse = new CategoryListResponse();
    CacheDAO cacheDAO = new CacheDAO(getContext());
    if (cacheDAO.existsItemInCache(CACHE_NAME)) {
      if (cacheDAO.itemHasExpired(CACHE_NAME)) {
        categoryList = serviceRepository.get(null);
        databaseRepository.store(categoryList);
        cacheDAO.updateCacheItem(CACHE_NAME, System.currentTimeMillis());
      } else {
        categoryList = databaseRepository.get(null);
      }
    } else {
      categoryList = serviceRepository.get(null);
      databaseRepository.store(categoryList);
      cacheDAO.putItemIntoCache(CACHE_NAME, System.currentTimeMillis());
    }
    mainResponse.categoryList = categoryList;
    return mainResponse;
  }
}