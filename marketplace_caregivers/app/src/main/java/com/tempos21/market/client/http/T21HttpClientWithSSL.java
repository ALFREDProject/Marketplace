package com.tempos21.market.client.http;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.worldline.alfredo.R;

import org.apache.http.HttpVersion;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;


/**
 * @author A519130
 *         This class implements a custom httpClient, which you can enter a file with SSL certificates or use their own system
 */

public class T21HttpClientWithSSL extends DefaultHttpClient {

    public static final int REGISTRATION_TIMEOUT = 30 * 1000; // ms
    private static final String TAG = "T21HttpClientWithSSL";
    private static KeyStore systemTrusted = null;
    private static KeyStore customTrusted = null;
    private static T21HttpClientWithSSL mInstance;
    private static String keystoreType = "System";


    /**
     * Constructor
     */
    private T21HttpClientWithSSL() {
        super();
    }


    /**
     * Constructor 2
     *
     * @param conman MultiThreadedHttpConnectionManager
     * @param params HttpParams
     */
    private T21HttpClientWithSSL(ThreadSafeClientConnManager conman, HttpParams params) {
        super(conman, params);
    }


    /**
     * This void loads default system keyStore and custom keyStore
     *
     * @param context AStartUp context
     */

    public static void loadKeyStore(Context context) {
        InputStream in = null;

        try {
            if (Build.VERSION.SDK_INT < 14) {
                // Load default system keyStore for SO version < 4.0
                systemTrusted = KeyStore.getInstance(KeyStore.getDefaultType());

                // no password is "changeit"
                in = new BufferedInputStream(new FileInputStream(System.getProperty("javax.net.ssl.trustStore")));
                systemTrusted.load(in, null);
            } else {
                // Load default system keyStore for SO version >= 4.0
                systemTrusted = KeyStore.getInstance("AndroidCAStore");
                systemTrusted.load(null, null);
            }

//			enumerateCertificates(systemTrusted);

            // Load custom keyStore
            customTrusted = KeyStore.getInstance("BKS");

            // no password is "changeit"
            in = context.getResources().openRawResource(R.raw.cacerts);
            customTrusted.load(in, null);

//			enumerateCertificates(customTrusted);
//			// Extract certificates of custom keyStore and merge with the system 
//			final long t = System.currentTimeMillis();
//			for (Enumeration<String> e = customTrusted.aliases(); e.hasMoreElements();) {
//				final String alias = e.nextElement();
//				KeyStore.Entry entry;
//				entry = customTrusted.getEntry(alias, null);
//				//systemTrusted.setEntry(alias + ":" + t, entry, null);
//			}
//			
//	        customTrusted = systemTrusted;
            Log.i(TAG, "loadKeyStore: OK");

            getInstance();

        } catch (Exception e) {
            Log.e("HttpsClient", "loadKeyStore: ERROR", e);

        } finally {
            // Close buffer
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (IOException e) {
                    Log.e("HttpsClient", "BufferedInput: ERROR", e);
                }
            }
        }
    }


    /**
     * This function create an instance of HttpClient
     *
     * @return HttpClient
     */

    public static T21HttpClientWithSSL getInstance() {
        if (mInstance == null) {
            mInstance = getNewHttpClient();
        }

        // Get and set parameters for this client
        final HttpParams params = mInstance.getParams();
        HttpConnectionParams.setConnectionTimeout(params, REGISTRATION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(params, REGISTRATION_TIMEOUT);
        ConnManagerParams.setTimeout(params, REGISTRATION_TIMEOUT);
        ((DefaultHttpClient) mInstance).setParams(params);

        return mInstance;
    }


    /**
     * This function create a DefaultHttpClient customized for accept http and https connections with systems or custom SSL certificates
     *
     * @return HttpClient
     */

    private static T21HttpClientWithSSL getNewHttpClient() {
        try {
            // Get and Set parameters for this client
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            X509HostnameVerifier sslsocket;

            if (customTrusted != null) {
                sslsocket = SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
                keystoreType = "Custom";

                if (systemTrusted == null) {
                    systemTrusted = customTrusted;
                }
            } else {
                if (systemTrusted != null) {
                    sslsocket = SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
                    customTrusted = systemTrusted;
                } else {
                    sslsocket = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                    if (Build.VERSION.SDK_INT < 14) {
                        // Load default system keyStore for SO version < 4.0
                        systemTrusted = KeyStore.getInstance(KeyStore.getDefaultType());
                        // no password is "changeit"
                        InputStream in = new BufferedInputStream(new FileInputStream(System.getProperty("javax.net.ssl.trustStore")));
                        systemTrusted.load(in, null);
                    } else {
                        // Load default system keyStore for SO version >= 4.0
                        systemTrusted = KeyStore.getInstance("AndroidCAStore");
                        systemTrusted.load(null, null);
                    }
                    customTrusted = systemTrusted;
                }
            }
//	        enumerateCertificates(systemTrusted);

            // Create sockets with keyStores
            SSLSocketFactory sf = new SSLSocketFactory(systemTrusted);
            SSLSocketFactory sf1 = new SSLSocketFactory(customTrusted);

            // Set type of verification host name
            sf.setHostnameVerifier(sslsocket);
            sf1.setHostnameVerifier(sslsocket);

            // Create scheme with http and https protocols
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));
            registry.register(new Scheme("https", sf1, 443));

            // Create connection manager
            ThreadSafeClientConnManager ccm = new ThreadSafeClientConnManager(params, registry);

            Log.i("HttpsClient", "getNewHttpClient: OK");
            return new T21HttpClientWithSSL(ccm, params);

        } catch (Exception e) {
            Log.e("HttpsClient", "getNewHttpClient: ERROR", e);
            return new T21HttpClientWithSSL();
        }
    }


    /**
     * This function indicate KeyStore type in use (system or custom)
     *
     * @return String
     */

    public static String getTypeKeystore() {
        return keystoreType;
    }


//	/**
//	 * This function enumerate the certificates of a specific KeyStore
//	 * 
//	 * @param trusted
//	 * 					specific keyStore with certificates
//	 */
//	private static void enumerateCertificates(KeyStore trusted){
//		Enumeration<String> aliases;
//		ArrayList<KeyStore.Entry> certificates = new ArrayList<KeyStore.Entry>();
//			
//		try {
//			aliases = trusted.aliases();
//			while (aliases.hasMoreElements()) {
//				String alias = aliases.nextElement();
//				certificates.add(trusted.getEntry(alias, null));
//			}
//		} catch (KeyStoreException e1) {
//			e1.printStackTrace();
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		} catch (UnrecoverableEntryException e) {
//			e.printStackTrace();
//		}
//			
//		String certs = certificates.toString();
//	}
}
