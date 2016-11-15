package com.vahn.cordova.phonestatedetection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Binder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;


/**
 * Created by vahn on 27/04/16.
 */

public class CustomPhoneStateListener extends PhoneStateListener {
    
    private boolean callHooked = false;
    private boolean phoneRinging = false;
    private boolean missedCall = false;
    private boolean headsetOn = false;
    private boolean isCallEnded = false;
    private static boolean firstCallback = true;
    private static Intent intent = new Intent();
    private static AudioManager audioManager;
    private boolean autoReplyBySMS;
    private boolean rejectPhoneCall;
    private SmsManager smsManager;
    private com.android.internal.telephony.ITelephony telephonyService;
    
    SharedPreferences prefs;
    //private static final String TAG = "PhoneStateChanged";
    Context context; //Context to make Toast if required
    public CustomPhoneStateListener(Context context, boolean rejectPhoneCall, boolean autoReplyBySMS) {
        super();
        this.context = context;
        this.prefs = this.context.getSharedPreferences(Constant.PSD, Context.MODE_PRIVATE);
        this.audioManager = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
        this.autoReplyBySMS = autoReplyBySMS;
        this.rejectPhoneCall = rejectPhoneCall;
        
    }
    
    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        
        
        
        
        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                //when Idle i.e no call
                if(phoneRinging && !callHooked)
                {   isCallEnded = true;
                    missedCall = true;
                    if(autoReplyBySMS){
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(incomingNumber, null, Constant.SMS_MESSAGE, null, null);
                    }
                } else if(phoneRinging && callHooked){
                    missedCall = false;
                    headsetOn = checkHeadsetConnection(audioManager);
                    isCallEnded = true;
                    
                }
                
                if(firstCallback)
                {
                    firstCallback = false;
                    
                    sendCustomBroadcast(phoneRinging, callHooked, missedCall, isCallEnded, headsetOn, context);
                    phoneRinging = false;
                    missedCall = false;
                    callHooked = false;
                    headsetOn = false;
                    isCallEnded = false;
                    
                    
                }
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //when Off hook i.e in call
                phoneRinging = true;
                callHooked = true;
                headsetOn = checkHeadsetConnection(audioManager);
                
                
                
                sendCustomBroadcast(phoneRinging, callHooked, isCallEnded, missedCall,headsetOn, context);
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                //when Ringing
                firstCallback = true;
                phoneRinging = true;
                callHooked = false;
                sendCustomBroadcast(phoneRinging, callHooked, isCallEnded, missedCall,headsetOn, context);
                if(rejectPhoneCall){
                    
                    sendRejectCall();
                    
                }
                break;
        }
    }
    
    private static boolean sendCustomBroadcast(boolean phoneRinging, boolean callHooked, boolean missedCall, boolean isCallEnded, boolean headsetOn, Context context){
        
        
        intent.setAction(Constant.BROADCAST_PHONE_STATE_INTENT_ACTION);
        intent.putExtra(Constant.IS_PHONE_RINGING, phoneRinging);
        intent.putExtra(Constant.CALL_HOOKED, callHooked);
        intent.putExtra(Constant.IS_MISSED_CALL, missedCall);
        intent.putExtra(Constant.IS_HEADSET_ON, headsetOn);
        intent.putExtra(Constant.IS_CALL_ENDED, isCallEnded);
        context.sendBroadcast(intent);
        
        return true;
    }
    
    private static boolean checkHeadsetConnection(AudioManager audioManager) {
        return (audioManager.isBluetoothScoOn() || audioManager.isWiredHeadsetOn() || audioManager.isBluetoothA2dpOn()) ? true : false;
    }
    
    public void sendRejectCall()
    {
        try {
            
            Log.d("REJCT","trying to reject call");
            
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            
            Method m1 = tm.getClass().getDeclaredMethod("getITelephony");
            m1.setAccessible(true);
            Object iTelephony = m1.invoke(tm);
            
            Method m3 = iTelephony.getClass().getDeclaredMethod("endCall");
            
            m3.invoke(iTelephony);
            
        }catch (Exception e){
            
        }
        
    }
}
