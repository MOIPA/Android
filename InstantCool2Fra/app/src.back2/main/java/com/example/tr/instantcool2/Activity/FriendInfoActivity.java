package com.example.tr.instantcool2.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tr.instantcool2.IndicatorView.TopBarIndicatorView;
import com.example.tr.instantcool2.LocalDB.UserInfoSotrage;
import com.example.tr.instantcool2.R;
import com.example.tr.instantcool2.Utils.ShowInfoUtil;
import com.example.tr.instantcool2.Utils.ot;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class FriendInfoActivity extends Activity implements TopBarIndicatorView.TopBarClickedListener{

    private TextView tv_friendName;
    private TextView tv_friendAccount;
    private ImageView iv_friendIcon;
    private String willAddFriend;
    private Button btnSend;
    private Button btnAdd;
    private String friendname;
    private String friendaccount;
    private String friendIcon;
    private TopBarIndicatorView topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);

        topbar = (TopBarIndicatorView) findViewById(R.id.top_bar_friend_info);
        initTopbar();
        final Intent intent = getIntent();
        friendname = intent.getStringExtra("friendname");
        friendaccount = intent.getStringExtra("friendaccount");
        willAddFriend = intent.getStringExtra("willAddFriend");
        friendIcon = intent.getStringExtra("friendicon");

        tv_friendAccount = (TextView) findViewById(R.id.tv_friend_info_account);
        tv_friendName = (TextView) findViewById(R.id.tv_friend_info_name);
        iv_friendIcon = (ImageView) findViewById(R.id.iv_friend_image_friend_info);
        btnAdd = (Button) findViewById(R.id.btn_friend_info_add_friend);
        tv_friendAccount.setText(friendaccount);
        tv_friendName.setText(friendname);
        Log.d("icon", "onCreate: "+friendIcon);
        iv_friendIcon.setImageResource(ot.getImageId(Integer.parseInt(friendIcon)));

        btnSend = (Button) findViewById(R.id.btn_friend_info_send_message);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //进入交流页面 并且判断是否可以插入Conversation 可以就插入
                //判断逻辑
                InsertConversation();
                Intent intent = new Intent(FriendInfoActivity.this,ChatActivity.class);
                intent.putExtra("friendaccount",friendaccount);
                intent.putExtra("friendname",friendname);
                intent.putExtra("friendicon",friendIcon);

                startActivity(intent);


            }
        });

        if(willAddFriend!=null&&willAddFriend.equals("yes")){
            //如果添加好友不为空则显示按钮并且添加监听
            //并且取消发送信息按钮
            btnSend.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Thread(){
                        @Override
                        public void run() {
                            if(UserInfoSotrage.Account.equals(friendaccount)){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShowInfoUtil.showInfo(getApplicationContext(),"your self!");
                                    }
                                });
                            }
                            else  {

                                new Thread(){
                                    @Override
                                    public void run() {

                                        try {
                                            final String path = "http://39.108.159.175/phpworkplace/androidLogin/SendInvitation.php?inviter="
                                                + URLEncoder.encode(UserInfoSotrage.Account,"utf-8")+"&targetaccount="
                                                +URLEncoder.encode(friendaccount,"utf-8")+"&isagree=0";
                                            URL url = new URL(path);
                                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                            conn.setRequestMethod("GET");
                                            int code = conn.getResponseCode();
                                            if(200==code){

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }.start();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShowInfoUtil.showInfo(getApplicationContext(),"请求已发送!");
                                    }
                                });

                            }

                        }
                    }.start();
                }
            });
        }
    }

    private void initTopbar() {
        topbar.setTitle("好友详情");
        topbar.setTopBarOnClickedListener(this);
    }

    //插入会话
    private void InsertConversation(){
        new Thread(){
            @Override
            public void run() {
                try{

                    String path="http://39.108.159.175/phpworkplace/androidLogin/InsertConversation.php?" +
                            "owner="+
                            URLEncoder.encode(UserInfoSotrage.Account,"utf-8")+"&targetaccount="
                            +URLEncoder.encode(friendaccount,"utf-8")+"&targetname="+URLEncoder.encode(friendname,"utf-8")
                            +"&icon="+friendIcon;

//                    Log.d("FriendInfo", path);
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int code=conn.getResponseCode();
                    if(200==code){
                        //没必要  反正插入不成功也没事
                    }else{
                        Log.d("FriendInfo", "链接服务器失败");
                    }


                }catch(Exception e){}
            }
        }.start();
    }

    @Override
    public void OnBackClicked() {
        finish();
    }
}
