package com.xiong.lockedgallery;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ShowImageActivity extends Activity implements OnSeekBarChangeListener {
	private ImageView image;
	private SeekBar seekBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_image);

		image = (ImageView) findViewById(R.id.show_activity_image);
		seekBar = (SeekBar) findViewById(R.id.view_activity_seekbar1);
		seekBar.setOnSeekBarChangeListener(this);;
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

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		float times = (float)progress * 2 / 100;
		image.setScaleX(times);
		image.setScaleY(times);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		
	}
}
