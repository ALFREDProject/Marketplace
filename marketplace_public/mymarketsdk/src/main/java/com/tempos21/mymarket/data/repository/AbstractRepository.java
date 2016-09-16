package com.tempos21.mymarket.data.repository;

import android.content.Context;

public abstract class AbstractRepository<I, O> {

    private final Context context;

    public AbstractRepository(Context context) {
        this.context = context;
    }

    public abstract O get(I input) throws Exception;

    public abstract void store(O output) throws Exception;

    public Context getContext() {
        return context;
    }
}
