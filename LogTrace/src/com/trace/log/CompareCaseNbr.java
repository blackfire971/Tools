package com.trace.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class CompareCaseNbr {
    
    
    
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
            
            /* 写入Txt文件 */  
            File writename = new File(outgoingFile); // 相对路径，如果没有则要建立一个新的output。txt文件  
            writename.createNewFile(); // 创建新文件  
            out = new BufferedWriter(new FileWriter(writename));  
            int count = 0;
            String tmpString = "";
            String[] targetStrings = {"194701291",
                    "270502847",
                    "287900486",
                    "293908401",
                    "294848534",
                    "303041768",
                    "303047977",
                    "303748587",
                    "303941107",
                    "303949034",
                    "303961726",
                    "304043989",
                    "304044079",
                    "304087018",
                    "304087254",
                    "304130590",
                    "304846090",
                    "304944744",
                    "305025985",
                    "305043408",
                    "305044421",
                    "305084815",
                    "305645728",
                    "305903408",
                    "305943644",
                    "305947567",
                    "305980011",
                    "305988803",
                    "306114456",
                    "306547043",
                    "306764723",
                    "306943402",
                    "307067612",
                    "307067984",
                    "307087734",
                    "307089302",
                    "307829600",
                    "308061887",
                    "308663396",
                    "308908755",
                    "308984574",
                    "309040144",
                    "309042201",
                    "309063411",
                    "309089679",
                    "309118246",
                    "309728645",
                    "309886339",
                    "309981426",
                    "310067517",
                    "310704174",
                    "310749639",
                    "311045217",
                    "311045730",
                    "311060058",
                    "311064122",
                    "311085569",
                    "311584088",
                    "311600120",
                    "311704753",
                    "311801820",
                    "311849042",
                    "311867277",
                    "311968555",
                    "312046541",
                    "312047829",
                    "312064846",
                    "312083858",
                    "312088873",
                    "312880302",
                    "313024668",
                    "313040813",
                    "313689059",
                    "313982516",
                    "314085265",
                    "314085987",
                    "314907427",
                    "314948750",
                    "315062208",
                    "315665131",
                    "315680863",
                    "315767425",
                    "315942015",
                    "316065371",
                    "316067239",
                    "316068070",
                    "316083810",
                    "316747266",
                    "316922968",
                    "316964250",
                    "317067248",
                    "317083858",
                    "317707154",
                    "317761497",
                    "317866287",
                    "317887556",
                    "317905424",
                    "317966601",
                    "317982911",
                    "320788827",
                    "327649734",
                    "350583974",
                    "350689555",
                    "365887820",
                    "369923047",
                    "371969407",
                    "400396017",
                    "401136053",
                    "405338001",
                    "425394854",
                    "432592720",
                    "443943801",
                    "491929727",
                    "512885557"};
            
            
            do {
                line = br.readLine(); 
                if (line == null || line.trim().length() == 0) {
                    break;
                }
                //split
                tmpArr = line.split(",");
                tmpArr[0] = tmpArr[0].trim();
                
                System.err.println("case_nbr: " +tmpArr[0] + ", line: "+ ++count);
                
                for (int i = 0; i < targetStrings.length; i++) {
                    if (tmpArr[0].equals(targetStrings[i])) {
                        System.err.println("Dup happened: "+tmpArr[0]);
                        out.write( tmpArr[0]+","+ tmpString); // 即为换行
                        out.write("\n");
                    }
                    
                }
                
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
        process("D:\\working\\0625\\2015_Fee_Adj_list.csv", "D:\\working\\0626\\result_file_check.csv");
    }
}
