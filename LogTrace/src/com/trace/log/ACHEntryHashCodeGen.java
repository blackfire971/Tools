package com.trace.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ACHEntryHashCodeGen {
    
    private long __currentBatchEntryHash = 0;
    private long __totalEntryHash = 0;
    static long totalDebit = 0;
    static long totalCredit = 0;
    static int batchCount = 0;
    static int detailCount = 0;
    
    public static List<String> convertFileToList(File file) {
        List<String> list = new ArrayList<String>();
        BufferedReader bufferedReader = null;
        ACHEntryHashCodeGen ach = new ACHEntryHashCodeGen();
        
        if (file == null || !file.isFile()) {
            return null;
        } else {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file)));
                String tmpLine = "";
                boolean flag = true;
                String rountNbr = null;
                do {
                    tmpLine = bufferedReader.readLine();
                    if (tmpLine == null || tmpLine.trim().length() == 0) {
                        break;
                    } else if (tmpLine.startsWith("6")){
                        rountNbr = tmpLine.substring(3,11);
                        ach.updateEntryHash(rountNbr);
                        detailCount++;
                    } else if (tmpLine.startsWith("7")) {
                        detailCount++;
                    } else if (tmpLine.startsWith("8")) {
                        //debit
                        totalDebit += Long.parseLong(tmpLine.substring(20,32));
                        //credit
                        totalCredit +=Long.parseLong(tmpLine.substring(32,44));
                        //batch count
                        batchCount++;
                    }
                } while (flag);
                //use the file header in pre-split file
//                System.out.println("101 072000096 0720000961410231122U094101ACS COMPCARD           COMERICA BANK                  ");
//                System.out.println("9"
//                        +lPad(String.valueOf(batchCount), 6, '0')
//                        +lPad("0", 6, '0')
//                        +lPad(String.valueOf(detailCount), 8, '0')
//                        +lPad(String.valueOf(ach.getTotalEntryHash()), 10, '0')
//                        +lPad(String.valueOf(totalDebit), 12, '0')
//                        +lPad(String.valueOf(totalCredit), 12, '0')
//                        +lPad(" ", 39, ' '));
                System.out.println("Hashcode:"+lPad(String.valueOf(ach.getTotalEntryHash()), 10, '0'));
                
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return null;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        //sort before return 
        Collections.sort(list);
        return list;
    }
    
    protected void updateEntryHash (String routingNumberString) {

        String string;
        final long routingNumber = Long.parseLong(routingNumberString);

        // update totalEntryHash and then check to see
        // if the hash has spilled over into 11 digits
        // if so, truncate to 10 digits before continuing
        __totalEntryHash += routingNumber;
        string = String.valueOf(__totalEntryHash);
        if (string.length() > 10) {
            string = string.substring(string.length() - 10, string.length());
            __totalEntryHash = Long.parseLong(string);
        }

        // update currentBatchEntryHash and then check to see
        // if the batch hash has spilled over into 11 digits
        // if so, truncate to 10
        __currentBatchEntryHash += routingNumber;
        string = String.valueOf(__currentBatchEntryHash);
        if (string.length() > 10) {
            string = string.substring(string.length() - 10, string.length());
            __currentBatchEntryHash = Long.parseLong(string);
        }
    }   
    
    public long getTotalEntryHash()
    {
        return __totalEntryHash;
    }
    
    public static String lPad(String s, int len, char padChar) {
        if (s == null)
        s = "";
          String retString = s.trim();
          if (retString.length() > len)
              return retString.substring(retString.length() - len, retString
                      .length());
          else {
              StringBuffer sb = new StringBuffer("");
              for (int i = retString.length(); i < len; i++) {
                  sb.append(padChar);
              }
              return sb.toString() + retString;
          }
      }
    
    public static void main(String[] args) {
        convertFileToList(new File("C:\\TEMP\\OH20150604123657007CSEDEP"));
    }
    
}
