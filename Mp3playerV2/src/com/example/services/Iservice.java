package com.example.services;

public interface Iservice {
	public void CallPlayMusic(String path);
	
	public void CallPauseMusic();
	
	public void CallResumeMusic();
	
	public void seek2Time(int position);
	
	public int MediaPlayerStatues();
}
