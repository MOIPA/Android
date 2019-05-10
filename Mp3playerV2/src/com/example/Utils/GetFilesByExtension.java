package com.example.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GetFilesByExtension {
	
	private static List<File> fileLists;
	//isENterΪtrueʱ���ݹ���ҵ�ǰĿ¼�������ļ�
	//false:ֻ���ҵ�ǰĿ¼  ��������Ŀ¼
	private  static void getfile(String directoryPath,String extension,boolean isEnter){
		
		File[] files = new File(directoryPath).listFiles();
		
		//�ݹ�����������ļ����ڲ�û���ļ�����
		if(files==null)return;
		if(files.length<=0){
			return;
		}
		
		//�����ļ�Ŀ¼
		for(int i=0;i<files.length;i++){
			File currentFile = files[i];
			
			//���ļ�������к�׺������
			String fileName = currentFile.getName();
//			System.out.println(fileName+"*****");
			if(currentFile.isFile()&&fileName.endsWith(extension)){
				fileLists.add(currentFile);
			}
			//��Ŀ¼���ҿ��Խ���Ŀ¼
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
