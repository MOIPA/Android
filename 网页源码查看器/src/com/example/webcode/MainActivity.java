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
	
	//���߳��ж���һ��handler
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
			//����ui
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
    	//����һ�����߳�
    	new Thread(){public void run() {
    		try {
    			//[2.1]��ȡԴ��·�� 
    			String path = et_path.getText().toString().trim();
    			//[2.2]����URL ����ָ������Ҫ���ʵ� ��ַ(·��)
    			URL url = new URL(path);
    			
    			//[2.3]�õ�httpurlconnection����  ���ڷ��ͻ��߽������� 
    			HttpURLConnection  conn = (HttpURLConnection) url.openConnection();
    			//[2.4]���÷���get���� 
    			conn.setRequestMethod("GET");//getҪ���д  Ĭ�Ͼ���get����
    			//[2.5]��������ʱʱ��
    			conn.setConnectTimeout(5000);
    			//[2.6]��ȡ���������ص�״̬�� 
    			int code = conn.getResponseCode();
    			//[2.7]���code == 200 ˵������ɹ�
    			if (code == 200) {
    				//[2.8]��ȡ���������ص�����   ����������ʽ���ص�  ���ڰ���ת�����ַ�����һ���ǳ������Ĳ���  �����ҳ��һ��������(utils)
    				InputStream in = conn.getInputStream(); 
    				
    				//[2.9]ʹ�����Ƕ���Ĺ����� ��inת����String
    				String content = readStream(in);
    				
    				//2.9.1 �������Ǵ�����handler(����) ����ϵͳ ˵��Ҫ����ui
    				Message msg = new Message();
    				msg.obj=content;
    				msg.what=0;
    				handler.sendMessage(msg);
    				//���͵������߳�
    				
//    				tv_result.setText(content);
    				
    			}else{
    				Message msg =new Message();
    				msg.what=1;
    				msg.obj="�޷����ʵ���վ";
    				handler.sendMessage(msg);
    			}
        	} catch (Exception e) {
        		e.printStackTrace();
        		Message msg =new Message();
				msg.what=2;
				msg.obj="�޷����ʵ���վ";
				handler.sendMessage(msg);
    		}
    	};}.start();
    	
    	
    }
public String readStream(InputStream in) throws Exception{
		
		//����һ���ڴ������
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
