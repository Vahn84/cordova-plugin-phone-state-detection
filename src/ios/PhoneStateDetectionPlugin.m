//
//  PhoneStateDetectionPlugin.m
//  Smartphoners
//
//  Created by Fabio Cingolani on 29/04/16.
//
//

#import <Foundation/Foundation.h>
#import <AVFoundation/AVFoundation.h>
#import <CoreTelephony/CTCallCenter.h>
#import <CoreTelephony/CTCall.h>
#import "PhoneStateDetectionPlugin.h"


@implementation PhoneStateDetectionPlugin : CDVPlugin

- (void) listenPhoneState:(CDVInvokedUrlCommand *) command {
    self.callCenter = [[CTCallCenter alloc] init];
    
    [self prepareAudioSession];
    
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(callReceived:) name:CTCallStateIncoming object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(callEnded:) name:CTCallStateDisconnected object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(callConnected:) name:CTCallStateConnected object:nil];
    self.isInPhoneCall = false;
    self.isPhoneRinging = false;
    self.isMissedCall = true;
    self.isHeadsetOn = false;
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
            self.isMissedCall = false;
            self.isHeadsetOn = [self isHeadsetPluggedIn];
            NSLog(@"Call Connected");
        }
        
        if(call.callState == CTCallStateDisconnected)
        {
            if(self.isInPhoneCall == true)
            {
                self.isHeadsetOn = [self isHeadsetPluggedIn];
            }
            
            //The call state Ended.
            NSLog(@"Call Ended");
            NSMutableDictionary *jsonObj = [ [NSMutableDictionary alloc]
                                            initWithObjectsAndKeys :
                                            [NSNumber numberWithBool:_isPhoneRinging], @"isPhoneRinging",
                                            [NSNumber numberWithBool:_isInPhoneCall], @"isInPhoneCall",
                                            [NSNumber numberWithBool:_isMissedCall], @"isMissedCall",
                                            [NSNumber numberWithBool:_isHeadsetOn], @"isHeadsetOn",
                                            nil
                                            ];
            CDVPluginResult *pluginResult = [ CDVPluginResult
                                             resultWithStatus    : CDVCommandStatus_OK
                                             messageAsDictionary : jsonObj
                                             ];
            [pluginResult setKeepCallbackAsBool:YES];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        }
        
    };
}

- (void) stopListenPhoneState:(CDVInvokedUrlCommand *) command {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (bool)isHeadsetPluggedIn {
    AVAudioSessionRouteDescription* route = [[AVAudioSession sharedInstance] currentRoute];
    
    NSArray *arrayInputs = [[AVAudioSession sharedInstance] availableInputs];
    for (AVAudioSessionPortDescription *port in arrayInputs)
    {
        if ([port.portType isEqualToString:AVAudioSessionPortBluetoothHFP])
        {
            return true;
        }
    }
    
    for (AVAudioSessionPortDescription* desc in [route outputs]) {
        NSLog(@"port ",[[desc portType]);
                        if ([[desc portType] isEqualToString:AVAudioSessionPortHeadphones])
                        return true;
                        }
                        return false;
                        }
                        
                        - (bool) prepareAudioSession {
                            
                            // deactivate session
                            bool success = [[AVAudioSession sharedInstance] setActive:NO error: nil];
                            if (!success) {
                                NSLog(@"deactivationError");
                            }
                            
                            // set audio session category AVAudioSessionCategoryPlayAndRecord options AVAudioSessionCategoryOptionAllowBluetooth
                            success = [[AVAudioSession sharedInstance] setCategory:AVAudioSessionCategoryPlayAndRecord withOptions:AVAudioSessionCategoryOptionAllowBluetooth error:nil];
                            if (!success) {
                                NSLog(@"setCategoryError");
                            }
                            
                            // activate audio session
                            success = [[AVAudioSession sharedInstance] setActive:YES error: nil];
                            if (!success) {
                                NSLog(@"activationError");
                            }
                            
                            return success;
                        }
                        
                        
                        @end
