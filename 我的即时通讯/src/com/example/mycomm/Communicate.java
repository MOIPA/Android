package com.example.mycomm;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.support.v7.app.ActionBarActivity;
import android.text.format.Time;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class Communicate extends ActionBarActivity {

	private List<MyMessage> lists;
	private ListView lv;
//	private View footView;
	private String username="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_communicate);
		
		Intent intent = getIntent();
//		System.out.println(intent);
		username = intent.getStringExtra("username").toString();
		
//		if(username==null)System.out.println("nothing");
//		System.out.println(username);
		
		//找到listview并且添加底部布局
		lv = (ListView) findViewById(R.id.lv);
//		footView = View.inflate(getApplicationContext(), R.layout.bottom,null);
//		lv.addFooterView(footView);
		
		//为底部布局发送消息按钮添加事件
//		Button btn_send = (Button) footView.findViewById(R.id.btn_send);
//		btn_send.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				try {
//					click_send(v);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
		
		//不停接收服务器消息刷新界面  但是由于设置了stackFromBottom 不能让用户看到上面记录
		initData();
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				initData();
			}
		};
		timer.schedule(task, 10,1000);
		
	}
	
	/**
	 * 发送消息到服务器
	 * @throws UnsupportedEncodingException 
	 */
	public void click_send(View v) throws UnsupportedEncodingException{
		//由于et_content在底部布局里  所以前面得加上footView不然et_content为null
		
		final EditText et_content = (EditText) findViewById(R.id.et_content);
		String contentx = et_content.getText().toString().trim();
		final String content = URLEncoder.encode(contentx, "utf-8");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");//设置日期格式
		final String nowTime = df.format(new java.util.Date());
//		System.out.println("time:"+nowTime+" content:"+content);
		
		new Thread(){public void run() {
			
			try {
				
				String path = "http://39.108.159.175/phpworkplace/androidLogin/SendMessage.php?"+"content=\""+content+"\"&&time=\""+nowTime.toString()+"\"&"+"sender="+"\""+username+"\"";
//				String path = "http://192.168.126.1:8080/androidLogin/SendMessage.php?content=\"testsss\"&&time=\"2017-08-09-14:32:14\"";
//				String path = "http://39.108.159.175/phpworkplace/androidLogin/SendMessage.php?content=\"测试x\"&&time=\"2017-08-10-11:10:33\"";
				
				System.out.println(path);
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				int code = conn.getResponseCode();
				
				if(code==200){
					InputStream in = conn.getInputStream();
					final String text = Stream2String.readStream(in).trim();
					runOnUiThread(new Runnable() {
						public void run() {
							et_content.setText("");
							if(text.equals("error"))
							Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
						}
					});
//					
					initData();
				}else{
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		};}.start();
		
	}
	
	/**
	 * 接收服务器数据
	 */
	public void initData(){
		
		new Thread(){

		public void run() {
			
			String path = "http://39.108.159.175/phpworkplace/androidLogin/GetMessage.php";
			try {
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				int code = conn.getResponseCode();
				
				if(code==200){
					//解析发送来的数据
					InputStream in = conn.getInputStream();
					lists = XmlParser.parserXml(in);
					
					runOnUiThread(new Runnable() {
						public void run() {
							lv.setAdapter(new MyAdapter());
						}
					});
					
					
				}else{
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		};}.start();
		
		
	}
	
	
	/**
	 * 适配器
	 */
	private class MyAdapter extends BaseAdapter{
		
		
		

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final View view ;
			if(convertView==null){
				view = View.inflate(getApplicationContext(), R.layout.item, null);
			}else{
				view = convertView;
			}
			
		
					MyMessage message = lists.get(position);
					TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
					TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
					TextView tv_sender = (TextView) view.findViewById(R.id.tv_sender);
					tv_time.setText(message.getTime());
					tv_sender.setText("  "+message.getSender()+"  ");
					tv_content.setText(message.getContent());
					
					if(message.getSender().equals("小笨蛋")){
						tv_sender.setBackgroundColor(Color.parseColor("#ff80aa"));
					}else if(message.getSender().equals("大聪明蛋")){
						tv_sender.setBackgroundColor(Color.parseColor("#00ccff"));
					}
					
					try {
						String currentName = URLDecoder.decode(username, "utf-8");
					
					
					if(currentName.equals("小笨蛋")&&(message.getSender().equals("小笨蛋"))){
						
						RelativeLayout.LayoutParams params = (LayoutParams) tv_time.getLayoutParams();
						params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						tv_time.setLayoutParams(params);
						
						params = (LayoutParams) tv_sender.getLayoutParams();
						params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						tv_sender.setLayoutParams(params);
						
						params = (LayoutParams) tv_content.getLayoutParams();
						params.addRule(RelativeLayout.LEFT_OF,R.id.tv_sender);
						params.addRule(RelativeLayout.RIGHT_OF,0);
						tv_content.setLayoutParams(params);
						
						
					}else if(currentName.equals("大聪明蛋")&&(message.getSender().equals("大聪明蛋"))){
						
						RelativeLayout.LayoutParams params = (LayoutParams) tv_time.getLayoutParams();
						params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						tv_time.setLayoutParams(params);
						
						params = (LayoutParams) tv_sender.getLayoutParams();
						params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
						tv_sender.setLayoutParams(params);
						
						params = (LayoutParams) tv_content.getLayoutParams();
						params.addRule(RelativeLayout.LEFT_OF,R.id.tv_sender);
						params.rightMargin = 50;
						params.addRule(RelativeLayout.RIGHT_OF,0);
						tv_content.setLayoutParams(params);
						
						
					}
//					System.out.println("username:"+username.toString().trim()+" sender:"+message.getSender());
					
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					
//					System.out.println(message.getSender());
			
			return view;
		}
		
	}


}
