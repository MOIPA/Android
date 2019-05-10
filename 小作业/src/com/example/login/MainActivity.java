package com.example.login;

import java.util.Map;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.example.LittleWork.R;

public class MainActivity extends Activity {

	private EditText et_username;
	private EditText et_passwd;
	private CheckBox cb_remember;
	private SharedPreferences sp;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		et_username = (EditText) findViewById(R.id.et_username);
		et_passwd = (EditText) findViewById(R.id.et_userpasswd);
		cb_remember = (CheckBox) findViewById(R.id.cb_remember);

		sp = getSharedPreferences("config", 0);
		
		
		et_username.setText( sp.getString("name", ""));
		et_passwd.setText( sp.getString("pwd", ""));
		cb_remember.setChecked(sp.getBoolean("cb_remember", false));
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
			Log.d("MainActivity", "login clicked");

			if (cb_remember.isChecked()) {
				sp = getSharedPreferences("config", 0);
				Editor edit = sp.edit();
				edit.putString("name", username);
				edit.putString("pwd", userpasswd);
				edit.putBoolean("cb_remember", cb_remember.isChecked());
				edit.commit();

			} else {
				sp = getSharedPreferences("config", 0);
				Editor edit = sp.edit();
				edit.putBoolean("cb_remember", cb_remember.isChecked());
				edit.putString("name", "");
				edit.putString("pwd", "");
				edit.commit();
				Toast.makeText(MainActivity.this, "未点击保存", Toast.LENGTH_SHORT)
						.show();
			}
		}

	}

}
