package com.example.tr.instantcool2.Activity;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.tr.instantcool2.IndicatorView.TopBarIndicatorView;
import com.example.tr.instantcool2.LocalDB.UserInfoSotrage;
import com.example.tr.instantcool2.R;
import com.example.tr.instantcool2.Utils.ShowInfoUtil;
import com.example.tr.instantcool2.Utils.StreamUtil;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class FillInfoActivity extends AppCompatActivity implements TopBarIndicatorView.TopBarClickedListener{

    private TopBarIndicatorView topBarIndicatorView;
    private EditText et_name;
    private Button btn_submit;
    private String IconId;
    private ImageView userRIcon1,userRIcon2,userRIcon3,userRIcon4;
    private ImageView userRIcon5,userRIcon6,userRIcon7,userRIcon8;
    private ImageView userRIcon9;
    private ImageView UserIconSelect1,UserIconSelect2,UserIconSelect3;
    private ImageView UserIconSelect4,UserIconSelect5,UserIconSelect6;
    private ImageView UserIconSelect7,UserIconSelect8,UserIconSelect9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);

        //init
        btn_submit = (Button) findViewById(R.id.btn_fill_information);
        et_name = (EditText) findViewById(R.id.et_fill_information);
//        IconId = (EditText) findViewById(R.id.et_fill_information_icon);
        topBarIndicatorView = (TopBarIndicatorView) findViewById(R.id.top_bar_container_fill_information);

        //user icon view
        initUserIcon();
        initUserSelectIcon();
        //init user icon click and change user icon id
        initUserIconClick();
        //init top bar
        initTopbar();
        //init btn submit and upload userinfo
        initBtnSubmit();

    }

    private void initUserSelectIcon() {
        UserIconSelect1 = (ImageView) findViewById(R.id.iv_user_select1);
        UserIconSelect2 = (ImageView) findViewById(R.id.iv_user_select2);
        UserIconSelect3 = (ImageView) findViewById(R.id.iv_user_select3);
        UserIconSelect4 = (ImageView) findViewById(R.id.iv_user_select4);
        UserIconSelect5 = (ImageView) findViewById(R.id.iv_user_select5);
        UserIconSelect6 = (ImageView) findViewById(R.id.iv_user_select6);
        UserIconSelect7 = (ImageView) findViewById(R.id.iv_user_select7);
        UserIconSelect8 = (ImageView) findViewById(R.id.iv_user_select8);
        UserIconSelect9 = (ImageView) findViewById(R.id.iv_user_select9);
    }

    private void initBtnSubmit() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_name.getText().toString().trim().equals("")){
                    ShowInfoUtil.showInfo(getApplicationContext(),"请输入你的名字！");
                    return;
                }
                UserInfoSotrage.Name = et_name.getText().toString().trim();
                //上传昵称
                new Thread(){
                    @Override
                    public void run() {
                        URL url = null;
                        try {
                            String path ="http://39.108.159.175/phpworkplace/androidLogin/SetUserName.php?name="+ URLEncoder.encode(UserInfoSotrage.Account,"utf-8")+"&nickname="+URLEncoder.encode( UserInfoSotrage.Name,"utf-8");
                            url = new URL(path);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(5000);
                            int code = connection.getResponseCode();
                            if(200==code){
                                if(StreamUtil.readStream(connection.getInputStream()).trim().equals("success")){

                                }
                            }else{
                                ShowInfoUtil.showInfo(getApplication(),"链接服务器失败！");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                //上传icon
                new Thread(){
                    @Override
                    public void run() {
                        URL url = null;
                        try {
                            String path ="http://39.108.159.175/phpworkplace/androidLogin/SetUserIcon.php?name="
                                    + URLEncoder.encode(UserInfoSotrage.Account,"utf-8")+"&icon="+URLEncoder.encode(IconId.trim(),"utf-8");
                            url = new URL(path);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(5000);
                            int code = connection.getResponseCode();
                            if(200==code){
                                if(StreamUtil.readStream(connection.getInputStream()).trim().equals("success")){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ShowInfoUtil.showInfo(getApplication(),"信息已完善");
                                        }
                                    });
                                    UserInfoSotrage.icon = IconId.trim();
                                    SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("name",UserInfoSotrage.Name);
                                    editor.putString("icon",UserInfoSotrage.icon);
                                    editor.putBoolean("isLogin",true);
                                    editor.apply();
                                }
                            }else{
                                ShowInfoUtil.showInfo(getApplication(),"链接服务器失败！");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                //开启homeActivity
                Intent intent = new Intent(getApplication(), HomeActivity.class);
                startActivity(intent);
                //开启后可以杀死自己了不然用户点击返回按钮很丑
                finish();
            }
        });
    }

    private void initUserIcon() {
        userRIcon1 = (ImageView) findViewById(R.id.iv_user_r_icon1);
        userRIcon2 = (ImageView) findViewById(R.id.iv_user_r_icon2);
        userRIcon3 = (ImageView) findViewById(R.id.iv_user_r_icon3);
        userRIcon4 = (ImageView) findViewById(R.id.iv_user_r_icon4);
        userRIcon5 = (ImageView) findViewById(R.id.iv_user_r_icon5);
        userRIcon6 = (ImageView) findViewById(R.id.iv_user_r_icon6);
        userRIcon7 = (ImageView) findViewById(R.id.iv_user_r_icon7);
        userRIcon8 = (ImageView) findViewById(R.id.iv_user_r_icon8);
        userRIcon9 = (ImageView) findViewById(R.id.iv_user_r_icon9);
    }

    private void initUserIconClick() {
//        userRIcon1.setBackgroundResource();
        MyUserIconClickListener clickListener = new MyUserIconClickListener();
        userRIcon1.setOnClickListener(clickListener);
        userRIcon2.setOnClickListener(clickListener);
        userRIcon3.setOnClickListener(clickListener);
        userRIcon4.setOnClickListener(clickListener);
        userRIcon5.setOnClickListener(clickListener);
        userRIcon6.setOnClickListener(clickListener);
        userRIcon7.setOnClickListener(clickListener);
        userRIcon8.setOnClickListener(clickListener);
        userRIcon9.setOnClickListener(clickListener);
    }

    private class MyUserIconClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_user_r_icon1:
                    ShowInfoUtil.showInfo(getApplicationContext(),"selected 11");
                    IconId = "11";
                    UnSelectIcon();
                    UserIconSelect1.setImageResource(R.mipmap.selected);
                    break;
                case R.id.iv_user_r_icon2:
                    IconId = "12";
                    UnSelectIcon();
                    UserIconSelect2.setImageResource(R.mipmap.selected);
                    break;
                case R.id.iv_user_r_icon3:
                    IconId = "13";
                    UnSelectIcon();
                    UserIconSelect3.setImageResource(R.mipmap.selected);
                    break;
                case R.id.iv_user_r_icon4:
                    IconId = "14";
                    UnSelectIcon();
                    UserIconSelect4.setImageResource(R.mipmap.selected);
                    break;
                case R.id.iv_user_r_icon5:
                    IconId = "15";
                    UnSelectIcon();
                    UserIconSelect5.setImageResource(R.mipmap.selected);
                    break;
                case R.id.iv_user_r_icon6:
                    IconId = "16";
                    UnSelectIcon();
                    UserIconSelect6.setImageResource(R.mipmap.selected);
                    break;
                case R.id.iv_user_r_icon7:
                    IconId = "17";
                    UnSelectIcon();
                    UserIconSelect7.setImageResource(R.mipmap.selected);
                    break;
                case R.id.iv_user_r_icon8:
                    IconId = "18";
                    UnSelectIcon();
                    UserIconSelect8.setImageResource(R.mipmap.selected);
                    break;
                case R.id.iv_user_r_icon9:
                    IconId = "19";
                    UnSelectIcon();
                    UserIconSelect9.setImageResource(R.mipmap.selected);
                    break;
            }
        }
    }

    private void UnSelectIcon(){
        UserIconSelect1.setImageResource(R.mipmap.un_selected);
        UserIconSelect2.setImageResource(R.mipmap.un_selected);
        UserIconSelect3.setImageResource(R.mipmap.un_selected);
        UserIconSelect4.setImageResource(R.mipmap.un_selected);
        UserIconSelect5.setImageResource(R.mipmap.un_selected);
        UserIconSelect6.setImageResource(R.mipmap.un_selected);
        UserIconSelect7.setImageResource(R.mipmap.un_selected);
        UserIconSelect8.setImageResource(R.mipmap.un_selected);
        UserIconSelect9.setImageResource(R.mipmap.un_selected);
    }

    private void initTopbar() {
        topBarIndicatorView.setTitle("完善信息");
        topBarIndicatorView.setTopBarOnClickedListener(this);
    }

    @Override
    public void OnBackClicked() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Instrumentation inst = new Instrumentation();
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
            }
        }.start();
    }
}
