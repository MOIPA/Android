package com.example.tr.instantcool2.Utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.example.tr.instantcool2.R;
import com.maning.mndialoglibrary.MToast;
import com.maning.mndialoglibrary.MToastConfig;

/**
 * Created by TR on 2017/10/11.
 */

public class ShowInfoUtil {

    //系统的太丑 以后修改
//    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void showInfo(Context context, String content){
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();

//        MToastConfig config = new MToastConfig.Builder()
//                //设置显示的位置
//                .setGravity(MToastConfig.MToastGravity.CENTRE)
//                //文字的颜色
//                .setTextColor(context.getColor(R.color.colorBlue))
//                //背景色
//                .setBackgroundColor(context.getColor(R.color.colorWhite))
//                //背景圆角
//                .setBackgroundCornerRadius(10)
//                //背景边框的颜色
//                .setBackgroundStrokeColor(Color.WHITE)
//                //背景边框的宽度
//                .setBackgroundStrokeWidth(1)
//                .build();
//
//        //多种方法
//        MToast.makeTextShort(context,content, config).show();
    }
}
