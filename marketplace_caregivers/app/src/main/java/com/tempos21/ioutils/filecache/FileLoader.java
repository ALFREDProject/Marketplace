package com.tempos21.ioutils.filecache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tempos21.market.client.http.T21HttpClientWithSSL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;


public class FileLoader {
    boolean hasCache = true;
    Context context;
    private CacheStorage cacheStorage;

    public FileLoader(Context context, String name) {
        try {
            this.cacheStorage = new CacheStorage(context, name);
        } catch (IOException e) {
            hasCache = false;
        }
        this.context = context;
    }

    public Bitmap getFile(String uri) {
        Bitmap result = null;
        if (hasCache && isFileInCache(uri))
            result = getFileFromCache(uri);

        else {
            result = getFileFromUri(uri);

            if (hasCache && result != null) {
                putFileInCache(result, uri);
            }
        }
        return result;
    }

    private Bitmap getFileFromUri(String uri) {
        Bitmap bitmap = null;
        byte[] in = null;
        try {
            in = OpenHttpConnection(uri);
            if (in == null) {
                return null;
            }
            bitmap = BitmapFactory.decodeByteArray(in, 0, in.length);

        } catch (IOException e1) {
        }
        return bitmap;
    }

    private byte[] OpenHttpConnection(String strURL) throws IOException {
        byte[] bytes = null;
        T21HttpClientWithSSL client = T21HttpClientWithSSL.getInstance();


        try {
            HttpGet request = new HttpGet(strURL);
            HttpResponse response = client.execute(request);


            if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
                bytes = EntityUtils.toByteArray(response.getEntity());
            }
        } catch (Exception ex) {
            return null;
        }
        return bytes;
    }

    private Bitmap getFileFromCache(String uri) {
        Bitmap result = null;
        if (isFileInCache(uri)) {

            String filename = getFileFullPath(uri);
            try {
                result = BitmapFactory.decodeFile(filename);
            } catch (OutOfMemoryError e) {
//				File file = new File(uri);
//				result = outMemory(file);
            }

        }
        return result;
    }

    private void putFileInCache(Bitmap bitmap, String uri) {

        String filePath = getFileFullPath(uri);

        FileOutputStream out;
        try {
            out = new FileOutputStream(filePath);

            if (bitmap.hasAlpha()) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean isFileInCache(String uri) {

        return new File(getFileFullPath(uri)).exists();
    }

    private String getFileFullPath(String uri) {
        String cacheFolder = cacheStorage.getCacheFolder().getPath();
        String filename = URLEncoder.encode(uri);
        String file = cacheFolder;
        if (!file.matches(".*/"))
            file += "/";
        file += filename;
        return file;
    }


//	private Bitmap outMemory(File f){
//	    try {
//	        //Decode image size
//	        BitmapFactory.Options o = new BitmapFactory.Options();
//	        o.inJustDecodeBounds = true;
//	        BitmapFactory.decodeStream(new FileInputStream(f),null,o);
//
//	        //The new size we want to scale to
//	        final int REQUIRED_SIZE=70;
//
//	        //Find the correct scale value. It should be the power of 2.
//	        int scale=1;
//	        while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
//	            scale*=2;
//
//	        //Decode with inSampleSize
//	        BitmapFactory.Options o2 = new BitmapFactory.Options();
//	        o2.inSampleSize=scale;
//	        return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
//	    } catch (FileNotFoundException e) {}
//	    return null;
//	}

}
