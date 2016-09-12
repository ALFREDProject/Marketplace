package com.tempos21.mymarket.data.repository.database;

import android.content.Context;

import com.tempos21.mymarket.data.database.dao.LanguageDAO;
import com.tempos21.mymarket.data.repository.AbstractRepository;
import com.tempos21.mymarket.sdk.model.Language;

import java.util.List;

public class LanguageListDatabaseRepository extends AbstractRepository<Void, List<Language>> {

  public LanguageListDatabaseRepository(Context context) {
    super(context);
  }

  @Override
  public List<Language> get(Void input) throws Exception {
    LanguageDAO dao = new LanguageDAO(getContext());
    return dao.getList();
  }

  @Override
  public void store(List<Language> output) throws Exception {
    LanguageDAO dao = new LanguageDAO(getContext());
    dao.clearAll();
    dao.putList(output);
  }
}
