package com.example.tr.instantcool2.Utils;

import android.os.Bundle;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by TR on 2017/10/11.
 */

public class NetWorkUtil {

    public static Bundle getSingleInfoFromServer(String path){
        Bundle data = new Bundle();
        int code = -1;
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            code = connection.getResponseCode();
            InputStream in = connection.getInputStream();
            String result = StreamUtil.readStream(in).trim();
            data.putString("result",result);
            data.putInt("code",code);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String DetechUnreadCount(String path){
        Bundle data = new Bundle();
        int code = -1;
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            code = connection.getResponseCode();
            InputStream in = connection.getInputStream();
            String result = StreamUtil.readStream(in).trim();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0+"";
    }

}
