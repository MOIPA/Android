//package com.example.tr.instantcool2.Fragment;
//
//import android.app.Instrumentation;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.example.tr.instantcool2.Activity.HomeActivity;
//import com.example.tr.instantcool2.IndicatorView.TopBarIndicatorView;
//import com.example.tr.instantcool2.LocalDB.UserInfoSotrage;
//import com.example.tr.instantcool2.R;
//import com.example.tr.instantcool2.Utils.ShowInfoUtil;
//import com.example.tr.instantcool2.Utils.StreamUtil;
//
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//
///**
// * Created by TR on 2017/10/11.
// */
////aobrt 老版本完善信息
//
//public class FillInfoMationFragment extends Fragment implements TopBarIndicatorView.TopBarClickedListener {
//
//    private TopBarIndicatorView topBarIndicatorView;
//    private EditText et_name;
//    private Button btn_submit;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_fill_infomation, null);
//        btn_submit = (Button) view.findViewById(R.id.btn_fill_information);
//        et_name = (EditText) view.findViewById(R.id.et_fill_information);
//
//        topBarIndicatorView = (TopBarIndicatorView) view.findViewById(R.id.top_bar_container_fill_information);
//        initTopbar();
//
//        btn_submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UserInfoSotrage.Name = et_name.getText().toString().trim();
//                //上传昵称
//                new Thread(){
//                    @Override
//                    public void run() {
//                        URL url = null;
//                        try {
//                            String path ="http://39.108.159.175/phpworkplace/androidLogin/SetUserName.php?name="+ URLEncoder.encode(UserInfoSotrage.Account,"utf-8")+"&nickname="+URLEncoder.encode( UserInfoSotrage.Name,"utf-8");
//                            url = new URL(path);
//                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                            connection.setRequestMethod("GET");
//                            connection.setConnectTimeout(5000);
//                            int code = connection.getResponseCode();
//                            if(200==code){
//                                if(StreamUtil.readStream(connection.getInputStream()).trim().equals("success")){
//                                    getActivity().runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            ShowInfoUtil.showInfo(getContext(),"信息已完善");
//                                        }
//                                    });
//                                }
//                            }else{
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        ShowInfoUtil.showInfo(getContext(),"链接服务器失败！");
//                                    }
//                                });
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.start();
//
//                //开启homeActivity
//                Intent intent = new Intent(getContext(), HomeActivity.class);
//                startActivity(intent);
//            }
//        });
//
//        return view;
//    }
//
//    private void initTopbar() {
//        topBarIndicatorView.setTitle("完善信息");
//        topBarIndicatorView.setTopBarOnClickedListener(this);
//    }
//
//    @Override
//    public void OnBackClicked() {
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                Instrumentation inst = new Instrumentation();
//                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
//            }
//        }.start();
//    }
//}
