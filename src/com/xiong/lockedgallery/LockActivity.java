package com.xiong.lockedgallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LockActivity extends Activity implements OnClickListener {
	private String password = "1234";
	
	private EditText edit1;
	private Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn0,btn_confirm,btn_cancel,btn_backspace;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock);
		bindViews();
	}

	void bindViews(){
		edit1 = (EditText) findViewById(R.id.lock_activity_edit_password);
		btn0 = (Button) findViewById(R.id.lock_activity_btn_0);
		btn1 = (Button) findViewById(R.id.lock_activity_btn_1);
		btn2 = (Button) findViewById(R.id.lock_activity_btn_2);
		btn3 = (Button) findViewById(R.id.lock_activity_btn_3);
		btn4 = (Button) findViewById(R.id.lock_activity_btn_4);
		btn5 = (Button) findViewById(R.id.lock_activity_btn_5);
		btn6 = (Button) findViewById(R.id.lock_activity_btn_6);
		btn7 = (Button) findViewById(R.id.lock_activity_btn_7);
		btn8 = (Button) findViewById(R.id.lock_activity_btn_8);
		btn9 = (Button) findViewById(R.id.lock_activity_btn_9);
		btn_confirm = (Button) findViewById(R.id.lock_activity_btn_confirm);
		btn_cancel = (Button) findViewById(R.id.lock_activity_btn_cancel);
		btn_backspace = (Button) findViewById(R.id.lock_activity_btn_backspace);
		btn0.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		btn4.setOnClickListener(this);
		btn5.setOnClickListener(this);
		btn6.setOnClickListener(this);
		btn7.setOnClickListener(this);
		btn8.setOnClickListener(this);
		btn9.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		btn_backspace.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lock_activity_btn_0:
			edit1.setText(edit1.getText().toString() + "0");
			break;
		case R.id.lock_activity_btn_1:
			edit1.setText(edit1.getText().toString() + "1");
			break;
		case R.id.lock_activity_btn_2:
			edit1.setText(edit1.getText().toString() + "2");
			break;
		case R.id.lock_activity_btn_3:
			edit1.setText(edit1.getText().toString() + "3");
			break;
		case R.id.lock_activity_btn_4:
			edit1.setText(edit1.getText().toString() + "4");
			break;
		case R.id.lock_activity_btn_5:
			edit1.setText(edit1.getText().toString() + "5");
			break;
		case R.id.lock_activity_btn_6:
			edit1.setText(edit1.getText().toString() + "6");
			break;
		case R.id.lock_activity_btn_7:
			edit1.setText(edit1.getText().toString() + "7");
			break;
		case R.id.lock_activity_btn_8:
			edit1.setText(edit1.getText().toString() + "8");
			break;
		case R.id.lock_activity_btn_9:
			edit1.setText(edit1.getText().toString() + "9");
			break;
		case R.id.lock_activity_btn_backspace:
			String str = edit1.getText().toString();
			if(str.length()>1){
				str = str.substring(0, str.length()-1);
				edit1.setText(str);
			}else{
				edit1.setText("");
			}
			break;
		case R.id.lock_activity_btn_confirm:
			if(edit1.getText().toString().equals(password)){
				startActivity(new Intent(this,HomeActivity.class));
				finish();
			}else{
				Toast.makeText(this, "密码不正确", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.lock_activity_btn_cancel:
			finish();
			System.exit(0);
			break;
		default:
			break;
		}
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		edit1.setText("");
		super.onPause();
	}
}
