package com.example.mymusic;

import android.support.v7.app.ActionBarActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class MainActivity extends ActionBarActivity {
	
	/**
	 *音乐框架：1、需要在后台也能播放，服务，能够获取并且长期运行：startService 和bind混合开启服务
	 */
	
	private Iservice iservice;
	private MyConn conn;
	private static SeekBar sk;
	
	public static Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			Bundle data = msg.getData();
			int duration = data.getInt("duration");
			int currentPosition = data.getInt("currentPosition");
			
			sk.setMax(duration);
			sk.setProgress(currentPosition);
			
		};
		
	};
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //混合开启服务
        Intent service  = new Intent(this,musicService.class);
        startService(service);
        conn = new MyConn();
        bindService(service, conn, BIND_AUTO_CREATE);
        
        sk = (SeekBar) findViewById(R.id.seekbar);
        
      //进度条监听
		sk.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			//拖动停止
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

				iservice.Callseekto(sk.getProgress());
			}
			
			//开始拖动
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			//进度条改变
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
    }
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	//解除绑定防止警报
    	unbindService(conn);
    }
    
    public void click1(View v){
    	iservice.CallplayMusic();
    }
    
    public void click2(View v){
    	iservice.CallpauseMusic();
    }
    
    public void click3(View v){
    	iservice.CallresumeMusic();
    }
    
    
    //监听服务状态 
    private class MyConn implements ServiceConnection{



		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			iservice = (Iservice)service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
    	
    }


}
