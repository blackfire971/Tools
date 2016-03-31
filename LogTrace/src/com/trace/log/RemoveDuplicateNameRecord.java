package com.trace.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RemoveDuplicateNameRecord {

    public static void process(String incomingFile, String outgoingFile) {
        BufferedReader br = null;
        BufferedReader br2 = null;
        BufferedWriter out = null;
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw  
            
            /* 读入TXT文件 */  
            String pathname = incomingFile; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径  
            File filename = new File(pathname); // 要读取以上路径的input。txt文件  
            InputStreamReader reader = new InputStreamReader(  
                    new FileInputStream(filename)); // 建立一个输入流对象reader  
            br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
            String line = ""; 
            
            /* 写入Txt文件 */  
            File writename = new File(outgoingFile); // 相对路径，如果没有则要建立一个新的output。txt文件  
            writename.createNewFile(); // 创建新文件  
            out = new BufferedWriter(new FileWriter(writename));  
            ArrayList<String> list = new ArrayList<String>();
            ArrayList<String> dupList = new ArrayList<String>();
            
            do {
                line = br.readLine(); 
                if (line != null && line.trim().length() >0) {
                    if (line.startsWith("D") && list.contains(line.substring(1, 27))) {
                        dupList.add(line.substring(1, 27));
                    } else if (line.startsWith("D")) {
                        list.add(line.substring(1, 27));
                    }
                }
            } while (line != null && line.trim().length() != 0);
            
            InputStreamReader reader2 = new InputStreamReader(  
                    new FileInputStream(filename)); // 建立一个输入流对象reader  
            br2 = new BufferedReader(reader2); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
            
            do {
                line = br2.readLine(); 
                if (line != null && line.trim().length() >0) {
                    if (line.startsWith("D") && dupList.contains(line.substring(1, 27))) {
                        dupList.remove(line.substring(1, 27));
                    }else {
                        out.write(line); // 即为换行
                        out.write("\r\n");
                    }
                }
            } while (line != null && line.trim().length() != 0);
            
                        
            out.flush(); // 把缓存区内容压入文件  
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
        process("D:\\working\\0918\\GA_new\\GA_output_0901_16.txt", "D:\\working\\0918\\GA_new\\remove_dup_GA_output_0901_16.txt");
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
