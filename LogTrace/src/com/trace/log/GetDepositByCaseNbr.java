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

public class GetDepositByCaseNbr {
    
    
    
    public static ArrayList<String> getValueFormDB(Connection conn, String value) {
        String sqlString = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<String> resultList = new ArrayList<String>();
        
        sqlString = "select 'Deposit Amount: ' || th1.amount || '  Entry Datetime: ' || th1.entry_datetime " + 
                "  from edctx.transaction_history th1, edctx.cases cs1 " + 
                " where cs1.case_id = th1.case_id " + 
                "   and th1.transaction_type_id = 1000 " + 
                "   and th1.entry_datetime >= to_date('20150615', 'yyyymmdd') " + 
                "   and case_nbr = '"+rPad(value, 16, ' ')+"' " + 
                " order by th1.entry_datetime desc";
        
        try {
            ps = conn.prepareStatement(sqlString);
            rs = ps.executeQuery();
            while(rs.next()) {
                resultList.add(rs.getString(1));
            }
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
        }
        return resultList;
    }
    
    public static void process(String incomingFile, String outgoingFile) {
        BufferedReader br = null;
        BufferedWriter out = null;
        Connection conn = DBUtils.getOracleConnection();
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw  
            
            /* 读入TXT文件 */  
            String pathname = incomingFile; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径  
            File filename = new File(pathname); // 要读取以上路径的input。txt文件  
            InputStreamReader reader = new InputStreamReader(  
                    new FileInputStream(filename)); // 建立一个输入流对象reader  
            br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
            String line = ""; 
            String[] tmpArr = null;
            ArrayList<String>tmpList = null;
            
            /* 写入Txt文件 */  
            File writename = new File(outgoingFile); // 相对路径，如果没有则要建立一个新的output。txt文件  
            writename.createNewFile(); // 创建新文件  
            out = new BufferedWriter(new FileWriter(writename));  
            int count = 0;
            
            do {
                line = br.readLine(); 
              //split
                tmpArr = line.split(",");
                tmpArr[0] =  tmpArr[0].trim();
                tmpList = getValueFormDB(conn, tmpArr[0]);
                
                if (tmpList.size() >0) {
                    for (String result : tmpList) {
                        out.write(tmpArr[0] + "," +result ); // 即为换行
                        out.write("\r\n");
                    }
                } else {
                    out.write(tmpArr[0] + "," +"None" ); // 即为换行
                    out.write("\r\n");
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
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
            
        }
    }
    
    public static void main(String[] args) {
        process("D:\\working\\0817\\deposit_case_nbr.txt", "D:\\working\\0817\\result_deposit_case_nbr.csv");
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
