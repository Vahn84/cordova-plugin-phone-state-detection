package com.vahn.cordova.phonestatedetection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.widget.Toast;

import com.vahn.cordova.phonestatedetection.Constant;

/**
 * Created by vahn on 27/04/16.
 */

public class PhoneStateDetectionPlugin extends CordovaPlugin {


    Context context;
    CallbackContext cbContext;
    PluginResult result;
    IntentFilter intentFilter = new IntentFilter(Constant.BROADCAST_PHONE_STATE_INTENT_ACTION);
    IntentFilter smsIntentFilter = new IntentFilter(Constant.BROADCAST_SMS_LISTENER_INTENT_ACTION);
    IntentFilter phoneCallIntentFilter = new IntentFilter(Constant.BROADCAST_PHONE_CALL_LISTENER_INTENT_ACTION);
    boolean rejectPhoneCall = false;
    boolean autoReplyBySMS = false;
    boolean autoReplyToSMSBySMS = false;
    TelephonyManager telephonyManager;
    CustomPhoneStateListener customPhoneStateListener;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {



        Log.d("args", args.toString());

        Log.d("action", action);

        Log.d("rejectPhoneCall", String.valueOf(rejectPhoneCall));
        Log.d("autoReplyBySMS", String.valueOf(autoReplyBySMS));
        Log.d("autoReplyToSMSBySMS", String.valueOf(autoReplyToSMSBySMS));

        this.context = this.cordova.getActivity().getApplicationContext();
        this.cbContext = callbackContext;

        // Route the Action
        if (action.equals(Constant.ACTION_CHECK_PHONE_STATE)) {

            this.context.registerReceiver(mMessageReceiver, intentFilter);

            if(customPhoneStateListener==null){
                customPhoneStateListener = new CustomPhoneStateListener(this.context , rejectPhoneCall, autoReplyBySMS);
            }
            if(telephonyManager==null){
                telephonyManager = (TelephonyManager) this.context .getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
            }

            if(autoReplyToSMSBySMS){
                this.context.registerReceiver(SMSReceiver, smsIntentFilter);
            }

            return true;
        } else if (action.equals(Constant.ACTION_DESTROY)) {
            try {

                if(telephonyManager!=null){
                    telephonyManager.listen(customPhoneStateListener, PhoneStateListener.LISTEN_NONE);
                }

                this.context.unregisterReceiver(mMessageReceiver);

                this.context.unregisterReceiver(SMSReceiver);

            } catch (Exception e) {

            }

            return true;

        } else {
            // Action not found
            cbContext.error("action not recognised");
            return false;
        }


    }

    private void sendPhoneState(CallbackContext callbackContext, Context context, boolean callHooked, boolean phoneRinging, boolean missedCalls, boolean isCallEnded, boolean isHeadsetOn) {

        Log.d("inside Check", "sendPhoneState");


        JSONObject parameter = new JSONObject();

        try {

            parameter.put(Constant.IS_PHONE_RINGING, phoneRinging);
            parameter.put(Constant.CALL_HOOKED, callHooked);
            parameter.put(Constant.IS_MISSED_CALL, missedCalls);
            parameter.put(Constant.IS_HEADSET_ON, isHeadsetOn);
            parameter.put(Constant.IS_CALL_ENDED, isCallEnded);

        } catch (JSONException e) {

            Log.e("ERRORE SMARTPHONERS", e.toString());

        }

        result = new PluginResult(PluginResult.Status.OK, parameter);
        result.setKeepCallback(true);
        callbackContext.sendPluginResult(result);

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context brContext, Intent intent) {
            boolean callHooked = intent.getBooleanExtra(Constant.CALL_HOOKED, false);
            boolean phoneRinging = intent.getBooleanExtra(Constant.IS_PHONE_RINGING, false);
            boolean isMissedCall = intent.getBooleanExtra(Constant.IS_MISSED_CALL, false);
            boolean isHeadsetOn = intent.getBooleanExtra(Constant.IS_HEADSET_ON, false);
            boolean isCallEnded = intent.getBooleanExtra(Constant.IS_CALL_ENDED, false);
            sendPhoneState(cbContext, context, callHooked, phoneRinging, isMissedCall, isCallEnded, isHeadsetOn);
        }
    };

    private BroadcastReceiver SMSReceiver = new BroadcastReceiver() {

        final SmsManager smsManager = SmsManager.getDefault();

        @Override
        public void onReceive(Context brContext, Intent intent) {

            // Retrieves a map of extended data from the intent.
            final Bundle bundle = intent.getExtras();
            final SmsMessage smsMessage;
            String phoneNumber = "";

            if (Build.VERSION.SDK_INT >= 19) {
                SmsMessage[] msgs = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                smsMessage = msgs[0];
                phoneNumber = smsMessage.getDisplayOriginatingAddress();
            } else {

                try {

                    if (bundle != null) {

                        final Object[] pdusObj = (Object[]) bundle.get("pdus");
                        smsMessage = SmsMessage.createFromPdu((byte[]) pdusObj[0]);
                        phoneNumber = smsMessage.getDisplayOriginatingAddress();
                    } // bundle is null

                } catch (Exception e) {
                    Log.e("SmsReceiver", "Exception smsReceiver" + e);

                }
            }
            if(phoneNumber != "") {
                smsManager.sendTextMessage(phoneNumber, null, Constant.SMS_MESSAGE, null, null);
            }
        }
    };



}
