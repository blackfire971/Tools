package com.trace.log;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FindCardNotIssueFromMailerFile {

    public static void process(String incomingFile, String incomingFile2, String outgoingFile) {
        BufferedReader br = null;
        BufferedReader br2 = null;
        BufferedWriter out = null;
        try { // ��ֹ�ļ��������ȡʧ�ܣ���catch��׽���󲢴�ӡ��Ҳ����throw  
            
            /* ����TXT�ļ� */  
            String pathname = incomingFile; // ����·�������·�������ԣ������Ǿ���·����д���ļ�ʱ��ʾ���·��  
            File filename = new File(pathname); // Ҫ��ȡ����·����input��txt�ļ�  
            InputStreamReader reader = new InputStreamReader(  
                    new FileInputStream(filename)); // ����һ������������reader  
            br = new BufferedReader(reader); // ����һ�����������ļ�����ת�ɼ�����ܶ���������  
            
            /* ����TXT�ļ� */  
            String pathname2 = incomingFile2; // ����·�������·�������ԣ������Ǿ���·����д���ļ�ʱ��ʾ���·��  
            File filename2 = new File(pathname2); // Ҫ��ȡ����·����input��txt�ļ�  
            InputStreamReader reader2 = new InputStreamReader(  
                    new FileInputStream(filename2)); // ����һ������������reader  
            br2 = new BufferedReader(reader2); // ����һ�����������ļ�����ת�ɼ�����ܶ���������  
            
            String line = ""; 
            
            /* д��Txt�ļ� */  
            File writename = new File(outgoingFile); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�  
            writename.createNewFile(); // �������ļ�  
            out = new BufferedWriter(new FileWriter(writename));  
            int count = 0;
            
            ArrayList<String> panList = new ArrayList<String>();
            
            
            
            do {
                line = br.readLine(); 
                if (line != null && line.trim().length() >0) {
                    if (line.startsWith("D")) {
                        panList.add( line.substring(214, 230));
                    }
                }
            } while (line != null && line.trim().length() != 0);
            
            do {
                line = br2.readLine(); 
                if (line != null && line.trim().length() >0) {
                    if (line.startsWith("D")) {
                        if (panList.contains(line.substring(214, 231))) {
                            out.write(line);
                            out.write("\r\n");
                            panList.remove(line.substring(214, 231));
                        }
                    }
                }
            } while (line != null && line.trim().length() != 0);
            
            
            out.flush(); // �ѻ���������ѹ���ļ�  
            
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
                if (br2 != null) {
                    br2.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
            
        }
    }
    
    public static void main(String[] args) {
        process("D:\\\\working\\\\0925\\\\mailer\\\\GO20150912003000571PCARDMAILER", "D:\\\\working\\\\0925\\\\mailer\\\\output.txt", "D:\\\\working\\\\0925\\\\mailer\\\\result.txt");
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
