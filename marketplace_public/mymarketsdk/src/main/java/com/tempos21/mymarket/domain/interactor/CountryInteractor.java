package com.tempos21.mymarket.domain.interactor;

import android.content.Context;

import com.tempos21.mymarket.data.database.dao.CacheDAO;
import com.tempos21.mymarket.data.repository.client.CountryListServiceRepository;
import com.tempos21.mymarket.data.repository.database.CountryListDatabaseRepository;
import com.tempos21.mymarket.domain.dto.response.CountryListResponse;
import com.tempos21.mymarket.sdk.model.CacheName;
import com.tempos21.mymarket.sdk.model.Country;

import java.util.List;

public class CountryInteractor extends Interactor<Void, CountryListResponse> {

    private static final String CACHE_NAME = CacheName.COUNTRY.getName();

    public CountryInteractor(Context context) {
        super(context);
    }

    @Override
    protected CountryListResponse perform(Void input) throws Exception {
        return getCountryList();
    }

    private CountryListResponse getCountryList() throws Exception {
        List<Country> categoryList;
        CountryListDatabaseRepository databaseRepository = new CountryListDatabaseRepository(getContext());
        CountryListServiceRepository serviceRepository = new CountryListServiceRepository(getContext());
        CountryListResponse mainResponse = new CountryListResponse();
        CacheDAO cacheDAO = new CacheDAO(getContext());
        if (cacheDAO.existsItemInCache(CACHE_NAME)) {
            if (cacheDAO.itemHasExpired(CACHE_NAME)) {
                categoryList = serviceRepository.get(null);
                databaseRepository.store(categoryList);
                cacheDAO.putItemIntoCache(CACHE_NAME, System.currentTimeMillis());
            } else {
                categoryList = databaseRepository.get(null);
            }
        } else {
            categoryList = serviceRepository.get(null);
            databaseRepository.store(categoryList);
            cacheDAO.putItemIntoCache(CACHE_NAME, System.currentTimeMillis());
        }
        mainResponse.countryList = categoryList;
        return mainResponse;
    }
}