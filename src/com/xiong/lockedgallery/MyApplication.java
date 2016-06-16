package com.xiong.lockedgallery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.LruCache;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;

public class MyApplication extends Application {
	private static RequestQueue mQueue;
	private static Random random ;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mQueue = Volley.newRequestQueue(getApplicationContext());
		random = new Random();
	}
	/**
	 * 请求队列
	 * @return
	 */
	public static RequestQueue getRequestQueue(){
		return mQueue;
	}
	/**
	 * 获得一个Imageloader
	 * @return
	 */
	public static ImageLoader getImageLoader(){
		ImageLoader loader = new ImageLoader(mQueue, new ImageCache() {
			LruCache<String,Bitmap> imgCache = new LruCache<String, Bitmap>(10 * 1024 * 1024){

				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();
				}
				
			};
			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				imgCache.put(url, bitmap);
			}
			
			@Override
			public Bitmap getBitmap(String url) {
				return imgCache.get(url);
			}
			
		});
		
		return loader;
	}
	/**
	 * 把bitmap保存到数据库的方法
	 * @param bitmap
	 */
	public static void saveBitmap(Bitmap response,Context context){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		response.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		SQLiteDatabase db = MySqliteHelper
				.getWriteableDB(context);
		ContentValues values = new ContentValues();

		values.put(MySqliteHelper.COLUMN_CONTENT, bos.toByteArray());
		values.put(MySqliteHelper.COLUMN_PATH, Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/"
				+ random.nextInt(10000) + "web.jpg");
		db.insert(MySqliteHelper.TABLE_PICTURE, "", values);
		db.close();
		try {
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
