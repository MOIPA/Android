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
		
		//������ʱ��ִ�еĶ���
		
		player = new MediaPlayer();

		
	}
	
	//��������
	public void playMusic(){
		//TODO ��Ҫ���Ʋ��Ŵ���
		
		try {
			player.reset();
			String path = "/data/music.mp3";
			player.setDataSource(path );
			player.prepare();
			player.start();
			
			//���½�����
			updateSeekBar();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
		System.out.println("playing music");
	}
	
	private void updateSeekBar() {
		//��ȡ��������
		final int duration = player.getDuration();
		final Timer timer = new Timer();
		final TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				//��ȡ��ǰ����
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
		
		//����ִ������ȡ��
		player.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				task.cancel();
				timer.cancel();
			}
		});
	}

	//��ͣ����
	public void pauseMusic(){
		//TODO ��Ҫ������ͣ���Ŵ���
		
		player.pause();
		
		System.out.println("paused music");
	}
	
	//�ָ�����
	public void resumeMusic(){
		//TODO ��Ҫ���Ƽ������Ŵ���
		
		player.start();
		
		System.out.println("resume music");
	}
	
	public void seekto(int position){
		player.seekTo(position);
	}
	
	//�ڷ����ڲ�����IBinder
	
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
