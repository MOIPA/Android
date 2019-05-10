package com.example.listview2;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ListView lv = (ListView)findViewById(R.id.lv);
        lv.setAdapter(new MyAdapter());
    }
    
    private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 6;
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
			
			TextView tv;
			if(convertView==null){
				tv = new TextView(getApplicationContext());
			}else{
				tv = (TextView)convertView;
			}
			tv.setText("text"+position);
			
			return tv;
		}
    	
    }

   
}
