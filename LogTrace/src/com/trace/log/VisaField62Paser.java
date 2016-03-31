package com.trace.log;

import com.tps.eppic.visadata.CustomPaymentServiceFields;

public class VisaField62Paser {
    public static void main(String[] args) {
        //parameter is the value of field 62
        CustomPaymentServiceFields customPaymentServiceFields = new CustomPaymentServiceFields("D000080000000000E50305216540471250C2F0F2F0F0");
        System.out.println(customPaymentServiceFields.f4);
    }
}
