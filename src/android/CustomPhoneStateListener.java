/**
 * Created by vahn on 27/04/16.
 */

package com.vahn.cordova.phonestatedetection;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import android.content.Intent;
import com.vahn.cordova.phonestatedetection.Constant;

public class CustomPhoneStateListener extends PhoneStateListener {



    SharedPreferences prefs;
    //private static final String TAG = "PhoneStateChanged";
    Context context; //Context to make Toast if required
    public CustomPhoneStateListener(Context context) {
        super();
        this.context = context;
        this.prefs = this.context.getSharedPreferences(Constant.PSD, Context.MODE_PRIVATE);
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                //when Idle i.e no call

                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //when Off hook i.e in call
                //Make intent and start your service here

                prefs.edit().putBoolean(Constant.CALL_HOOKED,true).commit();
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                //when Ringing
                
                prefs.edit().putBoolean(Constant.IS_PHONE_RINGING,true).commit();
                break;
            default:
                break;
        }
    }
}