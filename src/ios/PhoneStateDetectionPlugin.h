//
//  PhoneStateDetectionPlugin.h
//  Smartphoners
//
//  Created by Fabio Cingolani on 29/04/16.
//
//

#import <Cordova/CDV.h>
#import <CoreTelephony/CTCallCenter.h>
#import <CoreTelephony/CTCall.h>

@interface PhoneStateDetectionPlugin : CDVPlugin

@property (nonatomic, strong) CTCallCenter *callCenter;
@property bool isInPhoneCall;
@property bool isPhoneRinging;

- (void) listenPhoneState:(CDVInvokedUrlCommand *) command;
- (void) stopListenPhoneState:(CDVInvokedUrlCommand *) command;

@end