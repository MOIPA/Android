package com.example.mycomm;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private EditText et_username;
	private EditText et_password;
	private CheckBox cb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
		cb = (CheckBox) findViewById(R.id.cb_remember);

		SharedPreferences sp = getSharedPreferences("login", 1);
		
		cb.setChecked(sp.getBoolean("cb", false));
		et_username.setText(sp.getString("name", ""));
		et_password.setText(sp.getString("pwd", ""));

	}

	public void click_login(View v) {
		new Thread() {
			public void run() {

				EditText et_username = (EditText) findViewById(R.id.et_username);
				EditText et_password = (EditText) findViewById(R.id.et_password);
				CheckBox cb = (CheckBox) findViewById(R.id.cb_remember);
				
				String rowname = et_username.getText().toString().trim();
				String pwd = et_password.getText().toString().trim();

//				System.out.println("name:"+rowname);
//				System.out.println("pwd"+pwd);
				if (rowname.equals("") || pwd.equals("")) {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), "要输入用户名和密码哦",
									Toast.LENGTH_LONG).show();
						}
					});
					return;
				}
				
				// 记住登陆逻辑
				SharedPreferences sp = getSharedPreferences("login", 1);
				if (cb.isChecked()) {
					Editor editor = sp.edit();
					editor.putString("name", rowname);
					editor.putString("pwd", pwd);
					editor.putBoolean("cb", true);
					editor.commit();
				}else{
					Editor editor = sp.edit();
					editor.putString("name", "");
					editor.putString("pwd", "");
					editor.putBoolean("cb", false);
					editor.commit();
				}
				// *********

				String temp = "";
				try {
					temp = URLEncoder.encode(rowname, "utf-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				final String name = temp;

				// 提交的路径
				String path = "http://39.108.159.175/phpworkplace/androidLogin/Login.php"
						+ "?name=" + name + "&&password=" + pwd;
				try {
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET");
					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream in = conn.getInputStream();
						final String content = readStream(in);
						// 展示服务器返回的数据
						runOnUiThread(new Runnable() {
							public void run() {
								if (content.toString().trim().equals("success")) {
									// 登陆成功
									// 转到回话页面
									Intent intent = new Intent(
											MainActivity.this,
											Communicate.class);
									intent.putExtra("username", name);
									startActivity(intent);
								}
								Toast.makeText(getApplicationContext(),
										content.trim(), Toast.LENGTH_LONG)
										.show();
							}
						});

					} else {

					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			};
		}.start();
		// Get提交数据

	}

	public String readStream(InputStream in) throws Exception {

		// 定义一个内存输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len = -1;
		byte[] buffer = new byte[1024]; // 1kb
		while ((len = in.read(buffer)) != -1) {

			baos.write(buffer, 0, len);
		}
		in.close();
		String content = new String(baos.toByteArray());

		return content;

	}

}
