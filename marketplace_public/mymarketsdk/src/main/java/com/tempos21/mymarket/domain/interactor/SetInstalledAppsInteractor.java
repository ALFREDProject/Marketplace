package com.tempos21.mymarket.domain.interactor;

import android.content.Context;

import com.tempos21.mymarket.data.repository.client.SetInstalledAppsServiceRepository;
import com.tempos21.mymarket.domain.dto.request.InstalledAppRequest;

public class SetInstalledAppsInteractor extends Interactor<InstalledAppRequest, Void> {

  public SetInstalledAppsInteractor(Context context) {
    super(context);
  }

  @Override
  protected Void perform(InstalledAppRequest input) throws Exception {
    setInstalledApps(input);
    return null;
  }

  private void setInstalledApps(InstalledAppRequest input) throws Exception {
    SetInstalledAppsServiceRepository setInstalledAppsServiceRepository = new SetInstalledAppsServiceRepository(getContext());
    setInstalledAppsServiceRepository.get(input);
  }
}