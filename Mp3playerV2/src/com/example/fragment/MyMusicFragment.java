package com.example.fragment;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.example.DownLoadUtils.KrcText;
import com.example.Utils.GetAlbum;
import com.example.Utils.GetFilesByExtension;
import com.example.Utils.GetMusicInfo;
import com.example.bean.Music;
import com.example.mp3playerv2.PlayMusicActivity;
import com.example.mp3playerv2.R;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyMusicFragment extends Fragment {

	/**
	 * 本地音乐 通过系统自动注册的音乐数据库 取出数据显示到listview上 点击某条音乐跳转播放界面
	 */
	// 当前歌曲
	private static int currentMusicPosition = 0;
	private static List<Music> lists;

	public static Handler handler = new Handler() {
		private String album;
		private long albumid;
		private String artists;
		private long duration;
		private long id2;
		private String path;
		private long size;
		private String title;
		private Music music;

		public void handleMessage(android.os.Message msg) {
			music = null;
			// 获得playmusicactivity发送的信息 返回歌曲信息
			Bundle data = msg.getData();
			String request = data.getString("acqiureSong");
			Log.d("handler", request);
			Log.d("handler", "unchangedPosition:" + currentMusicPosition);

			if (request.equals("next")) {
				if (currentMusicPosition == lists.size() - 1) {
					currentMusicPosition = 0;
				} else {
					currentMusicPosition += 1;
				}
			} else if (request.equals("prev")) {
				if (currentMusicPosition == 0) {
					currentMusicPosition = lists.size()-1;
				} else {
					currentMusicPosition -= 1;
				}
			}
				Log.d("handler", "changedPosition:" + currentMusicPosition);
				music = lists.get(currentMusicPosition);
				album = music.getAlbum();
				albumid = music.getAlbumid();
				artists = music.getArtists();
				duration = music.getDuration();
				id2 = music.getId();
				path = music.getPath();
				size = music.getSize();
				title = music.getTitle();

				Message Respmsg = new Message();
				Bundle respData = new Bundle();

				respData.putString("album", album);
				respData.putString("artists", artists);
				respData.putString("path", path);
				respData.putString("title", title);
				respData.putLong("albumid", albumid);
				respData.putLong("duration", duration);
				respData.putLong("id2", id2);
				respData.putLong("size", size);

				Respmsg.setData(respData);
				PlayMusicActivity.handler.sendMessage(Respmsg);

				Log.d("handler", "sended");
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		lists = null;
		lists = GetMusicInfo.searchData(getActivity());
		View view = inflater.inflate(R.layout.mymusic, null);
		ListView lv = (ListView) view.findViewById(R.id.lv_musiclist);
		lv.setAdapter(new MyAdapter());

		// 列表项设置监听
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击相关歌曲 跳转到播放活动
				currentMusicPosition = position;
				Music music = lists.get(position);

				String album = music.getAlbum();
				long albumid = music.getAlbumid();
				String artists = music.getArtists();
				long duration = music.getDuration();
				long id2 = music.getId();
				String path = music.getPath();
				long size = music.getSize();
				String title = music.getTitle();

				Intent playActivity = new Intent(getActivity(),
						PlayMusicActivity.class);

				playActivity.putExtra("album", album);
				playActivity.putExtra("title", title);
				playActivity.putExtra("path", path);
				playActivity.putExtra("artists", artists);
				playActivity.putExtra("id", id2);
				playActivity.putExtra("albumid", albumid);

				// 开启活动
				startActivity(playActivity);

			}
		});

		return view;
	}

	private class MyAdapter extends BaseAdapter {

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
			if (convertView == null) {
				view = View.inflate(getActivity(), R.layout.item, null);
			} else {
				view = convertView;
			}
			Music music = lists.get(position);
			TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
			TextView tv_album = (TextView) view.findViewById(R.id.tv_album);
			TextView tv_artist = (TextView) view.findViewById(R.id.tv_artist);
			TextView tv_duration = (TextView) view.findViewById(R.id.duration);
			tv_title.setText(music.getTitle());
			tv_artist.setText(music.getArtists());
			tv_album.setText(music.getAlbum());
			tv_duration.setText(GetMusicInfo.formatTime(music.getDuration()));

			// 设置封面
			ImageView cover = (ImageView) view.findViewById(R.id.iv_cover);
			Bitmap bm = GetAlbum.getArtwork(getActivity(), music.getId(),
					music.getAlbumid(), true, true);
			cover.setImageBitmap(bm);

			return view;
		}

	}

}
