package com.vahn.cordova.phonestatedetection;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.cordova.api.Plugin
import org.apache.cordova.api.PluginResult;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PhoneStateDetectionPlugin extends CordovaPlugin {


    public static final String ACTION_CHECK_PHONE_STATE = "checkPhoneState";
    public static final String PSD = "PhoneStatePrefs";
    public static final String IS_PHONE_RINGING = "isPhoneRinging";
    public static final String CALL_HOOKED = "isInPhoneCall";

    SharedPreferences prefs;
    Context context;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        Log.d("action", action);
        this.context = this.cordova.getActivity().getApplicationContext();
        // Route the Action
        if (action.equals(ACTION_CHECK_PHONE_STATE)) {
            checkPhoneState(args, callbackContext, this.context);
            return true;
        }

        else {
             // Action not found
            callbackContext.error("action not recognised");
            return false;
        }

       
    }

    private void checkPhoneState(JSONArray args, CallbackContext callbackContext, Context context) {

        Log.d("inside Check", "checkPhoneState");

        boolean isInPhoneCall;
        boolean isPhoneRinging;

        prefs = context.getSharedPreferences(PSD, Context.MODE_PRIVATE);

        isPhoneRinging = prefs.getBoolean(IS_PHONE_RINGING, false);
        isInPhoneCall = prefs.getBoolean(CALL_HOOKED, false);
        prefs.edit().remove(CALL_HOOKED).remove(IS_PHONE_RINGING).commit();
        try {
            JSONObject parameter = new JSONObject();
            parameter.put("isPhoneRinging", isPhoneRinging);
            parameter.put("isInPhoneCall", isInPhoneCall);
        } catch (JSONException e) {
                Log.e("ERRORE SMARTPHONERS", e.toString());
        }

        PluginResult result = new PluginResult(PluginResult.Status.OK, parameter);
        result.setKeepCallback(true);
        callbackContext.sendPluginResult(result);
        
    }


}
