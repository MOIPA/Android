package com.example.tr.instantcool2.Activity;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.tr.instantcool2.Fragment.ChatFragment;
import com.example.tr.instantcool2.Fragment.FriendsFragment;
import com.example.tr.instantcool2.IndicatorView.TabindicatorView;
import com.example.tr.instantcool2.LocalDB.UserInfoSotrage;
import com.example.tr.instantcool2.R;
import com.example.tr.instantcool2.Utils.NetWorkUtil;
import com.example.tr.instantcool2.Utils.ShowInfoUtil;
import com.example.tr.instantcool2.Utils.StreamUtil;
import com.example.tr.instantcool2.Utils.ot;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.jpeng.jpspringmenu.SpringMenu;
import com.lilei.springactionmenu.ActionMenu;
import com.lilei.springactionmenu.OnActionItemClickListener;

import org.w3c.dom.Text;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 1:最后还是决定由HomeActivity实现OnTabChangeListener
 * 因为选中时需要把所有人都不选中 内部类无法获取外部的TabindicatorView
 * 2:使用回弹菜单
 * 3:决定退出登录方式使用数据库和侧滑栏联合实现
 * 4:使用fragmenttabhost联合view Page Adapter实现滑动
 * 5:实现消息接收提醒 显示在indicator上 登陆时存入数据库个人登陆信息
 */
public class HomeActivity extends FragmentActivity implements TabHost.OnTabChangeListener,ViewPager.OnPageChangeListener {
    private final static String TAG_CHAT = "chat";
    private final static String TAG_Friends = "friends";
    private final static String[] IDS = {"chat", "friends"};
    private final static int REFRESH_CHAT_INDICATOR = 1;

    private FragmentTabHost tabHost;
    private ViewPager viewPager;
    private List<String> ids = Arrays.asList(IDS);
    private List<Fragment> fragments = new ArrayList<Fragment>(IDS.length);
    private TimerTask task;
    private Timer timer;
    private TabindicatorView chatIndicator;
    private TabindicatorView findIndicator;
    private ActionMenu actionMenu;//开源果冻菜单
    private SpringMenu spMenu;//开源回弹侧滑菜单
//    private final int CLEAR_CHAT_INDICATOR= 2;
//    private final static String TAG_Functions = "functions";
//    private final static String TAG_MY = "my";
//    TabindicatorView connecotrIndicator;
//    TabindicatorView meIndicator;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return spMenu.dispatchTouchEvent(ev);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //初始化果冻菜单
        actionMenu = (ActionMenu) findViewById(R.id.actionMenu);
        actionMenu.addView(R.mipmap.lights);
        actionMenu.addView(R.mipmap.calendar);
        initActionMenu();//add listener

        //初始化回弹侧滑菜单
        spMenu = new SpringMenu(this,R.layout.menu_view);
        spMenu.setMenuSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(1,3));
        spMenu.setChildSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(1,5));
        initSpMenuItem();//初始化item的动作

        //初始化tabhost
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.main_viewpager);
        chatIndicator = new TabindicatorView(this);
        findIndicator = new TabindicatorView(this);
//        connecotrIndicator = new TabindicatorView(this);
//        meIndicator = new TabindicatorView(this);
        init("消息", TAG_CHAT, chatIndicator,new ChatFragment(),0);
        init("好友", TAG_Friends, findIndicator,new FriendsFragment(),1);
//        init("功能", TAG_Functions, connecotrIndicator,new FunctionFragment());
//        init("我", TAG_MY, meIndicator,new MeFragment());

        //初始化viewpager
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        viewPager.setAdapter(new TabsFragmentPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.setOnPageChangeListener(this);
        //开启自动检测消息线程
        detectUnreadMessageCount();

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==REFRESH_CHAT_INDICATOR&&!chatIndicator.isSelected()){
                Bundle data = msg.getData();
                int number = data.getInt("number");
                chatIndicator.setTabUnreadCount(number);
            }

            if(chatIndicator.isSelected()){
                chatIndicator.setTabUnreadCount(0);
            }
        }
    };

    //TODO 将退出登录改写为用户自己选择logout
    private void initSpMenuItem() {
        View menuView = spMenu.getMenuView();
        //初始化子控件
        TextView tvUserName = (TextView) menuView.findViewById(R.id.tv_user_name_menu_view);
        TextView tvUserAccount = (TextView) menuView.findViewById(R.id.tv_user_account_menu_view);
        ImageView ivUserIcon = (ImageView) menuView.findViewById(R.id.iv_user_icon_menu_view);
        Button btn_logout = (Button) menuView.findViewById(R.id.btn_logout_menu_view);
        Button btn_chname = (Button) menuView.findViewById(R.id.btn_chname_menu_view);
        Button btn_chpwd = (Button) menuView.findViewById(R.id.btn_chpwd_menu_view);
        Button btn_chicon = (Button) menuView.findViewById(R.id.btn_chicon_menu_view);
        //初始化基本信息
        initMenuInfo(tvUserName,tvUserAccount,ivUserIcon);

        //退出逻辑
        setLogoutBtnListener(btn_logout);
        //修改逻辑
        setChName(btn_chname);
        setChPwd(btn_chpwd);
        setChIcon(btn_chicon);
    }
    //TODO 实现每个方法
    private void setChName(Button btn){}
    private void setChPwd(Button btn){}
    private void setChIcon(Button btn){}
    private void setLogoutBtnListener(Button btn_logout){
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("userInfo",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isLogin",false);
                editor.apply();
                ShowInfoUtil.showInfo(getApplicationContext(),"拜拜！");
                ChangeUserLoginStatus();
                //发送退出广播
                sendExitBroadcast();
            }
        });
    }
    private void initMenuInfo(TextView tvUserName,TextView tvUserAccount,ImageView ivUserIcon){
        tvUserName.setText(UserInfoSotrage.Name);
        tvUserAccount.setText(UserInfoSotrage.Account);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        ivUserIcon.setImageResource(ot.getImageId(Integer.parseInt(UserInfoSotrage.icon)));
    }

    public void sendExitBroadcast() {
        new Thread(){
            @Override
            public void run() {
                Instrumentation inst = new Instrumentation();
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                //发送广播 让底层的SignInUpAcrtivity结束
                Intent intent = new Intent();
                intent.setAction("ExitApp");
                sendBroadcast(intent);
            }
        }.start();
    }

    private void initActionMenu() {
        actionMenu.setItemClickListener(new OnActionItemClickListener() {
            @Override
            public void onItemClick(int i) {
                //加号为0  第一个是1 第二个是2
//                ShowInfoUtil.showInfo(getApplicationContext(),"id+:"+i);
                //TODO 完善果冻弹出页面
                switch (i){
                    case 1:
                        //启动画图
                        Intent intentDraw = new Intent(HomeActivity.this,DrawerActivity.class);
                        startActivity(intentDraw);
                        break;
                    case 2:
                        //启动日历界面
                        Intent intent = new Intent(HomeActivity.this,CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        //启动其它功能
                }
            }
            @Override
            public void onAnimationEnd(boolean b) {

            }
        });
    }


    private void detectUnreadMessageCount(){
        //每隔一秒检测是否有新消息 查询消息未读总数量设置chatIndicator未读数
        new Thread(){
            @Override
            public void run() {
                timer = new Timer();
                //初始化task
                initTask();
                timer.schedule(task,500,500);
                Log.d("detect", "run: detect thread!");
            }
        }.start();
    }

    //初始化task方法 用于得知未读消息通知主线程刷新
    private void initTask(){
        task = new TimerTask() {
            @Override
            public void run() {
                Bundle bundle = NetWorkUtil.getSingleInfoFromServer("http://39.108.159.175/phpworkplace/androidLogin/GetTheMessageCount.php?receiver=" + UserInfoSotrage.Account);
                String result = bundle.getString("result");
                Log.d("detect", "run: detect thread!"+result+":"+UserInfoSotrage.Account);
//                        chatIndicator.setTabUnreadCount(1);
                //通知主线程刷新tab
                Bundle bud = new Bundle();
                bud.putInt("number",Integer.parseInt(result));
                Message msg = new Message();
                msg.what=REFRESH_CHAT_INDICATOR;
                msg.setData(bud);
                handler.sendMessage(msg);
            }
        };
    }

    /**
     *
     * @param title
     * @param TAG fragment的tag
     * @param indicator 自定義控件
     * @param fragment 添加的fragment
     */
    private void init(String title, String TAG, TabindicatorView indicator, Fragment fragment,int icon) {
        //新建tabspec
        TabHost.TabSpec spec = tabHost.newTabSpec(TAG);
//        TabindicatorView indicator = new TabindicatorView(this);
        indicator.setTabUnreadCount(0);
        indicator.setTableTitle(title);
        //0:xiaoxi 1:haoyou
        if(icon==0)
        indicator.setTableIcon(R.mipmap.comments, R.mipmap.commentsclicked);
        if(icon==1)
            indicator.setTableIcon(R.mipmap.bussinessman, R.mipmap.bussinessmanclick);
        spec.setIndicator(indicator);
//        spec.setIndicator(view)
        //添加tabspec
        tabHost.addTab(spec, fragment.getClass(), null);
        //消去分割线
        tabHost.getTabWidget().setDividerDrawable(android.R.color.white);
        //默认第一个为选中
        tabHost.setCurrentTabByTag(TAG_CHAT);
        chatIndicator.setTabSelected(true);
        //监听选中事件
        tabHost.setOnTabChangedListener(this);

        fragments.add(Fragment.instantiate(this, fragment.getClass().getName()));
    }

    //设置tabhost选中事件
    @Override
    public void onTabChanged(String tabId) {
        //要让某个选中得先全部不选中
        chatIndicator.setTabSelected(false);
//        meIndicator.setTabSelected(false);
//        connecotrIndicator.setTabSelected(false);
        findIndicator.setTabSelected(false);

        switch (tabId) {
            case TAG_CHAT:
                chatIndicator.setTabSelected(true);
                break;
//            case TAG_Functions:
//                connecotrIndicator.setTabSelected(true);
//                break;
            case TAG_Friends:
                findIndicator.setTabSelected(true);
                break;
//            case TAG_MY:
//                meIndicator.setTabSelected(true);
//                break;
        }

        viewPager.setCurrentItem(ids.indexOf(tabId));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(task!=null)
        //结束时取消线程
        task.cancel();
//        ChangeUserLoginStatus();
    }

    private void ChangeUserLoginStatus(){
        //改变用户登陆状态为未登陆
        new Thread(){
            @Override
            public void run() {
                try {
                    String statusPath = "http://39.108.159.175/phpworkplace/androidLogin/SetUserStatus.php?name="+ URLEncoder.encode(UserInfoSotrage.Account,"utf-8")+"&status="+0;
                    URL url = new URL(statusPath);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    int code1 = conn.getResponseCode();
                    if(200==code1){
                        final String satus = StreamUtil.readStream(conn.getInputStream()).trim();
                        Log.d("Login", "status is: "+satus);
                    }else{
                        runOnUiThread(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void run() {
                                ShowInfoUtil.showInfo(getApplicationContext(),"链接服务器失败");
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //重写viewpager方法
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tabHost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class TabsFragmentPagerAdapter extends FragmentPagerAdapter{

        List<Fragment> mfragments;

        public TabsFragmentPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
            super(fm);
            this.mfragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mfragments.get(position);
        }

        @Override
        public int getCount() {
            return mfragments.size();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initSpMenuItem();
    }
}


/**
 * 在honmeactivity  pause时或者stop时结束task节省资源
 * 恢复时重新初始化task
 */

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        if(task==null){
//            detectUnreadMessageCount();
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if(task!=null)task.cancel();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if(task!=null)task.cancel();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(task==null){
//            detectUnreadMessageCount();
//        }
//    }