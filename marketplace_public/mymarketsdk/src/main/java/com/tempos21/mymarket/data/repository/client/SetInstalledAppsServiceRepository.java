package com.tempos21.mymarket.data.repository.client;

import android.content.Context;

import com.tempos21.mymarket.data.client.SetInstalledAppsClient;
import com.tempos21.mymarket.data.repository.AbstractRepository;
import com.tempos21.mymarket.domain.dto.request.InstalledAppRequest;
import com.tempos21.mymarket.sdk.MyMarket;

public class SetInstalledAppsServiceRepository extends AbstractRepository<InstalledAppRequest, Void> {

    public SetInstalledAppsServiceRepository(Context context) {
        super(context);
    }

    @Override
    public Void get(InstalledAppRequest input) throws Exception {
        try {
            SetInstalledAppsClient client = new SetInstalledAppsClient(getContext());
            client.execute(input);
            return null;
        } catch (Exception e) {
            if (MyMarket.getInstance().isDebug()) {
                e.printStackTrace();
            }
            throw new Exception(e.getMessage());

        }
    }

    @Override
    public void store(Void output) throws Exception {
        throw new UnsupportedOperationException();
    }
}
