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

import com.tps.eppic.persistence.DMLCacheObject;
import com.tps.eppic.persistence.DictionaryDatasource;
import com.tps.eppic.persistence.StatementPacker;
import com.tps.eppic.persistence.StmtCompressorImpl;

public class TlogPlayBack {
	
	 public static void alterSession(Connection conn) {
	        String sqlString = "";
	        PreparedStatement ps = null;
	        ResultSet rs = null;
	        
	        sqlString = "alter session set current_schema= edcfl";
	       
	        
	        try {
	            ps = conn.prepareStatement(sqlString);
	            ps.execute();
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
            
//            alterSession(conn);
            
            /* д��Txt�ļ� */  
            File writename = new File(outgoingFile); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�  
            writename.createNewFile(); // �������ļ�  
            out = new BufferedWriter(new FileWriter(writename));  
            int count = 0;
            
            final Connection conn2=conn;
            
            StatementPacker stmtPacker=new StatementPacker(new DictionaryDatasource(){
				public Connection getConnection() throws SQLException{
					return conn2;
				}

				public boolean doClose() {
					return false;
				}
			});
            
            DMLCacheObject dml=null;
            String stmt= null;
            ArrayList<String> listDml = new ArrayList<String>();
            ArrayList<Object> listArgs = new ArrayList<Object>();
            String tmp = null;
			StmtCompressorImpl stmtCompressor=new StmtCompressorImpl(stmtPacker);
            
            do {
//                line = br.readLine(); 
//              //split
//                tmpArr = line.split(",");
//                stmt =  tmpArr[1].trim();
				stmt = "H4sIAAAAAAAAAO2ayZKjOBBAq7+kf2AOgO0Jc+gDi8DCCDcgxBJ9GIOrwGymhyqzfE//YH/BjJArJmpqmejTHAgOKWRLpCGfMsmU+fn57re7T/ju7sfdp6+sDViL37S377+96eM3Z317oe3l6Ms5f7wa/fnX51RS9tpw3utur9ie8+AUoutwohesH0jAWV+Jt/FM0Mse/3rMkQnwGlJoiHgE75WzpPp8dq+cd7xbpPtB6qEeDbHApecsaUwVjKZyUi0sDdboDZaStAbHX496WUQ+eYKaldH+2Ri62s4jxVGTEY1Ec3JZP6hoYxdQsNSwsws0WKomH0CkWCOoTUWq4aRrkGpTlVrkdj1SBmk3ShtzhDyVDmGPzcMed3WAffVJGfkuvSYoPSAMr2gE1wMuNgc3aaFysrArAkcRo2meMRQtVEH7rLM3mYANUqV3de7tL19U+7skwXoHmS26NF1tWwjvY1NRJDR9VtE/doNKekkl/jEM0ksmes2/bKZIHRV6P/Qcedsd3O1gUUEq4KmdUmo/y+QcYPL0t0lR7102r0fn7Yjc7Wa6p/fnFc/XeAn28BKZwJAdTdQxb+ww5ZyJ5MHmRIJLu8VAw+TVmAM0z3FL3/Z64hB02UPJin1tvYcPZO9Kf1J7pVFtXGN3WltFizCirBGPVLhGOJ2uvXYrLT8O8i5eOdzCfObMc2mcGDu6+BRXZQkBnyW1dQn9vl3Yz509ZP5uB0Yd+ZsmGWQvXkVZcp7iPxTswl6hEfGOF3ZOno4Hz1Cs3BYQMFSU272NM93Ok57d18RcSeix68yJEZbaX7PTxBIM5ogoy2Ky0/vrg9p+WlOTTmrPSfoDlrr/tunC/QPuwsQd+2V+1LUyVmQ/8mnMVxbu8+aOJma1V2lVFMCUCITGfDIscX723G95nL+hcb5LXZ0McaU9RTr5fWE/a/YbNErriTGpyvEkgNQRxI7mdtlxZTSJkD2F/pra9aRYhQEsugIsj+gIhGs0loqdh5yVy4o9Qg55YO0UcIUYMxbv2TqgtqLxGXSMrVpcUe5RkXrrA7a/Zq8updyedYIrE1x0SHlXZ7Pw/4g/6tFYcLdnvTYc/W1qV+QpDKKG+v64+P6s2XO0v2LP+xX5HgbOJXRl8xTI1P83T7Tvhv528f358ufRCJmPE58vEyFNXSErI/1xqe/mzV044PSW0/t9ea+zPG+AgL8mldZGAVp8fr7sV9Zos3wPV5rA9u6BkcWVkyW1t3CfLXd7sDBie3meoDUQPGbxrszis6wedY2jfs8d3SXmz5i/QPPzDYv5QjYcA5TagniJltpu5sxhx3xez5pYLzsITtmR5vlRABfu8+W+onU028fFARlO/mmK8+ToP2annUP7S5yfOXuW27s6ae4r0lKfv8Z1k8XLXu7s2Vs5usX7lVOeKi+d9vSSgJT3i8/PmfvaUovndzRoTl9pbuhvphyfj1ltrzVE174vvj/nNeB1Vp6ubnV9OdKaju3nTHV9vPxnP3fu69s7eeKF5nm7U+BwUWA80uNl8fn/h/3f1olGkBMsAAA=";
                dml=stmtCompressor.getStmtsFromSavedBytes(new com.tps.eppic.persistence.B64Packer().packStr(stmt.trim()));
//                for (String dmlString : dml.getDml()) {
//                	listDml.add(dmlString);
//				}
                
//                for (Object args : dml.getArgs()) {
//                	listArgs.add(args);
//				}
                
                if (stmt != null) {
//                	tmp = tmpArr[0]+",";
                	for (String string : dml.getDml()) {
                		tmp = tmp + string + "|";
					} 
//                	sb.append(",");
//                	for (Object object : listArgs) {
//                		sb.append(object).append("|");
//					}
                	out.write(tmp); 
                    out.write("\r\n");
                    tmp = null;
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
        process("c:\\working\\1210\\1.txt", "c:\\working\\1210\\2.txt");
    }
    
}
	
	
	   
