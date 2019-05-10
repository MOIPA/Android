package com.example.webcode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private EditText et_path;
	private TextView tv_result;
	private String tag = "MainActivity";
	
	//主线程中定义一个handler
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			//跟新ui
			if(msg.what==0){
				Toast.makeText(getApplicationContext(), "succeed"+msg.what, Toast.LENGTH_SHORT).show();
				tv_result.setText((String)msg.obj);
			}
			if(msg.what==1||msg.what==2){
				Toast.makeText(getApplicationContext(), "error"+msg.what, Toast.LENGTH_SHORT).show();
				tv_result.setText((String)msg.obj);
			}
		};
	};

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        et_path = (EditText) findViewById(R.id.et_path);
        tv_result = (TextView) findViewById(R.id.tv_result);
        
//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }
        Log.d(tag, Thread.currentThread().getName());
    }
    
    public void click(View v){
    	//创建一个子线程
    	new Thread(){public void run() {
    		try {
    			//[2.1]获取源码路径 
    			String path = et_path.getText().toString().trim();
    			//[2.2]创建URL 对象指定我们要访问的 网址(路径)
    			URL url = new URL(path);
    			
    			//[2.3]拿到httpurlconnection对象  用于发送或者接收数据 
    			HttpURLConnection  conn = (HttpURLConnection) url.openConnection();
    			//[2.4]设置发送get请求 
    			conn.setRequestMethod("GET");//get要求大写  默认就是get请求
    			//[2.5]设置请求超时时间
    			conn.setConnectTimeout(5000);
    			//[2.6]获取服务器返回的状态码 
    			int code = conn.getResponseCode();
    			//[2.7]如果code == 200 说明请求成功
    			if (code == 200) {
    				//[2.8]获取服务器返回的数据   是以流的形式返回的  由于把流转换成字符串是一个非常常见的操作  所以我抽出一个工具类(utils)
    				InputStream in = conn.getInputStream(); 
    				
    				//[2.9]使用我们定义的工具类 把in转换成String
    				String content = readStream(in);
    				
    				//2.9.1 拿着我们创建的handler(助手) 告诉系统 说我要更新ui
    				Message msg = new Message();
    				msg.obj=content;
    				msg.what=0;
    				handler.sendMessage(msg);
    				//发送到了主线程
    				
//    				tv_result.setText(content);
    				
    			}else{
    				Message msg =new Message();
    				msg.what=1;
    				msg.obj="无法访问到网站";
    				handler.sendMessage(msg);
    			}
        	} catch (Exception e) {
        		e.printStackTrace();
        		Message msg =new Message();
				msg.what=2;
				msg.obj="无法访问到网站";
				handler.sendMessage(msg);
    		}
    	};}.start();
    	
    	
    }
public String readStream(InputStream in) throws Exception{
		
		//定义一个内存输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len = -1;
		byte[] buffer = new byte[1024]; //1kb
		while((len=in.read(buffer))!=-1){
			
			baos.write(buffer, 0, len);
		}
		in.close();
		String content = new String(baos.toByteArray());
		
		return content;
		
	}


}
