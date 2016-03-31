package com.trace.log;

import java.sql.Connection;
import java.sql.SQLException;

public class ORA01013TestCase {

    public static void main(String[] args) {
        Connection conn = DBUtils.getOracleConnection();
        java.sql.Statement stat = null;
        try {
            conn.setAutoCommit(false);
            stat = conn.createStatement();
            stat.setQueryTimeout(10);  
            stat.execute("insert into user_status values (3,'PENDING', to_date('19000101','yyyymmdd'),   to_date('99991231','yyyymmdd'))");  
            stat.execute("update card_type set descr='xx' where card_type_id=3");  
            stat.execute("insert into user_status values (4,'REMOVED', to_date('19000101','yyyymmdd'),   to_date('99991231','yyyymmdd'))");
            conn.commit();  
            
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            try {  
                Thread.sleep(30000);  
            } catch (Exception f) {  
                f.printStackTrace();
            }  
        }finally {
            if (stat!=null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (conn!= null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
