package com.example.smslistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SmsMessageListnener extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

//		System.out.println("executed");
		
		Object objects[] = (Object[]) intent.getExtras().get("pdus");
		
		for (Object obj : objects) {
			SmsMessage message = SmsMessage.createFromPdu((byte[]) obj);
			String body = message.getMessageBody();
			String address = message.getOriginatingAddress();
			
			System.out.println(body+"  "+address);
		}
		
	}

}
