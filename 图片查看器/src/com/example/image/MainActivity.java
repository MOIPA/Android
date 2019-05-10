package com.example.image;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.support.v7.app.ActionBarActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {

    private EditText et_path;
	private ImageView iv;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case 0:
				iv.setImageBitmap((Bitmap)msg.obj);
				break;

			default:
				break;
			}
			
		};
	};

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        et_path = (EditText) findViewById(R.id.et_path);
        
        iv = (ImageView) findViewById(R.id.iv);
    }
	
	public void click(View v){
		new Thread(){
			public void run() {

				String path = et_path.getText().toString().trim();
				try {
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					int code = conn.getResponseCode();
					
					if(code==200){
						InputStream in = conn.getInputStream();
						
						Bitmap bm = BitmapFactory.decodeStream(in);
						//ת��Ϊbitmapλͼ
//						iv.setImageBitmap(bm);
						Message msg = Message.obtain();
						msg.obj=bm;
						msg.what=0;
						handler.sendMessage(msg);
						
					}else{
						
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
		
		
		
		
	}


}
