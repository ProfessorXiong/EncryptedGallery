package com.xiong.lockedgallery;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

/**
 * 查看网络上图片的activity
 * 
 * @author Administrator
 *
 */
public class WebImageActivity extends FragmentActivity {
	private ViewPager pager;
	private List<Picture> pics;
	private MyFragmentAdapter adapter;
	private Random random;
	private TextView textPage;
	private static final int ALL_PAGES = 500;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_image);

		pics = new ArrayList<Picture>();
		textPage = (TextView) findViewById(R.id.web_activity_text_page);
		pager = (ViewPager) findViewById(R.id.web_activity_pager1);
		adapter = new MyFragmentAdapter(getSupportFragmentManager(),pics);
		pager.setAdapter(adapter);
		// 从网络上加载数据填充pics
		loadImageFromInternet(1);
		random = new Random();
	}
	/**
	 * 切换下一批图片
	 * @param view
	 */
	public void nextPictures(View view){
		loadImageFromInternet(random.nextInt(ALL_PAGES));
	}
	/**
	 * 从网络上加载图片的 方法
	 */
	private void loadImageFromInternet(final int page) {
		// 获取美女图片的接口地址
		String url = "http://route.showapi.com/197-1?showapi_appid=20593&showapi_sign=40e8e95964dc46fbbe146795fb172342&num=10&page=" + page;
		JsonObjectRequest request = new JsonObjectRequest(url, null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						pics.clear();
						pics.addAll(parseJson(response));
						adapter.notifyDataSetChanged();
						textPage.setText("当前是第" + page + "批");
					}
				}, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						System.out.println("从网络上获取美女列表数据失败");
					}
				});
		MyApplication.getRequestQueue().add(request);
	}

	/*
	 * 把json数据解析出来的办法
	 */
	public List<Picture> parseJson(JSONObject response) {
		List<Picture> pics = new ArrayList<Picture>();
		try {
			JSONObject object = response.getJSONObject("showapi_res_body");
			JSONArray array = object.getJSONArray("newslist");
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				Picture pic = new Picture();
				pic.title = obj.optString("title");
				pic.picUrl = obj.optString("picUrl");
				pic.url = obj.optString("url");
				pics.add(pic);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return pics;
	}

	class MyFragmentAdapter extends FragmentStatePagerAdapter {
		private List<Picture> pics;
		// 构造方法
		public MyFragmentAdapter(FragmentManager fm,List<Picture> pics) {
			super(fm);
			this.pics = pics;
		}

		@Override
		public Fragment getItem(int arg0) {
			ImageFragment fragment = new ImageFragment();
			return fragment;
		}
		
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			ImageFragment fragment = (ImageFragment) super.instantiateItem(container, position);
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("pic", pics.get(position));
			// 设置framgent的参数
			fragment.setArguments(mBundle);
			return fragment;
		}

		@Override
		public int getCount() {
			return pics.size();
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return PagerAdapter.POSITION_NONE;
		}
		
		
	}
}
