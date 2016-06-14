package com.xiong.lockedgallery;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private List<Picture> pictures;
	private GridView grid;
	private MyGridAdapter adapter;
	private Handler mHander;
	private static final int REFRESH = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		setTitle(this.toString());
		bindViews();
		
		mHander = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case REFRESH:
					refreshItems();
					break;
				default:
					super.handleMessage(msg);
					break;
				}
			}
		};
		
		pictures = new ArrayList<Picture>();
		adapter = new MyGridAdapter();
		grid.setAdapter(adapter);
		//条目的短按点击事件
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(HomeActivity.this,ShowImageActivity.class);
					intent.putExtra("_id", pictures.get(position)._id);
				startActivity(intent);
			}
		});
		//条目的长按点击事件
		grid.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(HomeActivity.this);
					dialog.setTitle("选择操作");
					dialog.setContentView(R.layout.home_activity_dialog1);
					dialog.findViewById(R.id.home_acitivity_dialolg1_btn_export).setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
					dialog.findViewById(R.id.home_acitivity_dialolg1_btn_delete).setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							deletePictureFromDB(pictures.get(position));
						}
					});
				dialog.show();
				return true;
			}
		});

	}

	@Override
	protected void onResume() {
		refreshItems();
		super.onResume();
	}

	private void bindViews() {
		grid = (GridView) findViewById(R.id.home_activity_grid);
	}
	
	private void deletePictureFromDB(final Picture pic){
		new Thread(){
			public void run() {
				SQLiteDatabase db = MySqliteHelper.getWriteableDB(HomeActivity.this);
					db.delete(MySqliteHelper.TABLE_PICTURE, MySqliteHelper.COLUMN_ID + "=?", new String[]{pic._id + ""});
				db.close();
				mHander.sendEmptyMessage(REFRESH);
			};
		}.start();
	}
	/**
	 * 点击退出按钮时的逻辑
	 */
	private long lastPressedBackButtonTime = 0;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (System.currentTimeMillis() - lastPressedBackButtonTime < 1000) {
			finish();
			System.exit(0);
		} else {
			lastPressedBackButtonTime = System.currentTimeMillis();
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 导入按钮的点击事件
	 * 
	 * @param view
	 */
	public void importImages(View view) {
		
		//弹出一个对话框提示输入图片路径
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("输入图片路径");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 取得输入的 路径
					String picPath = (String)((EditText)((AlertDialog)dialog).findViewById(R.id.home_activity_dialog2_edit1)).getText().toString();
					final File file = new File(picPath);
					//判断文件是否存在
					if(file.isFile() && file.exists()){
						//判断文件的后缀
						if(file.getAbsolutePath().endsWith(".jpg")||file.getAbsolutePath().endsWith(".png")||file.getAbsolutePath().endsWith(".bmp")){
							new Thread(){
							public void run() {
								byte[] content = getByteFromFile(file);
								SQLiteDatabase db = MySqliteHelper.getWriteableDB(HomeActivity.this);
								ContentValues values = new ContentValues();
								
								values.put(MySqliteHelper.COLUMN_CONTENT, content);
								values.put(MySqliteHelper.COLUMN_PATH, file.getAbsolutePath());
								db.insert(MySqliteHelper.TABLE_PICTURE, "", values);
								db.close();
								
								mHander.sendEmptyMessage(REFRESH);
							};
						}.start();
						}else{
							Toast.makeText(HomeActivity.this, "文件格式不支持", Toast.LENGTH_SHORT).show();
						}
						
					}else{
						Toast.makeText(HomeActivity.this, "文件不存在!", Toast.LENGTH_SHORT).show();
					}
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
		View view1 = LayoutInflater.from(this).inflate(R.layout.home_activity_dialog2, null,false);
		EditText edit = (EditText)view1.findViewById(R.id.home_activity_dialog2_edit1);
		edit.setText(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
		builder.setView(view1);
		//弹出对话框
		builder.show();
		
	}

	/**
	 * 把文件转换成byte数组的方法
	 * 
	 * @param file
	 * @return
	 */
	public byte[] getByteFromFile(File file) {
		// 文件输入流
		FileInputStream fis = null;
		// 字节输出流
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		// 字节数组
		byte[] contents = null;
		try {
			fis = new FileInputStream(file);
			int len;
			byte[] buffer = new byte[1024];
			while ((len = fis.read(buffer)) != -1) {
				bos.write(buffer);
			}
			contents = bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			// 关闭流
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return contents;
	}
	/**
	 * 刷新列表
	 */
	public void refreshItems() {

		// 首先清空pictures
		pictures.clear();
		SQLiteDatabase db = MySqliteHelper.getReadableDB(this);
		Cursor cursor = db.query(MySqliteHelper.TABLE_PICTURE,
				new String[] { MySqliteHelper.COLUMN_ID,MySqliteHelper.COLUMN_CONTENT,MySqliteHelper.COLUMN_PATH}, null, null,
				null, null, null);
		while (cursor.moveToNext()) {
			byte[] datas = cursor.getBlob(cursor.getColumnIndex(MySqliteHelper.COLUMN_CONTENT));
			Bitmap bm = BitmapFactory.decodeByteArray(datas, 0, datas.length);
			bm = Bitmap.createScaledBitmap(
					bm,
					(int)getResources().getDimension(R.dimen.grid_item_image_width),
					(int)getResources().getDimension(R.dimen.grid_item_image_height),
					true);
			System.out.println(bm.getWidth());
			System.out.println(bm.getHeight());
			String _path = cursor.getString(cursor.getColumnIndex(MySqliteHelper.COLUMN_PATH));
			int _id = cursor.getInt(cursor.getColumnIndex(MySqliteHelper.COLUMN_ID));
			pictures.add(new Picture(bm, _id, _path));
		}
		adapter.notifyDataSetChanged();
		if (db != null) {
			db.close();
		}

	}

	class MyGridAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pictures.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = View.inflate(HomeActivity.this,
						R.layout.grid_item, null);
				ImageView image = (ImageView) convertView
						.findViewById(R.id.grid_item_image);
				GridViewHolder holder = new GridViewHolder();
				holder.image = image;
				convertView.setTag(holder);
			}
			((GridViewHolder) convertView.getTag()).image
					.setImageBitmap(pictures.get(position).bitmap);
			return convertView;
		}
	}

	class GridViewHolder {
		// 存放图片的image;
		ImageView image;
	}
	
	/**
	 * 图片实体类
	 * @author Administrator
	 *
	 */
	class Picture{
		Bitmap bitmap;
		int _id;
		String _path;
		public Picture(Bitmap bitmap, int _id, String _path) {
			super();
			this.bitmap = bitmap;
			this._id = _id;
			this._path = _path;
		}
	}
}
