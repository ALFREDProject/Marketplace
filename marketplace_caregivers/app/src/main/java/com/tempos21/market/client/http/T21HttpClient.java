/**
 *
 */
package com.tempos21.market.client.http;

import android.content.Context;

import com.tempos21.market.util.TLog;
import com.worldline.alfredo.R;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Enumeration;

/**
 * @author A501063
 */
public class T21HttpClient extends DefaultHttpClient { // ThreadSafeClientConnManager


    public static final int REGISTRATION_TIMEOUT = 30 * 1000; // ms
    private static final String CLASS_NAME = T21HttpClient.class
            .getSimpleName() + " :: ";
    private static T21HttpClient mInstance;
    private static KeyStore trusted = null;

    /**
     *
     */
    private T21HttpClient() {
        super();
    }

    /**
     * @param params
     */
    private T21HttpClient(HttpParams params) {
        super(params);
    }

    /**
     * @param conman
     * @param params
     */
    private T21HttpClient(ClientConnectionManager conman, HttpParams params) {
        super(conman, params);
    }

    public static T21HttpClient getInstance(Context context) {

        TLog.d(CLASS_NAME + "getInstance()");

        if (mInstance == null) {
            mInstance = getNewHttpClient(context);
        }

        final HttpParams params = mInstance.getParams();
        HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, REGISTRATION_TIMEOUT);
        ConnManagerParams.setTimeout(params, REGISTRATION_TIMEOUT);
        mInstance.setParams(params);
        return mInstance;
    }

    /**
     * Load & Merge KeyStores (System & Application)
     *
     * @param context
     * @return true if keystore is loaded correctly
     */
    private static boolean loadKeyStore(Context context) {
        TLog.d(CLASS_NAME + "getNewHttpClient()");
        if (trusted != null) {
            TLog.d(CLASS_NAME + "loadKeyStore(): already loaded");
            return true;
        }

        try {
            InputStream in = null;

            // Load default system keystore
            final KeyStore sysTrusted = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            try {
                in = new BufferedInputStream(new FileInputStream(
                        System.getProperty("javax.net.ssl.trustStore")));
                sysTrusted.load(in, null); // no password is "changeit"
            } finally {
                if (in != null) {
                    in.close();
                    in = null;
                }
            }

            // Load application keystore & merge with system
            try {
                final KeyStore appTrusted = KeyStore.getInstance("BKS");
                final long t = System.currentTimeMillis();
                in = context.getResources().openRawResource(R.raw.cacerts);
                appTrusted.load(in, null); // no password is "changeit"
                for (Enumeration<String> e = appTrusted.aliases(); e
                        .hasMoreElements(); ) {
                    final String alias = e.nextElement();
                    final KeyStore.Entry entry = appTrusted.getEntry(alias,
                            null);
                    sysTrusted.setEntry(alias + ":" + t, entry, null);
                }
            } finally {
                if (in != null) {
                    in.close();
                    in = null;
                }
            }

            trusted = sysTrusted;
            TLog.d(CLASS_NAME + "loadKeyStore(): OK");
            return true;
        } catch (Exception e) {
            TLog.d(CLASS_NAME + "loadKeyStore(): ERROR" + e.getMessage());

        }
        return false;
    }

    /**
     * HttpClient que se traga todos lo certificado. Es un WorkAround para
     * evitar los problemas de algunos móviles con las conexiones HTTPS
     *
     * @return
     */
    private static T21HttpClient getNewHttpClient(Context context) {
        TLog.d(CLASS_NAME + "getNewHttpClient()");
        try {
            if (loadKeyStore(context)) {

                HttpParams params = new BasicHttpParams();
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

                SSLSocketFactory sf = new SSLSocketFactory(trusted);
                sf.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory
                        .getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                        params, registry);

                return new T21HttpClient(ccm, params);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            return new T21HttpClient();
        }
    }

}
