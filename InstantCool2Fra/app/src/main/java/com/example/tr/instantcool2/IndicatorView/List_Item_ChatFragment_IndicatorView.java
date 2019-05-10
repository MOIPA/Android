package com.example.tr.instantcool2.IndicatorView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tr.instantcool2.R;
import com.example.tr.instantcool2.Utils.ShowInfoUtil;

/**
 * Created by TR on 2017/10/22.
 */

public class List_Item_ChatFragment_IndicatorView extends RelativeLayout {

    private ImageView iv_userImage;
    private TextView iv_unreadCount;
    private TextView tv_name;
    private TextView tv_account;

    public List_Item_ChatFragment_IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.indicator_item_chat_fragment,this);
        iv_unreadCount = (TextView) view.findViewById(R.id.iv_unread_count);
        iv_userImage = (ImageView) view.findViewById(R.id.iv_user_image);
        tv_account = (TextView) view.findViewById(R.id.tv_user_account);
        tv_name = (TextView) view.findViewById(R.id.tv_user_name);
    }

    public void setIv_userImage(int resourceID){
        iv_userImage.setImageResource(resourceID);
    }

    public void setIv_unreadCount(int count){

        //设置未读数
//        public void setTabUnreadCount(int unreadCount){
//            if(unreadCount<=0){
//                tvTabUnread.setVisibility(View.GONE);
//            }else if(unreadCount<=99){
//                tvTabUnread.setVisibility(View.VISIBLE);
//                tvTabUnread.setText(unreadCount+"");
//            }else{
//                tvTabUnread.setVisibility(View.VISIBLE);
//                tvTabUnread.setText("99+");
//            }
//        }
//        ShowInfoUtil.showInfo(getContext(),"count is:"+count);
        if(count<=0){
            iv_unreadCount.setVisibility(View.GONE);
        }else if(count<=99){
            iv_unreadCount.setVisibility(View.VISIBLE);
            iv_unreadCount.setText(count+"");
        }else if(count>=100){
            iv_unreadCount.setVisibility(View.VISIBLE);
            iv_unreadCount.setText("99+");
        }
    }

    public void setTv_name(String name){
        tv_name.setText(name);
    }

    public void setTv_account(String account){
        tv_account.setText(account);
    }

    public String getAccount(){
        return tv_account.getText().toString().trim();
    }

}
