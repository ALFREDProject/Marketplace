package com.tempos21.market.device;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import com.tempos21.market.Constants;
import com.tempos21.market.client.http.T21HttpClientWithSSL;
import com.worldline.alfredo.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;

public class BinaryInstaller {

    public static String downloadOnSDcard(Context context, String directory, String appId, String versionId, int appVersion, int test) throws IOException {
        T21HttpClientWithSSL client = T21HttpClientWithSSL.getInstance();
        File outputFile = new File("");

        if (test == 1) {
            outputFile = new File(directory, "app" + versionId + ".apk");
            if (outputFile.exists()) {
                outputFile.delete();
            }
        } else {
            outputFile = new File(directory, "app" + versionId + ".apk");
            if (outputFile.exists()) {
                outputFile.delete();
            }
        }

        FileOutputStream fos = new FileOutputStream(outputFile);
        InputStream is = null;

        HttpGet request = new HttpGet();
        if (test == 1) {
            request = new HttpGet(Constants.GET_BINARY + versionId);
        } else {
            request = new HttpGet(Constants.GET_BINARY + versionId);
        }

        HttpResponse response = client.execute(request);

        if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }

        byte[] buffer = new byte[1024];
        int len1 = 0;
        while ((len1 = is.read(buffer)) != -1)
            fos.write(buffer, 0, len1);

        is.close();
        fos.close();
        return outputFile.getAbsolutePath();
    }

    public static String saveOnSDcard(Context context, String directory, String appId, String versionId, int appVersion, int test, String apkDatStr) throws IOException {
        //T21HttpClientWithSSL client = T21HttpClientWithSSL.getInstance();
        File outputFile = new File("");

        if (test == 1) {
            outputFile = new File(directory, "app" + versionId + ".apk");
            if (outputFile.exists()) {
                outputFile.delete();
            }
        } else {
            outputFile = new File(directory, "app" + versionId + ".apk");
            if (outputFile.exists()) {
                outputFile.delete();
            }
        }

        FileOutputStream fos = new FileOutputStream(outputFile);
        //InputStream is = null;

        /*HttpGet request = new HttpGet();
        if (test == 1) {
            request = new HttpGet(Constants.GET_BINARY + versionId);
        } else {
            request = new HttpGet(Constants.GET_BINARY + versionId);
        }

        HttpResponse response = client.execute(request);

        if (response.getStatusLine().getStatusCode() == HttpURLConnection.HTTP_OK) {
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }

        byte[] buffer = new byte[1024];
        int len1 = 0;
        while ((len1 = is.read(buffer)) != -1)
            fos.write(buffer, 0, len1);
        */

        byte[] buffer = apkDatStr.getBytes();
        fos.write(buffer, 0, buffer.length);

        //is.close();
        fos.close();

        return outputFile.getAbsolutePath();
    }

    public static void installApplication(Context context, String appId, String versionId, int appVersion, int test, String apkDataStr) throws Exception {

        String directory = getCacheDirectory(context);
        String apkPath = saveOnSDcard(context, directory, appId, versionId, appVersion, test, apkDataStr);
        final PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath, 0);
        if (packageInfo == null) {
            throw new Exception();
        }
        Uri packageURI = Uri.parse("package:" + packageInfo.packageName);

        Intent intent = new Intent(Intent.ACTION_VIEW, packageURI);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void installApplication(Context context, String appId, String versionId, int appVersion, int test) throws Exception {
        // Hacer startResultActivity y en HomeFragment borrar el apk del SDCard
        // si se ha instalado

        String directory = getCacheDirectory(context);
        String apkPath = downloadOnSDcard(context, directory, appId, versionId, appVersion, test);
        final PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(apkPath, 0);
        if (packageInfo == null) {
            throw new Exception();
        }
        Uri packageURI = Uri.parse("package:" + packageInfo.packageName);

        Intent intent = new Intent(Intent.ACTION_VIEW, packageURI);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void installApplication(Context context, String path) throws Exception {
        // Hacer startResultActivity y en HomeFragment borrar el apk del SDCard
        // si se ha instalado

        final PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(path, 0);
        if (packageInfo == null) {
            throw new Exception();
        }
        Uri packageURI = Uri.parse("package:" + packageInfo.packageName);

        Intent intent = new Intent(Intent.ACTION_VIEW, packageURI);
        intent.setDataAndType(Uri.fromFile(new File(path)),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void unInstallApplication(Context context, String packageName) {
        if (isAppInstalled(context, packageName)) {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
            context.startActivity(uninstallIntent);
        }
    }

    private static String getCacheDirectory(Context context) {
        return Environment.getExternalStorageDirectory() + "/" + context.getResources().getString(R.string.app_name) + "/";
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                if (packageInfo.applicationInfo.packageName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isAppInstalled(Context context, String packageName, int version) {
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            ApplicationInfo info = packageInfo.applicationInfo;
            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                if (info.packageName.equals(packageName) && packageInfo.versionCode == version) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getVersion(Context context, String packageName) {


        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            ApplicationInfo info = packageInfo.applicationInfo;
            if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                if (info.packageName.equals(packageName)) {
                    return packageInfo.versionCode;
                }
            }
        }
        return 0;

    }

    public static void deleteApksCache(Context context) {
        File[] apks;
        File directory = new File(getCacheDirectory(context));
        if (directory.exists()) {
            apks = directory.listFiles();

            for (File apk : apks) {
                if (apk.getName().endsWith(".apk"))
                    apk.delete();
            }
        }
    }


}
