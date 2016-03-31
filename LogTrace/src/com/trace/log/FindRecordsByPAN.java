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
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw  
            
            /* 读入TXT文件 */  
            String pathname = incomingFile; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径  
            File filename = new File(pathname); // 要读取以上路径的input。txt文件  
            InputStreamReader reader = new InputStreamReader(  
                    new FileInputStream(filename)); // 建立一个输入流对象reader  
            br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
            
            String standardName = standardFile; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径  
            File standardFilename = new File(standardName); // 要读取以上路径的input。txt文件  
            InputStreamReader standardReader = new InputStreamReader(  
                    new FileInputStream(standardFilename)); // 建立一个输入流对象reader  
            br2 = new BufferedReader(standardReader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
            
            String line = ""; 
            ArrayList<String>tmpList = new ArrayList<String>();
            
            /* 写入Txt文件 */  
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
                            out.write(line); // 即为换行
                            out.write("\r\n");
                            break;
                        }
                    }
                }
                System.err.println(++count);
            } while (line != null && line.trim().length() != 0);
            
            out.flush(); // 把缓存区内容压入文件  
            
            
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
