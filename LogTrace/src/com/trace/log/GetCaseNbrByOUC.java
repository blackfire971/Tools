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
import java.text.DecimalFormat;


public class GetCaseNbrByOUC {
    
    public static String getValueFormDB(Connection conn, String value) {
        String sqlString = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultString = null;
        
        sqlString = "select trim(cs1.case_nbr) cs_nbr from edctx.cases cs1 where cs1.account_id=(select account_id from edctx.cases where case_nbr='"+rPad(value, 16, ' ')+"') and cs1.program_id<>77" ;
        
        try {
            System.out.println("SQL:  "+sqlString);
            ps = conn.prepareStatement(sqlString);
            rs = ps.executeQuery();
            if (rs.next()) {
                resultString = rs.getString(1);
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
        DecimalFormat df = new DecimalFormat("#.00");
        try { // ��ֹ�ļ��������ȡʧ�ܣ���catch��׽���󲢴�ӡ��Ҳ����throw  
            
            /* ����TXT�ļ� */  
            String pathname = incomingFile; // ����·�������·�������ԣ������Ǿ���·����д���ļ�ʱ��ʾ���·��  
            File filename = new File(pathname); // Ҫ��ȡ����·����input��txt�ļ�  
            InputStreamReader reader = new InputStreamReader(  
                    new FileInputStream(filename)); // ����һ������������reader  
            br = new BufferedReader(reader); // ����һ�����������ļ�����ת�ɼ�����ܶ���������  
            String line = ""; 
            String[] tmpArr = null;
            
            /* д��Txt�ļ� */  
            File writename = new File(outgoingFile); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�  
            writename.createNewFile(); // �������ļ�  
            out = new BufferedWriter(new FileWriter(writename));  
            int count = 0;
            String tmpString = "";
            
            do {
                line = br.readLine(); 
                //split
                tmpArr = line.split(",");
                tmpString = getValueFormDB(conn, tmpArr[0]);
                if (tmpString != null && tmpString.trim().length() != 0) {
                    out.write( tmpString+"," + "inactivity fee reversal"+ "," + df.format(Double.parseDouble(tmpArr[1]))+","+"CREDIT"); // ��Ϊ����
                    out.write("\n");
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
        process("D:\\working\\0707\\tx_inact_fee_credit_records.csv", "D:\\working\\0707\\result.csv");
        
//        double amt = 7;    
//
//        DecimalFormat df = new DecimalFormat("#.00");
//        System.out.println(df.format(amt));
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
    
}
