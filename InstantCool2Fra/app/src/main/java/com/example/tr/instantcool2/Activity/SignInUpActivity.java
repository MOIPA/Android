package com.example.tr.instantcool2.Activity;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tr.instantcool2.LocalDB.UserInfoSotrage;
import com.example.tr.instantcool2.R;
import com.example.tr.instantcool2.Utils.ShowInfoUtil;
import com.example.tr.instantcool2.Utils.StreamUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


public class SignInUpActivity extends AppCompatActivity {

    private Boolean isLogin = false;
    private CheckBox cb;
    private EditText email, pass, email2, pass2, confirmPass;
    private RelativeLayout relativeLayout, relativeLayout2;
    private LinearLayout mainLinear, img;
    private TextView signUp, login;
    private ImageView logo, back;
    private LinearLayout.LayoutParams params, params2;
    private FrameLayout.LayoutParams params3;
    private FrameLayout mainFrame;
    private ObjectAnimator animator2, animator1;
    private boolean isOnLoginPage = true;
    private SharedPreferences sp;

    //接受广播后结束应用
    protected BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.broadcastReceiver);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        sp = getSharedPreferences("userInfo", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in_up);

        //在当前activity中注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("ExitApp");
        this.registerReceiver(this.broadcastReceiver, filter);

        //TODO 用户登陆信息
        //判断用户信息 如果sp有登陆信息则直接跳转

        String account = sp.getString("account", "");
        String pwd = sp.getString("pwd", "");
        String icon = sp.getString("icon", "");
        String name = sp.getString("name", "");
        Boolean isLogin = sp.getBoolean("isLogin", false);
        Boolean isFirstLogin = sp.getBoolean("isFirstLogin", true);
        if (isFirstLogin) {
//            ShowInfoUtil.showInfo(getApplicationContext(),"firstTime");
            SharedPreferences.Editor edit = sp.edit();
            edit.putBoolean("isFirstLogin", false);
            edit.apply();
        }//若是第一次开启 启动动画界面
        else {
            //判断是否已经登出 登出需要重新登陆 未登出需要赋值account
            if (isLogin) {
//                ShowInfoUtil.showInfo(getApplicationContext(),"islogin");
                UserInfoSotrage.Account = account;
                UserInfoSotrage.pwd = pwd;
                UserInfoSotrage.icon = icon;
                UserInfoSotrage.Name = name;
                Intent intent = new Intent(SignInUpActivity.this, HomeActivity.class);
                startActivity(intent);
                //杀死自己
                finish();
            } else {
//                ShowInfoUtil.showInfo(getApplicationContext(),"isNotlogin");
            }
        }



        params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params3 = new FrameLayout.LayoutParams(inDp(50), inDp(50));

        cb = (CheckBox) findViewById(R.id.cb_sign_in_up);
        signUp = (TextView) findViewById(R.id.signUp);
        login = (TextView) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.email);
        pass = (EditText) findViewById(R.id.pass);
        img = (LinearLayout) findViewById(R.id.img);
        email2 = (EditText) findViewById(R.id.email2);

//        forgetPass = (TextView) findViewById(R.id.forget);
        pass2 = (EditText) findViewById(R.id.pass2);
        mainFrame = (FrameLayout) findViewById(R.id.mainFrame);
        confirmPass = (EditText) findViewById(R.id.pass3);
        back = (ImageView) findViewById(R.id.backImg);

        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relative2);
        mainLinear = (LinearLayout) findViewById(R.id.mainLinear);

        logo = new ImageView(this);
        logo.setImageResource(R.drawable.logo);
        logo.setLayoutParams(params3);

        relativeLayout.post(new Runnable() {
            @Override
            public void run() {

                logo.setX((relativeLayout2.getRight() / 2));
                logo.setY(inDp(50));
                mainFrame.addView(logo);
            }
        });

        params.weight = (float) 0.75;
        params.weight = (float) 0.75;
        params2.weight = (float) 4.25;

        mainLinear.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                mainLinear.getWindowVisibleDisplayFrame(r);
                int screenHeight = mainFrame.getRootView().getHeight();


                int keypadHeight = screenHeight - r.bottom;


                if (keypadHeight > screenHeight * 0.15) {
                    // keyboard is opened
                    if (params.weight == 4.25) {

                        animator1 = ObjectAnimator.ofFloat(back, "scaleX", (float) 1.95);
                        animator2 = ObjectAnimator.ofFloat(back, "scaleY", (float) 1.95);
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animator1, animator2);
                        set.setDuration(1000);
                        set.start();

                    } else {

                        animator1 = ObjectAnimator.ofFloat(back, "scaleX", (float) 1.75);
                        animator2 = ObjectAnimator.ofFloat(back, "scaleY", (float) 1.75);
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animator1, animator2);
                        set.setDuration(500);
                        set.start();
                    }
                } else {
                    // keyboard is closed
                    animator1 = ObjectAnimator.ofFloat(back, "scaleX", 3);
                    animator2 = ObjectAnimator.ofFloat(back, "scaleY", 3);
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(animator1, animator2);
                    set.setDuration(500);
                    set.start();
                }
            }
        });

        //记住登陆逻辑
        SharedPreferences sp = getSharedPreferences("login", getApplicationContext().MODE_PRIVATE);
        cb.setChecked(sp.getBoolean("cb", false));
        email.setText(sp.getString("account", ""));
        pass.setText(sp.getString("pwd", ""));

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getApplicationContext(),"signup"+email2.getText()+"**"+pass2.getText(),Toast.LENGTH_LONG).show();
                //TODO
                if (!isOnLoginPage)
                    signUpFunction(email2, pass2, confirmPass);

                isOnLoginPage = false;

                if (params.weight == 4.25) {

                    Snackbar.make(relativeLayout, "Sign Up Complete", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                email2.setVisibility(View.VISIBLE);
                pass2.setVisibility(View.VISIBLE);
                confirmPass.setVisibility(View.VISIBLE);

                final ChangeBounds bounds = new ChangeBounds();
                bounds.setDuration(1500);
                bounds.addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {


                        ObjectAnimator animator1 = ObjectAnimator.ofFloat(signUp, "translationX", mainLinear.getWidth() / 2 - relativeLayout2.getWidth() / 2 - signUp.getWidth() / 2);
                        ObjectAnimator animator2 = ObjectAnimator.ofFloat(img, "translationX", -relativeLayout2.getX());
                        ObjectAnimator animator3 = ObjectAnimator.ofFloat(signUp, "rotation", 0);

                        ObjectAnimator animator4 = ObjectAnimator.ofFloat(email, "alpha", 1, 0);
                        ObjectAnimator animator5 = ObjectAnimator.ofFloat(pass, "alpha", 1, 0);
//                        ObjectAnimator animator6 = ObjectAnimator.ofFloat(forgetPass, "alpha", 1, 0);

                        ObjectAnimator animator7 = ObjectAnimator.ofFloat(login, "rotation", 90);
                        ObjectAnimator animator8 = ObjectAnimator.ofFloat(login, "y", relativeLayout2.getHeight() / 2);
                        ObjectAnimator animator9 = ObjectAnimator.ofFloat(email2, "alpha", 0, 1);

                        ObjectAnimator animator10 = ObjectAnimator.ofFloat(confirmPass, "alpha", 0, 1);
                        ObjectAnimator animator11 = ObjectAnimator.ofFloat(pass2, "alpha", 0, 1);
                        ObjectAnimator animator12 = ObjectAnimator.ofFloat(signUp, "y", login.getY());

                        ObjectAnimator animator13 = ObjectAnimator.ofFloat(back, "translationX", img.getX());
                        ObjectAnimator animator14 = ObjectAnimator.ofFloat(signUp, "scaleX", 2);
                        ObjectAnimator animator15 = ObjectAnimator.ofFloat(signUp, "scaleY", 2);

                        ObjectAnimator animator16 = ObjectAnimator.ofFloat(login, "scaleX", 1);
                        ObjectAnimator animator17 = ObjectAnimator.ofFloat(login, "scaleY", 1);
                        ObjectAnimator animator18 = ObjectAnimator.ofFloat(logo, "x", relativeLayout2.getRight() / 2 - relativeLayout.getRight());

                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animator1, animator2, animator3, animator4, animator5, animator7,
                                animator8, animator9, animator10, animator11, animator12, animator13, animator14, animator15, animator16, animator17, animator18);
                        set.setDuration(1500).start();


                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {


                        email.setVisibility(View.INVISIBLE);
                        pass.setVisibility(View.INVISIBLE);
//                        forgetPass.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(Transition transition) {


                    }
                });

                TransitionManager.beginDelayedTransition(mainLinear, bounds);

                params.weight = (float) 4.25;
                params2.weight = (float) 0.75;


                relativeLayout.setLayoutParams(params);
                relativeLayout2.setLayoutParams(params2);

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isOnLoginPage)
                    signInFunction(email.getText().toString().trim(), pass.getText().toString().trim());

                isOnLoginPage = true;

//                Toast.makeText(getApplicationContext(),"login"+email.getText()+"**"+pass.getText(),Toast.LENGTH_LONG).show();

//                TODO

                if (params2.weight == 4.25) {

                    Snackbar.make(relativeLayout2, "Login Complete", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                email.setVisibility(View.VISIBLE);
                pass.setVisibility(View.VISIBLE);
//                forgetPass.setVisibility(View.VISIBLE);


                final ChangeBounds bounds = new ChangeBounds();
                bounds.setDuration(1500);
                bounds.addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(Transition transition) {


                        ObjectAnimator animator1 = ObjectAnimator.ofFloat(login, "translationX", mainLinear.getWidth() / 2 - relativeLayout.getWidth() / 2 - login.getWidth() / 2);
                        ObjectAnimator animator2 = ObjectAnimator.ofFloat(img, "translationX", (relativeLayout.getX()));
                        ObjectAnimator animator3 = ObjectAnimator.ofFloat(login, "rotation", 0);

                        ObjectAnimator animator4 = ObjectAnimator.ofFloat(email, "alpha", 0, 1);
                        ObjectAnimator animator5 = ObjectAnimator.ofFloat(pass, "alpha", 0, 1);
//                        ObjectAnimator animator6 = ObjectAnimator.ofFloat(forgetPass, "alpha", 0, 1);

                        ObjectAnimator animator7 = ObjectAnimator.ofFloat(signUp, "rotation", 90);
                        ObjectAnimator animator8 = ObjectAnimator.ofFloat(signUp, "y", relativeLayout.getHeight() / 2);
                        ObjectAnimator animator9 = ObjectAnimator.ofFloat(email2, "alpha", 1, 0);

                        ObjectAnimator animator10 = ObjectAnimator.ofFloat(confirmPass, "alpha", 1, 0);
                        ObjectAnimator animator11 = ObjectAnimator.ofFloat(pass2, "alpha", 1, 0);
                        ObjectAnimator animator12 = ObjectAnimator.ofFloat(login, "y", signUp.getY());

                        ObjectAnimator animator13 = ObjectAnimator.ofFloat(back, "translationX", -img.getX());
                        ObjectAnimator animator14 = ObjectAnimator.ofFloat(login, "scaleX", 2);
                        ObjectAnimator animator15 = ObjectAnimator.ofFloat(login, "scaleY", 2);

                        ObjectAnimator animator16 = ObjectAnimator.ofFloat(signUp, "scaleX", 1);
                        ObjectAnimator animator17 = ObjectAnimator.ofFloat(signUp, "scaleY", 1);
                        ObjectAnimator animator18 = ObjectAnimator.ofFloat(logo, "x", logo.getX() + relativeLayout2.getWidth());


                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(animator1, animator2, animator3, animator4, animator5, animator7,
                                animator8, animator9, animator10, animator11, animator12, animator13, animator14, animator15, animator16, animator17, animator18);
                        set.setDuration(1500).start();

                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {

                        email2.setVisibility(View.INVISIBLE);
                        pass2.setVisibility(View.INVISIBLE);
                        confirmPass.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(Transition transition) {

                    }
                });

                TransitionManager.beginDelayedTransition(mainLinear, bounds);

                params.weight = (float) 0.75;
                params2.weight = (float) 4.25;

                relativeLayout.setLayoutParams(params);
                relativeLayout2.setLayoutParams(params2);


            }
        });
    }


    private int inDp(int dp) {

        Resources resources = getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int) (dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private void signInFunction(final String account, final String pwd) {
        //登陆逻辑
        //TODO 点击登陆后的逻辑 从此就不需要再次开启动画
//        Log.d("Login",account+":"+pwd);
        final SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("isFirstLogin", false);
        edit.apply();

        //判断是否可以登录
        if (account.equals("") || pwd.equals("")) {
            ShowInfoUtil.showInfo(getApplicationContext(), "need password and username!");
        }
        //判断是否已经被登陆
        new Thread() {
            @Override
            public void run() {
                try {
                    String checkPath = "http://39.108.159.175/phpworkplace/androidLogin/CheckLoginStatus.php?name=" + URLEncoder.encode(account, "utf-8");
                    Log.d("Login", "Check path is:" + checkPath);
                    URL url = new URL(checkPath);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    int code1 = conn.getResponseCode();
                    if (200 == code1) {
                        final String satus = StreamUtil.readStream(conn.getInputStream()).trim();
                        Log.d("Login", "Login Status is:" + satus + ":" + UserInfoSotrage.Account);

                        if (satus.equals("1")) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ShowInfoUtil.showInfo(getApplicationContext(), "该账户已经登陆！");
                                }
                            });
                            isLogin = true;
                            Log.d("Login", "Unalbe to Login " + isLogin);
                            return;
                        } else {
                            //未登录 可以执行登陆
                            isLogin = false;
                            Log.d("Login", "OK to Login :" + satus);
                            new Thread() {
                                @Override
                                public void run() {
                                    //登陆业务

                                    try {
                                        final String path = "http://39.108.159.175/phpworkplace/androidLogin/Login.php?name=" + URLEncoder.encode(account, "utf-8") + "&password=" + URLEncoder.encode(pwd, "utf-8");
                                        URL url = new URL(path);
                                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                        connection.setConnectTimeout(5000);
                                        connection.setRequestMethod("GET");
                                        int code = connection.getResponseCode();
                                        if (200 == code) {
                                            InputStream inputStream = connection.getInputStream();
                                            final String result = StreamUtil.readStream(inputStream).trim();
//                                        ShowInfoUtil.showInfo(getContext(),result);
                                            if (result.trim().equals("success")) {
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ShowInfoUtil.showInfo(getApplicationContext(), "登陆成功");
                                                    }
                                                });
                                                //存储登陆用户昵称信息
                                                UserInfoSotrage.Account = account;
                                                UserInfoSotrage.pwd = pwd;

                                                new Thread() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            final String infoPath = "http://39.108.159.175/phpworkplace/androidLogin/GetNickName.php?name=" + URLEncoder.encode(UserInfoSotrage.Account, "utf-8");
                                                            URL url = new URL(infoPath);
                                                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                            conn.setRequestMethod("GET");
                                                            conn.setConnectTimeout(5000);
                                                            int code1 = conn.getResponseCode();
                                                            if (200 == code1) {
                                                                final String nickname = StreamUtil.readStream(conn.getInputStream()).trim();
                                                                UserInfoSotrage.Name = nickname;
                                                                edit.putString("name", nickname);
                                                                Log.d("Login", "run: " + UserInfoSotrage.Name + ":" + UserInfoSotrage.Account + ":" + UserInfoSotrage.pwd);
                                                            } else {
                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        ShowInfoUtil.showInfo(getApplicationContext(), "链接服务器失败");
                                                                    }
                                                                });
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }.start();
                                                new Thread() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            final String infoPath = "http://39.108.159.175/phpworkplace/androidLogin/GetIcon.php?name=" + URLEncoder.encode(UserInfoSotrage.Account, "utf-8");
                                                            URL url = new URL(infoPath);
                                                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                            conn.setRequestMethod("GET");
                                                            conn.setConnectTimeout(5000);
                                                            int code1 = conn.getResponseCode();
                                                            if (200 == code1) {
                                                                final String icon = StreamUtil.readStream(conn.getInputStream()).trim();
                                                                UserInfoSotrage.icon = icon;
                                                                edit.putString("account", account);
                                                                edit.putString("pwd", pwd);
                                                                edit.putString("icon", icon);
                                                            } else {
                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        ShowInfoUtil.showInfo(getApplicationContext(), "链接服务器失败");
                                                                    }
                                                                });
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }.start();
                                                //修改用户服务器登陆信息
                                                new Thread() {
                                                    @Override
                                                    public void run() {
                                                        try {
                                                            String statusPath = "http://39.108.159.175/phpworkplace/androidLogin/SetUserStatus.php?name=" + URLEncoder.encode(account, "utf-8") + "&status=" + 1;
                                                            URL url = new URL(statusPath);
                                                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                            conn.setRequestMethod("GET");
                                                            conn.setConnectTimeout(5000);
                                                            int code1 = conn.getResponseCode();
                                                            if (200 == code1) {
                                                                final String satus = StreamUtil.readStream(conn.getInputStream()).trim();
                                                                Log.d("Login", "status is: " + satus);
                                                                edit.putBoolean("isLogin", true);
                                                                edit.apply();
                                                            } else {
                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        ShowInfoUtil.showInfo(getApplicationContext(), "链接服务器失败");
                                                                    }
                                                                });
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }.start();
                                                //先沉睡2秒确定获取登陆状态启动homeactivity
                                                if (!isLogin) {
                                                    Log.d("Login", "islogin:" + isLogin);
                                                    Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                                                    startActivity(homeIntent);
                                                }

                                            } else {
//                                                        ShowInfoUtil.showInfo(getApplicationContext(),"登陆失败");
                                                //TODO
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        ShowInfoUtil.showInfo(getApplicationContext(), "登陆失败");
                                                    }
                                                });
                                            }
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                        }

                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowInfoUtil.showInfo(getApplicationContext(), "链接服务器失败");
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        //保存记住登陆逻辑
        if (cb.isChecked()) {
            SharedPreferences preferences = getSharedPreferences("login", 1);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("cb", true);
            editor.putString("account", account);
            editor.putString("pwd", pwd);
            editor.commit();
        }

    }

    //注册逻辑
    private void signUpFunction(final EditText etAccount, final EditText etPwd, final EditText confirmPwd) {

        final String account_name = etAccount.getText().toString().trim();
        final String account_pwd = etPwd.getText().toString().trim();
        final String account_confirm_pwd = confirmPwd.getText().toString().trim();
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("isFirstLogin", false);
        edit.apply();
        //由于访问网络 创建新线程
        new Thread() {

            @Override
            public void run() {
                if (account_name.equals("") || account_pwd.equals("")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowInfoUtil.showInfo(getApplicationContext(), "欢迎注册 请输入用户名和密码" + account_name + "*" + account_pwd);
                        }
                    });
                    //无法更新ui
//                                Toast.makeText(getContext(),"请输入用户名和密码",Toast.LENGTH_LONG).show();
                    return;
                }
                if (!account_confirm_pwd.equals(account_pwd)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ShowInfoUtil.showInfo(getApplicationContext(), "密码不一致" + account_name + "*" + account_pwd);
                        }
                    });
                    return;
                }
                try {
                    //开始注册代码
                    String tempName = URLEncoder.encode(account_name, "utf-8");
                    //设置路径
//                                System.out.println("tempName:"+tempName+"account_name:"+account_name);
                    String path = "http://39.108.159.175/phpworkplace/androidLogin/Register.php"
                            + "?name=" + tempName + "&password=" + account_pwd;
                    System.out.println(path);
//                                //开始连接
//                                URL url = new URL(path);
//                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                                connection.setConnectTimeout(5000);
//                                connection.setRequestMethod("GET");
//                                int code = connection.getResponseCode();
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        //连接成功获得传递的流信息
                        InputStream in = conn.getInputStream();
                        String stream = StreamUtil.readStream(in).trim();
                        if (stream.equals("注册成功")) {
                            System.out.println("注册成功");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ShowInfoUtil.showInfo(getApplicationContext(), "注册成功");
                                }
                            });
                            UserInfoSotrage.Account = account_name;
                            UserInfoSotrage.pwd = account_pwd;
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("account", account_name);
                            editor.putString("pwd", account_pwd);
                            editor.apply();
//                                        getActivity().runOnUiThread();也可以 不过用handler看起来更加分离UI
//                                        Toast.makeText(getContext(), "注册成功", Toast.LENGTH_SHORT).show();
                            //TODO
                            //跳转到成功界面
//                                        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.start_frame_container,new SignUpFragment()).commit();
//                            getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.start_frame_container,new FillInfoMationFragment()).commit();
                            Intent intent = new Intent(SignInUpActivity.this, FillInfoActivity.class);
                            startActivity(intent);
                            //杀死自己
                            finish();

                        } else if (stream.equals("存在用户")) {
                            System.out.println("存在用户");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ShowInfoUtil.showInfo(getApplicationContext(), "用户已存在");
                                }
                            });
//                                        Toast.makeText(getContext(), "用户已存在", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        System.out.println("连接服务器失败");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }.start();
    }

}
