package com.example.login_server;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private EditText et_username;
	private EditText et_password;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        
    }
    
    public void click1(View v){
    	new Thread(){public void run() {
    		
    		String name = et_username.getText().toString().trim();
        	String pwd = et_password.getText().toString().trim();
        
        	//�ύ��·��
        	String path = "http://192.168.58.1:8080/androidLogin/Login.php"+"?name="+name+"&&password="+pwd;
        	try {
    			URL url = new URL(path);
    			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    			conn.setConnectTimeout(5000);
    			conn.setRequestMethod("GET");
    			int code = conn.getResponseCode();
    			if(code==200){
    				InputStream in = conn.getInputStream();
    				final String content = readStream(in);
    				//չʾ���������ص�����
    				runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(getApplicationContext(), content, Toast.LENGTH_LONG).show();
						}
					});
    				
    			}else{
    				
    			}
    			
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		
    	};}.start();
    	//Get�ύ����
    	
    }
    
    public void click2(View v){
    	//Post�ύ����
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
