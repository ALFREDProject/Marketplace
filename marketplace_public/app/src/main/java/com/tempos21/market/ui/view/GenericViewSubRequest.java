package com.tempos21.market.ui.view;

public interface GenericViewSubRequest<R, S> extends GenericView<R> {

  void onViewSuccessSubRequest(S response);

  void onViewErrorSubRequest(long id, Exception e);
}
