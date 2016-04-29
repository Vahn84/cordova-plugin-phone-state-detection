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

    - (void) listen2PhoneSate:(CDVInvokedUrlCommand *) command {
        CTCallCenter *callCenter = [[CTCallCenter alloc] init];
        callCenter.callEventHandler=^(CTCall* call)
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
            }
            
            if(call.callState == CTCallStateConnected)
            {
                //The call state when the call is fully established for all parties involved.
                NSLog(@"Call Connected");
            }   
            
            if(call.callState == CTCallStateDisconnected)
            {
                //The call state Ended.
                NSLog(@"Call Ended");
            }
            
        };
    }

@end