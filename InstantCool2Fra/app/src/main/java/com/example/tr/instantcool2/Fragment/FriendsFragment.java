package com.example.tr.instantcool2.Fragment;


import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.example.tr.instantcool2.Activity.AddFriendActivity;
import com.example.tr.instantcool2.Activity.FriendInfoActivity;
import com.example.tr.instantcool2.IndicatorView.List_Item_FriendFragment_indicatorView;
import com.example.tr.instantcool2.IndicatorView.TopBarIndicatorFriendsView;
import com.example.tr.instantcool2.JavaBean.Friend;
import com.example.tr.instantcool2.LocalDB.UserInfoSotrage;
import com.example.tr.instantcool2.R;
import com.example.tr.instantcool2.Utils.ShowInfoUtil;
import com.example.tr.instantcool2.Utils.StreamUtil;
import com.example.tr.instantcool2.Utils.ChosePicByIconId;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class FriendsFragment extends Fragment implements TopBarIndicatorFriendsView.TopBarClickedListener{

    private List<Friend> friends;
    private ListView lv_friend;
    private List_Item_FriendFragment_indicatorView itemIndicator;
//    private MyAdapter adapter;
    private MyAdapterSwipeVertion adapter;
    private Timer timerFriend;
    private TimerTask taskFriend;
    private int friendCounts=0;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                //add adapter
                if(friendCounts == friends.size())return;
                if(friends==null)return;
                adapter  = new MyAdapterSwipeVertion(getContext());
                lv_friend.setAdapter(adapter);
            }
        }
    };



    private TopBarIndicatorFriendsView topBarIndicatorView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        lv_friend = (ListView) view.findViewById(R.id.lv_friend_fragment_f);
        topBarIndicatorView = (TopBarIndicatorFriendsView) view.findViewById(R.id.topbar_container_friend_fragment);
        initData();
        initTopBar();
        //刷新好友列表
        timerFriend = new Timer();
        taskFriend = new TimerTask() {
            @Override
            public void run() {
                initData();
            }
        };
        timerFriend.schedule(taskFriend,1000,1000);

        lv_friend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击进入好友详细信息页面
                Friend friend = friends.get(position);
                Intent intent = new Intent(getActivity(), FriendInfoActivity.class);
                intent.putExtra("friendname",friend.getFriendName());
                intent.putExtra("friendaccount",friend.getFriendAccount());
                Log.d("icon", "onItemClick friendsFragment: "+friend.getFriendIcon());
                intent.putExtra("friendicon",friend.getFriendIcon()+"");
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                adapter.notifyDataSetChanged();
//            }
//        };
//        timer.schedule(task,3000,1000);
        Log.d("CONFIRM", "onCreate: start");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timerFriend.cancel();
    }

    //切换时启动ONSTART
    @Override
    public void onStart() {
        super.onStart();
//        adapter.notifyDataSetChanged();
        //重新绑定
        if(adapter!=null)
        lv_friend.setAdapter(adapter);
    }

    private List<Friend> initData() {

        new Thread(){
            @Override
            public void run() {
                try{

                    String path= "http://39.108.159.175/phpworkplace/androidLogin/GetFriend.php?owner="+ URLEncoder.encode(UserInfoSotrage.Account,"utf-8");
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    int responseCode = connection.getResponseCode();
                    if(responseCode==200){
                        InputStream inputStream = connection.getInputStream();
                        //解析xml流信息
                        friends = StreamUtil.XmlParserFriend(inputStream);


                        //解析完毕添加适配器 在某些手机会直接因为不能在线程中修改失败 使用handler
//                        lv_conversation.setAdapter(new MyAdapter());
                        Message msg = new Message();
                        msg.what=1;
                        handler.sendMessage(msg);
                    }else{
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowInfoUtil.showInfo(getContext(),"初始化会话失败！");
                            }
                        });
                    }


                }catch(Exception e){}
            }
        }.start();

        return friends;
    }

    private void initTopBar(){
        topBarIndicatorView.setTitle("好友列表");
        topBarIndicatorView.setTopBarOnClickedListener(this);
    }

//    class MyAdapter extends BaseAdapter{
//
//        @Override
//        public int getCount() {
//            return friends.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            final View view;
//            if(convertView==null){
//                view = View.inflate(getContext(),R.layout.iten_listview_friend_fragment,null);
//            }else{
//                view = convertView;
//            }
//            itemIndicator = (List_Item_FriendFragment_indicatorView) view.findViewById(R.id.item_friend_fragment_lv);
//            Friend friend = friends.get(position);
//            itemIndicator.setTv_name(friend.getFriendName());
//            itemIndicator.setTv_account(friend.getFriendAccount());
//
//            return view;
//        }
//    }

    private void deleteConversation(final String friendAccount){
        new Thread(){
            @Override
            public void run() {
                try{
                    String path = "http://39.108.159.175/phpworkplace/androidLogin/DeleteConversation.php?owner="+UserInfoSotrage.Account+"&targetaccount="+friendAccount;
                    URL url = new URL(path);
//                    Log.d("delete", "run: "+path);
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    int code = conn.getResponseCode();
                    if(200==code){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ShowInfoUtil.showInfo(getContext(),"删除好友");
                                try {
                                    InputStream inputStream = conn.getInputStream();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }catch (Exception e){}
            }
        }.start();
    }

    private void deleteFriend(final String friendAccount){
        new Thread(){
            @Override
            public void run() {
                try{
                    String path = "http://39.108.159.175/phpworkplace/androidLogin/DeleteFriend.php?owner="+UserInfoSotrage.Account+"&targetaccount="+friendAccount;
                    URL url = new URL(path);
//                    Log.d("delete", "run: "+path);
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    int code = conn.getResponseCode();
                    if(200==code){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                ShowInfoUtil.showInfo(getContext(),"删除好友");
                                try {
                                    InputStream inputStream = conn.getInputStream();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }catch (Exception e){}
            }
        }.start();
    }

    private void deleteInvitation(final String friendAccount){
        new Thread(){
            @Override
            public void run() {
                try{
                    String path = "http://39.108.159.175/phpworkplace/androidLogin/DeleteInvitation.php?owner="+UserInfoSotrage.Account+"&targetaccount="+friendAccount;
                    URL url = new URL(path);
//                    Log.d("delete", "run: "+path);
                    final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    int code = conn.getResponseCode();
                    if(200==code){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                ShowInfoUtil.showInfo(getContext(),"删除好友");
                                try {
                                    InputStream inputStream = conn.getInputStream();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }catch (Exception e){}
            }
        }.start();
    }

    //滑动适配器  美化
    class MyAdapterSwipeVertion extends BaseSwipeAdapter{

        Context context;

        public MyAdapterSwipeVertion(Context context){
            this.context = context;
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe_item_friends_fragment;
        }

        @Override
        public View generateView(int position, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.iten_listview_friend_fragment,null);
        }

        @Override
        public void fillValues(final int position, View convertView) {
            itemIndicator = (List_Item_FriendFragment_indicatorView) convertView.findViewById(R.id.item_friend_fragment_lv);
            ImageButton ib = (ImageButton) convertView.findViewById(R.id.btn_iten_delete_friend);
            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //美化  使用了开源框架的sweetAlertDialog
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("确定删除好友?")
                            .setContentText("删除后无法恢复！")
                            .setConfirmText("删除!")
                            .setCancelText("取消")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog
                                            .setTitleText("已删除!")
                                            .setContentText("好友已删除!")
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(null)
                                            .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                    deleteConversation(friends.get(position).getFriendAccount());
                                    deleteFriend(friends.get(position).getFriendAccount());
                                    deleteInvitation(friends.get(position).getFriendAccount());
                                }
                            })
                            .showCancelButton(true)
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                }
                            })
                            .show();
                }
            });

            Friend friend = friends.get(position);
            itemIndicator.setTv_name(friend.getFriendName());
            itemIndicator.setTv_account(friend.getFriendAccount());
            //好友头像
//            switch (friend.getFriendIcon()){
//                case 0:
//                    itemIndicator.setIv_friend(R.mipmap.uicon1);
//                    break;
//                case 1:
//                    itemIndicator.setIv_friend(R.mipmap.uicon2);
//                    break;
//                case 2:
//                    itemIndicator.setIv_friend(R.mipmap.uicon3);
//                    break;
//            }
            Log.d("icon", "fillValues: friendsFraggment_adapter:"+ ChosePicByIconId.getImageId(friend.getFriendIcon()));
            itemIndicator.setIv_friend(ChosePicByIconId.getImageId(friend.getFriendIcon()));
        }

        @Override
        public int getCount() {
            friendCounts =friends.size();
            return friends.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    @Override
    public void OnBackClicked() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Thread(){
                    @Override
                    public void run() {
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                        Intent intent = new Intent();
                        intent.setAction("ExitApp");
                        getContext().sendBroadcast(intent);
                    }
                }.start();
            }
        });
    }

    @Override
    public void OnAddClicked() {
        //add friend
        //启动添加好友活动
        Intent intent = new Intent(getActivity(), AddFriendActivity.class);
        startActivity(intent);
    }
}
