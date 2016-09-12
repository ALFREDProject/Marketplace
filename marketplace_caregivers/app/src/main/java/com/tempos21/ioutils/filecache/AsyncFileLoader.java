package com.tempos21.ioutils.filecache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class AsyncFileLoader extends AsyncTask<String, Void, Bitmap> {

    private OnFileLoadedListener onFileLoadedListener;
    private String originalUrl;
    private String id;
    private String folderName;
    private Context context;

    public AsyncFileLoader(Context context, String folderName) {
        this.setFolderName(folderName);
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        originalUrl = params[0];
        id = params[1];
        FileLoader loader = new FileLoader(context, folderName);
        Bitmap bm = loader.getFile(originalUrl);
        return bm;
    }

    protected void onPostExecute(Bitmap bitmap) {
        if (onFileLoadedListener != null)
            onFileLoadedListener.onFileLoaded(id, bitmap);
    }

    public void setOnFileLoadedListener(OnFileLoadedListener onFileLoadedListener) {
        this.onFileLoadedListener = onFileLoadedListener;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public interface OnFileLoadedListener {

        /**
         * @param id
         * @param image
         */
        void onFileLoaded(String id, Bitmap bitmap);

    }
}