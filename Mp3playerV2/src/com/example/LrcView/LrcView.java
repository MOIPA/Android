package com.example.LrcView;

import com.example.bean.SongLyrics;

public interface LrcView {
	public void getLrc(SongLyrics lrcinfo);
	
	public void seekLrc2Time(Long time);
	
	public void setListener(LrcViewListener listener);
}
