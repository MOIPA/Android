package com.example.tr.instantcool2.Fragment;

import android.app.Instrumentation;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.tr.instantcool2.Activity.StartActivity;
import com.example.tr.instantcool2.IndicatorView.TopBarIndicatorView;
import com.example.tr.instantcool2.LocalDB.UserInfoSotrage;
import com.example.tr.instantcool2.R;
import com.example.tr.instantcool2.Utils.StreamUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class SignUpFragment extends Fragment implements TopBarIndicatorView.TopBarClickedListener{

    private EditText etAccount;
    private EditText etPwd;
    private Button btnSignUp;
    private TopBarIndicatorView topbar;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up,null,false);

        etAccount = (EditText) view.findViewById(R.id.et_sign_up_account);
        etPwd = (EditText) view.findViewById(R.id.et_sign_up_pwd);
        btnSignUp = (Button) view.findViewById(R.id.btn_sign_up);
        topbar = (TopBarIndicatorView) view.findViewById(R.id.topbar_container_sign_up);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                final String account_name = etAccount.getText().toString().trim();
                final String account_pwd = etPwd.getText().toString().trim();
                Message msg;
                    System.out.println("account_name:"+account_name);
                    System.out.println("account_pwd:"+account_pwd);
                    //由于访问网络 创建新线程
                    new Thread(){

                        @Override
                        public void run() {
                            if(account_name.equals("")||account_pwd.equals("")){
                                sendMsg2Activity("needContent","请输入用户名和密码"+account_name+"*"+account_pwd);
                                //无法更新ui
//                                Toast.makeText(getContext(),"请输入用户名和密码",Toast.LENGTH_LONG).show();
                                return;
                            }
                            try {
                                //开始注册代码
                                String tempName= URLEncoder.encode(account_name,"utf-8");
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
                                if(code==200){
                                    //连接成功获得传递的流信息
                                    InputStream in = conn.getInputStream();
                                    String stream = StreamUtil.readStream(in).trim();
                                    if(stream.equals("注册成功")){
                                        System.out.println("注册成功");
                                        sendMsg2Activity("succeed","注册成功");
                                        UserInfoSotrage.Account = account_name;
                                        UserInfoSotrage.pwd=account_pwd;
//                                        getActivity().runOnUiThread();也可以 不过用handler看起来更加分离UI
//                                        Toast.makeText(getContext(), "注册成功", Toast.LENGTH_SHORT).show();
                                        //TODO
                                        //跳转到成功界面
//                                        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.start_frame_container,new SignUpFragment()).commit();
//                                        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.start_frame_container,new FillInfoMationFragment()).commit();
                                    }else if(stream.equals("存在用户")){
                                        System.out.println("存在用户");
                                        sendMsg2Activity("failed","用户已存在");
//                                        Toast.makeText(getContext(), "用户已存在", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    System.out.println("连接服务器失败");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }.start();

            }
        });


        //使用topbarIndicator
        initTopBar();

        return view;
    }

    public void initTopBar(){
        //自身implements 接口成为Listener
        topbar.setTopBarOnClickedListener(this);
        topbar.setTitle("注册");
    }

    //重写onclicked方法
    @Override
    public void OnBackClicked() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                ShowInfoUtil.showInfo(getContext(),"返回执行");
                //返回上一级
                new Thread(){
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
    
    private void sendMsg2Activity(String key,String content){
        Message msg = new Message();
        Bundle data = new Bundle();
        data.putString(key,content);
        msg.setData(data);
//        StartActivity.handeToast.sendMessage(msg);
    }

}
