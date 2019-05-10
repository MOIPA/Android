package com.example.myrecorder;

import java.util.ArrayList;
import java.util.List;

import database.MyOpenHelper;
import android.support.v7.app.ActionBarActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// MyOpenHelper mh = new MyOpenHelper(getApplicationContext());
		// mh.getReadableDatabase();
		// mh.close();
		bind();
		Button refresh = (Button) findViewById(R.id.refresh);
		refresh.setOnClickListener(new myListener());
		Button add = (Button)findViewById(R.id.add);
		add.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,NewText.class);
				startActivity(intent);
			}
		});

	}

	public List<Thing> reteriveData() {
		List<Thing> lists = new ArrayList<Thing>();
		MyOpenHelper mh = new MyOpenHelper(getApplicationContext());
		SQLiteDatabase db = mh.getWritableDatabase();
		Cursor cursor = db.query("thing", new String[] { "thing", "time" },
				null, null, null, null, null);

		// 检索信息用javabean保存
		while (cursor.moveToNext()) {
			String thing = cursor.getString(0);
			String time = cursor.getString(1);
			Thing th = new Thing();
			th.setThing(thing);
			th.setTime(time);
			lists.add(th);
		}
		db.close();
		mh.close();
		
		return lists;

	}
	
	private void bind(){
		List<Thing> data = reteriveData();
		
		ListView lv = (ListView) findViewById(R.id.lv);
		lv.setAdapter(new MyAdapter(data));
	}
	
	private class myListener implements android.view.View.OnClickListener{

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch(id){
			case R.id.refresh:
				Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT);
				
//				List<Thing> data = reteriveData();
//				
//				ListView lv = (ListView) findViewById(R.id.lv);
//				lv.setAdapter(new MyAdapter(data));
				bind();
				
			}
		}
	}
	
	
	private class MyAdapter extends BaseAdapter{
		
		private List<Thing> data;
		
		public MyAdapter(List<Thing> data){
			this.data=data;
		}

		@Override
		public int getCount() {
			return data.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			if(convertView==null){
				view = View.inflate(getApplicationContext(), R.layout.item, null);
			}else{
				view = convertView;
			}
			TextView thing = (TextView) view.findViewById(R.id.tv_thing);
			TextView time = (TextView) view.findViewById(R.id.tv_time);
			
			Thing th = data.get(position);
			thing.setText(th.getThing());
			time.setText(th.getTime());
			
			return view;
		}
		
	}

}
