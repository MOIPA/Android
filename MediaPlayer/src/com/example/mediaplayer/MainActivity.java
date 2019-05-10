package com.example.mediaplayer;

import java.io.IOException;

import android.support.v7.app.ActionBarActivity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    
    public void click(View v){
    	//初始化mediaplayer
    	
    	MediaPlayer player = new MediaPlayer();
    	
    	//设置播放路劲 可以是网络路径
    	try {
			player.setDataSource("/data/music.mp3");
			
			
			//准备u播放
			player.prepare();
			player.start();
		} catch (Exception e) {
			e.printStackTrace();
		} 
    	
    }


}
