package com.trace.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FilterGOCardMailerFileWithCardStatus {

    public static void process(String incomingFile, String filteredFile, String outgoingFile) {
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
            
            String pathname2 = filteredFile; // ����·�������·�������ԣ������Ǿ���·����д���ļ�ʱ��ʾ���·��  
            File filename2= new File(pathname2); // Ҫ��ȡ����·����input��txt�ļ�  
            InputStreamReader reader2 = new InputStreamReader(  
                    new FileInputStream(filename2)); // ����һ������������reader  
            br2 = new BufferedReader(reader2); // ����һ�����������ļ�����ת�ɼ�����ܶ���������  
            
            String line = ""; 
            ArrayList<String> list = new ArrayList<String>();
            
            /* д��Txt�ļ� */  
            File writename = new File(outgoingFile); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�  
            writename.createNewFile(); // �������ļ�  
            out = new BufferedWriter(new FileWriter(writename));  
            
            do {
                line = br2.readLine(); 
                if (line != null && line.trim().length() >0) {
                    list.add(line.trim());
                }
            } while (line != null && line.trim().length() != 0);
            
            do {
                line = br.readLine(); 
                if (line != null && line.trim().length() >0) {
                    if (!line.startsWith("D")) {
                        out.write(line); // ��Ϊ����
                        out.write("\r\n");
                    }
                    else if (line.startsWith("D") && list.contains(line.substring(214, 230))) {
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
                if (br2 != null) {
                    br2.close();
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
        process("D:\\working\\0918\\GA_new\\remove_dup_exp_GA_output.txt", "D:\\working\\0918\\GA_new\\filtered_cards_ga.txt", "D:\\working\\0918\\GA_new\\filtered_final_result.txt");
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