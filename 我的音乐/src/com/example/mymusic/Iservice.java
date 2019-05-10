package com.example.mymusic;

public interface Iservice {
	
	//希望暴露的方法
	
	public void CallplayMusic();
	
	public void CallpauseMusic();
	
	public void CallresumeMusic();
	
	public void Callseekto(int position);
	
}
