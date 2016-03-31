package com.trace.log;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtils {

    public static Connection getOracleConnection()
    {
        Connection con = null;// 创建一个数据库连接
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
            String url = "jdbc:oracle:" + "thin:@10.120.90.150:1521:PITDB506";// 127.0.0.1是本机地址，XE是精简版Oracle的默认数据库名
            String user = "L3_SLI01";// 用户名,系统默认的账户名
            String password = "6fcukeMM!@#";// 你安装时选设置的密码
            
            con = DriverManager.getConnection(url, user, password);// 获取连接
            System.out.println("connected...");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return con;
    }
    
    
}
