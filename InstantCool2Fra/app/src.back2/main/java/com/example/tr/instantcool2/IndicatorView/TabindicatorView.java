package com.example.tr.instantcool2.IndicatorView;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tr.instantcool2.R;

/**
 * Created by TR on 2017/10/9.
 */

public class TabindicatorView extends RelativeLayout {

    private ImageView ivTabCon;
    private TextView tvTabHint;
    private TextView tvTabUnread;

    private int normalIconID;
    private int focusIconId;

    public TabindicatorView(Context context) {
        this(context,null);
    }

    public TabindicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //将布局文件和代码绑定
        View.inflate(context, R.layout.indicator_tab,this);
        ivTabCon = (ImageView) findViewById(R.id.tab_indicator_icon);
        tvTabHint = (TextView) findViewById(R.id.tab_indicator_hint);
        tvTabUnread = (TextView) findViewById(R.id.tab_indicator_unread);
        //初始化设置未读数为0
//        setTabUnreadCount(1);
    }
    //设置文本title
    public void setTableTitle(String title){
        tvTabHint.setText(title);
    }
    public void setTableTitle(int titleId){
        tvTabHint.setText(titleId);
    }

    //初始化图标icon
    public void setTableIcon(int normalIconid,int focusIconId){
        this.focusIconId=focusIconId;
        this.normalIconID=normalIconid;

        ivTabCon.setImageResource(normalIconID);
    }
    //设置未读数
    public void setTabUnreadCount(int unreadCount){
        if(unreadCount<=0){
            tvTabUnread.setVisibility(View.GONE);
        }else if(unreadCount<=99){
            tvTabUnread.setVisibility(View.VISIBLE);
            tvTabUnread.setText(unreadCount+"");
        }else{
            tvTabUnread.setVisibility(View.VISIBLE);
            tvTabUnread.setText("99+");
        }
    }
//设置选中
    public void setTabSelected(boolean selected){
        if(selected){
            ivTabCon.setImageResource(focusIconId);
        }else{
            ivTabCon.setImageResource(normalIconID);
        }
    }
    //设置字体颜色
    public void setFontColor(int color){
        tvTabUnread.setTextColor(color);
    }

}
