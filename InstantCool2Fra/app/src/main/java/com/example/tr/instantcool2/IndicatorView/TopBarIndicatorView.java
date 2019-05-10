package com.example.tr.instantcool2.IndicatorView;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tr.instantcool2.R;

/**
 * Created by TR on 2017/10/11.
 */

public class TopBarIndicatorView extends RelativeLayout {

    private ImageButton ib_back;
    private TextView tv_title;
    private TopBarClickedListener topBarClickedListener;

    public TopBarIndicatorView(Context context) {
        this(context, null);
    }

    public TopBarIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //绑定布局文件
        View view = View.inflate(context, R.layout.indicator_top_bar, this);
        ib_back = (ImageButton) view.findViewById(R.id.ib_top_bar_back);
        tv_title = (TextView) findViewById(R.id.tv_top_bar_title);

    }


    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setBackImage(int resource) {
        ib_back.setImageResource(resource);
    }

    //设置监听
    public interface TopBarClickedListener {
        public void OnBackClicked();

    }



    public void setTopBarOnClickedListener(TopBarClickedListener listener) {
        this.topBarClickedListener = listener;
    }

    //加载完视图xml后执行
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ib_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(topBarClickedListener!=null){
                    topBarClickedListener.OnBackClicked();
                }
            }
        });
    }

}
