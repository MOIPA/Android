package com.example.tr.instantcool2.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by TR on 2017/10/18.
 */

public class DetectMessage extends Service implements InterfaceDetectMessage{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    class MyBinder extends Binder {

    }

    @Override
    public int GetMessageCount() {
        return 0;
    }
}
