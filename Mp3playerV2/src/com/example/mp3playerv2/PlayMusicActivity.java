package com.example.mp3playerv2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.DownLoadUtils.KrcText;
import com.example.Utils.GetFilesByExtension;
import com.example.Utils.ParserLrcFile;
import com.example.bean.SongLyrics;
import com.example.fragment.MyMusicFragment;
import com.example.services.Iservice;
import com.example.services.MusicPlayService;

import android.support.v7.app.ActionBarActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * 
 * @author tr 接收参数，匹配解析歌词
 */
public class PlayMusicActivity extends ActionBarActivity {

	private String filename;
	private MyConn conn;
	private Iservice manager;
	private static String currentMusicPath;
	private ImageButton ib_next;
	private ImageButton ib_prev;
	private ImageButton ib_start_pause;
	private static long albumid;
	private static long songid;
	private static long duration;
	private static long currentPosition;
	private static SeekBar sk;

	// 当点击上一首或者下一首是发送MyMusic信息需求上一首歌曲，MyMusic的handler接收后发送给播放活动
	public static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// Log.d("handler", "unupdated:" + currentMusicPath);
			// 接收歌曲信息
			Bundle data = msg.getData();
			// 更新歌曲信息
			currentMusicPath = data.getString("path");
			albumid = data.getLong("albumid");
			songid = data.getLong("id2");

			// Log.d("handler", "updated:" + currentMusicPath);
		};
	};

	// for seekBar
	public static Handler handlerForSeekBar = new Handler() {
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			duration = data.getLong("duration");
			currentPosition = data.getLong("currentPosition");
			// 设置seekbar进度条
			sk.setMax((int) duration);
			sk.setProgress((int) currentPosition);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_music);

		Button btnText = (Button) findViewById(R.id.play_btn_text);
		sk = (SeekBar) findViewById(R.id.playMusic_sk);

		// 接收传过来的参数 就是点击的那首歌曲的数据
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		albumid = extras.getLong("albumid");
		songid = extras.getLong("id");
		currentMusicPath = extras.getString("path");
		File file = new File(currentMusicPath);
		filename = file.getName();

		// 找到按钮设置监听
		ib_next = (ImageButton) findViewById(R.id.playMusic_next);
		ib_prev = (ImageButton) findViewById(R.id.playMusic_prev);
		ib_start_pause = (ImageButton) findViewById(R.id.playMusic_start_pause);

		ib_next.setOnClickListener(new MyClickListener());
		ib_prev.setOnClickListener(new MyClickListener());
		ib_start_pause.setOnClickListener(new MyClickListener());

		// 开启服务
		Intent service = new Intent(this, MusicPlayService.class);
		startService(service);
		conn = new MyConn();
		bindService(service, conn, BIND_AUTO_CREATE);

		// seekbar拖动监听
		sk.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// 停止拖动后设置歌曲位置
				manager.seek2Time(seekBar.getProgress());
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub

			}
		});
	}

	private class MyConn implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			manager = (Iservice) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d("MusicService", "ServiceDisconnected.");
		}

	}

	private class MyClickListener implements OnClickListener {

		@Override
		public void onClick(final View v) {

			String temp;
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					manager.CallPlayMusic(currentMusicPath);
				}
			};

			switch (v.getId()) {

			case R.id.playMusic_next:
				// 发送请求下一首消息
				// Message msg = new Message();
				// Bundle data = new Bundle();
				// data.putString("acqiureSong", "next");
				// msg.setData(data);
				// MyMusicFragment.handler.sendMessage(msg);
				temp = currentMusicPath;
				Log.d("PlayMusicActivity", currentMusicPath);
				// 发送请求下一首消息
				SendSongRequest(true);
				// 等待歌曲信息更新
				ib_start_pause
						.setImageResource(R.drawable.player_btn_pause_normal);
				timer.schedule(task, 200);
				Log.d("PlayMusicActivity", "updated");

				break;
			case R.id.playMusic_prev:
				temp = currentMusicPath;
				Log.d("PlayMusicActivity", currentMusicPath);
				// 发送请求上一首消息
				SendSongRequest(false);
				// 等待歌曲信息更新
				ib_start_pause
						.setImageResource(R.drawable.player_btn_pause_normal);
				timer.schedule(task, 200);
				Log.d("PlayMusicActivity", "updated");
				break;
			case R.id.playMusic_start_pause:
				// 判断当前播放状态

				// 闲置状态 0 开始播放
				if (manager.MediaPlayerStatues() == 0) {
					manager.CallPlayMusic(currentMusicPath);
					ib_start_pause
							.setImageResource(R.drawable.player_btn_pause_normal);
					Log.d("status", manager.MediaPlayerStatues()+"");
				} 
				// 正在播放状态1  暂停2
				else if (manager.MediaPlayerStatues() == 1) {// 正在播放 修改图标和点击行为
					manager.CallPauseMusic();
					ib_start_pause
							.setImageResource(R.drawable.player_btn_play_normal);
					
					Log.d("status", manager.MediaPlayerStatues()+"");
				}
				//暂停播放状态2  恢复1
				else if(manager.MediaPlayerStatues() == 2) {
					// 继续播放
					manager.CallResumeMusic();
					ib_start_pause
							.setImageResource(R.drawable.player_btn_pause_normal);
					Log.d("status", manager.MediaPlayerStatues()+"");
				}

				break;
			}
		}

	}

	// 发送上一首或者下一首歌曲请求
	private void SendSongRequest(boolean next) {
		Message msg = new Message();
		Bundle data = new Bundle();
		if (next) {
			data.clear();
			data.putString("acqiureSong", "next");
			msg.setData(data);
			MyMusicFragment.handler.sendMessage(msg);
		} else {
			data.clear();
			data.putString("acqiureSong", "prev");
			msg.setData(data);
			MyMusicFragment.handler.sendMessage(msg);
		}
	}

	public void test(View v) throws IOException {
		/* 测试方法 */
		// List<File> list = GetFilesByExtension.getFileByExtention("/mnt/",
		// "krc", true);
		//
		// for(int i=0;i<list.size();i++){
		// System.out.println(list.get(i).getPath());
		// }
		//
		// File file = list.get(0);
		//
		// File outFile = new File("/mnt/sdcard/text.lrc");
		//
		// try {
		// String text = KrcText.getKrcText(file.getPath());
		// if(!outFile.exists())outFile.createNewFile();
		// // System.out.println("***");
		// FileWriter fw = new FileWriter(outFile.getAbsoluteFile());
		// BufferedWriter bw = new BufferedWriter(fw);
		// bw.write(text);
		// bw.close();
		// System.out.println("DONE");
		//
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }

		checkIsHasKlc();
		try {
			checkIsHasLrc(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 封装检测krc转化类 检查有没有klc歌词 酷狗的klc用downloadUtils里的解密类解析到lrc，并且删除原来的klc文件
	 * 
	 * @author tr
	 * @throws IOException
	 */
	private void checkIsHasKlc() throws IOException {

		// 获取klc文件看有没有和歌曲名匹配的，如果有，转换为同名lrc文件

		List<File> lists = GetFilesByExtension.getFileByExtention("/mnt",
				"krc", true);

		for (int i = 0; i < lists.size(); i++) {
			String name = lists.get(i).getName();
			String path = lists.get(i).getPath();
			// 去掉.mp3 通过str.replace();
			// 存在对应klc文件
			if (name.startsWith(filename.replace(".mp3", ""))) {
				// 变为同名lrc文件
				String newPath = path.replace(name, "")
						+ filename.replace(".mp3", "") + ".lrc";
				File newLrcFile = new File(newPath);
				if (!newLrcFile.exists())
					newLrcFile.createNewFile();

				// 解析klc

				String content = KrcText.getKrcText(path);

				// 向文件内写入内容
				FileWriter fw = new FileWriter(newLrcFile.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);

				bw.write(content);
				bw.close();
				bw.close();

				// 删除klc
				lists.get(i).delete();

			}
			// else System.out.println(filename.replace(".mp3", "")+"***"+name);
		}

		// 读取lrc文件，解析歌词存放着javabean里

	}

	/**
	 * @author tr 检测是否存在lrc歌词，存在则解析，不存则显示无歌词
	 * @throws Exception
	 */

	private void checkIsHasLrc(boolean twoParam) throws Exception {

		File lrcFile = null;

		List<File> fileLists = GetFilesByExtension.getFileByExtention("/mnt/",
				"lrc", true);

		// 检测和filename同名的lrc文件
		for (int i = 0; i < fileLists.size(); i++) {
			File file = fileLists.get(i);
			String lrcName = file.getName().replace(".lrc", "");

			if (filename.replace(".mp3", "").trim().equals(lrcName.trim())) {
				// 如果检测到lrc文件
				lrcFile = fileLists.get(i);
			}
		}

		if (lrcFile != null) {
			// --开始解析

			SongLyrics lrcinfos = ParserLrcFile.parser(lrcFile.getPath(),
					twoParam);
			System.out.println(lrcinfos.getAlbum());
			System.out.println(lrcinfos.getSinger());
			System.out.println(lrcinfos.getTitle());
			HashMap<Long, String> map = lrcinfos.getInfos();

			// for(Object obj:map.keySet()){
			// System.out.print(obj+": ");
			// System.out.println(map.get(obj));
			// }

		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(conn);
	}
}
