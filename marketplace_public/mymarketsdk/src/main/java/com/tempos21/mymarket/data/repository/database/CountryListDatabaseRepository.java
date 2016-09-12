package com.tempos21.mymarket.data.repository.database;

import android.content.Context;

import com.tempos21.mymarket.data.database.dao.CountryDAO;
import com.tempos21.mymarket.data.repository.AbstractRepository;
import com.tempos21.mymarket.sdk.model.Country;

import java.util.List;

public class CountryListDatabaseRepository extends AbstractRepository<Void, List<Country>> {

  public CountryListDatabaseRepository(Context context) {
    super(context);
  }

  @Override
  public List<Country> get(Void input) throws Exception {
    CountryDAO dao = new CountryDAO(getContext());
    return dao.getList();
  }

  @Override
  public void store(List<Country> output) throws Exception {
    CountryDAO dao = new CountryDAO(getContext());
    dao.clearAll();
    dao.putList(output);
  }
}
