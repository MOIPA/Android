package com.example.tr.instantcool2.Utils;

import com.example.tr.instantcool2.R;

/**
 * Created by trlvtzq on 2017/11/28.
 */

public class ChosePicByIconId {

    public static int getImageId(int id){
        switch (id){
            case 0:
                return R.mipmap.uicon1;
            case 1:
                return R.mipmap.uicon2;
            case 2:
                return R.mipmap.uicon3;
            case 11:
                return R.mipmap.user_r_icon1;
            case 12:
                return R.mipmap.user_r_icon2;
            case 13:
                return R.mipmap.user_r_icon3;
            case 14:
                return R.mipmap.user_r_icon4;
            case 15:
                return R.mipmap.user_r_icon5;
            case 16:
                return R.mipmap.user_r_icon6;
            case 17:
                return R.mipmap.user_r_icon7;
            case 18:
                return R.mipmap.user_r_icon8;
            case 19:
                return R.mipmap.user_r_icon9;

        }

        return 0;
    }

}
