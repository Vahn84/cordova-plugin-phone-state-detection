//
//  PhoneStateDetectionPlugin.m
//  Smartphoners
//
//  Created by Fabio Cingolani on 29/04/16.
//
//

#import <Foundation/Foundation.h>
#import <CoreTelephony/CTCallCenter.h>
#import <CoreTelephony/CTCall.h>
#import "PhoneStateDetectionPlugin.h"


@implementation PhoneStateDetectionPlugin : CDVPlugin

    - (void) listenPhoneState:(CDVInvokedUrlCommand *) command {
        self.callCenter = [[CTCallCenter alloc] init];
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(callReceived:) name:CTCallStateIncoming object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(callEnded:) name:CTCallStateDisconnected object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(callConnected:) name:CTCallStateConnected object:nil];
        self.isInPhoneCall = false;
        self.isPhoneRinging = false;
        self.callCenter.callEventHandler=^(CTCall* call)
        {
            
            if(call.callState == CTCallStateDialing)
            {
                //The call state, before connection is established, when the user initiates the call.
                NSLog(@"Call is dailing");
            }
            if(call.callState == CTCallStateIncoming)
            {
                //The call state, before connection is established, when a call is incoming but not yet answered by the user.
                NSLog(@"Call is Coming");
                self.isPhoneRinging = true;
            }
            
            if(call.callState == CTCallStateConnected)
            {
                //The call state when the call is fully established for all parties involved.
                NSLog(@"Call Connected");
                self.isInPhoneCall = true;
            }   
            
            if(call.callState == CTCallStateDisconnected)
            {
                //The call state Ended.
                NSLog(@"Call Ended");
                NSMutableDictionary *jsonObj = [ [NSMutableDictionary alloc]
                                                initWithObjectsAndKeys :
                                                [NSNumber numberWithBool:_isPhoneRinging], @"isPhoneRinging",
                                                [NSNumber numberWithBool:_isInPhoneCall], @"isInPhoneCall",
                                                nil
                                               ];
                CDVPluginResult *pluginResult = [ CDVPluginResult
                                                 resultWithStatus    : CDVCommandStatus_OK
                                                 messageAsDictionary : jsonObj
                                                 ];
                [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
            }
            
        };
    }

    - (void) stopListenPhoneState:(CDVInvokedUrlCommand *) command {
        [[NSNotificationCenter defaultCenter] removeObserver:self];
    }

@end