package com.lhycode.news.utils;

import com.lhycode.news.R;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class MyBitmapUtils {
	private NetCacheUtils netCacheUtils;
	private LocalCacheutils localCacheutils;
	private MemoryCacheUtils memoryCacheUtils;

	public MyBitmapUtils() {
		memoryCacheUtils=new MemoryCacheUtils();
		localCacheutils = new LocalCacheutils();
		this.netCacheUtils = new NetCacheUtils(memoryCacheUtils,localCacheutils);
	}

	public void display(ImageView imageView, String url) {

		Bitmap bitmap=null;
		imageView.setImageResource(R.drawable.news_logo);
		bitmap=memoryCacheUtils.getBitmapFromMemory(url);
		if(bitmap!=null){
			imageView.setImageBitmap(bitmap);
			System.out.println("���ڴ��ȡͼƬ��������������������");
			return;
		}
		bitmap = localCacheutils.getBitmapFromLocal(url);
		if(bitmap!=null){
			imageView.setImageBitmap(bitmap);
			System.out.println("�ӱ��ض�ȡͼƬ��������������������");
			memoryCacheUtils.setBitmapToMemory(bitmap, url);
			return;
		}
		//���绺��
		netCacheUtils.getBitmapFromNet(imageView, url);
	}
}
