package com.trace.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class GetExpediteCardRecord_GO {

    public static void process(String incomingFile, String outgoingFile, String outgoingFile2) {
        BufferedReader br = null;
        BufferedWriter out = null;
        BufferedWriter out2 = null;
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
            File writename2 = new File(outgoingFile2); 
            writename.createNewFile(); // �������ļ�  
            writename2.createNewFile();
            out = new BufferedWriter(new FileWriter(writename));  
            out2 = new BufferedWriter(new FileWriter(writename2));  
            int count = 0;
            
            do {
                line = br.readLine(); 
                if (line != null && line.trim().length() >0) {
                    if (line.startsWith("T") || line.startsWith("H") ||line.startsWith("F")) {
                        out.write(line); // ��Ϊ����
                        out.write("\r\n");
                        out2.write(line);
                        out2.write("\r\n");
                    }
                    else if (line.substring(81, 82).equals("1")) {
                        out.write(line); // ��Ϊ����
                        out.write("\r\n");
                    }else if (line.substring(81, 82).equals("0")) {
                        out2.write(line); // ��Ϊ����
                        out2.write("\r\n");
                    }
                }
                System.err.println(++count);
            } while (line != null && line.trim().length() != 0);
            
            out.flush(); // �ѻ���������ѹ���ļ�  
            out2.flush();
            
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
                if (out2 != null) {
                    out2.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
            
        }
    }
    
    public static void main(String[] args) {
        process("D:\\working\\0918\\GO_new\\aug19\\output.txt", "D:\\working\\0918\\GO_new\\aug19\\go_exp.txt", "D:\\working\\0918\\GO_new\\aug19\\go_normal.txt");
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
