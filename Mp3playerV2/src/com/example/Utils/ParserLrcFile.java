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

	//***摘自csdn
	private static void parserLine(String str,boolean two) {
		
		// 取得歌曲名信息
				if (str.startsWith("[ti:")) {
					String title = str.substring(4, str.length() - 1);
					System.out.println("title--->" + title);
					lrcinfo.setTitle(title);

				}// 取得歌手信息
				else if (str.startsWith("[ar:")) {
					String singer = str.substring(4, str.length() - 1);
					System.out.println("singer--->" + singer);
					lrcinfo.setSinger(singer);

				}// 取得专辑信息
				else if (str.startsWith("[al:")) {
					String album = str.substring(4, str.length() - 1);
					System.out.println("album--->" + album);
					lrcinfo.setAlbum(album);

				}// 通过正则取得每句歌词信息
				else {
					// 设置正则规则
					if(two = false){
						reg = "\\[(\\d{2}:\\d{2}\\.\\d{2})\\]";
					}else{
						reg = "\\[(\\d{1,8}\\,\\d{1,8})\\]";
					}
					// 编译
					Pattern pattern = Pattern.compile(reg);
					Matcher matcher = pattern.matcher(str);

					// 如果存在匹配项，则执行以下操作
					while (matcher.find()) {
						// 得到匹配的所有内容
						String msg = matcher.group();
						// 得到这个匹配项开始的索引
						int start = matcher.start();
						// 得到这个匹配项结束的索引
						int end = matcher.end();

						// 得到这个匹配项中的组数
						int groupCount = matcher.groupCount();
						// 得到每个组中内容
						for (int i = 0; i <= groupCount; i++) {
							String timeStr = matcher.group(i);
							if (i == 1) {
								// 将第二组中的内容设置为当前的一个时间点
								currentTime = strToLong(timeStr,two);
							}
						}

						// 得到时间点后的内容
						String[] content = pattern.split(str);
						// 输出数组内容
						for (int i = 0; i < content.length; i++) {
							if (i == content.length - 1) {
								// 将内容设置为当前内容
								currentContent = content[i];
							}
						}
						// 设置时间点和内容的映射
						map.put(currentTime, currentContent);
//						System.out.println("put---currentTime--->" + currentTime
//								+ "----currentContent---->" + currentContent);
					}
				}
		
	}

	private static long strToLong(String timeStr,boolean two) {
		// 因为给如的字符串的时间格式为XX:XX.XX,返回的long要求是以毫秒为单位
		// 1:使用：分割 2：使用.分割
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
