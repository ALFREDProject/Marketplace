package com.tempos21.mymarket.sdk.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.Editable;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Strings {

  public static boolean isEmpty(CharSequence string) {
    return isEmpty(string.toString());
  }

  public static boolean isEmpty(Editable string) {
    return isEmpty(string.toString());
  }

  public static boolean isEmpty(String string) {
    return TextUtils.isEmpty(string.trim());
  }

  public static String getStringFromFile(Context context, String file, String charset) throws Exception {
    AssetManager manager = context.getAssets();
    InputStream stream = manager.open(file);
    String string = convertStreamToString(stream, charset);
    stream.close();
    return string;
  }

  public static String convertStreamToString(InputStream stream, String charset) throws Exception {
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
    StringBuilder builder = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      builder.append(line).append("\n");
    }
    builder.delete(builder.length() - 1, builder.length());
    return builder.toString();
  }
}
