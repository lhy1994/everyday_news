package com.lhycode.news.utils;

import com.lhycode.news.PreferenceUtils;

import android.content.Context;

public class CacheUtil {

	public static String getCache(Context context,String key) {
		return PreferenceUtils.getString(context, key, "");
	}
	public static void setCache(Context context,String key,String value) {
		PreferenceUtils.setString(context, key, value);
	}
}
