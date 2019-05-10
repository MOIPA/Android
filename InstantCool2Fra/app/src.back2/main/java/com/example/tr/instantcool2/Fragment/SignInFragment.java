package com.example.tr.instantcool2.Fragment;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.tr.instantcool2.Activity.HomeActivity;
import com.example.tr.instantcool2.IndicatorView.TopBarIndicatorView;
import com.example.tr.instantcool2.LocalDB.UserInfoSotrage;
import com.example.tr.instantcool2.R;
import com.example.tr.instantcool2.Utils.ShowInfoUtil;
import com.example.tr.instantcool2.Utils.StreamUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static android.content.Context.MODE_PRIVATE;


public class SignInFragment extends Fragment implements TopBarIndicatorView.TopBarClickedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private TopBarIndicatorView topBarIndicatorView;
    private CheckBox cb_remember;
    private Button btnSignIn;
    private EditText et_account;
    private EditText et_pwd;
    private String account;
    private String pwd;
    private Boolean isLogin =false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        cb_remember = (CheckBox) view.findViewById(R.id.cb_sign_in_remember);
        btnSignIn = (Button) view.findViewById(R.id.btn_sign_in_login);
        et_account = (EditText) view.findViewById(R.id.et_sign_in_account);
        et_pwd = (EditText) view.findViewById(R.id.et_sign_in_pwd);
        topBarIndicatorView = (TopBarIndicatorView) view.findViewById(R.id.topbar_container_sign_in);
        initTopBar();

        account = et_account.getText().toString().trim();
        pwd=et_pwd.getText().toString().trim();

        //记住登陆逻辑
        SharedPreferences sp = getActivity().getSharedPreferences("login",getContext().MODE_PRIVATE);
        cb_remember.setChecked(sp.getBoolean("cb",false));
        et_account.setText(sp.getString("account",""));
        et_pwd.setText(sp.getString("pwd",""));

        //登陆按钮事件
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            /**
             * 登陆逻辑
             * 首先检查是否可登陆 如果被登陆或者输入不正确不可登陆
             * 成功则保存信息到全局并且开启主界面
             * @param v
             */
            @Override
            public void onClick(View v) { //登陆逻辑
                account=et_account.getText().toString().trim();
                pwd=et_pwd.getText().toString().trim();
                Log.d("Login",account+":"+pwd);

                //判断是否可以登录
                if(account.equals("")||pwd.equals("")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowInfoUtil.showInfo(getContext(),"need password and username!");
                        }
                    });
                }
                //判断是否已经被登陆
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            String checkPath = "http://39.108.159.175/phpworkplace/androidLogin/CheckLoginStatus.php?name="+ URLEncoder.encode(account,"utf-8");
                            Log.d("Login", "Check path is:"+checkPath);
                            URL url = new URL(checkPath);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(5000);
                            int code1 = conn.getResponseCode();
                            if(200==code1){
                                final String satus = StreamUtil.readStream(conn.getInputStream()).trim();
                                Log.d("Login", "Login Status is:"+satus+":"+ UserInfoSotrage.Account);

                                if(satus.equals("1")){
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ShowInfoUtil.showInfo(getContext(),"该账户已经登陆！");
                                            isLogin = true;
                                            Log.d("Login", "Unalbe to Login "+isLogin);
                                            return;
                                        }
                                    });
                                }else{
                                    //未登录 可以执行登陆
                                    isLogin=false;
                                    Log.d("Login", "OK to Login :"+satus);
                                    new Thread(){
                                        @Override
                                        public void run() {
                                            //登陆业务

                                            try {
                                                final String path  = "http://39.108.159.175/phpworkplace/androidLogin/Login.php?name="+URLEncoder.encode(account,"utf-8")+"&password="+URLEncoder.encode(pwd,"utf-8");
                                                URL url = new URL(path);
                                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                                connection.setConnectTimeout(5000);
                                                connection.setRequestMethod("GET");
                                                int code = connection.getResponseCode();
                                                if(200==code){
                                                    InputStream inputStream = connection.getInputStream();
                                                    final String result = StreamUtil.readStream(inputStream).trim();
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
//                                        ShowInfoUtil.showInfo(getContext(),result);
                                                            if (result.trim().equals("success")){
                                                                ShowInfoUtil.showInfo(getContext(),"登陆成功");
                                                                //存储登陆用户昵称信息
                                                                UserInfoSotrage.Account =account;
                                                                UserInfoSotrage.pwd=pwd;

                                                                new Thread(){
                                                                    @Override
                                                                    public void run() {
                                                                        try {
                                                                            final String infoPath ="http://39.108.159.175/phpworkplace/androidLogin/GetNickName.php?name="+ URLEncoder.encode(UserInfoSotrage.Account,"utf-8");
                                                                            URL url = new URL(infoPath);
                                                                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                                            conn.setRequestMethod("GET");
                                                                            conn.setConnectTimeout(5000);
                                                                            int code1 = conn.getResponseCode();
                                                                            if(200==code1){
                                                                                final String nickname = StreamUtil.readStream(conn.getInputStream()).trim();
                                                                                UserInfoSotrage.Name =nickname;
                                                                                Log.d("Login", "run: "+ UserInfoSotrage.Name +":"+ UserInfoSotrage.Account +":"+ UserInfoSotrage.pwd);
                                                                            }else{
                                                                                getActivity().runOnUiThread(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        ShowInfoUtil.showInfo(getContext(),"链接服务器失败");
                                                                                    }
                                                                                });
                                                                            }
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }.start();
                                                                //修改用户服务器登陆信息
                                                                new Thread(){
                                                                    @Override
                                                                    public void run() {
                                                                        try {
                                                                            String statusPath = "http://39.108.159.175/phpworkplace/androidLogin/SetUserStatus.php?name="+URLEncoder.encode(account,"utf-8")+"&status="+1;
                                                                            URL url = new URL(statusPath);
                                                                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                                            conn.setRequestMethod("GET");
                                                                            conn.setConnectTimeout(5000);
                                                                            int code1 = conn.getResponseCode();
                                                                            if(200==code1){
                                                                                final String satus = StreamUtil.readStream(conn.getInputStream()).trim();
                                                                                Log.d("Login", "status is: "+satus);
                                                                            }else{
                                                                                getActivity().runOnUiThread(new Runnable() {
                                                                                    @Override
                                                                                    public void run() {
                                                                                        ShowInfoUtil.showInfo(getContext(),"链接服务器失败");
                                                                                    }
                                                                                });
                                                                            }
                                                                        } catch (Exception e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                }.start();
                                                                //先沉睡2秒确定获取登陆状态启动homeactivity
                                                                if(!isLogin){
                                                                    Log.d("Login", "islogin:"+isLogin);
                                                                    Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                                                                    startActivity(homeIntent);
                                                                }

                                                            }else{
                                                                ShowInfoUtil.showInfo(getContext(),"登陆失败");
                                                            }
                                                        }
                                                    });
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }.start();
                                }

                            }else{
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShowInfoUtil.showInfo(getContext(),"链接服务器失败");
                                    }
                                });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                //保存记住登陆逻辑
                if(cb_remember.isChecked()){
                    SharedPreferences preferences = getActivity().getSharedPreferences("login", 1);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("cb",true);
                    editor.putString("account",account);
                    editor.putString("pwd",pwd);
                    editor.commit();
                }

            }
        });

        return view;
    }

    private void initTopBar() {
        topBarIndicatorView.setTitle("登陆");
        topBarIndicatorView.setTopBarOnClickedListener(this);
    }


    @Override
    public void OnBackClicked() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                ShowInfoUtil.showInfo(getContext(),"返回执行");
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                    }
                }.start();
            }
        });
    }
}
