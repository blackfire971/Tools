package com.trace.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.tps.eppic.callcenter.stringDHelper;

public class FindRecordsByPAN {

    public static void process(String incomingFile, String standardFile, String outgoingFile2) {
        BufferedReader br = null;
        BufferedReader br2= null;
        BufferedWriter out = null;
        try { // ��ֹ�ļ��������ȡʧ�ܣ���catch��׽���󲢴�ӡ��Ҳ����throw  
            
            /* ����TXT�ļ� */  
            String pathname = incomingFile; // ����·�������·�������ԣ������Ǿ���·����д���ļ�ʱ��ʾ���·��  
            File filename = new File(pathname); // Ҫ��ȡ����·����input��txt�ļ�  
            InputStreamReader reader = new InputStreamReader(  
                    new FileInputStream(filename)); // ����һ������������reader  
            br = new BufferedReader(reader); // ����һ�����������ļ�����ת�ɼ�����ܶ���������  
            
            String standardName = standardFile; // ����·�������·�������ԣ������Ǿ���·����д���ļ�ʱ��ʾ���·��  
            File standardFilename = new File(standardName); // Ҫ��ȡ����·����input��txt�ļ�  
            InputStreamReader standardReader = new InputStreamReader(  
                    new FileInputStream(standardFilename)); // ����һ������������reader  
            br2 = new BufferedReader(standardReader); // ����һ�����������ļ�����ת�ɼ�����ܶ���������  
            
            String line = ""; 
            ArrayList<String>tmpList = new ArrayList<String>();
            
            /* д��Txt�ļ� */  
            File writename2 = new File(outgoingFile2); 
            writename2.createNewFile();
            out = new BufferedWriter(new FileWriter(writename2));  
            int count = 0;
            
            do {
                String tmpLine = br2.readLine();
                if (tmpLine != null && tmpLine.trim().length() >0) {
                    tmpList.add(tmpLine.trim());
                }
            } while (line != null && line.trim().length() != 0);
            
            
            do {
                line = br.readLine(); 
                if (line != null && line.trim().length() >0) {
                    for (String string : tmpList) {
                        if (line.contains(string)) {
                            out.write(line); // ��Ϊ����
                            out.write("\r\n");
                            break;
                        }
                    }
                }
                System.err.println(++count);
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
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
            
        }
    }
    
    public static void main(String[] args) {
        process("D:\\working\\0917\\GO20150917122700001PCARDMAILER", "D:\\working\\0917\\go_exp.txt", "D:\\working\\0917\\go_normal.txt");
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
