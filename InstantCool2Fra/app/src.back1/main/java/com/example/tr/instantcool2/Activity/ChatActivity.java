package com.example.tr.instantcool2.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tr.instantcool2.JavaBean.MyMessage;
import com.example.tr.instantcool2.LocalDB.UserInfoSotrage;
import com.example.tr.instantcool2.R;
import com.example.tr.instantcool2.Utils.ShowInfoUtil;
import com.example.tr.instantcool2.Utils.StreamUtil;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class ChatActivity extends AppCompatActivity {

    private static final int ADDADAPTER = 1;
    private int messageCounts=0;
    private String friendaccount;
    private String friendname;
    private EditText etMessage;
    private Button btnSend;
    private ListView lvMessage;
    private TextView tvFriendName;
    private List<MyMessage> lists;
    private Timer timerMessage;
    private TimerTask taskMessage;
    boolean isInCurrentActivity=true;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==ADDADAPTER&&isInCurrentActivity){
                //如果没有消息变动就没必要刷新
                if(lists==null)return;
                if(messageCounts==lists.size())return;

                //有数据更新 跳转到底部
                //TODO
//                并且设置好友发的消息已读
                lvMessage.setAdapter(new MyAdapter());
                lvMessage.setSelection(ListView.FOCUS_DOWN);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //在当前activity
        isInCurrentActivity=true;
        Intent intent = getIntent();
        friendaccount = intent.getStringExtra("friendaccount");
        friendname = intent.getStringExtra("friendname");

        tvFriendName = (TextView) findViewById(R.id.tv_friend_name_chat_activity);
        etMessage = (EditText) findViewById(R.id.et_message_chat_activity);
        lvMessage = (ListView) findViewById(R.id.lv_show_message_chat_activity);
        btnSend = (Button) findViewById(R.id.btn_send_chat_activity);

        //设置聊天对象
        tvFriendName.setText(friendname.trim());

        //设置线程不断初始化消息源达到刷新消息
        initMessageList();


        //每隔一秒刷新
        updateMessage();

        //监听
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etMessage.getText().toString().trim();
                if(content.equals("")){
                    ShowInfoUtil.showInfo(getApplicationContext(),"空消息！");
                    return;
                }
                sendMessage(content);
                etMessage.setText("");
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        setUnread2Read();
    }

    private void setUnread2Read(){
//        friendaccount;
//        UserInfoSotrage.Account;

        new Thread(){
            @Override
            public void run() {
                try{
                    String path = "http://39.108.159.175/phpworkplace/androidLogin/UpdateTheMessageUnreadCount.php?owner="+ UserInfoSotrage.Account+"&receiver="+friendaccount;
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int code = conn.getResponseCode();
                    if(200==code){
                        conn.getInputStream();
                    }
                }catch (Exception e){}
            }
        }.start();

    }

    private void sendMessage(final String content){
        new Thread(){
            @Override
            public void run() {
                try {
                    Log.d("send", "run:********** ");
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");//设置日期格式
                    final String nowTime = df.format(new Date());
                    String path = "http://39.108.159.175/phpworkplace/androidLogin/TheSendMessage.php" +
                            "?owner="+UserInfoSotrage.Account+"&direct=1&targetaccount="+friendaccount+"&content="+ URLEncoder.encode(content,"utf-8")+"&isread=0&time="+URLEncoder.encode(nowTime,"utf-8");

                    Log.d("send", "run:"+path);
                    //linking
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    int responseCode = connection.getResponseCode();
                    if(200==responseCode){
                        InputStream inputStream = connection.getInputStream();
                        String reslut = StreamUtil.readStream(inputStream);
                        if(reslut.equals("error")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ShowInfoUtil.showInfo(getApplicationContext(),"服务器繁忙");
                                }
                            });
                        }else{
                            //成功发送
                            initMessageList();
                        }
                    }else{
                        //failed connetc server
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
    private void updateMessage(){
        taskMessage = new TimerTask() {
            @Override
            public void run() {
                initMessageList();

            }
        };
        timerMessage = new Timer();
        timerMessage.schedule(taskMessage,1000,1000);
    }

    @Override
    protected void onDestroy() {
        isInCurrentActivity=false;
        super.onDestroy();
        timerMessage.cancel();
    }

    private void initMessageList() {
        new Thread(){
            @Override
            public void run() {

                String path = "http://39.108.159.175/phpworkplace/androidLogin/GetTheMessageASU.php?owner="+ UserInfoSotrage.Account+"&receiver="+friendaccount;
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int code = conn.getResponseCode();
                    if(200==code){
                        //处理好数据
                        InputStream in = conn.getInputStream();
//                        Log.d("chatActivity", "*********");
                        lists = StreamUtil.XmlParserMessage(in);
//                        Log.d("chatActivity", "run: "+lists.size()+lists.get(0).getContent());
                        //通知适配
                        Message msg = new Message();
                        msg.what=ADDADAPTER;
                        handler.sendMessage(msg);
                    }else{
                        //连接服务器失败
                    }
                } catch (Exception e) {
                }

            }
        }.start();
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return lists.size();
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

            View view;
            if(convertView==null){
                view = View.inflate(getApplicationContext(),R.layout.item_listview_chat_activity,null);
            }else{
                view = convertView;
            }

            messageCounts=lists.size();

            TextView tvTime  = (TextView) view.findViewById(R.id.tv_time_chat_activity);
            TextView tvName  = (TextView) view.findViewById(R.id.tv_name_chat_activity);
            TextView tvIsread  = (TextView) view.findViewById(R.id.tv_isread_chat_activity);
            TextView tvMessage  = (TextView) view.findViewById(R.id.tv_show_message_chat_activity);

            MyMessage myMessage = lists.get(position);
            tvTime.setText(myMessage.getTime().trim());
//            tvName.setText(myMessage.getSender().trim());
            tvMessage.setText(myMessage.getContent().trim());

            //设置背景和对方信息位置  自己：右边 对方：左边
            if(myMessage.getSender().trim().equals(UserInfoSotrage.Account)){

                //我发送的是否已读
                if(myMessage.getIsRead()==0){
                    //unread
                    tvIsread.setVisibility(View.VISIBLE);
                    tvIsread.setText("未读");
                }else{
                    //read
                }
                //自己发的 发送到右边
                tvName.setText(UserInfoSotrage.Name);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.rightMargin=20;
                tvName.setLayoutParams(params);
                tvName.setBackgroundColor(getResources().getColor(R.color.colorBlue));

//                params = (RelativeLayout.LayoutParams) tvMessage.getLayoutParams();
//                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//                tvMessage.setLayoutParams(params);

                params = (RelativeLayout.LayoutParams) tvMessage.getLayoutParams();
                params.addRule(RelativeLayout.LEFT_OF,R.id.tv_name_chat_activity);
                params.addRule(RelativeLayout.RIGHT_OF,0);
                tvMessage.setLayoutParams(params);
            }else{
                //对方的消息
                tvName.setText(friendname);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_LEFT);
                params.leftMargin=20;
                tvName.setLayoutParams(params);
                tvName.setBackgroundColor(getResources().getColor(R.color.colorDeepGreen));
            }




            return view;
        }
    }
}
