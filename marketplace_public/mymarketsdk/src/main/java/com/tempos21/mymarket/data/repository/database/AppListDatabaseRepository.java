package com.tempos21.mymarket.data.repository.database;

import android.content.Context;

import com.tempos21.mymarket.data.database.dao.AppDAO;
import com.tempos21.mymarket.data.repository.AbstractRepository;
import com.tempos21.mymarket.domain.dto.request.AppListRequest;
import com.tempos21.mymarket.sdk.AbstractAppList;
import com.tempos21.mymarket.sdk.AppList;

public class AppListDatabaseRepository extends AbstractRepository<AppListRequest, com.tempos21.mymarket.sdk.model.AppList> {

    private final AbstractAppList list;

    public AppListDatabaseRepository(Context context, AbstractAppList list) {
        super(context);
        this.list = list;
    }

    @Override
    public com.tempos21.mymarket.sdk.model.AppList get(AppListRequest input) throws Exception {
        AppDAO dao = new AppDAO(getContext());
        return dao.getList(input.name);
    }

    @Override
    public void store(com.tempos21.mymarket.sdk.model.AppList output) throws Exception {
        AppDAO dao = new AppDAO(getContext());
        if (list.getState() == AppList.State.LIST || list.getState() == AppList.State.REFRESH) {
            dao.delete(output.name);
            dao.putList(output);
        } else {
            dao.appendList(output);
        }
    }
}
