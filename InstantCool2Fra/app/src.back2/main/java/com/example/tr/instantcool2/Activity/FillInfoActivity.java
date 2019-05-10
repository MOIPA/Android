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
    private EditText etIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_info);

        btn_submit = (Button) findViewById(R.id.btn_fill_information);
        et_name = (EditText) findViewById(R.id.et_fill_information);
        etIcon = (EditText) findViewById(R.id.et_fill_information_icon);

        topBarIndicatorView = (TopBarIndicatorView) findViewById(R.id.top_bar_container_fill_information);
        initTopbar();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                    + URLEncoder.encode(UserInfoSotrage.Account,"utf-8")+"&icon="+URLEncoder.encode(etIcon.getText().toString().trim(),"utf-8");
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
                                    UserInfoSotrage.icon = etIcon.getText().toString().trim();
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
            }
        });



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
