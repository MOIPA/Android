package com.example.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetFilesByExtension {
	
	private static List<File> fileLists;
	//isENter为true时：递归查找当前目录下所有文件
	//false:只查找当前目录  不查找子目录
	private  static void getfile(String directoryPath,String extension,boolean isEnter){
		
		File[] files = new File(directoryPath).listFiles();
		
		//递归结束条件：文件夹内部没有文件返回
		if(files==null)return;
		if(files.length<=0){
			return;
		}
		
		//便利文件目录
		for(int i=0;i<files.length;i++){
			File currentFile = files[i];
			
			//是文件则添加切后缀名符合
			String fileName = currentFile.getName();
//			System.out.println(fileName+"*****");
			if(currentFile.isFile()&&fileName.endsWith(extension)){
				fileLists.add(currentFile);
			}
			//是目录并且可以进入目录
			if(currentFile.isDirectory()&&isEnter){
				getfile(currentFile.getPath(), extension, true);
			}
		}
		
	}
	
	public static List<File> getFileByExtention(String directoryPath,String extension,boolean isEnter){
		fileLists = new ArrayList<File>();
		
		getfile(directoryPath, extension, isEnter);
		
		return fileLists;
		
	}
}
