//
//  PhoneStateDetectionPlugin.m
//  Smartphoners
//
//  Created by vahn on 29/04/16.
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

    self.callCenter.callEventHandler = ^(CTCall *call){
        
        if ([call.callState isEqualToString: CTCallStateConnected])
        {
            NSLog(@"call CTCallStateConnected");//Background task stopped
            self.isInPhoneCall = true;
        }
        else if ([call.callState isEqualToString: CTCallStateDialing])
        {
            NSLog(@"call CTCallStateDialing");
        }
        else if ([call.callState isEqualToString: CTCallStateIncoming])
        {
            NSLog(@"call CTCallStateIncoming");
            
            self.isPhoneRinging = true;

        }
        else if ([call.callState isEqualToString: CTCallStateDisconnected])
        {
            NSLog(@"call CTCallStateDisconnected");//Background task started
            
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
        else
        {
            NSLog(@"call NO");
        }
    };

    

}



@end