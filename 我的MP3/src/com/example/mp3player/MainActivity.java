package com.example.mp3player;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	
	private List<Music> lists = new ArrayList();
	private Iservice musicService;
	private MyConn conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        searchMusic();

        Intent intent = new Intent(this,MusicPlayService.class);
        startService(intent);
        conn = new MyConn();
        bindService(intent, conn, BIND_AUTO_CREATE);
        
        //listView设置监听
        ListView lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new MyAdapter());
        
        lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Music music = lists.get(position);
				String path = music.getPath();
				
				MediaPlayer player = new MediaPlayer();
				try {
//					player.setDataSource(path);
//					
//					player.prepare();
//					
//					player.start();
//					System.out.println(musicService);
					musicService.CallplayMusic(path);
					
				} catch (Exception e) {
					e.printStackTrace();
				} 
				
			}
		});
    }
    
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	unbindService(conn);
    }
    
    //按钮事件
    
    public void play(View v){
    	String path = "";
    	if(lists.get(0)!=null){
    		path=lists.get(0).getPath();
//    		System.out.println(path);
    		musicService.CallplayMusic(path);
    	}else{
    		Toast.makeText(getApplicationContext(), "没有曲目", 1).show();
    	}
    	
    }
    
    public void pause(View v){
    	musicService.CallpauseMusic();
    }

	public void replay(View v){
		musicService.CallreplayMusic();
	}
    
    
    public void searchMusic(){
    	Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.AudioColumns.TITLE);
//    	getContentResolver().query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder)
//    	System.out.println(cursor);
    	
//    	if(cursor!=null){
//    	System.out.println(cursor.getCount());
    	
    	Music music;
    		
    		while(cursor.moveToNext()){
        		int titleIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE);
        		int artistIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST);
        		int albumIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM);
        		int pathIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
        		
        		String title = cursor.getString(titleIndex);
        		String artist = cursor.getString(artistIndex);
        		String album = cursor.getString(albumIndex);
        		String path = cursor.getString(pathIndex);
        		
        		music = new Music();
        		music.setAlbum(album);
        		music.setArtists(artist);
        		music.setPath(path);
        		music.setTitle(title);
        		
        		lists.add(music);
        		
        	}
//    	}
    	
    }
    
    public class MyAdapter extends BaseAdapter{

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
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View view;
			
			if(convertView==null){
				view = View.inflate(getApplicationContext(), R.layout.item, null);
			}else{
				view = convertView;
			}
			TextView tv_album = (TextView) view.findViewById(R.id.tv_album);
			TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
			TextView tv_artist = (TextView) view.findViewById(R.id.tv_artist);
			
			Music music = lists.get(position);
			
			
			try {
				//TODO 需要正确转码
				String album = new String(music.getAlbum().getBytes("gbk"),"gbk");
				String title = new String(music.getTitle().getBytes(),"utf-8");
				String artist = new String(music.getArtists().getBytes("utf-8"),"gbk");
				
				System.out.println(album);
				System.out.println(title);
				System.out.println(artist);
				
				tv_album.setText(album);
				tv_title.setText(title);
				tv_artist.setText(artist);
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			return view;
		}
    	
    }

    private class MyConn implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
//			System.out.println(service);
			musicService = (Iservice)service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
//			System.out.println("fucking failed**********");
		}
    	
    }
}
