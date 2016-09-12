package com.tempos21.mymarket.domain.interactor;

import android.content.Context;

import com.tempos21.mymarket.data.repository.client.LanguageListServiceRepository;
import com.tempos21.mymarket.data.database.dao.CacheDAO;
import com.tempos21.mymarket.data.repository.database.LanguageListDatabaseRepository;
import com.tempos21.mymarket.domain.dto.response.LanguageListResponse;
import com.tempos21.mymarket.sdk.model.CacheName;
import com.tempos21.mymarket.sdk.model.Language;

import java.util.List;

public class LanguageInteractor extends Interactor<Void, LanguageListResponse> {

  private static final String CACHE_NAME = CacheName.LANGUAGE.getName();

  public LanguageInteractor(Context context) {
    super(context);
  }

  @Override
  protected LanguageListResponse perform(Void input) throws Exception {
    return getLanguageList();
  }

  private LanguageListResponse getLanguageList() throws Exception {
    List<Language> languageList;
    LanguageListDatabaseRepository databaseRepository = new LanguageListDatabaseRepository(getContext());
    LanguageListServiceRepository serviceRepository = new LanguageListServiceRepository(getContext());
    LanguageListResponse mainResponse = new LanguageListResponse();
    CacheDAO cacheDAO = new CacheDAO(getContext());
    if (cacheDAO.existsItemInCache(CACHE_NAME)) {
      if (cacheDAO.itemHasExpired(CACHE_NAME)) {
        languageList = serviceRepository.get(null);
        databaseRepository.store(languageList);
        cacheDAO.updateCacheItem(CACHE_NAME, System.currentTimeMillis());
      } else {
        languageList = databaseRepository.get(null);
      }
    } else {
      languageList = serviceRepository.get(null);
      databaseRepository.store(languageList);
      cacheDAO.putItemIntoCache(CACHE_NAME, System.currentTimeMillis());
    }
    mainResponse.languageList = languageList;
    return mainResponse;
  }
}