package com.example.login;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public class UserInfoUtils {

	public static boolean saveInfo(Context context,String username, String userpwd) {

		
		try {
//			String path = context.getFilesDir().getPath();
			String result = username + "##" + userpwd;
////			File file = new File("/data/data/com.example.login/info.txt");
//			File file = new File(path,"info.txt");
//			FileOutputStream fos = new FileOutputStream(file);
//			fos.write(result.getBytes());
//			fos.close();
			
			//直接用context类里的方法
			FileOutputStream fos2 = context.openFileOutput("infoo.txt", 0);
			fos2.write(result.getBytes());
			fos2.close();
			
			return true;
		} catch (Exception e) {
			return false;
		}

	}
	
	public static Map<String,String> readInfo(Context context){
		
		try {
			String path = context.getFilesDir().getPath();
			Map<String,String> maps = new HashMap<String, String>();
//			File file = new File("/data/data/com.example.login/info.txt");
//			File file = new File(path,"info.txt");
			FileInputStream fis;
			fis = context.openFileInput("infoo.txt");
			BufferedReader bufr = new BufferedReader(new InputStreamReader(fis));
			String content = bufr.readLine();
			
			
			
			String[] splits = content.split("##");
			String name = splits[0];
			String pwd= splits[1];
			maps.put("name", name);
			maps.put("pwd", pwd);
			fis.close();
			return maps;
		} catch (Exception e) {
			return null;
		}
		
		
	}
}
