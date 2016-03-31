package com.trace.log;

import com.tps.eppic.visadata.AdditionalResponseData;
import com.tps.eppic.visadata.AddressVerificationData;
import com.tps.eppic.visadata.VisaPrivateUseFields;

/*
 *Copy this class to project to make the parsing 
 * */

public class VisaField123Paser {
    public static void main(String[] args) {
        AddressVerificationData addressVerificationData  = new AddressVerificationData("66000DC005F7F7F0F5F1CF04F1F2F9F5");
        System.out.println(addressVerificationData.toString());
        
        
        AdditionalResponseData additionalResponseData = new AdditionalResponseData("7CD36CD3F2F8F0F2");
        System.out.println(additionalResponseData.codeMap.get(""));
        
        VisaPrivateUseFields visaPrivateUseFields = new VisaPrivateUseFields("0040000000032300F0F040404040");
        System.out.println(visaPrivateUseFields.cvv2AuthorizationRequestData.cvv2Value);
        
    }
}
