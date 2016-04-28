package com.vahn.cordova.phonestatedetection;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.widget.Toast;
import com.vahn.cordova.phonestatedetection.Constant;

public class PhoneStateDetectionPlugin extends CordovaPlugin {


    SharedPreferences prefs;
    Context context;
    CallbackContext cbContext;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        Log.d("action", action);
        this.context = this.cordova.getActivity().getApplicationContext();
        this.cbContext = callbackContext;
        
        // Route the Action
        if (action.equals(Constant.ACTION_CHECK_PHONE_STATE)) {
            Intent intentFilter = new IntentFilter(Constant.BROADCAST_PHONE_STATE_INTENT_ACTION);
            this.context.registerReceiver(mMessageReceiver, intentFilter);
            return true;
        }

        else if(action.equals(Constant.ACTION_DESTROY)) {
            this.context.unregisterReceiver(mMessageReceiver);
        }

        else {
             // Action not found
            cbContext.error("action not recognised");
            return false;
        }

       
    }

    private void checkPhoneState(CallbackContext callbackContext, Context context) {

        Log.d("inside Check", "checkPhoneState");

        boolean isInPhoneCall;
        boolean isPhoneRinging;
        JSONObject parameter = new JSONObject();;

        prefs = context.getSharedPreferences(PSD, Context.MODE_PRIVATE);

        isPhoneRinging = prefs.getBoolean(IS_PHONE_RINGING, false);
        isInPhoneCall = prefs.getBoolean(CALL_HOOKED, false);
        prefs.edit().remove(CALL_HOOKED).remove(IS_PHONE_RINGING).commit();
        try {
           
            parameter.put("isPhoneRinging", isPhoneRinging);
            parameter.put("isInPhoneCall", isInPhoneCall);
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
            
            checkPhoneState(cbContext, context);
        }
    }


}
