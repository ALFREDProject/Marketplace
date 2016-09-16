package com.tempos21.market.ui.view;

public interface GenericView<S> {

    void showProgress();

    void hideProgress();

    void onViewSuccess(S list);

    void onViewError(long id, Exception e);
}
