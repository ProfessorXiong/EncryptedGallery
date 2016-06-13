package com.xiong.lockedgallery;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteHelper extends SQLiteOpenHelper {
	public static final String COLUMN_CONTENT = "_content";//图片实际内容列名
	public static final String COLUMN_ID = "_id";			//图片id列名
	public static final String COLUMN_PATH = "_path";		//图片的路径列名
	public static final String TABLE_PICTURE = "pictures";	
	
	//数据库的名称
	private static final String DB_NAME = "pics.db";
	//数据库的版本
	private static final int DB_VERSION = 1;
	private static  MySqliteHelper helper = null;
	/**
	 * 构造方法
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	private MySqliteHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql = new String("CREATE TABLE pictures(_id INTEGER PRIMARY KEY AUTOINCREMENT,_content BLOB NOT NULL,_path TEXT NOT NULL)");
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	/**
	 * 获得只读数据库的方法
	 * @param context
	 * @return
	 */
	public static SQLiteDatabase getReadableDB(Context context){
		if(helper == null){
			helper = new MySqliteHelper(context);
		}
		return helper.getReadableDatabase();
	}
	/**
	 * 获得可写数据库的方法
	 * @param context
	 * @return
	 */
	public static SQLiteDatabase getWriteableDB(Context context){
		if(helper == null){
			helper = new MySqliteHelper(context);
		}
		return helper.getWritableDatabase();
	}

}
