package com.example.login;

import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private String tag = "MainActivity";
	private Button login;
	private Button regesiter;
	private myListener listener;
	private EditText et_name;
	private EditText et_pwd;
	private SharedPreferences sp;
	private CheckBox cb_remember;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		et_name = (EditText)findViewById(R.id.username);
		et_pwd= (EditText)findViewById(R.id.userpassword);
		cb_remember = (CheckBox)findViewById(R.id.cb_remember);
		
		sp = getSharedPreferences("info", 1);
		et_name.setText(sp.getString("name", ""));
		et_pwd.setText(sp.getString("pwd", ""));
		cb_remember.setChecked(sp.getBoolean("ischecked", false));
		
		listener = new myListener();
		login = (Button)findViewById(R.id.login);
		login.setOnClickListener(listener);
		regesiter = (Button)findViewById(R.id.regesiter);
		regesiter.setOnClickListener(listener);
		
		
		
	}

	class myListener implements android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.login:
				Log.d(tag, "cicked");
				
				EditText et_name = (EditText)findViewById(R.id.username);
				EditText et_pwd = (EditText)findViewById(R.id.userpassword);
				CheckBox cb_remember = (CheckBox)findViewById(R.id.cb_remember);
				String name = et_name.getText().toString().trim();
				String pwd = et_pwd.getText().toString().trim();
				
				if(name.equals("")||pwd.equals("")){
					Toast.makeText(getApplicationContext(), "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
				}else{
					if(cb_remember.isChecked()){
						SharedPreferences sp = MainActivity.this.getSharedPreferences("info", 1);
						Editor edit = sp.edit();
						edit.putString("name", name);
						edit.putString("pwd", pwd);
						edit.putBoolean("ischecked", true);
						edit.commit();
						
						
					}else{
						SharedPreferences sp = MainActivity.this.getSharedPreferences("info", 1);
						Editor edit = sp.edit();
						edit.putString("name", "");
						edit.putString("pwd", "");
						edit.putBoolean("ischecked", false);
						edit.commit();
					}
					//检测是否存在账户
					ManageUser mu = new ManageUser(MainActivity.this);
					List<User> lists = mu.reterive();

					boolean flag = false;
					for(int i=0;i<lists.size();i++){
						User user = lists.get(i);
						Log.d("MainActivity", "actual name is:"+name+"pwd is:"+pwd);
						Log.d("MainActivity", "current name is:"+user.getName()+"pwd is:"+user.getPassword());
						if(user.getName().equals(name)&&user.getPassword().equals(pwd)){
							flag=true;
						}
					}
					if(flag){
//						Toast.makeText(getApplicationContext(), "合法账户", Toast.LENGTH_SHORT).show();
					
						Intent intent = new Intent(getApplicationContext(),Logined.class);
						startActivity(intent);
						
					}else{
						Toast.makeText(getApplicationContext(), "非合法账户", Toast.LENGTH_SHORT).show();
					}
					
				}
				
				break;
			case R.id.regesiter:
				Log.d(tag, "regesiter");
				Intent intent = new Intent(MainActivity.this,Regesiter.class);
				startActivity(intent);
				break;
			}
		}

	}

}
