package com.lhycode.news.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class NetCacheUtils {

    LocalCacheutils localCacheutils;
    MemoryCacheUtils memoryCacheUtils;

    public NetCacheUtils(MemoryCacheUtils memoryCacheUtils, LocalCacheutils localCacheutils) {
        this.localCacheutils = localCacheutils;
        this.memoryCacheUtils = memoryCacheUtils;
    }

    /**
     * @param args
     */
    public void getBitmapFromNet(ImageView imageView, String url) {

        new BitmapTask().execute(imageView, url);
    }

    class BitmapTask extends AsyncTask<Object, Void, Bitmap> {

        private ImageView imageView;
        private String url;

        @Override
        protected Bitmap doInBackground(Object... params) {
            imageView = (ImageView) params[0];
            url = (String) params[1];
            imageView.setTag(url);
            return downloadBitmap(url);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                String bindurl = (String) imageView.getTag();
                if (bindurl.equals(url)) {
                    imageView.setImageBitmap(result);
                    localCacheutils.setBitmapToLocal(result, url);
                    memoryCacheUtils.setBitmapToMemory(result, url);
                }
            }
        }

    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection connection = null;
        try {
            System.out.println(url);
            URL url2 = new URL(url);
            connection = (HttpURLConnection) url2.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                InputStream inputStream = connection.getInputStream();
                //ͼƬѹ��
//				Options options = new BitmapFactory.Options();
//				options.inSampleSize=2;
//				options.inPreferredConfig=Bitmap.Config.RGB_565;
//				Bitmap bitmap = BitmapFactory.decodeStream(inputStream,null,options);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }
}
