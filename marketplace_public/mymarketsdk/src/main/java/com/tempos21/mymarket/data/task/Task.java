package com.tempos21.mymarket.data.task;

import android.content.Context;
import android.os.Handler;

import java.util.Random;

/**
 * Base class for executable tasks
 */
public abstract class Task<I, O> {

    private static Random rnd = new Random(System.currentTimeMillis());
    private final Context context;
    private boolean executing = false;
    private Handler handler;
    private OnTaskResultListener<O> onTaskResultListener;

    public Task(Context context) {
        this.context = context;
    }

    /**
     * Executes the task and return the result if succesful
     *
     * @return The result of the task
     * @throws Exception If the task finished with an error
     */
    public O execute(final I input) throws Exception {
        executing = true;
        O t = perform(input);
        executing = false;
        return t;
    }

    /**
     * Executes the task in another thread. Result will be delivered via listener.
     *
     * @return An id to recognize the task when listener invoked
     */
    public long executeAsync(final I input) {
        executing = true;
        handler = new Handler();
        final long id = rnd.nextInt(Integer.MAX_VALUE);
        JobExecutor.getInstance().execute(new Runnable() {

            @Override
            public void run() {
                try {
                    finishSuccess(id, perform(input));
                } catch (Exception e) {
                    finishFailure(id, e);
                }
                executing = false;
            }
        });
        return id;
    }

    protected abstract O perform(I input) throws Exception;

    protected void finishSuccess(final long id, final O result) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (onTaskResultListener != null) {
                    onTaskResultListener.onTaskSuccess(id, result);
                }
            }
        });

    }

    protected void finishFailure(final long id, final Exception error) {
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (onTaskResultListener != null) {
                    onTaskResultListener.onTaskFailure(id, error);
                }
            }
        });
    }

    public OnTaskResultListener<O> getOnTaskResultListener() {
        return onTaskResultListener;
    }

    public void setClientResultListener(OnTaskResultListener<O> onTaskResultListener) {
        this.onTaskResultListener = onTaskResultListener;
    }

    protected Context getContext() {
        return context;
    }

    public boolean isExecuting() {
        return executing;
    }

    public interface OnTaskResultListener<T> {

        void onTaskSuccess(long id, T result);

        void onTaskFailure(long id, Exception error);
    }
}
