package com.trace.log;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtils {

    public static Connection getOracleConnection()
    {
        Connection con = null;// ����һ�����ݿ�����
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");// ����Oracle��������
            String url = "jdbc:oracle:" + "thin:@10.120.90.150:1521:PITDB506";// 127.0.0.1�Ǳ�����ַ��XE�Ǿ����Oracle��Ĭ�����ݿ���
            String user = "L3_SLI01";// �û���,ϵͳĬ�ϵ��˻���
            String password = "6fcukeMM!@#";// �㰲װʱѡ���õ�����
            
            con = DriverManager.getConnection(url, user, password);// ��ȡ����
            System.out.println("connected...");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return con;
    }
    
    
}
