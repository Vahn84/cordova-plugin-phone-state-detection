package com.vahn.cordova.phonestatedetection;

public class Constant
{
    public static final String ACTION_CHECK_PHONE_STATE = "listenPhoneState";
    public static final String ACTION_DESTROY = "stopListenPhoneState";
    public static final String PSD = "PhoneStatePrefs";
    public static final String IS_PHONE_RINGING = "isPhoneRinging";
    public static final String CALL_HOOKED = "isInPhoneCall";
    public static final String IS_MISSED_CALL = "isMissedCall";
    public static final String IS_CALL_ENDED = "isCallEnded";
    public static final String IS_HEADSET_ON = "isHeadsetOn";
    public static final String SMS_MESSAGE = "Ciao! Non posso risponderti! Sto guidando con Smartphoners! http://smartphoners.it";
    public static final String BROADCAST_PHONE_STATE_INTENT_ACTION = "com.vahn.cordova.phonestatedetection.BROADCAST_PHONE_STATE_EVENT";
    public static final String BROADCAST_SMS_LISTENER_INTENT_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    public static final String BROADCAST_PHONE_CALL_LISTENER_INTENT_ACTION = "android.intent.action.PHONE_STATE";
}
