package com.example.Utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.bean.Music;

public class GetMusicInfo {
	
	private static List<Music> lists = new ArrayList<Music>();
	
	public static List<Music> searchData(Context context) {
		
		//�������ṩ�߻�ȡ��Ҫ�ĸ�����Ϣ
		Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.AudioColumns.TITLE);
    	
    	Music music;
    	
    		while(cursor.moveToNext()){
    			
    			long id = cursor.getLong(cursor  
                        .getColumnIndex(MediaStore.Audio.Media._ID));   //����id  
                String title = cursor.getString((cursor   
                        .getColumnIndex(MediaStore.Audio.Media.TITLE))); // ���ֱ���  
                String artist = cursor.getString(cursor  
                        .getColumnIndex(MediaStore.Audio.Media.ARTIST)); // ������  
                String album = cursor.getString(cursor  
                        .getColumnIndex(MediaStore.Audio.Media.ALBUM)); //ר��  
                long albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));  
                long duration = cursor.getLong(cursor  
                        .getColumnIndex(MediaStore.Audio.Media.DURATION)); // ʱ��  
                long size = cursor.getLong(cursor  
                        .getColumnIndex(MediaStore.Audio.Media.SIZE)); // �ļ���С  
                String path = cursor.getString(cursor  
                        .getColumnIndex(MediaStore.Audio.Media.DATA)); // �ļ�·��  
                int isMusic = cursor.getInt(cursor  
                        .getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); // �Ƿ�Ϊ����  
        		
        		if(isMusic!=0){
	        		music = new Music();
	        		music.setAlbum(album);
	        		music.setArtists(artist);
	        		music.setPath(path);
	        		music.setTitle(title);
	        		music.setId(id);
	        		music.setDuration(duration);
	        		music.setAlbumid(albumId);
	        		music.setSize(size);
					lists.add(music);
	        	}
        	}
    		return lists;
	}
	
	public static String  formatTime(long time){
		
		String fen;
		long miao;
		
		fen = time/(60*1000)+"";
		miao=(time%(60*1000))/1000;
		
		if(miao<10)return fen+":0"+miao;
		if(miao==0)return fen+":00";
		else return fen+":"+miao;
			
	}
}
