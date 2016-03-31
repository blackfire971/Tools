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

import com.tps.eppic.callcenter.stringDHelper;

public class GenBatchAdj {
    
    public static String getValueFormDB(Connection conn, String value) {
        String sqlString = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultString = null;
        String sqlStringForGo = null;
        
        sqlString = " " + 
                "select card.pan " + 
                "  from prod_oh.cases, prod_oh.program_access, prod_oh.cardholder, prod_oh.card " + 
                " where case_nbr = '"+rPad(value, 16, ' ')+"' " + 
                "   and cases.case_id = program_access.case_id " + 
                "   and cardholder.cardholder_id = program_access.cardholder_id " +
                "   and cardholder.card_id = card.card_id " + 
                "   and program_access.access_status_id=1 " + 
                "   and cardholder.cardholder_status_id=1 " + 
                "   and rownum <2";
        //
        sqlStringForGo = " " + 
                "select distinct card.pan " + 
                "  from edcgo.cases, edcgo.program_access, edcgo.card, edcgo.person " + 
                " where case_nbr = '"+rPad(value, 16, ' ')+"' " + 
                "   and cases.case_id = program_access.case_id " + 
                "   and program_access.person_id = person.person_id " + 
                "   and card.person_id = person.person_id " + 
                "   and program_access.access_status_id=1 " + 
                "   and card.last_card_flag = 'Y' "  + 
                "   and rownum <2"
                 + " and cases.program_id = 41";
        
        try {
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
        try { // ��ֹ�ļ��������ȡʧ�ܣ���catch��׽���󲢴�ӡ��Ҳ����throw  
            
            /* ����TXT�ļ� */  
            String pathname = incomingFile; // ����·�������·�������ԣ������Ǿ���·����д���ļ�ʱ��ʾ���·��  
            File filename = new File(pathname); // Ҫ��ȡ����·����input��txt�ļ�  
            InputStreamReader reader = new InputStreamReader(  
                    new FileInputStream(filename)); // ����һ������������reader  
            br = new BufferedReader(reader); // ����һ�����������ļ�����ת�ɼ�����ܶ���������  
            String line = ""; 
            String[] tmpArr = null;
            String tmpPan = null;
            
            /* д��Txt�ļ� */  
            File writename = new File(outgoingFile); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�  
            writename.createNewFile(); // �������ļ�  
            out = new BufferedWriter(new FileWriter(writename));  
            int count = 0;
            
            do {
                line = br.readLine(); 
              //split
                tmpArr = line.split(",");
                tmpArr[0] =  tmpArr[0].trim();
                tmpPan = getValueFormDB(conn, tmpArr[0]);
                
                if (tmpPan != null) {
                    out.write( "-1" + "," + "03/14/2016" + "," + "Clearing report 03/12/2016 Merchant posted credit" +","+ tmpPan.trim() + "," + tmpArr[1].trim() + ","+ "CREDIT"); // ��Ϊ����
                    out.write("\r\n");
                } else {
                    continue;
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
        process("C:\\working\\0314\\incoming.txt", "C:\\working\\0314\\result.txt");
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
