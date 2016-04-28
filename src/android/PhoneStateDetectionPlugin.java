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

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        Log.d("action", action);
        this.context = this.cordova.getActivity().getApplicationContext();


        // Route the Action
        if (action.equals(Constant.ACTION_CHECK_PHONE_STATE)) {
            IntentFilter intentFilter = new IntentFilter(Constant.BROADCAST_PHONE_STATE_INTENT_ACTION);

            this.context.registerReceiver(mMessageReceiver, intentFilter);
            return true;
        } else if (action.equals(Constant.ACTION_DESTROY)) {
            try {
 
                this.context.unregisterReceiver(mMessageReceiver);

            } catch (Exception e) {

            }

            return true;

        } else {
            // Action not found
            cbContext.error("action not recognised");
            return false;
        }


    }

    private void sendPhoneState(CallbackContext callbackContext, Context context, boolean callHooked, boolean phoneRinging) {

        Log.d("inside Check", "sendPhoneState");

        boolean isInPhoneCall;
        boolean isPhoneRinging;
        JSONObject parameter = new JSONObject();
        ;


        try {

            parameter.put(Constant.IS_PHONE_RINGING, phoneRinging);
            parameter.put(Constant.CALL_HOOKED, callHooked);

        } catch (JSONException e) {

            Log.e("ERRORE SMARTPHONERS", e.toString());

        }

        PluginResult result = new PluginResult(PluginResult.Status.OK, parameter);
        result.setKeepCallback(true);
        callbackContext.sendPluginResult(result);

    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context brContext, Intent intent) {
            boolean callHooked = intent.getBooleanExtra(Constant.CALL_HOOKED, false);
            boolean phoneRinging = intent.getBooleanExtra(Constant.IS_PHONE_RINGING, false);
            sendPhoneState(cbContext, context, callHooked, phoneRinging);
        }
    };


}
