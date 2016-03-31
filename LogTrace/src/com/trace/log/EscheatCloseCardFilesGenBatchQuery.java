package com.trace.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EscheatCloseCardFilesGenBatchQuery {
	
	static List<String> cardIds = new ArrayList<>();
	static List<String> udpateCardStatusBakList = new ArrayList<>();
	static List<String> udpateCardStatusHistBakList = new ArrayList<>();
	static List<String> cardudpateCardAccessStatusBakIdsList = new ArrayList<>();
	static List<String> udpateCardStatusList = new ArrayList<>();
	static List<String> udpateCardStatusHistList = new ArrayList<>();
	static List<String> udpateCardAccessStatusList = new ArrayList<>();
	
    
    public static void getValueFormDB(Connection conn, String value, int opt) {
    	String getCards = "";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        String udpateCardStatusBak = "";
        String udpateCardStatusHistBak = null;
        String udpateCardAccessStatusBak = null;
        String udpateCardStatus = null;
        String udpateCardStatusHist = null;
        String udpateCardAccessStatus = null;
        
        
        getCards = " select max(card.card_id) card_id                                                        "+
        		"   from card, cases, program_access, cardholder                                             "+
        		"  where card.card_id = cardholder.card_id                                                   "+
        		"    and cardholder.cardholder_id = program_access.cardholder_id                             "+
        		"    and program_access.case_id = cases.case_id                                              "+
        		"    and card.next_card_id = -1                                                              "+
        		"    and cases.program_id<>77                                                                "+
        		"    and cases.account_id in ( "+value    +")"                                               +  
        		"    group by cases.account_id                                                               ";
        
        udpateCardStatusBak = " with escheated_account_temp as (                                                                "+
        		" select card_id from card where card_id in ( "+value    +"))" +
        		" select 'update card set card_status_id=' || card.card_status_id ||' where card_id=' || card.card_id "+
        		"   from card, escheated_account_temp                                                                 "+
        		"  where escheated_account_temp.card_id = card.card_id                                               ";
        //
        udpateCardStatusHistBak = " with escheated_account_temp as (                                                                "+
        		" select card_id from card where card_id in ("+value+") )"+", escheated_last_hist_tmp as (                                                                                                                              "+
        		" select max(card_status_history.trx_id) trx_id, card_status_history.card_id from card_status_history, escheated_account_temp                                  "+
        		" where card_status_history.card_id = escheated_account_temp.card_id group by card_status_history.card_id                                                      "+
        		" )                                                                                                                                                            "+
        		" select 'update card_status_history set card_status_id='|| card_status_history.card_status_id ||',card_subtype_id=' || card_status_history.card_subtype_id || "+
        		"        ' where trx_id=' || escheated_last_hist_tmp.trx_id ||                                                                                                 "+
        		"        ' and card_id=' || escheated_last_hist_tmp.card_id                                                                                                  "+
        		"   from escheated_last_hist_tmp, card_status_history                                                                                                          "+
        		"  where card_status_history.trx_id = escheated_last_hist_tmp.trx_id                                                                                           "+
        		"    and escheated_last_hist_tmp.card_id = card_status_history.card_id                                                                                         ";
        
        udpateCardAccessStatusBak = " with escheated_account_temp as (                                                                "+
        		" select card_id from card where card_id in ("+value+") )"+", escheated_person_temp as (                                                                                                          "+
        		"    select distinct cardholder.person_id from escheated_account_temp, cardholder where cardholder.card_id = escheated_account_temp.card_id "+
        		" ), pa_ch_backout_tmp as (                                                                                                                 "+
        		" select 'update cardholder set cardholder_status_id='|| cardholder.cardholder_status_id ||' where cardholder_id=' ||                       "+
        		"  cardholder.cardholder_id  sqrsql_record                                                                                                  "+
        		" from cardholder,escheated_person_temp where cardholder.person_id = escheated_person_temp.person_id                                        "+
        		" union                                                                                                                                     "+
        		" select distinct 'update program_access set access_status_id='|| program_access.access_status_id ||' where cardholder_id='                 "+
        		"  || cardholder.cardholder_id sqrsql_record                                                                                                "+
        		" from cardholder,escheated_person_temp, program_access where cardholder.person_id = escheated_person_temp.person_id                        "+
        		" and program_access.cardholder_id = cardholder.cardholder_id                                                                               "+
        		" ) select sqrsql_record from pa_ch_backout_tmp                                                                                             ";
        
        udpateCardStatus = " with escheated_card_tmp as (                                                                                   "+
        		" select card_id from card where card_id in ("+value+"))"+                                                                                               
        		"    select SQRSQL                                                                                               "+
        		"    from (                                                                                                      "+
        		"    select distinct ('update card set card_status_id=22 where card_id =' || escheated_card_tmp.card_id ) SQRSQL "+
        		" from escheated_card_tmp)                                                                                      ";

        udpateCardStatusHist = " with escheated_account_temp as (                                                                                            "+
        		" select card_id from card where card_id in ("+value+") )"+                                                                          
        		" , escheated_last_hist_tmp as (                                                                                             "+
        		" select max(card_status_history.trx_id) trx_id, card_status_history.card_id from card_status_history, escheated_account_temp "+
        		" where card_status_history.card_id = escheated_account_temp.card_id group by card_status_history.card_id                     "+
        		" )                                                                                                                           "+
        		" select 'update card_status_history set card_status_id=22, card_subtype_id=19 where trx_id=' ||                              "+
        		" escheated_last_hist_tmp.trx_id || ' and card_id=' || escheated_last_hist_tmp.card_id from escheated_last_hist_tmp           ";
        
        udpateCardAccessStatus = " with escheated_account_temp as (                                                                                           "+              
        		" select card_id from card where card_id in ("+value+"))"+                                                                                   
        		"    , escheated_person_temp as (                                                                                                          "+
        		"    select distinct cardholder.person_id from escheated_account_temp, cardholder where cardholder.card_id = escheated_account_temp.card_id "+
        		" ), pa_ch_tmp as (                                                                                                                         "+
        		" select 'update cardholder set cardholder_status_id=0 where cardholder_id=' || cardholder.cardholder_id sqrsql_record                      "+
        		" from cardholder,escheated_person_temp where cardholder.person_id = escheated_person_temp.person_id                                        "+
        		" union                                                                                                                                     "+
        		" select 'update cardholder set cardholder_status_id=1 where cardholder_id=' || max(cardholder.cardholder_id ) sqrsql_record                "+
        		" from cardholder,escheated_person_temp where cardholder.person_id = escheated_person_temp.person_id group by cardholder.person_id          "+
        		" union                                                                                                                                     "+
        		" select 'update program_access set access_status_id=0 where cardholder_id=' || cardholder.cardholder_id sqrsql_record                      "+
        		" from cardholder,escheated_person_temp where cardholder.person_id = escheated_person_temp.person_id                                        "+
        		" union                                                                                                                                     "+
        		" select 'update program_access set access_status_id=1 where cardholder_id=' || max(cardholder.cardholder_id ) sqrsql_record                "+
        		" from cardholder,escheated_person_temp where cardholder.person_id = escheated_person_temp.person_id group by cardholder.person_id          "+
        		" )                                                                                                                                         "+
        		" select sqrsql_record from pa_ch_tmp                                                                                                      ";
        
        
        
        
        try {
        	
        	if (opt ==0) {
        		ps = conn.prepareStatement(getCards);
        		rs = ps.executeQuery();
        		while(rs.next()){
        			cardIds.add(rs.getString(1).trim());
        		}
			} else if (opt ==1) {
				ps = conn.prepareStatement(udpateCardStatusBak);
        		rs = ps.executeQuery();
        		 while (rs.next()) {
        			 udpateCardStatusBakList.add(rs.getString(1));
                 }
			} else if (opt ==2) {
				ps = conn.prepareStatement(udpateCardStatusHistBak);
        		rs = ps.executeQuery();
        		while (rs.next()) {
        			 udpateCardStatusHistBakList.add(rs.getString(1));
                 }
			} else if (opt ==3) {
				ps = conn.prepareStatement(udpateCardAccessStatusBak);
        		rs = ps.executeQuery();
        		while (rs.next()) {
        			cardudpateCardAccessStatusBakIdsList.add(rs.getString(1));
                 }
			} else if (opt ==4) {
				ps = conn.prepareStatement(udpateCardStatus);
        		rs = ps.executeQuery();
        		while (rs.next()) {
        			 udpateCardStatusList.add(rs.getString(1));
                 }
			} else if (opt ==5) {
				ps = conn.prepareStatement(udpateCardStatusHist);
        		rs = ps.executeQuery();
        		while (rs.next()) {
        			 udpateCardStatusHistList.add(rs.getString(1));
                 }
			} else if (opt ==6) {
				ps = conn.prepareStatement(udpateCardAccessStatus);
        		rs = ps.executeQuery();
        		while (rs.next()) {
        			udpateCardAccessStatusList.add(rs.getString(1));
                 }
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
    }
    
	public static void process(String incomingFile, String outgoingFile1, String outgoingFile2, String outgoingFile3,
			String outgoingFile4, String outgoingFile5, String outgoingFile6) {
        BufferedReader br = null;
        BufferedWriter out1 = null;
        BufferedWriter out2 = null;
        BufferedWriter out3 = null;
        BufferedWriter out4 = null;
        BufferedWriter out5 = null;
        BufferedWriter out6 = null;
        Connection conn = DBUtils.getOracleConnection();
        PreparedStatement ps00 = null;
        
        try {
			ps00 = conn.prepareStatement("alter session set current_schema= edcva");
			ps00.execute();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}finally{
			if (ps00!=null) {
				try {
					ps00.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
        
        
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw  
            
            /* 读入TXT文件 */  
            String pathname = incomingFile; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径  
            File filename = new File(pathname); // 要读取以上路径的input。txt文件  
            InputStreamReader reader = new InputStreamReader(  
                    new FileInputStream(filename)); // 建立一个输入流对象reader  
            br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
            
            String[] tmpArr = null;
            
            /* 写入Txt文件 */  
            File writename1 = new File(outgoingFile1); // 相对路径，如果没有则要建立一个新的output。txt文件
            File writename2 = new File(outgoingFile2); 
            File writename3 = new File(outgoingFile3); 
            File writename4 = new File(outgoingFile4); 
            File writename5 = new File(outgoingFile5); 
            File writename6 = new File(outgoingFile6); 
            
            writename1.createNewFile(); // 创建新文件  
            writename2.createNewFile();
            writename3.createNewFile();
            writename4.createNewFile();
            writename5.createNewFile();
            writename6.createNewFile();
            
            out1 = new BufferedWriter(new FileWriter(writename1));  
            out2 = new BufferedWriter(new FileWriter(writename2));  
            out3 = new BufferedWriter(new FileWriter(writename3));  
            out4 = new BufferedWriter(new FileWriter(writename4));  
            out5 = new BufferedWriter(new FileWriter(writename5));  
            out6 = new BufferedWriter(new FileWriter(writename6));  
            
            
            List<String> accountIds = new ArrayList<>();
            int fileRecordCount = 0;
            //read id from file
            for (String line = br.readLine(); line != null && line.trim().length() != 0; line = br.readLine()) {
            	fileRecordCount++;
                tmpArr = line.split(",");
                tmpArr[7] =  tmpArr[7].trim();
                accountIds.add(tmpArr[7]);
			}
            System.err.println("File Size: "+fileRecordCount);
            
            //convert account id to card id 
            constructParamAndGetResult(conn, accountIds, 0);
            // generate file per card ids
            constructParamAndGetResult(conn, cardIds, 1);
            constructParamAndGetResult(conn, cardIds, 2);
            constructParamAndGetResult(conn, cardIds, 3);
            constructParamAndGetResult(conn, cardIds, 4);
            constructParamAndGetResult(conn, cardIds, 5);
            constructParamAndGetResult(conn, cardIds, 6);
            
            //write records to files
            writeFiles(out1, udpateCardStatusBakList);
            writeFiles(out2, udpateCardStatusHistBakList);
            writeFiles(out3, cardudpateCardAccessStatusBakIdsList);
            writeFiles(out4, udpateCardStatusList);
            writeFiles(out5, udpateCardStatusHistList);
            writeFiles(out6, udpateCardAccessStatusList);
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (out1 != null) {
                    out1.close();
                }
                if (out2 != null) {
                    out2.close();
                }
                if (out3 != null) {
                    out3.close();
                }
                if (out4 != null) {
                    out4.close();
                }
                if (out5 != null) {
                    out5.close();
                }
                if (out6 != null) {
                    out6.close();
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

	private static void writeFiles(BufferedWriter out, List<String> list) throws IOException {
		long start = System.currentTimeMillis();
		int counter = 0;
		String ext = "";
		//query and write to files
			
		for (String record : list) {
			counter++;
			if (counter%4000 == 0) {
				ext = ";----";
			}else{
				ext = ";;;;;";
			}
			out.write(record+ext);
			out.write("\r\n");
		}
		out.flush(); // 把缓存区内容压入文件
		
		long end = System.currentTimeMillis();
	    System.err.println("writing files:"+counter+", time used:"+(end-start)+"ms");
		  
	}

	private static void constructParamAndGetResult(Connection conn, List<String> list, int opt) {
		long start = System.currentTimeMillis();
		StringBuffer params = new StringBuffer();
		int count=0;
		if (list.size() < 1000) {
			//construct params
			for (String param : list) {
				params.append(param).append(",");
			}
			getValueFormDB(conn, params.substring(0, params.lastIndexOf(",")).toString(), opt);
		}else{
			for (String param : list) {
				count++;
				params.append(param).append(",");
				if (count%1000 == 0) {
					getValueFormDB(conn, params.substring(0, params.lastIndexOf(",")).toString(), opt);
					//clear params after query
					params = new StringBuffer();
				}else if (list.size() - count == 0) { //construct the rest of params
					getValueFormDB(conn, params.substring(0, params.lastIndexOf(",")).toString(), opt);
					//clear params after query
					params = new StringBuffer();
				}
			}
		}
		long end = System.currentTimeMillis();
		System.err.println("Query for opt:"+opt+", count:"+count+", time used:"+(end-start)+"ms");
	}
	
//	public static void main(String[] args) {
//		System.out.println(5287%1000);
//	}
    
    public static void main(String[] args) {
    	SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    	String timestamp = format.format(new Date());
        process("C:\\working\\0330\\Copy of XEROXVA_Status Closed.csv", 
        		"C:\\working\\0330\\"+args[0]+"_"+timestamp+"_close_card_BACKOUT_eppic_SQRSQL", 
        		"C:\\working\\0330\\"+args[0]+"_"+timestamp+"_update_card_status_hist_BACKOUT_eppic_SQRSQL", 
        		"C:\\working\\0330\\"+args[0]+"_"+timestamp+"_update_access_status_BACKOUT_eppic_SQRSQL", 
        		"C:\\working\\0330\\"+args[0]+"_"+timestamp+"_close_card_eppic_SQRSQL", 
        		"C:\\working\\0330\\"+args[0]+"_"+timestamp+"_update_card_status_hist_eppic_SQRSQL", 
        		"C:\\working\\0330\\"+args[0]+"_"+timestamp+"_update_access_status_eppic_SQRSQL");
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
