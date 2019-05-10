package com.example.tr.instantcool2.IndicatorView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tr.instantcool2.R;

/**
 * Created by tangz on 2017/10/23.
 */

public class List_Item_FriendFragment_indicatorView extends RelativeLayout {

    private ImageView iv_friend;
    private TextView tv_name;
    private TextView tv_account;

    public List_Item_FriendFragment_indicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View view = View.inflate(context,R.layout.indicator_item_friend_fragment,this);
        iv_friend = (ImageView) view.findViewById(R.id.iv_friend_image);
        tv_name = (TextView) view.findViewById(R.id.tv_friend_name);
        tv_account = (TextView) view.findViewById(R.id.tv_friend_account);
    }

    public void setIv_friend(int ResourceID){
        iv_friend.setImageResource(ResourceID);
    }
    public void setTv_name(String name){
        tv_name.setText(name);
    }
    public void setTv_account(String account){
        tv_account.setText(account);
    }
}
