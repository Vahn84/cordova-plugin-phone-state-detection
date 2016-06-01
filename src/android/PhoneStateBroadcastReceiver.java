package com.vahn.cordova.phonestatedetection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.vahn.cordova.phonestatedetection.CustomPhoneStateListener;

/**
 * Created by vahn on 27/04/16.
 */
public class PhoneStateBroadcastReceiver extends BroadcastReceiver {

	private CustomPhoneStateListener customPhoneStateListener;
	private TelephonyManager telephonyManager;

    @Override
    public void onReceive(Context context, Intent intent) {
    	if(customPhoneStateListener==null){
    		customPhoneStateListener = new CustomPhoneStateListener(context);
    	}
    	if(telephonyManager==null){
         	telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    	}
        telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

    }

}
