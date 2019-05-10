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
 * @author tr ���ղ�����ƥ��������
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

	// �������һ�׻�����һ���Ƿ���MyMusic��Ϣ������һ�׸�����MyMusic��handler���պ��͸����Ż
	public static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// Log.d("handler", "unupdated:" + currentMusicPath);
			// ���ո�����Ϣ
			Bundle data = msg.getData();
			// ���¸�����Ϣ
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
			// ����seekbar������
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

		// ���մ������Ĳ��� ���ǵ�������׸���������
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		albumid = extras.getLong("albumid");
		songid = extras.getLong("id");
		currentMusicPath = extras.getString("path");
		File file = new File(currentMusicPath);
		filename = file.getName();

		// �ҵ���ť���ü���
		ib_next = (ImageButton) findViewById(R.id.playMusic_next);
		ib_prev = (ImageButton) findViewById(R.id.playMusic_prev);
		ib_start_pause = (ImageButton) findViewById(R.id.playMusic_start_pause);

		ib_next.setOnClickListener(new MyClickListener());
		ib_prev.setOnClickListener(new MyClickListener());
		ib_start_pause.setOnClickListener(new MyClickListener());

		// ��������
		Intent service = new Intent(this, MusicPlayService.class);
		startService(service);
		conn = new MyConn();
		bindService(service, conn, BIND_AUTO_CREATE);

		// seekbar�϶�����
		sk.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// ֹͣ�϶������ø���λ��
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
				// ����������һ����Ϣ
				// Message msg = new Message();
				// Bundle data = new Bundle();
				// data.putString("acqiureSong", "next");
				// msg.setData(data);
				// MyMusicFragment.handler.sendMessage(msg);
				temp = currentMusicPath;
				Log.d("PlayMusicActivity", currentMusicPath);
				// ����������һ����Ϣ
				SendSongRequest(true);
				// �ȴ�������Ϣ����
				ib_start_pause
						.setImageResource(R.drawable.player_btn_pause_normal);
				timer.schedule(task, 200);
				Log.d("PlayMusicActivity", "updated");

				break;
			case R.id.playMusic_prev:
				temp = currentMusicPath;
				Log.d("PlayMusicActivity", currentMusicPath);
				// ����������һ����Ϣ
				SendSongRequest(false);
				// �ȴ�������Ϣ����
				ib_start_pause
						.setImageResource(R.drawable.player_btn_pause_normal);
				timer.schedule(task, 200);
				Log.d("PlayMusicActivity", "updated");
				break;
			case R.id.playMusic_start_pause:
				// �жϵ�ǰ����״̬

				// ����״̬ 0 ��ʼ����
				if (manager.MediaPlayerStatues() == 0) {
					manager.CallPlayMusic(currentMusicPath);
					ib_start_pause
							.setImageResource(R.drawable.player_btn_pause_normal);
					Log.d("status", manager.MediaPlayerStatues()+"");
				} 
				// ���ڲ���״̬1  ��ͣ2
				else if (manager.MediaPlayerStatues() == 1) {// ���ڲ��� �޸�ͼ��͵����Ϊ
					manager.CallPauseMusic();
					ib_start_pause
							.setImageResource(R.drawable.player_btn_play_normal);
					
					Log.d("status", manager.MediaPlayerStatues()+"");
				}
				//��ͣ����״̬2  �ָ�1
				else if(manager.MediaPlayerStatues() == 2) {
					// ��������
					manager.CallResumeMusic();
					ib_start_pause
							.setImageResource(R.drawable.player_btn_pause_normal);
					Log.d("status", manager.MediaPlayerStatues()+"");
				}

				break;
			}
		}

	}

	// ������һ�׻�����һ�׸�������
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
		/* ���Է��� */
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
	 * ��װ���krcת���� �����û��klc��� �ṷ��klc��downloadUtils��Ľ����������lrc������ɾ��ԭ����klc�ļ�
	 * 
	 * @author tr
	 * @throws IOException
	 */
	private void checkIsHasKlc() throws IOException {

		// ��ȡklc�ļ�����û�к͸�����ƥ��ģ�����У�ת��Ϊͬ��lrc�ļ�

		List<File> lists = GetFilesByExtension.getFileByExtention("/mnt",
				"krc", true);

		for (int i = 0; i < lists.size(); i++) {
			String name = lists.get(i).getName();
			String path = lists.get(i).getPath();
			// ȥ��.mp3 ͨ��str.replace();
			// ���ڶ�Ӧklc�ļ�
			if (name.startsWith(filename.replace(".mp3", ""))) {
				// ��Ϊͬ��lrc�ļ�
				String newPath = path.replace(name, "")
						+ filename.replace(".mp3", "") + ".lrc";
				File newLrcFile = new File(newPath);
				if (!newLrcFile.exists())
					newLrcFile.createNewFile();

				// ����klc

				String content = KrcText.getKrcText(path);

				// ���ļ���д������
				FileWriter fw = new FileWriter(newLrcFile.getAbsoluteFile());
				BufferedWriter bw = new BufferedWriter(fw);

				bw.write(content);
				bw.close();
				bw.close();

				// ɾ��klc
				lists.get(i).delete();

			}
			// else System.out.println(filename.replace(".mp3", "")+"***"+name);
		}

		// ��ȡlrc�ļ���������ʴ����javabean��

	}

	/**
	 * @author tr ����Ƿ����lrc��ʣ��������������������ʾ�޸��
	 * @throws Exception
	 */

	private void checkIsHasLrc(boolean twoParam) throws Exception {

		File lrcFile = null;

		List<File> fileLists = GetFilesByExtension.getFileByExtention("/mnt/",
				"lrc", true);

		// ����filenameͬ����lrc�ļ�
		for (int i = 0; i < fileLists.size(); i++) {
			File file = fileLists.get(i);
			String lrcName = file.getName().replace(".lrc", "");

			if (filename.replace(".mp3", "").trim().equals(lrcName.trim())) {
				// �����⵽lrc�ļ�
				lrcFile = fileLists.get(i);
			}
		}

		if (lrcFile != null) {
			// --��ʼ����

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
