package com.xiong.lockedgallery;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.app.Application;

public class MyApplication extends Application {
	private static RequestQueue mQueue;

	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mQueue = Volley.newRequestQueue(getApplicationContext());
	}
	
	public static RequestQueue getRequestQueue(){
		return mQueue;
	}
}
