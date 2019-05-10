package com.example.mycomm;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;


public class XmlParser {
	
	public static List<MyMessage> parserXml(InputStream in) throws Exception{
		List<MyMessage> list = null;
		MyMessage msg = null;
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(in, "utf-8");
		int type = parser.getEventType();
		while(type!=XmlPullParser.END_DOCUMENT){
			
			switch (type) {
			case XmlPullParser.START_TAG:
				
				if("contents".equals(parser.getName())){
					list = new ArrayList<MyMessage>();
				}else if("message".equals(parser.getName())){
					msg = new MyMessage();
				}else if("sender".equals(parser.getName())){
					msg.setSender(parser.nextText());
				}else if("time".equals(parser.getName())){
					String text = parser.nextText();
					msg.setTime(text);
				}else if("content".equals(parser.getName())){
					String text = parser.nextText();
					msg.setContent(text);
				}
				
				break;
			case XmlPullParser.END_TAG:
				
				if("message".equals(parser.getName())){
					list.add(msg);
				}
				
				break;

			default:
				break;
			}
			
			type=parser.next();
		}
		return list;
	}
	
	
//	public static List<News> xmlParser(InputStream in) throws Exception {
//
//		List<News> list = null;
//		News news = null;
//
//		XmlPullParser parser = Xml.newPullParser();
//
//		parser.setInput(in, "utf-8");
//
//		int type = parser.getEventType();
//
//		while (type != XmlPullParser.END_DOCUMENT) {
//
//			switch (type) {
//			case XmlPullParser.START_TAG:
//
//				if ("channel".equals(parser.getName())) {
//					list = new ArrayList<News>();
//				} else if ("item".equals(parser.getName())) {
//					news = new News();
//				} else if ("title".equals(parser.getName())) {
//					String title = parser.nextText();
//					news.setTitle(title);
//				} else if ("description".equals(parser.getName())) {
//					String description = parser.nextText();
//					news.setDescription(description);
//				} else if ("image".equals(parser.getName())) {
//					String image = parser.nextText();
//					news.setImage(image);
//				} else if ("type".equals(parser.getName())) {
//					String types = parser.nextText();
//					news.setType(types);
//				} else if ("comment".equals(parser.getName())) {
//					String comment = parser.nextText();
//					news.setComment(comment);
//				}
//
//				break;
//			case XmlPullParser.END_TAG:
//
//				if ("item".equals(parser.getName())) {
//					list.add(news);
//				}
//
//				break;
//			default:
//				break;
//			}
//
//			type = parser.next();
//		}
//
//		return list;
//	}

}
