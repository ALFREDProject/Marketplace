package com.tempos21.ioutils.filecache;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

public class CacheStorage {

    private static final int MB = 1024 * 1024;

    private Context context;
    private String cacheName;
    private File cacheFolder;
    private int maxCacheSize = 10 * MB;

    public CacheStorage(Context context, String name) throws IOException {
        this.context = context;
        this.cacheName = name;
        createStorage();
        setMaxCacheSize(maxCacheSize);
        cleanCache();
    }

    public File getCacheFolder() {
        return cacheFolder;
    }

    public void setMaxCacheSize(int newSize) {
        maxCacheSize = newSize;

    }

    private void createStorage() throws IOException {
        String baseFolder = getBaseFolder().toString();
        if (!baseFolder.endsWith("/")) {
            baseFolder += "/";
        }
        cacheFolder = new File(baseFolder + cacheName);
        cacheFolder.mkdirs();
        (new File(baseFolder + cacheName + "/.nomedia")).createNewFile();
    }

    private File getBaseFolder() {
        if (isExternalStorageAvailable()) {
            return Environment.getExternalStorageDirectory();
        } else {
            return context.getCacheDir();
        }
    }

    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public void cleanCache() {
        boolean candelete = true;
        File toDelete = null;
        while (candelete && isCacheFull()) {
            toDelete = lastFileModified(cacheFolder);
            if (toDelete == null) {
                candelete = false;
            } else {
                candelete = toDelete.delete();

            }
        }
    }

    private boolean isCacheFull() {
        return getTotalSize(cacheFolder) > maxCacheSize;
    }

    public long getTotalSize(File dir) {
        long result = 0;
        File[] fileList = dir.listFiles();

        for (int i = 0; i < fileList.length; i++) {
            // Recursive call if it's a directory
            if (fileList[i].isDirectory()) {
                result += getTotalSize(fileList[i]);
            } else {
                // Sum the file size in bytes
                result += fileList[i].length();
            }
        }
        return result; // return the file size
    }

    private File lastFileModified(File dir) {

        File[] files = dir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile() && !file.isHidden();
            }
        });
        long lastMod = Long.MAX_VALUE;
        File result = null;
        for (File file : files) {
            if (file.lastModified() < lastMod) {
                result = file;
                lastMod = file.lastModified();
            }
        }
        return result;
    }

}
