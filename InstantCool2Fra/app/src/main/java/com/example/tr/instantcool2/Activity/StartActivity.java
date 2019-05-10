package com.example.tr.instantcool2.Activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

//import com.example.tr.instantcool2.Fragment.FirstSinghtFragment;
import com.example.tr.instantcool2.Fragment.SignUpFragment;
import com.example.tr.instantcool2.R;
import com.example.tr.instantcool2.Utils.ShowInfoUtil;


/**
 * 老版本初始界面 现在已废弃原有功能
 * 现在作为开启的动画
 */
public class StartActivity extends AppCompatActivity {

//    private FirstSinghtFragment firstFragment ;
    private static Context context;
    private Intent service;
    private ImageView iv_start;

//    //负责处理界面toast的handler
//    public static Handler handeToast = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            Bundle data = msg.getData();
//            String needContent = "";
//            String succeed = "";
//            String failed = "";
//            needContent = data.getString("needContent");
//            succeed = data.getString("succeed");
//            failed = data.getString("failed");
//            System.out.println(needContent + "*" + succeed + "*" + failed);
//            if (!(needContent == null)) {
////                Toast.makeText(context, needContent, Toast.LENGTH_SHORT).show();
//                ShowInfoUtil.showInfo(context,needContent);
//                needContent = null;
//            } else if (!(succeed == null)) {
//                ShowInfoUtil.showInfo(context,succeed);
//                succeed = null;
//            } else if (!(failed == null)) {
//                ShowInfoUtil.showInfo(context,failed);
//                failed = null;
//            }
//
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //隐藏标题栏
//        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        iv_start = (ImageView) findViewById(R.id.iv_start);
        initImage();
//        //添加起始页面
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        SignUpFragment fragment = new SignUpFragment();
//        firstFragment=new FirstSinghtFragment();
//        transaction.add(R.id.start_frame_container, firstFragment);
//        transaction.commit();
//        context = getApplicationContext();
    }

    private void initImage() {
        iv_start.setImageResource(R.mipmap.side_menu_bk);
        //进行缩放动画
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.4f, 1.0f, 1.4f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(2000);
        //动画播放完成后保持形状
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //可以在这里先进行某些操作
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        iv_start.startAnimation(scaleAnimation);
    }
    private void startActivity() {
        Intent intent = new Intent(StartActivity.this, SignInUpActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }


//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        unbindService(serviceConn);
//    }
}
