package com.example.tr.instantcool2.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.tr.instantcool2.JavaBean.Friend;
import com.example.tr.instantcool2.LocalDB.UserInfoSotrage;
import com.example.tr.instantcool2.R;
import com.example.tr.instantcool2.Utils.ShowInfoUtil;
import com.example.tr.instantcool2.Utils.StreamUtil;
import com.example.tr.instantcool2.Utils.ot;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class ConfirmInvitationActivity extends AppCompatActivity {

    private static final int REQUEST_UPDATE = 1;
    private TextView et_name;
    private TextView et_account;
    private Button btn_agree;
    private Button btn_refuse;
    private Friend friend;
    private ImageView ivFriend;

    private Handler hander = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(msg.what==REQUEST_UPDATE){
                if(friend!=null){
                    String friendAccount = friend.getFriendAccount();
                    String friendName = friend.getFriendName();
//            Log.d("confirmInvitation", "onCreate: "+friendAccount+":"+friendName);
                    et_account.setText(friendAccount);
                    et_name.setText(friendName);
                    ivFriend.setImageResource(ot.getImageId(friend.getFriendIcon()));
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_invitation);
        Intent intent = getIntent();
        final String targetaccount = intent.getStringExtra("targetaccount");
        Log.d("confirmInvitation", "onCreate: "+targetaccount);
        //检测发送者的详细信息
        getDetailInfoInviter(targetaccount);

        //init
        et_account = (TextView) findViewById(R.id.tv_friend_confirm_account);
        et_name = (TextView) findViewById(R.id.tv_friend_confirm_name);
        ivFriend = (ImageView) findViewById(R.id.iv_friend_image_confirm_activity);
        btn_agree = (Button) findViewById(R.id.btn_friend_confirm_confirm);
        btn_refuse = (Button) findViewById(R.id.btn_friend_confirm_refuse);

        btn_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInfoUtil.showInfo(getApplication(),"已拒绝");
                onBackPressed();

                updateInvitation(targetaccount);
            }
        });

        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ShowInfoUtil.showInfo(getApplication(),"已同意");
                agreeInvitation();
                updateInvitation(targetaccount);

                onBackPressed();
            }


        });

    }

    private void updateInvitation(final String targetaccount){
        //设置数据库里的这条invitation已读
        new Thread(){
            @Override
            public void run() {
                try {
                    String path = "http://39.108.159.175/phpworkplace/androidLogin/updateInvitation.php?receiver="+URLEncoder.encode(targetaccount,"utf-8");
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    int code = connection.getResponseCode();
                    if(200==code){
                        connection.getInputStream();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //同意请求逻辑
    private void agreeInvitation() {
        new Thread(){
            @Override
            public void run() {
                try {
                    String path = "http://39.108.159.175/phpworkplace/androidLogin/SetFriend.php?owner="
                            +URLEncoder.encode(UserInfoSotrage.Account,"utf-8")+"&friendaccount="
                            +URLEncoder.encode(friend.getFriendAccount(),"utf-8")+"&friendname="+URLEncoder.encode(friend.getFriendName(),"utf-8")
                            +"&icon="+friend.getFriendIcon();
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    int responseCode = connection.getResponseCode();
                    if(responseCode==200){
                        InputStream inputStream = connection.getInputStream();
                        final String s = StreamUtil.readStream(inputStream);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowInfoUtil.showInfo(getApplicationContext(),s);
                            }
                        });
                    }else{
                        ShowInfoUtil.showInfo(getApplication(),"失败！");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
//                URLEncoder.encode(,"utf-8")
                try {
                    String path = "http://39.108.159.175/phpworkplace/androidLogin/SetFriend.php?owner="+URLEncoder.encode(friend.getFriendAccount(),"utf-8")
                            +"&friendaccount="+URLEncoder.encode(UserInfoSotrage.Account,"utf-8")+"&friendname="+URLEncoder.encode(UserInfoSotrage.Name,"utf-8")
                            +"&icon="+UserInfoSotrage.icon;
                    Log.d("ConfirmInvitation", "run: "+path);
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    int responseCode = connection.getResponseCode();
                    if(responseCode==200){
//                        InputStream inputStream = connection.getInputStream();
//                        final String s = StreamUtil.readStream(inputStream);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ShowInfoUtil.showInfo(getApplicationContext(),s);
//                            }
//                        });
                    }else{
                        ShowInfoUtil.showInfo(getApplication(),"失败！");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void getDetailInfoInviter(final String inviter) {
        new Thread(){

        @Override
        public void run() {
            try {
                String path = "http://39.108.159.175/phpworkplace/androidLogin/SearchPerson.php?targetaccount="+ URLEncoder.encode(inviter,"utf-8");
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                int responseCode = connection.getResponseCode();
                if(responseCode==200){
                    InputStream inputStream = connection.getInputStream();
                    //回送信息
                    List<Friend> friends = StreamUtil.XmlParserFriend(inputStream);
                    friend = friends.get(0);
                    //取得请求者信息   发送刷新请求
                    Message message = new Message();
                    message.what = REQUEST_UPDATE;
                    hander.sendMessage(message);

                    Log.d("confirmInvitation", "friendsize:"+friends.size());
                }else{
                            ShowInfoUtil.showInfo(getApplication(),"失败！");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }.start();
    }
}
