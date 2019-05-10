package com.example.mymusic;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class musicService extends Service {

	private MediaPlayer player;
	
	

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("binded");
		return new MyBinder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		//服务开启时候执行的动作
		
		player = new MediaPlayer();

		
	}
	
	//播放音乐
	public void playMusic(){
		//TODO 需要完善播放代码
		
		try {
			player.reset();
			String path = "/data/music.mp3";
			player.setDataSource(path );
			player.prepare();
			player.start();
			
			//更新进度条
			updateSeekBar();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		System.out.println("playing music");
	}
	
	private void updateSeekBar() {
		//获取歌曲长度
		final int duration = player.getDuration();
		final Timer timer = new Timer();
		final TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				//获取当前进度
				int currentPosition = player.getCurrentPosition();
				
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putInt("duration", duration);
				data.putInt("currentPosition", currentPosition);
				msg.setData(data);
				MainActivity.handler.sendMessage(msg);
			}
		};
		timer.schedule(task, 400, 1000);
		
		//歌曲执行完了取消
		player.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				task.cancel();
				timer.cancel();
			}
		});
	}

	//暂停音乐
	public void pauseMusic(){
		//TODO 需要完善暂停播放代码
		
		player.pause();
		
		System.out.println("paused music");
	}
	
	//恢复音乐
	public void resumeMusic(){
		//TODO 需要完善继续播放代码
		
		player.start();
		
		System.out.println("resume music");
	}
	
	public void seekto(int position){
		player.seekTo(position);
	}
	
	//在服务内部定义IBinder
	
	private class MyBinder extends Binder implements Iservice{

		@Override
		public void CallplayMusic() {
			playMusic();
		}

		@Override
		public void CallpauseMusic() {
			pauseMusic();
		}

		@Override
		public void CallresumeMusic() {
			resumeMusic();
		}

		@Override
		public void Callseekto(int position) {
			seekto(position);
			
		}


		
		
		
	}
	
	
}
