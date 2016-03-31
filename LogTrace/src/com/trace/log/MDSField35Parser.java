package com.trace.log;

import com.tps.childsupport.CSUtil;
import com.tps.childsupport.fes.Track2Data;

public class MDSField35Parser {

    public static void main(String[] args) {
        
        Track2Data track2Data = null;
        try {
            track2Data = CSUtil.findTrack2Data("99738BD03013057C=1806101xxxxxxxxxxxxx");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(track2Data.discretionaryData);
    }
}
