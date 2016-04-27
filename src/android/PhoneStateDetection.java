package com.vahn.cordova.phonestatedetection;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class PhoneStateDetection extends CordovaPlugin {


    public static final String ACTION_CHECK_PHONE_STATE = "checkPhoneState";
    public static final String PSD = "PhoneStatePrefs";
    public static final String IS_PHONE_RINGING = "isPhoneRinging";
    public static final String CALL_HOOKED = "isInPhoneCall";

    SharedPreferences prefs;
    Context context;
    CustomPhoneStateListener cpsListener;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        // Route the Action

        cpsListener = new CustomPhoneStateListener();
        TelephonyManager TelephonyMgr = (TelephonyManager) cordova.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyMgr.listen(cpsListener, PhoneStateListener.LISTEN_CALL_STATE);
        cpsListener.setCallbackContext(callbackContext);

       
    }

}

class CustomPhoneStateListener extends PhoneStateListener {

    private CallbackContext callbackContext;

    public void setCallbackContext(CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
    }

    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        if (callbackContext == null) return;

        String msg = "";

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
            msg = "IDLE";
            break;

            case TelephonyManager.CALL_STATE_OFFHOOK:
            msg = "OFFHOOK";
            break;

            case TelephonyManager.CALL_STATE_RINGING:
            msg = "RINGING";
            break;
        }

        PluginResult result = new PluginResult(PluginResult.Status.OK, msg);
        result.setKeepCallback(true);

        callbackContext.sendPluginResult(result);
    }
}
