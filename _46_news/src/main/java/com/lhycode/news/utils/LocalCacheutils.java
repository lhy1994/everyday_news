package com.lhycode.news.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.R.string;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class LocalCacheutils {

	private static final String PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/news_photo";

	public Bitmap getBitmapFromLocal(String url) {
		String fileName;
		try {
			fileName = MD5Encoder.encode(url);
			File file=new File(PATH,fileName);
			if (file.exists()) {
				return BitmapFactory.decodeStream(new FileInputStream(file));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setBitmapToLocal(Bitmap bitmap, String url) {

		try {
			String fileName=MD5Encoder.encode(url);
			File file=new File(PATH,fileName);
			File parentFile = file.getParentFile();
			if(!parentFile.exists()){
				parentFile.mkdirs();
			}
			bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
