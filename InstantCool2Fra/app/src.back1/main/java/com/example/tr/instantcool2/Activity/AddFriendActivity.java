package com.example.tr.instantcool2.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tr.instantcool2.IndicatorView.List_Item_FriendFragment_indicatorView;
import com.example.tr.instantcool2.JavaBean.Friend;
import com.example.tr.instantcool2.LocalDB.UserInfoSotrage;
import com.example.tr.instantcool2.R;
import com.example.tr.instantcool2.Utils.ShowInfoUtil;
import com.example.tr.instantcool2.Utils.StreamUtil;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.example.tr.instantcool2.R.id.et_search_friend;

public class AddFriendActivity extends AppCompatActivity {

    private ImageButton ib_add_friend;
    private EditText et_add_friend;
    private ListView lv_friends;
    private List<Friend> lists;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1)lv_friends.setAdapter(new MyAdapter());
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        ib_add_friend = (ImageButton) findViewById(R.id.ib_search_friend);
        et_add_friend = (EditText) findViewById(et_search_friend);
        lv_friends = (ListView) findViewById(R.id.lv_show_searched_friend);



        ib_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断逻辑
                String friendaccount = et_add_friend.getText().toString().trim();
                if(friendaccount.equals("")){
                    ShowInfoUtil.showInfo(getApplication(),"请输入好友账号");
                    return;
                }

                //搜索好友并且展示到lv
                SearchFriends(friendaccount);
            }
        });

    }
    private void SearchFriends(final String targetaccount){
        new Thread(){
            @Override
            public void run() {
                String path = "http://39.108.159.175/phpworkplace/androidLogin/SearchPerson.php?targetaccount="+targetaccount;
                Log.d("SearchFriends", "run: "+path);
                try{
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    int code = conn.getResponseCode();
                    if(200==code){
                        InputStream in = conn.getInputStream();
                        lists = StreamUtil.XmlParserFriend(in);

                        //解析完数据绑定lv
                        Log.d("SearchFriends", "run: bind");
                        Message msg = new Message();
                        msg.what=1;
                        handler.sendMessage(msg);
                        Log.d("SearchFriends", "run: bindover");
                    }else{
                        Log.d("AddFriendActivity", "服务器连接失败!");
                    }

                }catch (Exception e){
                    e.getStackTrace();
                }
            }
        }.start();

        lv_friends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Friend friend = lists.get(position);
                Intent intent = new Intent(AddFriendActivity.this,FriendInfoActivity.class);
                intent.putExtra("friendname",friend.getFriendName());
                Log.d("icon", "onItemClick: "+friend.getFriendIcon());
                intent.putExtra("friendicon",friend.getFriendIcon()+"");
                intent.putExtra("friendaccount",friend.getFriendAccount());
                intent.putExtra("willAddFriend","yes");
                startActivity(intent);
            }
        });
    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            Log.d("SearchFriends", "run: "+lists.size());
            if(lists.size()==0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowInfoUtil.showInfo(getApplicationContext(),"未找到用户！");
                    }
                });
            };
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if(convertView==null){
                view = View.inflate(getApplicationContext(),R.layout.iten_listview_friend_fragment,null);
            }else{
                view = convertView;
            }

            List_Item_FriendFragment_indicatorView lv_item = (List_Item_FriendFragment_indicatorView) view.findViewById(R.id.item_friend_fragment_lv);

                Friend friend = lists.get(position);
                lv_item.setTv_account(friend.getFriendAccount());
                lv_item.setTv_name(friend.getFriendName());





            return view;
        }
    }

}
