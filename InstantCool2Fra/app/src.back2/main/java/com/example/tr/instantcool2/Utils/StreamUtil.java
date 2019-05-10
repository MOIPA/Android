package com.example.tr.instantcool2.Utils;

import android.util.Log;
import android.util.Xml;

import com.example.tr.instantcool2.JavaBean.Conversation;
import com.example.tr.instantcool2.JavaBean.Friend;
import com.example.tr.instantcool2.JavaBean.MyMessage;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TR on 2017/10/10.
 */

public class StreamUtil {

    public static String readStream(InputStream in) throws Exception {

        // 定义一个内存输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = -1;
        byte[] buffer = new byte[1024]; // 1kb
        while ((len = in.read(buffer)) != -1) {

            baos.write(buffer, 0, len);
        }
        in.close();
        String content = new String(baos.toByteArray());

        return content;

    }

    /**
     * <?xml version='1.0'encoding='UTF-8?>
     *<contents>
         *<conversation>
         *  <targetaccount>tzq</targetaccount>
         *  <targetname></targetname>
         *</conversation>
         *<conversation>
         *  <targetaccount>sx</targetaccount>
         *  <targetname>ssx</targetname>
         *</conversation>
     *</contents>
     */
    public static List<Conversation> XmlParserConversation(InputStream in)throws Exception{

        List<Conversation> list = null;
        Conversation conversation=null;

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in,"utf-8");
        int eventType = parser.getEventType();
        while(eventType!=XmlPullParser.END_DOCUMENT){

            switch (eventType){
                case XmlPullParser.START_TAG :
                    if(parser.getName().equals("contents"))list = new ArrayList<Conversation>();
                    else if(parser.getName().equals("conversation"))conversation = new Conversation();
                    else if(parser.getName().equals("targetaccount")){
                        conversation.setTargetaccount(parser.nextText());
                    }else if(parser.getName().equals("targetname")){
                        conversation.setTargetname(parser.nextText());
                    }else if(parser.getName().equals("targeticon")){
                        conversation.setIcon(Integer.parseInt(parser.nextText()));
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(parser.getName().equals("conversation")){
                        list.add(conversation);
                    }
                    break;
            }

            eventType = parser.next();
        }

        return list;
    }
    public static List<Friend> XmlParserFriend(InputStream in)throws Exception{

        List<Friend> list = null;
        Friend friend=null;

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in,"utf-8");
        int eventType = parser.getEventType();
        while(eventType!=XmlPullParser.END_DOCUMENT){

            switch (eventType){
                case XmlPullParser.START_TAG :
                    if(parser.getName().equals("contents"))list = new ArrayList<Friend>();
                    else if(parser.getName().equals("Friends"))friend = new Friend();
                    else if(parser.getName().equals("friendaccount")){
                        friend.setFriendAccount(parser.nextText());
                    }else if(parser.getName().equals("friendname")){
                        friend.setFriendName(parser.nextText());
                    }else if(parser.getName().equals("friendicon")){
                        friend.setFriendIcon(Integer.parseInt(parser.nextText()));
                    }

                    break;
                case XmlPullParser.END_TAG:
                    if(parser.getName().equals("Friends")){
                        list.add(friend);
                    }
                    break;
            }

            eventType = parser.next();
        }

        return list;
    }

    public static List<String> XmlParserInvitation(InputStream in)throws Exception{

        List<String> list = null;
        String inviter=null;

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in,"utf-8");
        int eventType = parser.getEventType();
        Log.d("paserinvitation", "XmlParserInvitation: 开始解析"+StreamUtil.readStream(in)+";");
        while(eventType!=XmlPullParser.END_DOCUMENT){

            Log.d("paserinvitation", "XmlParserInvitation:event type:"+parser.getEventType()+";");
            Log.d("paserinvitation", "XmlParserInvitation:event type:"+parser.getName()+";");

            switch (eventType){
                case XmlPullParser.START_TAG :
                    Log.d("paserinvitation", "XmlParserInvitation: start_tag"+parser.getName());
                    if(parser.getName().equals("root")){
                        list = new ArrayList<String>();
                        Log.d("paserinvitation", "XmlParserInvitation: root");
                    }
                    else if(parser.getName().equals("invite")){
                        inviter = new String();
//                        inviter = parser.nextText();
//                        Log.d("paserinvitation", "XmlParserInvitation: "+inviter);
                    }else if(parser.getName().equals("inviter")){
                        inviter = parser.nextText();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(parser.getName().equals("invite")){
                        list.add(inviter);
                    }
                    break;
            }

            eventType = parser.next();
        }

        return list;
    }

    public static List<MyMessage> XmlParserMessage(InputStream in)throws Exception{
        Log.d("chatActivity", "*********2");

        List<MyMessage> list = null;
        MyMessage myMessage =null;
//
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(in,"utf-8");
        int eventType = parser.getEventType();
        while(eventType!=XmlPullParser.END_DOCUMENT){
//
            switch (eventType){
                case XmlPullParser.START_TAG :
                    if(parser.getName().equals("contents"))list = new ArrayList<MyMessage>();
                    else if(parser.getName().equals("message"))myMessage = new MyMessage();
                    else if(parser.getName().equals("time")){
                        myMessage.setTime(parser.nextText());
                    }else if(parser.getName().equals("owner")){
                        myMessage.setSender(parser.nextText());
                    }else if(parser.getName().equals("content")){
                        myMessage.setContent(parser.nextText());
                    }else if(parser.getName().equals("isread")){
                        myMessage.setIsRead(Integer.parseInt(parser.nextText()));
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if(parser.getName().equals("message")){
                        list.add(myMessage);
                    }
                    break;
            }
//
            eventType = parser.next();
        }

        return list;
    }
}
