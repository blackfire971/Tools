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

public class GenAcctMaintFileByCsNbrForGOILConv {
    
    public static String getValueFormDB(Connection conn, String value) {
        String sqlString = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        String resultString = null;
        
        sqlString =
                "        select 'FDR' || '021' || '039' || '01' || " + 
                "                rpad(nvl(trim(c.case_nbr), ' '), 12, ' ') || " + 
                "                rpad(nvl(trim(p.ssn), ' '), 9, ' ') || " + 
                "                rpad(nvl(trim(to_char(p.dob, 'YYYYMMDD')), ' '), 8, ' ') || " + 
                "                nvl(trim(p.gender), 'U') || " + 
                "                rpad(nvl(trim(p.lastname), ' '), 25, ' ') || " + 
                "                rpad(nvl(trim(p.firstname), ' '), 25, ' ') || " + 
                "                rpad(nvl(trim(p.minitial), ' '), 1, ' ') || " + 
                "                rpad(nvl(trim(p.address_01), ' '), 30, ' ') || " + 
                "                rpad(nvl(trim(p.address_02), ' '), 30, ' ') || " + 
                "                rpad(nvl(trim(p.city), ' '), 30, ' ') || " + 
                "                rpad(nvl(trim(p.state), ' '), 2, ' ') || " + 
                "                rpad(nvl(trim(p.zip), ' '), 9, '0') || trim(cc.iso_code) || " + 
                "                rpad(nvl(trim(p.phone_number), ' '), 10, ' ') || " + 
                "                decode(p.special_need_id, '0', 'N', '1', 'Y') || " + 
                "                lpad(nvl(trim(p.language_id), ' '), 2, '0') || " + 
                "                rpad(' ', 50) || " + 
                "                '0' || rpad(' ', 198) || '0' as acct_maint " + 
                "          from edcgo.program_access pa, edcgo.person p, edcgo.cases c, edcgo.country_code cc " + 
                "         where pa.access_status_id = 1 " + 
                "           and pa.case_id = c.case_id " + 
                "           and p.country_code_id = cc.country_code_id " + 
                "           and p.person_id=pa.person_id" +
                "           and c.case_nbr in " + 
                "               ('"+rPad(value, 16, ' ') +"') " ;
            
        
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
                    out.write(tmpString); // ��Ϊ����
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
        process("D:\\working\\0810\\caseid.csv", "D:\\working\\0810\\result.csv");
        
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
