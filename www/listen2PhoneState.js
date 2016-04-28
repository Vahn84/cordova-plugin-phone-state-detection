/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


var phone_state =
        {
            listen2PhoneState: function (phoneStateSuccessCallback, phoneStateErrorCallback, action)
            {
                cordova.exec(
                        phoneStateSuccessCallback,
                        phoneStateErrorCallback,
                        'PhoneStateDetectionPlugin',
                        action,
                        []
                            );
            }
        };
        
        module.exports = phone_state;