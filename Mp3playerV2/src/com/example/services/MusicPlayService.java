package com.example.services;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.example.mp3playerv2.PlayMusicActivity;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class MusicPlayService extends Service {

	private MediaPlayer player;
	private int MusicTaskStatues = 0;
	private Timer timer = new Timer();
	private TimerTask task = new TimerTask() {
		
		@Override
		public void run() {
			
		}
	};

	// 设定状态 0为闲置 1为播放 2为暂停

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("MusicService", "ServiceStarted.");
	}

	@Override
	public IBinder onBind(Intent intent) {
		player = new MediaPlayer();
		Log.d("MusicService", "ServiceBinded.");
		return new MyBinder();
	}

	private void playMusic(String path) {
		try {
			// 重新播放重启任务
			timer.cancel();
			task.cancel();
			
			player.setDataSource(path);
			player.prepare();
			player.start();
			MusicTaskStatues = 1;
			// 服务提供播放进度给playmusicActivity的seekbar
			updateSeekBar();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void updateSeekBar() {

		final long duration = player.getDuration();
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				Message msg = new Message();
				Bundle data = new Bundle();

				int currentPosition = player.getCurrentPosition();
				data.putLong("duration", duration);
				data.putLong("currentPosition", currentPosition);
				msg.setData(data);
				PlayMusicActivity.handlerForSeekBar.sendMessage(msg);
			}
		};
		timer.schedule(task, 10, 1000);

		player.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// 歌曲播放完了就取消任务
				timer.cancel();
				task.cancel();
				MusicTaskStatues = 0;
			}
		});
	}

	private void pauseMusic() {
		player.pause();
		MusicTaskStatues = 2;
	}

	private void resumeMusic() {
		player.start();
		updateSeekBar();
		MusicTaskStatues = 1;
	}

	private void seek(int position) {
		player.seekTo(position);
	}

	private class MyBinder extends Binder implements Iservice {

		@Override
		public void CallPlayMusic(String path) {
			player.reset();
			playMusic(path);
		}

		@Override
		public void CallPauseMusic() {
			pauseMusic();
		}

		@Override
		public void CallResumeMusic() {
			resumeMusic();
		}

		@Override
		public void seek2Time(int position) {
			seek(position);
		}

		@Override
		public int MediaPlayerStatues() {
			return MusicTaskStatues;
		}

	}

}
