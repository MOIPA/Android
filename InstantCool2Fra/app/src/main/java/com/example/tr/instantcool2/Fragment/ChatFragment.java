
package com.example.tr.instantcool2.Fragment;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.tr.instantcool2.Activity.ChatActivity;
import com.example.tr.instantcool2.Activity.ConfirmInvitationActivity;
import com.example.tr.instantcool2.IndicatorView.List_Item_ChatFragment_IndicatorView;
import com.example.tr.instantcool2.IndicatorView.TopBarIndicatorView;
import com.example.tr.instantcool2.JavaBean.Conversation;
import com.example.tr.instantcool2.LocalDB.UserInfoSotrage;
import com.example.tr.instantcool2.R;
import com.example.tr.instantcool2.Utils.NetWorkUtil;
import com.example.tr.instantcool2.Utils.ShowInfoUtil;
import com.example.tr.instantcool2.Utils.StreamUtil;
import com.example.tr.instantcool2.Utils.ChosePicByIconId;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class ChatFragment extends Fragment implements TopBarIndicatorView.TopBarClickedListener{

    private final static int UPDATE_ADAPTER=1;
    private final static int UPDATE_UNREAD_COUNT=2;
    private final static int UPDATA_INVITATION = 3;
    private MyAdapter adapter;
    private Boolean isFirstStart = true;
    private ListView lv_conversation;
    private TopBarIndicatorView topbarview;
    private List_Item_ChatFragment_IndicatorView listItemChatFragmentIndicatorView;
    private TimerTask taskUnread;
    private Timer timer;
    private boolean flag=true;
    private TimerTask taskRefreshUnread;
    private Timer timerRefreshUnread;
    private TimerTask taskRefreshData;
    private Timer timerRefreshData;
    private int conversationListCountsPre = -1;
    private int conversationListCountsAft = 0;
    private List<String> invitaitonList;
    private List<Conversation> conversationList;
    private String preUnreadCount = "0";

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//TODO
            if(msg.what==0){

                Log.d("uirefresh", "handleMessage process");

//                if(conversationListCountsAft==0||conversationListCountsPre==0){
//                    Log.d("tmptest", "handleMessage: "+conversationList.size()+"aft:"+conversationListCountsAft+":pre"+conversationListCountsPre);
//                    adapter = new MyAdapter();
//                    lv_conversation.setAdapter(adapter);
//
//                }
//                if(conversationListCountsPre!=conversationListCountsAft) {
                    Log.d("tmptest", "msg " + msg.what + ":" + conversationList.size() + "aft:" + conversationListCountsAft + ":pre" + conversationListCountsPre);
                    adapter = new MyAdapter();
                    lv_conversation.setAdapter(adapter);
                    conversationListCountsPre = conversationListCountsAft;
//                }
            }
            else if(msg.what==2){
                Bundle data = msg.getData();
                int position = data.getInt("position");
                String unreadcount = data.getString("unreadcount");
                View child = lv_conversation.getChildAt(position);
                List_Item_ChatFragment_IndicatorView indicatorV = (List_Item_ChatFragment_IndicatorView) child.findViewById(R.id.indicator_list_view_user);
                indicatorV.setIv_unreadCount(Integer.parseInt(unreadcount));
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化数据
        Log.d("tmptest", "handleMessage: on create");
        taskRefreshData = new TimerTask() {
            @Override
            public void run() {
                initCnvrstnAndInvttn();
            }
        };
        timerRefreshData = new Timer();
        timerRefreshData.schedule(taskRefreshData,0,2000);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat,container,false);
        topbarview = (TopBarIndicatorView) view.findViewById(R.id.topbar_container_chat_fragment);
        lv_conversation = (ListView) view.findViewById(R.id.lv_conversation_chat_fragment);


        initTopbar();
        //开启检测消息未读数量线程
        initUnreadDetech();
        //开启检测是否有好友请求线程
//        initInvitationDetech();



        //listview item点击事件 点击后进入聊天界面并且设置未读数量为0
        lv_conversation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                listItemIndicatorView.setIv_unreadCount(29);
                //点击后修改Conversation list数据源里的unreadCount然后重新绑定后启动ChatActivity

                int firstPosition = lv_conversation.getFirstVisiblePosition();
//                if(position-firstPosition>0){
                View itemView = lv_conversation.getChildAt(position);
                List_Item_ChatFragment_IndicatorView view1 = (List_Item_ChatFragment_IndicatorView) itemView.findViewById(R.id.indicator_list_view_user);
                view1.setIv_unreadCount(0);
//                }
                //TODO 进入交流界面

                //如果acccount是新好友请求 进入请求页面
                Conversation conversation = conversationList.get(position);
                String targetaccount = conversation.getTargetaccount();
                String targetname = conversation.getTargetname();
                if(targetaccount.equals("有新的好友请求！")){
                    //进入请求界面
                    Intent intent = new Intent(getActivity(), ConfirmInvitationActivity.class);
                    intent.putExtra("targetaccount",targetname);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    intent.putExtra("friendaccount",targetaccount);
                    intent.putExtra("friendname",targetname);
                    startActivity(intent);
                }


//                Intent intent = new Intent(getActivity(), ChatActivity.class);
//                startActivity(intent);
            }

        });

        return view;
    }
    //初始化invitation和conversation 加入list  判断list大小是否和之前的一样 一样则不必刷新
    private void initCnvrstnAndInvttn(){
        Log.d("tmptest", "handleMessage: start init");
        //TODO
        //初始化和好友的会话
        initConversationList();
    }
    //初始化list数据
    private void initConversationList(){

        Log.d("tmptest", "handleMessage: start init conversation");
        new Thread(){
            @Override
            public void run() {
                String path = "http://39.108.159.175/phpworkplace/androidLogin/GetConversation.php?owner="+ UserInfoSotrage.Account;
                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    int responseCode = connection.getResponseCode();
                    if(responseCode==200){
                        InputStream inputStream = connection.getInputStream();
                        //解析xml流信息
                        conversationList = new ArrayList<Conversation>();
                        conversationList = StreamUtil.XmlParserConversation(inputStream);

                        //初始化好友邀请并且把好友邀请添加到了conversationList；随后传递主线程要求刷新
                        initInvitationList();
                    }else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowInfoUtil.showInfo(getContext(),"初始化会话失败！");
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initInvitationList(){
        Log.d("tmptest", "handleMessage: start init invitation");
        try {
            String path="http://39.108.159.175/phpworkplace/androidLogin/GetInvitation.php?receiver="+ URLEncoder.encode(UserInfoSotrage.Account,"utf-8");
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            int code = connection.getResponseCode();
            InputStream in= connection.getInputStream();

//                    Log.d("ChatFragmentInvitation", "return info"+StreamUtil.readStream(in));
            if(200==code){
                //复制流信息 待会解析两次
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = in.read(buffer)) > -1 ) {
                    baos.write(buffer, 0, len);
                }
                baos.flush();
                //第一次解析用来判断值是否为空
                InputStream streamJudge = new ByteArrayInputStream(baos.toByteArray());
                String returnInfo = StreamUtil.readStream(streamJudge);
//                        Log.d("ChatFragmentInvitation", "return info"+returnInfo);
//                        Log.d("ChatFragmentInvitation", "return info"+StreamUtil.readStream(in));

                if(returnInfo.equals("")){
//                            Log.d("ChatFragmentInvitation", "No Inviter");
                    //没人邀请
                    Message msg = new Message();
//                    Log.d("tmptest", "handleMessage: start send:"+conversationList.size());
//                    msg.what=0;
                    handler.sendMessage(msg);
                }else{
                    //有人邀请
                    //解析xml流
                    Log.d("ChatFragmentInvitation", "解析中");
                    InputStream streamInfo = new ByteArrayInputStream(baos.toByteArray());
                    invitaitonList = new ArrayList<String>();
                    invitaitonList = StreamUtil.XmlParserInvitation(streamInfo);
                    if(invitaitonList==null){
                        Log.d("ChatFragmentInvitation", "解析失败 返回为空");
                        return;
                    }

                    for(int i=0;i<invitaitonList.size();i++){
                        Conversation conversation = new Conversation();
                        conversation.setUnreadCount(0);
                        conversation.setTargetaccount("有新的好友请求！");
                        conversation.setTargetname(invitaitonList.get(i));
                        conversation.setIcon(0);
                        //添加到conversationList
                        conversationList.add(conversation);
                    }
                    //征询主线程
                    conversationListCountsAft =conversationList.size();
                    //如果变化发送给主线程
                    if(conversationListCountsAft!=conversationListCountsPre) {
                        Log.d("uirefresh", "handleMessage b: start send: pre:" + conversationListCountsPre+":aft:"+conversationListCountsAft);
                        conversationListCountsPre = conversationListCountsAft;
                        Message msg = new Message();
                        Log.d("uirefresh", "handleMessage a: start send: pre:" + conversationListCountsPre+":aft:"+conversationListCountsAft);
                        msg.what = 0;
                        handler.sendMessage(msg);
                    }
                }
            }else{}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //检测是否有人发送好友邀请 有则交给主线程处理
    //TODO 存在bug  需要修改
    private void initInvitationDetech() {
        timerRefreshUnread = new Timer();
        taskRefreshUnread = new TimerTask() {
            @Override
            public void run() {
                try {
                    String path="http://39.108.159.175/phpworkplace/androidLogin/GetInvitation.php?receiver="+ URLEncoder.encode(UserInfoSotrage.Account,"utf-8");
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");
                    int code = connection.getResponseCode();
                    InputStream in= connection.getInputStream();

//                    Log.d("ChatFragmentInvitation", "return info"+StreamUtil.readStream(in));
                    if(200==code){
                        //复制流信息 待会解析两次
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = in.read(buffer)) > -1 ) {
                            baos.write(buffer, 0, len);
                        }
                        baos.flush();
                        //第一次解析用来判断值是否为空
                        InputStream streamJudge = new ByteArrayInputStream(baos.toByteArray());
                        String returnInfo = StreamUtil.readStream(streamJudge);
//                        Log.d("ChatFragmentInvitation", "return info"+returnInfo);
//                        Log.d("ChatFragmentInvitation", "return info"+StreamUtil.readStream(in));

                        if(returnInfo.equals("")){
//                            Log.d("ChatFragmentInvitation", "No Inviter");
                            //没人邀请
                        }else{
                            //有人邀请
                            //解析xml流
                            Log.d("ChatFragmentInvitation", "解析中");
                            InputStream streamInfo = new ByteArrayInputStream(baos.toByteArray());
                            List<String> invitaitonList = StreamUtil.XmlParserInvitation(streamInfo);
                            if(invitaitonList==null){
                                Log.d("ChatFragmentInvitation", "解析失败 返回为空");
                                return;
                            }
                            String[] invitationS = new String[invitaitonList.size()];
                            Log.d("ChatFragmentInvitation", "解析中 大小："+invitationS.length+";");
                            for(int i=0;i<invitaitonList.size();i++){
                                invitationS[i] = invitaitonList.get(i);
                                Log.d("ChatFragmentInvitation", "run: "+invitationS[i]);
                            }
                            Message msg  = new Message();
                            msg.what=3;
                            Bundle data = new Bundle();
                            data.putStringArray("inviter",invitationS);
                            msg.setData(data);
                            handler.sendMessage(msg);
                        }
                    }else{}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timerRefreshUnread.schedule(taskRefreshUnread,1000,1000);

    }

    //                遍历查看未读数量
    private void initUnreadDetech() {
        initTimerDetechUnreadCount();
//                timer.schedule(taskUnread,0,1000);
    }


    //切换fragment导致丢失绑定 重新绑定
    @Override
    public void onStart() {
        super.onStart();
        if(!isFirstStart)
            lv_conversation.setAdapter(adapter);
        isFirstStart=false;
    }

    //初始化topbar
    private void initTopbar(){
        topbarview.setTitle("会话");
        topbarview.setTopBarOnClickedListener(this);
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    //
    private void initTimerDetechUnreadCount(){
        if(taskUnread!=null)taskUnread.cancel();
        if(timer!=null)timer.cancel();

        timer=new Timer();
        taskUnread = new TimerTask() {
            @Override
            public void run() {
                View v;
                List_Item_ChatFragment_IndicatorView indicatorV;
                Log.d("initUnreadDetech", "run: "+lv_conversation.getChildCount());
                for(int i=0;i<lv_conversation.getChildCount();i++) {
                    v = lv_conversation.getChildAt(i);
                    indicatorV = (List_Item_ChatFragment_IndicatorView) v.findViewById(R.id.indicator_list_view_user);
                    String account = indicatorV.getAccount();
                    if (!account.equals("有新的好友请求！")) {
                        String path = "http://39.108.159.175/phpworkplace/androidLogin/GetTheMessageCountSingle.php?owner=" + UserInfoSotrage.Account + "&receiver=" + account;
                        String unreadCount = NetWorkUtil.DetechUnreadCount(path);
                        Log.d("chatUnread", "run: " + unreadCount);
                        //如果未读数改变通知主线程修改
                        if (!unreadCount.equals(preUnreadCount)) {

                            Log.d("uirefresh", "请求刷新未读数pre:"+preUnreadCount+":aft:"+unreadCount);
                            preUnreadCount = unreadCount;
                            Message msg = new Message();
                            Bundle data = new Bundle();
                            data.putInt("position", i);
                            data.putString("unreadcount", unreadCount);
                            msg.what = 2;
                            msg.setData(data);
                            handler.sendMessage(msg);
//                            indicatorV.setIv_unreadCount(Integer.parseInt(unreadCount));

                        }
                    }
                }
            }
        };
        timer.schedule(taskUnread,0,1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        taskUnread.cancel();
        taskRefreshData.cancel();
//        taskRefreshUnread.cancel();
//        timer.cancel();
//        taskRefreshUnread.cancel();
//        timerRefreshUnread.cancel();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void OnBackClicked() {
        new Thread(){
            @Override
            public void run() {
                Instrumentation inst = new Instrumentation();
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);

                        //发送广播 让底层的SignInUpAcrtivity结束
                        Intent intent = new Intent();
                        intent.setAction("ExitApp");
                        getContext().sendBroadcast(intent);

            }
        }.start();
    }

    //适配器
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return conversationList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final View view;
            if(convertView==null){
                view = View.inflate(getContext(),R.layout.item_listview_chat_fragment,null);
            }else{
                view = convertView;
            }
            listItemChatFragmentIndicatorView = (List_Item_ChatFragment_IndicatorView) view.findViewById(R.id.indicator_list_view_user);
//            TextView tv_account = (TextView) view.findViewById(R.id.tv);
//            TextView tv_name = (TextView) view.findViewById(R.id.item_listview_chat_fragment);
//            Conversation conversation = list.get(position);
//            tv_account.setText("好友账户："+conversation.getTargetaccount());
//            tv_name.setText("好友姓名："+conversation.getTargetname());
            Conversation conversation = conversationList.get(position);
            listItemChatFragmentIndicatorView.setTv_account(conversation.getTargetaccount());
            listItemChatFragmentIndicatorView.setTv_name(conversation.getTargetname());
            //设置用户头像
//            switch (conversation.getIcon()){
//                case 0:
//                    listItemChatFragmentIndicatorView.setIv_userImage(R.mipmap.uicon1);
//                    break;
//                case 1:
//                    listItemChatFragmentIndicatorView.setIv_userImage(R.mipmap.uicon2);
//                    break;
//                case 2:
//                    listItemChatFragmentIndicatorView.setIv_userImage(R.mipmap.uicon3);
//                    break;
//
//            }
            listItemChatFragmentIndicatorView.setIv_userImage(ChosePicByIconId.getImageId(conversation.getIcon()));
//            listItemChatFragmentIndicatorView.setIv_unreadCount(99);

            return view;
        }
    }
}
