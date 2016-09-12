package com.tempos21.ioutils.filecache;

import android.content.Context;
import android.graphics.Bitmap;

import com.tempos21.ioutils.filecache.AsyncFileLoader.OnFileLoadedListener;

import java.util.LinkedList;

public class FileLoaderQueue implements OnFileLoadedListener {
    private static LinkedList<Work> loaderQueue;
    private String folderName;
    private boolean processRunning;
    private OnFileLoadedListener onFileLoadedListener;
    private Context context;

    public FileLoaderQueue(Context context, String folderName) {
        loaderQueue = new LinkedList<Work>();
        this.folderName = folderName;
        this.context = context;
    }

    public static void clearQueue() {
        if (loaderQueue != null) {
            loaderQueue.clear();
        }
    }

    public void loadFile(String id, String url) {
        Work work = new Work(id, url);
        loaderQueue.add(work);
        if (!isProcessRunning()) {
            runNext();
        }
    }

    private void runNext() {
        if (loaderQueue.size() > 0) {
            setProcessRunning(true);
            AsyncFileLoader asyncLoader = new AsyncFileLoader(context, folderName);
            asyncLoader.setOnFileLoadedListener(this);
            Work work = loaderQueue.poll();
            asyncLoader.execute(work.url, work.id);
        } else {
            setProcessRunning(false);
        }
    }

    public boolean isProcessRunning() {
        return processRunning;
    }

    public void setProcessRunning(boolean processRunning) {
        this.processRunning = processRunning;
    }

    public void onFileLoaded(String id, Bitmap bitmap) {
        if (onFileLoadedListener != null) {
            onFileLoadedListener.onFileLoaded(id, bitmap);
        }
        runNext();

    }

    public void setOnFileLoadedListener(
            OnFileLoadedListener onFileLoadedListener) {
        this.onFileLoadedListener = onFileLoadedListener;
    }

    public interface OnFileLoadedListener {

        /**
         * @param id
         * @param image
         */
        void onFileLoaded(String id, Bitmap bitmap);

    }

    private class Work {
        String id;
        String url;

        public Work(String id, String url) {
            this.url = url;
            this.id = id;
        }

    }

}
