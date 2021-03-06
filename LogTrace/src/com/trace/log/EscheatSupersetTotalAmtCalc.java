package com.trace.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class EscheatSupersetTotalAmtCalc {
    
    
    
    public static void process(String incomingFile, String outgoingFile) {
        BufferedReader br = null;
        BufferedWriter out = null;
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw  
            
            /* 读入TXT文件 */  
            String pathname = incomingFile; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径  
            File filename = new File(pathname); // 要读取以上路径的input。txt文件  
            InputStreamReader reader = new InputStreamReader(  
                    new FileInputStream(filename)); // 建立一个输入流对象reader  
            br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
            String line = ""; 
            String[] tmpArr = null;
            int inNbrCount= 0;
            long totalAmt = 0;
            
            
            /* 写入Txt文件 */  
            File writename = new File(outgoingFile); // 相对路径，如果没有则要建立一个新的output。txt文件  
            writename.createNewFile(); // 创建新文件  
            out = new BufferedWriter(new FileWriter(writename));  
            Map<String, String> map = new HashMap<>();
            
            do {
                line = br.readLine(); 
                if (line == null || line.trim().length() == 0) {
                    break;
                }
                //split
                tmpArr = line.split("\\|");
                tmpArr[20] = tmpArr[20].trim();
                map.put(tmpArr[6], tmpArr[1]);
                
                totalAmt += (Double.parseDouble(tmpArr[20]));
                inNbrCount++;
                
            } while (line != null && line.trim().length() != 0);
            
            DecimalFormat df = new DecimalFormat("############.00");
            System.out.println("inNbrCount:::"+inNbrCount);
            System.out.println(df.format(totalAmt));
            System.out.println(map.size());
//            out.write(String.valueOf(totalAmt)); // 即为换行
//            out.write("\r\n");
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
    
    public static boolean isNumberic(String number) {
        String[] numberStrings = {"0","1","2","3","4","5","6","7","8","9"};
        for (int i = 0; i < numberStrings.length; i++) {
            if (numberStrings[i].equals(number.substring(0, 1))) {
                return true;
            }
        }
        return false;
    }
    
    
    public static void main(String[] args) {
        process("c:\\working\\0318\\XEROXVA_CMRA_20160316160407EscheatSuperSet", "c:\\working\\0318\\result.txt");
    }
}
