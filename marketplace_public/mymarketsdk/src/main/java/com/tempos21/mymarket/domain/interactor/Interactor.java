package com.tempos21.mymarket.domain.interactor;

import android.content.Context;

import com.tempos21.mymarket.domain.task.Task;

/**
 * Base class for executable tasks
 */
public abstract class Interactor<I, O> extends Task<I, O> {

    public Interactor(Context context) {
        super(context);
    }
}
