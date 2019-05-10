package com.example.mp3player;

import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

public class MusicPlayService extends Service {

	private MediaPlayer player;


	@Override
	public IBinder onBind(Intent intent) {
//		System.out.println("bind");
		return new MyBinder();
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		player = new MediaPlayer();
	}
	
	private void playMusic(String path){
		
//		System.out.println("*******play");
		
		player.reset();
		try {
			player.setDataSource(path);
			
			player.prepare();
			
			player.start();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private void pauseMusic(){
		player.pause();
	}
	
	private void replayMusic(){
		player.start();
	}
	
	
	private class MyBinder extends Binder implements Iservice{

		@Override
		public void CallplayMusic(String path) {
//			System.out.println("*******call");
			playMusic(path);
		}

		@Override
		public void CallpauseMusic() {
			pauseMusic();
		}

		@Override
		public void CallreplayMusic() {
			replayMusic();
		}
		
		
		
		
	}

}
