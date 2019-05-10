package com.example.mp3playerv2;

import com.example.fragment.FindMusicFragment;
import com.example.fragment.MusicStoreFragment;
import com.example.fragment.MyMusicFragment;
import com.example.fragment.SearchMusicFragment;

import android.support.v7.app.ActionBarActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements OnClickListener{


	private FragmentManager fragmentManager;
	private FragmentTransaction beginTransaction;
	private FindMusicFragment findMusic=null;
	private MusicStoreFragment musicStore=null;
	private SearchMusicFragment searchMusic=null;
	private MyMusicFragment myMusic=null;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton btn1 = (ImageButton) findViewById(R.id.ll_mymusic_img);
        ImageButton btn2 = (ImageButton) findViewById(R.id.ll_musicstore_img);
        ImageButton btn3 = (ImageButton) findViewById(R.id.ll_find_img);
        ImageButton btn4 = (ImageButton) findViewById(R.id.ll_search_img);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        
//        FragmentManager manger = getFragmentManager();
//		FragmentTransaction beginTransaction = manger.beginTransaction(); 
//		beginTransaction.replace(android.R.id.content, new MusicStore());
//		beginTransaction.commit();
        
        
        
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "通知数据库刷新");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 1:
			//通知mediastorage刷新一下
			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+Environment.getExternalStorageDirectory())));
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		//每次切换数据重新加载 次数多了手机卡顿
		//已解决
		
		fragmentManager = getFragmentManager();
		beginTransaction = fragmentManager.beginTransaction(); 
		TextView title = (TextView) findViewById(R.id.tv_currentTitle);
		
		int id = v.getId();
		switch(id){
		case R.id.ll_mymusic_img:
			title.setText("本地音乐");
			
			if(musicStore!=null) beginTransaction.hide(musicStore);
			if(findMusic!=null) beginTransaction.hide(findMusic);
			if(searchMusic!=null) beginTransaction.hide(searchMusic);
			
			
			if(myMusic==null){
				myMusic = new MyMusicFragment();
//				beginTransaction.replace(R.id.view_pop_show, myMusic);
//				beginTransaction.add(R.id.id_content, myMusic);
			}
//			System.out.println(myMusic.isAdded()+"*********");
			if(!myMusic.isAdded()){
				beginTransaction.add(R.id.id_content, myMusic);
			}else{
				beginTransaction.show(myMusic);
			}
			
				
			beginTransaction.commit();
			break;
			
		case R.id.ll_musicstore_img:
			title.setText("音乐库");
			
			if(myMusic!=null) beginTransaction.hide(myMusic);
			if(findMusic!=null) beginTransaction.hide(findMusic);
			if(searchMusic!=null) beginTransaction.hide(searchMusic);
			
			if(musicStore==null){
				musicStore = new MusicStoreFragment();
			}
			if(!musicStore.isAdded()){
				beginTransaction.add(R.id.id_content, musicStore);
			}else{
				beginTransaction.show(musicStore);
			}
			
			beginTransaction.commit();
			break;
			
		case R.id.ll_search_img:
			title.setText("搜索音乐");
			
			if(myMusic!=null) beginTransaction.hide(myMusic);
			if(findMusic!=null) beginTransaction.hide(findMusic);
			if(musicStore!=null) beginTransaction.hide(musicStore);
			
			if(searchMusic==null){
				searchMusic = new SearchMusicFragment();
			}
			if(!searchMusic.isAdded()){
				beginTransaction.add(R.id.id_content, searchMusic);
			}else{
				beginTransaction.show(searchMusic);
			}
			
			beginTransaction.commit();
			break;
			
		case R.id.ll_find_img:
			title.setText("发现");
			
			if(myMusic!=null) beginTransaction.hide(myMusic);
			if(searchMusic!=null) beginTransaction.hide(searchMusic);
			if(musicStore!=null) beginTransaction.hide(musicStore);
			
			if(findMusic==null){
				findMusic = new FindMusicFragment();
			}
			if(!findMusic.isAdded()){
				beginTransaction.add(R.id.id_content, findMusic);
			}else{
				beginTransaction.show(findMusic);
			}
			
			
			beginTransaction.commit();
			break;
		}
	}
	


   
}
