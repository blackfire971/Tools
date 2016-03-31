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

public class GetBalanceByAccountNbr {
    
    public static String getValueFormDB(Connection conn, String value) {
        String sqlString = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultString = null;
        
//        sqlString = "select sum(b1.balance) balance  "+ 
//                 " from prod_ga.benefit b1   "+
//                " where b1.case_id in   "+
//                   "    (select cs1.case_id   "+
//                     "     from prod_ga.cases cs1   "+
//                      "   where cs1.account_id in   "+
//                        "       (select account_id   "+
//                        "          from prod_ga.account_bucket "+
//                " where account_nbr  = '"+rPad(value, 15, ' ')+"' ))";
//        
        
        sqlString = "select sum(b1.balance) balance, cases.case_nbr  "+
                "  from benefit b1, cases    "+
               "  where cases.case_id = b1.case_id and b1.case_id in   "+
                  "     (select cs1.case_id   "+
                   "       from cases cs1   "+
                   "      where cs1.account_id in   "+
                   "            (select account_id   "+
                   "               from account_bucket " +
                   " where account_nbr  = '"+rPad(value, 15, ' ')+"' ))" +
                   " group by cases.case_nbr ";
        
        try {
            ps = conn.prepareStatement(sqlString);
            rs = ps.executeQuery();
            if (rs.next()) {
                resultString = rs.getString(2) + rs.getString(1);
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
        return resultString;
    }
    
    public static void process(String incomingFile, String outgoingFile) {
        BufferedReader br = null;
        BufferedWriter out = null;
        Connection conn = DBUtils.getOracleConnection();
        try { // ��ֹ�ļ��������ȡʧ�ܣ���catch��׽���󲢴�ӡ��Ҳ����throw  
            
            /* ����TXT�ļ� */  
            String pathname = incomingFile; // ����·�������·�������ԣ������Ǿ���·����д���ļ�ʱ��ʾ���·��  
            File filename = new File(pathname); // Ҫ��ȡ����·����input��txt�ļ�  
            InputStreamReader reader = new InputStreamReader(  
                    new FileInputStream(filename)); // ����һ������������reader  
            br = new BufferedReader(reader); // ����һ�����������ļ�����ת�ɼ�����ܶ���������  
            String line = ""; 
            String[] tmpArr = null;
            String tmpResultString = null;
            
            /* д��Txt�ļ� */  
            File writename = new File(outgoingFile); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�  
            writename.createNewFile(); // �������ļ�  
            out = new BufferedWriter(new FileWriter(writename));  
            int count = 0;
            
            do {
                line = br.readLine(); 
              //split
                tmpArr = line.split("\\|");
                tmpArr[1] =  tmpArr[1].trim();
                tmpResultString = getValueFormDB(conn, tmpArr[1].substring(0, 10));
                if (tmpResultString != null) {
                    out.write(tmpArr[1].substring(0, 10) + "," + tmpResultString); // ��Ϊ����
                    out.write("\r\n");
                }else {
                    tmpResultString = null;
                    continue;
                }
                System.err.println(++count);
                tmpResultString = null;
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
        process("c:\\working\\0303\\XEROXGA_CMRA_20151029202853922EscheatFiling", "c:\\working\\0303\\89007_balance_result.txt");
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
