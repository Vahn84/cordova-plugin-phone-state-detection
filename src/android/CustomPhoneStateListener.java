package com.vahn.cordova.phonestatedetection;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import android.content.Intent;
import com.vahn.cordova.phonestatedetection.Constant;

/**
 * Created by vahn on 27/04/16.
 */

public class CustomPhoneStateListener extends PhoneStateListener {

    private boolean callHooked = false;
    private boolean phoneRinging = false;
    private boolean missedCall = false;
    private static firstCallback = true;
    Intent intent;

    SharedPreferences prefs;
    //private static final String TAG = "PhoneStateChanged";
    Context context; //Context to make Toast if required
    public CustomPhoneStateListener(Context context) {
        super();
        this.context = context;
        this.prefs = this.context.getSharedPreferences(Constant.PSD, Context.MODE_PRIVATE);
        this.intent = new Intent();
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                //when Idle i.e no call
            	if(phoneRinging && !callHooked)
            	{
            		missedCall = true;
            	} else if(phoneRinging && callHooked){
            		missedCall = false;
            	}
            	if(firstCallback)
            	{	
            		firstCallback = false;
            		sendCustomBroadcast(phoneRinging, callHooked, missedCall, context);
            		phoneRinging = false;
            		missedCall = false;
            		callHooked = false;
            	}
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //when Off hook i.e in call
                phoneRinging = true;
                callHooked = true;
                //sendCustomBroadcast(phoneRinging, callHooked, missedCall, context);
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                //when Ringing
            	firstCallback = true;
                phoneRinging = true;
                callHooked = false;
                //sendCustomBroadcast(phoneRinging, callHooked, missedCall, context);
                break;
        }
    }

    private static boolean sendCustomBroadcast(boolean phoneRinging, boolean callHooked, boolean missedCall, Context context){

        
        intent.setAction(Constant.BROADCAST_PHONE_STATE_INTENT_ACTION);
        intent.putExtra(Constant.IS_PHONE_RINGING, phoneRinging);
        intent.putExtra(Constant.CALL_HOOKED, callHooked);
        intent.putExtra(Constant.IS_MISSED_CALL, missedCall);
        context.sendBroadcast(intent);

        return true;
    }
}