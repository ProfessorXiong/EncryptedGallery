package com.xiong.lockedgallery;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class ShowImageActivity extends Activity {
	private ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_image);

		image = (ImageView) findViewById(R.id.show_activity_image);
		
		getBitmapFromDB();
	}

	void getBitmapFromDB(){
		SQLiteDatabase db = MySqliteHelper.getReadableDB(this);
		Cursor cursor = db.query(MySqliteHelper.TABLE_PICTURE, new String[]{MySqliteHelper.COLUMN_CONTENT}, MySqliteHelper.COLUMN_ID+ "=?", new String[]{getIntent().getIntExtra("_id", -1) + ""}, null, null, null);
		byte[] data = null;
		while(cursor.moveToNext()){
			data = cursor.getBlob(0);
		}
		image.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));
		db.close();
	}
}
