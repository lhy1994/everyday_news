package com.lhycode.news.utils;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class MemoryCacheUtils {

	// private HashMap<String, SoftReference<Bitmap>> memHashMap=new
	// HashMap<String,SoftReference<Bitmap> >();
	private LruCache<String, Bitmap> lruCache;
	public MemoryCacheUtils(){
		long maxMemory = Runtime.getRuntime().maxMemory();
		lruCache=new LruCache<String, Bitmap>((int) (maxMemory/8)){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
		};
		
	}
	public Bitmap getBitmapFromMemory(String url) {
		// SoftReference<Bitmap> softReference = memHashMap.get(url);
		// if (softReference!=null) {
		// Bitmap bitmap = softReference.get();
		// return bitmap;
		// }
		// return null;
		return lruCache.get(url);
	}

	public void setBitmapToMemory(Bitmap bitmap, String url) {
		// SoftReference<Bitmap> softReference=new
		// SoftReference<Bitmap>(bitmap);
		// memHashMap.put(url, softReference);
		lruCache.put(url, bitmap);
	}
}
