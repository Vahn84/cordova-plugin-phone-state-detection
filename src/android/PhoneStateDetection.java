/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author vahn
 */
package com.vahn.cordova.phonestatedetection;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PhoneStateDetection extends CordovaPlugin {


    public static final String ACTION_CHECK_PHONE_STATE = "checkPhoneState";
    public static final String PSD = "PhoneStatePrefs";
    public static final String IS_PHONE_RINGING = "isPhoneRinging";
    public static final String CALL_HOOKED = "isInPhoneCall";

    SharedPreferences prefs;
    Context context;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        Log.d("action", "execute action: " + action);

        // Route the Action
        if (action.equals(ACTION_CHECK_PHONE_STATE)) {
            return isInPhoneCall(args, callbackContext);
        }

        // Action not found
        callbackContext.error("action not recognised");
        return false;
    }

    private boolean isInPhoneCall(JSONArray args, CallbackContext callbackContext) {

        boolean isInPhoneCall;
        boolean isPhoneRinging;

        context = this.cordova.getActivity().getApplicationContext();
        prefs = context.getSharedPreferences(PSD, Context.MODE_PRIVATE);

        isPhoneRinging = prefs.getBoolean(IS_PHONE_RINGING, false);
        isInPhoneCall = prefs.getBoolean(CALL_HOOKED, false);
        prefs.edit().remove(CALL_HOOKED).remove(IS_PHONE_RINGING).commit();

        if (isPhoneRinging) {

            return isInPhoneCall;

        }

        return false;
    }


}
