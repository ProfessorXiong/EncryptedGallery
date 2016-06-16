package com.xiong.lockedgallery;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 放置图片的fragment
 * @author Administrator
 *
 */
public class ImageFragment extends Fragment implements OnLongClickListener {
	private Picture pic;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//获取参数
		pic = (Picture) getArguments().getSerializable("pic");
		View view = inflater.inflate(R.layout.image_fragment_view, container, false);
		//填充标题
		((TextView)view.findViewById(R.id.image_fragment_text_title)).setText(pic.title);
		//填充网址
		((TextView)view.findViewById(R.id.image_fragment_text_url)).setText(pic.url);
		//加载图片
		ImageView image = (ImageView) view.findViewById(R.id.image_fragment_image1);
		ImageListener listener = ImageLoader.getImageListener(image, R.drawable.ic_launcher, R.drawable.ic_launcher);
		MyApplication.getImageLoader().get(pic.picUrl, listener);
		//设置长按事件
		image.setOnLongClickListener(this);
		return view;
	}
	/**
	 * 长按后将图片保存到本地
	 */
	@Override
	public boolean onLongClick(View v) {
		MyApplication.saveBitmap(((BitmapDrawable)((ImageView)v).getDrawable()).getBitmap(), getActivity().getApplicationContext());
		return true;
	}
}
