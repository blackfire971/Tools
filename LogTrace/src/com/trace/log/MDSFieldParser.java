package com.trace.log;

import java.util.ArrayList;

import org.apache.commons.validator.GenericValidator;

import com.tps.childsupport.MasterCardUtil;
import com.tps.childsupport.fes.SubElement;
import com.tps.childsupport.idl.TransactionRequestD;

public class MDSFieldParser {
    public static void main(String[] args) {
        TransactionRequestD authRequest = new TransactionRequestD();
        authRequest.messageType = (short) 200; // field 0
        // authRequest.pan = card.getPan(); //field 2
        // authRequest.processingCode = "000000"; //means withdrawal field 3
        // authRequest.trxAmount = 02.00; //field 4
        // authRequest.transmissionDT =IsoUtils.getTransmissionDate(); //field 7
        // authRequest.traceNumber = IsoUtils.getNextTraceNbr(); //field 11
        // authRequest.localTransactionDT = localTransactionDate; //DE 13
        // authRequest.settlementDT = settlementDate; //field 15
        // authRequest.merchantType = "6010"; //field 18
        // authRequest.posEntry = new
        // POSEntryModeD(PANentryModeD.MAGNETIC_STRIPE,PINentryModeD.PIN_ENTRY_CAPABILITY);
        // //field 22
        // authRequest.acquirer = acquirerFormat.format(new Date()); //field 32
        // authRequest.forwarderId = sdf.format(new Date()); //field 33
        // authRequest.retrievalRefNum =
        // IsoUtils.getRetrievalRefNbr(authRequest.traceNumber,
        // authRequest.localTransactionDT); //field 37
        // authRequest.caTerminalId = "99999999"; //field 41
        // authRequest.caIdCode = "087531000760660"; //field 42
        authRequest.additionalData = "R610500000"; // F48
        // authRequest.transactionCurrencyCode = "840"; //field 49
        // authRequest.pin = StringUtil.hexString2Bytes(card.getPin()); //field
        // 52
        // authRequest.posData = "10100040010018400000000000"; //char(6) == 4
        // means preauth (for msg type 200) //field 61
        // authRequest.originalData = new OriginalDataElementsD(new
        // Short(authRequest.messageType).toString(),
        // authRequest.traceNumber,authRequest.transmissionDT,
        // authRequest.acquirer,authRequest.forwarderId); //field 90 Original
        // Data Element
        //
        // authRequest.networkData = IsoMDSUtils.getNetworkData("MD");
        // //+ new
        // Long(rPad(TransactiononlineAccessor.getNetworkRefNbr().toString(),9,'0')).longValue()
        // + 1); //field 63
        //
        if ((authRequest.additionalData == null)
                || (authRequest.additionalData.length() == 0)) {
            /* 802 */System.out.println("it is null");
            /*     */}
        /* 804 */int start = 0;
        /* 805 */if (!Character.isDigit(authRequest.additionalData.charAt(0))) {
            /* 806 */start++;
            /*     */}
        /* 808 */ArrayList subElements = MasterCardUtil.findSubElements(
                authRequest.additionalData, start);
        /* 809 */SubElement subElement = MasterCardUtil.findElement(
                subElements, 61);
        /* 810 */if ((subElement != null)
                && (!GenericValidator.isBlankOrNull(subElement.data))) {
            /* 811 */System.out.println( subElement.data.substring(0, 1));
            /*     */}
        /* 813 */System.out.println("it is null");

    }
}
