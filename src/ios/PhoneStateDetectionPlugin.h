//
//  PhoneStateDetectionPlugin.h
//  Smartphoners
//
//  Created by Fabio Cingolani on 29/04/16.
//
//

#import <Cordova/CDV.h>

@interface PhoneStateDetectionPlugin : CDVPlugin

- (void) listen2PhoneSate:(CDVInvokedUrlCommand *) command;

@end