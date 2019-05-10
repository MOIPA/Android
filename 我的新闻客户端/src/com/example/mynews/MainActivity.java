package com.example.mynews;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.client.entity.UrlEncodedFormEntity;

import android.support.v7.app.ActionBarActivity;
import android.text.Html.ImageGetter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

	private List<News> newsLists;
	private ListView lv;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        lv = (ListView) findViewById(R.id.lv);
        
        //准备数据给lv 去服务器取得数据封装
        initData();
        
        
    }

	private void initData() {
		new Thread(){
			
			public void run() {
				
				try {
					URL url = new URL("http://192.168.58.1:8080/news.xml");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);
					int code = conn.getResponseCode();
					if(code==200){
						InputStream in = conn.getInputStream();
						
						newsLists = XmlParserUtil.xmlParser(in);
						
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
				
			};
			
		}.start();
		
	}
	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return newsLists.size();
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
			View view;
			if(convertView==null){
				view  = View.inflate(getApplicationContext(), R.layout.item, null);
			}else{
				view = convertView;
			}
			
			final ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
			TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
			TextView tv_desc = (TextView) view.findViewById(R.id.tv_desc);
			TextView tv_type = (TextView) view.findViewById(R.id.tv_type);
			
			tv_title.setText(newsLists.get(position).getTitle());
			tv_desc.setText(newsLists.get(position).getDescription());
			String typ = newsLists.get(position).getType();
			String comment = newsLists.get(position).getComment();
			int type = Integer.parseInt(typ);
			switch(type){
			case 0:
				tv_type.setText("国内"+comment);
				break;
			case 1:
				tv_type.setText("国外"+comment);
				break;
			case 2:
				tv_type.setText("跟帖"+comment);
				break;
			}
			
			//huoqu image
			new Thread(){public void run() {
				try {
					String path = newsLists.get(position).getImage();
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET");
					InputStream in = conn.getInputStream();
					final Bitmap bm = BitmapFactory.decodeStream(in);
					
					runOnUiThread(new Runnable() {
						public void run() {
							iv_icon.setImageBitmap(bm);
						}
					});
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			};}.start();
			
			
			
			return view;
		}
		
	}

}
