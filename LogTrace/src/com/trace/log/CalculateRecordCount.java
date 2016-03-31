package com.trace.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CalculateRecordCount {

    public static void process(String incomingFile) {
        BufferedReader br = null;
        BufferedWriter out = null;
        try { // ��ֹ�ļ��������ȡʧ�ܣ���catch��׽���󲢴�ӡ��Ҳ����throw  
            
            /* ����TXT�ļ� */  
            String pathname = incomingFile; // ����·�������·�������ԣ������Ǿ���·����д���ļ�ʱ��ʾ���·��  
            File filename = new File(pathname); // Ҫ��ȡ����·����input��txt�ļ�  
            String line = ""; 
            
            if (filename.isDirectory()) {
                File[] files = filename.listFiles();
                for (int i = 0; i < files.length; i++) {
                    InputStreamReader reader = new InputStreamReader(  
                            new FileInputStream(files[i])); // ����һ������������reader  
                    br = new BufferedReader(reader); // ����һ�����������ļ�����ת�ɼ�����ܶ��������� 
                    int count = 0;
                    do {
                        line = br.readLine(); 
                        if (line != null && line.trim().length() >0) {
                            if (line.startsWith("D")) {
                                count++;
                            }
                        }
                    } while (line != null && line.trim().length() != 0);
                    System.out.println(files[i].getPath() + ": "+count);
                    if (br != null) {
                        br.close();
                    }
                }
                System.out.println("Done");
            }
            
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
        process("D:\\working\\0917\\GO_new\\send10\\send\\");
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
    

}
