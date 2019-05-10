package com.example.LrcView;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.example.bean.SongLyrics;

public class MyLrcView extends View implements LrcView{

	public MyLrcView(Context context) {
		super(context);
	}
	
	//÷ÿ–¥ondraw∑Ω∑®
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		
	}

	@Override
	public void getLrc(SongLyrics lrcinfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void seekLrc2Time(Long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setListener(LrcViewListener listener) {
		// TODO Auto-generated method stub
		
	}

}
