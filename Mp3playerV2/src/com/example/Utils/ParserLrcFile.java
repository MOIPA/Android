package com.example.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.bean.SongLyrics;

public class ParserLrcFile {
	
	private static SongLyrics lrcinfo = new SongLyrics();
	private static long currentTime = 0;
	private static String currentContent = null;
	private static HashMap<Long, String> map = new HashMap<Long, String>();
	private static String reg;
	
	private static InputStream readLrcFIle(String path) throws FileNotFoundException{
		File f = new File(path);
		InputStream in = new FileInputStream(f);
		return in;
	}
	
	private static SongLyrics lrcParser(InputStream in,boolean two) throws IOException{
		InputStreamReader reader = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(reader);
		
		String line = null;
		while((line = br.readLine())!=null){
			parserLine(line,two);
		}
		
		lrcinfo.setInfos(map);
		
		return lrcinfo;
		
	}

	//***ժ��csdn
	private static void parserLine(String str,boolean two) {
		
		// ȡ�ø�������Ϣ
				if (str.startsWith("[ti:")) {
					String title = str.substring(4, str.length() - 1);
					System.out.println("title--->" + title);
					lrcinfo.setTitle(title);

				}// ȡ�ø�����Ϣ
				else if (str.startsWith("[ar:")) {
					String singer = str.substring(4, str.length() - 1);
					System.out.println("singer--->" + singer);
					lrcinfo.setSinger(singer);

				}// ȡ��ר����Ϣ
				else if (str.startsWith("[al:")) {
					String album = str.substring(4, str.length() - 1);
					System.out.println("album--->" + album);
					lrcinfo.setAlbum(album);

				}// ͨ������ȡ��ÿ������Ϣ
				else {
					// �����������
					if(two = false){
						reg = "\\[(\\d{2}:\\d{2}\\.\\d{2})\\]";
					}else{
						reg = "\\[(\\d{1,8}\\,\\d{1,8})\\]";
					}
					// ����
					Pattern pattern = Pattern.compile(reg);
					Matcher matcher = pattern.matcher(str);

					// �������ƥ�����ִ�����²���
					while (matcher.find()) {
						// �õ�ƥ�����������
						String msg = matcher.group();
						// �õ����ƥ���ʼ������
						int start = matcher.start();
						// �õ����ƥ�������������
						int end = matcher.end();

						// �õ����ƥ�����е�����
						int groupCount = matcher.groupCount();
						// �õ�ÿ����������
						for (int i = 0; i <= groupCount; i++) {
							String timeStr = matcher.group(i);
							if (i == 1) {
								// ���ڶ����е���������Ϊ��ǰ��һ��ʱ���
								currentTime = strToLong(timeStr,two);
							}
						}

						// �õ�ʱ���������
						String[] content = pattern.split(str);
						// �����������
						for (int i = 0; i < content.length; i++) {
							if (i == content.length - 1) {
								// ����������Ϊ��ǰ����
								currentContent = content[i];
							}
						}
						// ����ʱ�������ݵ�ӳ��
						map.put(currentTime, currentContent);
//						System.out.println("put---currentTime--->" + currentTime
//								+ "----currentContent---->" + currentContent);
					}
				}
		
	}

	private static long strToLong(String timeStr,boolean two) {
		// ��Ϊ������ַ�����ʱ���ʽΪXX:XX.XX,���ص�longҪ�����Ժ���Ϊ��λ
		// 1:ʹ�ã��ָ� 2��ʹ��.�ָ�
		if(two){
			String[] s = timeStr.split(":");
			int min = Integer.parseInt(s[0]);
			String[] ss = s[1].split("\\.");
			int sec = Integer.parseInt(ss[0]);
			int mill = Integer.parseInt(ss[1]);
			return min * 60 * 1000 + sec * 1000 + mill * 10;
		}else{
			String[] s =timeStr.split(",");
			int time = Integer.parseInt(s[0]);
			return time;
		}
	}

	public static SongLyrics parser(String path,boolean two) throws Exception {
		InputStream in = readLrcFIle(path);
		lrcinfo = lrcParser(in, two);
		return lrcinfo;

	}
	
}
