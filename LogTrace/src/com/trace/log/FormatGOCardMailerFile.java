package com.trace.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FormatGOCardMailerFile {

    public static void process(String incomingFile, String outgoingFile) {
        BufferedReader br = null;
        BufferedWriter out = null;
        try { // ��ֹ�ļ��������ȡʧ�ܣ���catch��׽���󲢴�ӡ��Ҳ����throw  
            
            /* ����TXT�ļ� */  
            String pathname = incomingFile; // ����·�������·�������ԣ������Ǿ���·����д���ļ�ʱ��ʾ���·��  
            File filename = new File(pathname); // Ҫ��ȡ����·����input��txt�ļ�  
            InputStreamReader reader = new InputStreamReader(  
                    new FileInputStream(filename)); // ����һ������������reader  
            br = new BufferedReader(reader); // ����һ�����������ļ�����ת�ɼ�����ܶ���������  
            String line = ""; 
            
            /* д��Txt�ļ� */  
            File writename = new File(outgoingFile); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�  
            writename.createNewFile(); // �������ļ�  
            out = new BufferedWriter(new FileWriter(writename));  
            int batch_id = 0;
            int cvc2Sum = 0;
            int detailCount = 0;
            
            do {
                line = br.readLine(); 
                if (line != null && line.trim().length() >0) {
                    if (line.startsWith("FH") || line.startsWith("TT")) {
                        continue;
                    }else if (line.startsWith("H")) {
                        batch_id++;
                        char[] batchChars = line.toCharArray();
                        String batchIdString = padLeft(String.valueOf(batch_id), '0', 6);
                        char[] batchIdChars = batchIdString.toCharArray();
                        for (int i = 0; i < batchIdChars.length; i++) {
                            batchChars[i+1] = batchIdChars[i];
                        }
                        line = String.valueOf(batchChars);
                        out.write(line); // ��Ϊ����
                        out.write("\r\n");
                    }else if (line.startsWith("T")) {
                        char[] batchChars = line.toCharArray();
                        String batchIdString = padLeft(String.valueOf(batch_id), '0', 6);
                        char[] batchIdChars = batchIdString.toCharArray();
                        for (int i = 0; i < batchIdChars.length; i++) {
                            batchChars[i+1] = batchIdChars[i];
                        }
//                        line = batchChars.toString();
//                        out.write(line); // ��Ϊ����
//                        out.write("\r\n");
                        
                        //udpate detail count
                        String detailCountString = padLeft(String.valueOf(detailCount), '0', 8);
                        char[] detailCountChars = detailCountString.toCharArray();
                        for (int i = 0; i < detailCountChars.length; i++) {
                            batchChars[i+7] = detailCountChars[i];
                        }
                        
                        //udpate detail count
                        String cvc2SumString = padLeft(String.valueOf(cvc2Sum), '0', 8);
                        char[] cvc2SumChars = cvc2SumString.toCharArray();
                        for (int i = 0; i < cvc2SumChars.length; i++) {
                            batchChars[i+15] = cvc2SumChars[i];
                        }
                        
                        line = String.valueOf(batchChars);
                        //reset cvc2
                        cvc2Sum = 0;
                        detailCount = 0;
                        out.write(line); // ��Ϊ����
                        out.write("\r\n");
                    }else if (line.startsWith("D")) {
                        cvc2Sum += Integer.parseInt(line.substring(230, 233));
                        detailCount++;
                        out.write(line); // ��Ϊ����
                        out.write("\r\n");
                    }
                }
            } while (line != null && line.trim().length() != 0);
            
            out.flush(); // �ѻ���������ѹ���ļ�  
            System.out.println("Done!!");
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
            
        }
    }
    
    public static void main(String[] args) {
        process("D:\\working\\0918\\GO_new\\filtered_final_result.txt", "D:\\working\\0918\\GO_new\\filtered_formated_final_result.txt");
    }
    
    public static String rPad(String s, int len, char padChar) {
        /*  72 */     if (s == null)
        /*  73 */       s = "";
        /*  74 */     String retString = s.trim();
        /*  75 */     if (retString.length() > len) {
        /*  76 */       return retString.substring(0, len);
        /*     */     }
        /*  78 */     StringBuffer sb = new StringBuffer(retString);
        /*  79 */     for (int i = retString.length(); i < len; i++) {
        /*  80 */       sb.append(padChar);
        /*     */     }
        /*  82 */     return sb.toString();
        /*     */   }
    
    
    public static boolean isNumberic(String number) {
        String[] numberStrings = {"0","1","2","3","4","5","6","7","8","9"};
        for (int i = 0; i < numberStrings.length; i++) {
            if (numberStrings[i].equals(number.substring(0, 1))) {
                return true;
            }
        }
        return false;
    }
    
    public static final String padLeft(String str, char padding, int length)
    {
      if (str == null)
      {
        str = "";
      }
      if (str.length() >= length)
      {
        return str;
      }
      StringBuffer buf = new StringBuffer(length);
      for (int i = str.length(); i < length; i++)
      {
        buf.append(padding);
      }
      buf.append(str);
      return buf.toString();
    }
    

}
