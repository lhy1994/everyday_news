package com.lhycode.news;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {
	public static final String NAME = "config";

	public static boolean getBoolean(Context context,String key,boolean defaultvalue) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				context.MODE_PRIVATE);
		return preferences.getBoolean(key,defaultvalue);
	}
	public static void setBoolean(Context context,String key,boolean value) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				context.MODE_PRIVATE);
		preferences.edit().putBoolean(key, value).commit();

	}
	public static String getString(Context context,String key,String defaultvalue) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				context.MODE_PRIVATE);
		return preferences.getString(key, defaultvalue);
	}
	public static void setString(Context context,String key,String value) {
		SharedPreferences preferences = context.getSharedPreferences(NAME,
				context.MODE_PRIVATE);
		preferences.edit().putString(key, value).commit();

	}
}
