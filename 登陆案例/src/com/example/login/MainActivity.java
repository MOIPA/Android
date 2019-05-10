package com.example.login;

import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText et_username;
	private EditText et_passwd;
	private CheckBox cb_remember;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		et_username = (EditText) findViewById(R.id.et_username);
		et_passwd = (EditText) findViewById(R.id.et_userpasswd);
		cb_remember = (CheckBox) findViewById(R.id.cb_remember);
		
		Map<String,String> maps = UserInfoUtils.readInfo(MainActivity.this);
		if(maps!=null){
			et_username.setText(maps.get("name"));
			et_passwd.setText(maps.get("pwd"));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void Login(View v) {
		String username = et_username.getText().toString().trim();
		String userpasswd = et_passwd.getText().toString().trim();

		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(userpasswd)) {
			Toast.makeText(MainActivity.this, "用户名密码不能为空", Toast.LENGTH_SHORT)
					.show();
		} else {
			System.out.println("logined");

			if (cb_remember.isChecked()) {
				boolean result = UserInfoUtils.saveInfo(MainActivity.this,username, userpasswd);
				if (result)
					Toast.makeText(MainActivity.this, "用户信息保存成功",
							Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(MainActivity.this, "用户信息保存失败",
							Toast.LENGTH_SHORT).show();

			} else {
				Toast.makeText(MainActivity.this, "未点击保存", Toast.LENGTH_SHORT)
						.show();
			}
		}

	}

}
