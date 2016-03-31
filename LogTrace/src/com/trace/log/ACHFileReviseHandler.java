package com.trace.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ACHFileReviseHandler {
    
	public static final String LINE_BREAK = "\r\n";
	
    public static void process(String incomingFile, String outgoingFile) {
        BufferedReader br = null;
        BufferedReader br2 = null;
        BufferedWriter out = null;
        
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw  
            
            /* 读入TXT文件 */  
            String pathname = incomingFile; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径  
            File filename = new File(pathname); // 要读取以上路径的input。txt文件  
            InputStreamReader reader = new InputStreamReader(  
                    new FileInputStream(filename)); // 建立一个输入流对象reader  
            InputStreamReader reader2 = new InputStreamReader(  
                    new FileInputStream(filename)); // 建立一个输入流对象reader  
            br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            br2 = new BufferedReader(reader2); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
            String line = ""; 
            
            /* 写入Txt文件 */  
            File writename = new File(outgoingFile); // 相对路径，如果没有则要建立一个新的output。txt文件  
            writename.createNewFile(); // 创建新文件  
            out = new BufferedWriter(new FileWriter(writename));  
            String rountNbr = null;
            ACHEntryHashCodeGen achForBatch = null;
            ACHEntryHashCodeGen achForFile = new ACHEntryHashCodeGen();;
            //detail count in one batch
            int detailCountInOneBatch = 0;
            long totalBatchCreditAmt = 0;
            long totalBatchDebitAmt = 0;
            long totalFileCreditAmt = 0;
            long totalFileDebitAmt = 0;
            Map<String, Map<String, String>> fileCache = new HashMap<>();
            Map<String, String> batchFileInfoCache = null;
            Map<String, String> FileTrailerInfoCache = new HashMap<>();
            int totalDetailCount = 0;
            //batch count in the file.
            int totalBatchCountInFile = 0;
            String creditOrDebit = null;
            int totalRecordCountInFile = 0;
            
            //read ACH file first time to collect the data
            do {
            	line = br.readLine();
                if (line == null || line.trim().length() == 0) {
                    break;
                }else{
                	//plus 1 when found record
                	totalRecordCountInFile++;
                	
                	//batch header
                	if (line.startsWith("5")) {
                    	//use batch header as a key
                    	//create new ach processor every batch
                    	achForBatch = new ACHEntryHashCodeGen();
                    	//plus 1 when found batch header
                    	totalBatchCountInFile++;
                    	
                    	batchFileInfoCache = new HashMap<>();
                    	//put batch sequence 
                    	batchFileInfoCache.put("batchCountInBatchHeader", lPad(String.valueOf(totalBatchCountInFile), 7, '0'));
                    	//batch header and trailer key have the same object
                    	fileCache.put(line, batchFileInfoCache);
    				}
                	//details
                    else if (line.startsWith("6")){
                    	//collect and calculate data from detail records

                    	totalDetailCount++;
                    	//credit/debit amount calculation
                        creditOrDebit =  line.substring(1,3);
                        if ("22".equals(creditOrDebit.trim()) || "32".equals(creditOrDebit.trim())) {
                        	//credit deposit
//                        	creditOrDebit = "C";
                        	totalBatchCreditAmt += Long.parseLong(line.substring(29,39));
                        	totalFileCreditAmt += Long.parseLong(line.substring(29,39));
    					} else if ("27".equals(creditOrDebit.trim()) || "37".equals(creditOrDebit.trim())) {
    						//debit deposit
//    						creditOrDebit = "D";
    						totalBatchDebitAmt += Long.parseLong(line.substring(29,39));
    						totalFileDebitAmt += Long.parseLong(line.substring(29,39));
    					} else if ("23".equals(creditOrDebit.trim()) || "28".equals(creditOrDebit.trim())
    							|| "33".equals(creditOrDebit.trim()) || "38".equals(creditOrDebit.trim())) {
    						//pre-note
//    						creditOrDebit = "P";
    						System.err.println("Pre-note record: "+line);
    					}
                        
                        //The number of entry detail and entry detail addenda records within the batch calculation
                        detailCountInOneBatch++;
                        
                        //hash entry number calculation
                        rountNbr = line.substring(3,11);
                        achForBatch.updateEntryHash(rountNbr);
                        //calculate the hash for the whole file
                        // and use it in file trailer
                        achForFile.updateEntryHash(rountNbr);
                    //a
                    } else if (line.startsWith("7")) {
                    	//The number of entry detail and entry detail addenda records within the batch calculation
                    	detailCountInOneBatch++;
                    	totalDetailCount++;
                    } else if (line.startsWith("8")) {
    	                //put hash code after go through all the batch records
                    	batchFileInfoCache.put("totalBatchEntryHash", lPad(String.valueOf(achForBatch.getTotalEntryHash()), 10, '0'));
                    	//reset ach
                    	achForBatch = null;
                    	//put batch detail record count
                    	//format the number
                    	batchFileInfoCache.put("detailCountInOneBatch", lPad(String.valueOf(detailCountInOneBatch), 6, '0'));
                    	//reset counter
                    	detailCountInOneBatch = 0;
                    	//put batch total debit amount
                    	batchFileInfoCache.put("totalBatchDebitAmt", lPad(String.valueOf(totalBatchDebitAmt), 12, '0'));
                    	//reset debit amount 
                    	totalBatchDebitAmt = 0;
                    	//put batch total credit amount
                    	batchFileInfoCache.put("totalBatchCreditAmt", lPad(String.valueOf(totalBatchCreditAmt), 12, '0'));
                    	//reset credit amount
                    	totalBatchCreditAmt = 0;
                    	//put batch count in batch's information
                    	batchFileInfoCache.put("totalBatchCountInFile", lPad(String.valueOf(totalBatchCountInFile), 7, '0'));
                    	// put batch information here, use batch header as a key
                    	fileCache.put(line, batchFileInfoCache);
                    }else if (line.startsWith("9")) {
                    	//put file trailer in file cache
                    	//put total batch count 
                    	FileTrailerInfoCache.put("totalBatchCountInFile", lPad(String.valueOf(totalBatchCountInFile), 6, '0'));
                    	//get blocked count and put in cache
                    	//ignore block count for file when the value is '000000' in original ACH file
                    	FileTrailerInfoCache.put("blockedCountInFile", 
                    			lPad("000000".equals(line.substring(7,13)) == true?"000000":String.valueOf(getBlockedCountByRecordsCount(totalRecordCountInFile)), 6, '0'));
                    	//put total detail record count 
                    	FileTrailerInfoCache.put("totalRecordCountInFile", 
                    			lPad(String.valueOf(totalDetailCount), 8, '0'));
                    	//put total entry hash code
                    	FileTrailerInfoCache.put("totalFileEntryHash", 
                    			lPad(String.valueOf(achForFile.getTotalEntryHash()), 10, '0'));
                    	//put batch total debit amount
                    	FileTrailerInfoCache.put("totalFileDebitAmt", lPad(String.valueOf(totalFileDebitAmt), 12, '0'));
                    	//put batch total credit amount
                    	FileTrailerInfoCache.put("totalFileCreditAmt", lPad(String.valueOf(totalFileCreditAmt), 12, '0'));
                    	
						fileCache.put(line, FileTrailerInfoCache);
					}
                }
            } while (line != null && line.trim().length() != 0);
            
            
            //======================================================================
            StringBuffer stringBuffer = null;
            //read ACH file second time to generate the new ACH file 
            do {
            	line = br2.readLine();
                if (line == null || line.trim().length() == 0) {
                    break;
                }else{
                	//batch header
                	if (line.startsWith("1")) {
                		out.write(line+LINE_BREAK);
					}
                	else if (line.startsWith("5")) {
                		stringBuffer = new StringBuffer();
                		stringBuffer.append(line.substring(0, 87));
                		//append batch id 
                		stringBuffer.append(fileCache.get(line).get("batchCountInBatchHeader"));
                		//write batch header
                		out.write(stringBuffer.toString()+LINE_BREAK);
                	}
                	//details
                    else if (line.startsWith("6")){
                    	//write detail records
                    	out.write(line+LINE_BREAK);
                    //a
                    } else if (line.startsWith("7")) {
                    	//write addenda records
                    	out.write(line+LINE_BREAK);
                    } else if (line.startsWith("8")) {
                    	stringBuffer = new StringBuffer();
                    	stringBuffer.append(line.substring(0, 4));
                    	//append record count in batch
                    	stringBuffer.append(fileCache.get(line).get("detailCountInOneBatch"));
                    	//append entry hash for batch
                    	stringBuffer.append(fileCache.get(line).get("totalBatchEntryHash"));
                    	//append debit amount in batch
                    	stringBuffer.append(fileCache.get(line).get("totalBatchDebitAmt"));
                    	//append credit amount in batch
                    	stringBuffer.append(fileCache.get(line).get("totalBatchCreditAmt"));
                    	//append with original file value
                    	stringBuffer.append(line.substring(44, 87));
                    	//append batch id 
                    	stringBuffer.append(fileCache.get(line).get("totalBatchCountInFile"));
                    	
    	                out.write(stringBuffer.toString()+LINE_BREAK);
                    }else if (line.startsWith("9")) {
                    	stringBuffer = new StringBuffer();
                    	stringBuffer.append(line.substring(0, 1));
                    	//append total batch count in the file 
                    	stringBuffer.append(fileCache.get(line).get("totalBatchCountInFile"));
                    	//append block count in the file
                    	stringBuffer.append(fileCache.get(line).get("blockedCountInFile"));
                    	//append total detail record count in the file
                    	stringBuffer.append(fileCache.get(line).get("totalRecordCountInFile"));
                    	//append total entry hash code for file
                    	stringBuffer.append(fileCache.get(line).get("totalFileEntryHash"));
                    	//append total file debit amount
                    	stringBuffer.append(fileCache.get(line).get("totalFileDebitAmt"));
                    	//append total file credit amount
                    	stringBuffer.append(fileCache.get(line).get("totalFileCreditAmt"));
                    	//append the rest of the fields in record
                    	stringBuffer.append(line.substring(55, 94));
                    	
                    	out.write(stringBuffer.toString()+LINE_BREAK);
                    	
					}
                }
            } while (line != null && line.trim().length() != 0);
            
            
            out.flush(); // 把缓存区内容压入文件  
            
            
        } catch (Exception e) {  
            e.printStackTrace();  
        }  finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (br2 != null) {
                    br2.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
            
        }
    }
    
    public static void main(String[] args) {
        process("C:\\working\\0315\\incoming.txt", "C:\\working\\0315\\result.txt");
        System.out.println("Done!!");
    }
    
    //block count calculator
    public static int getBlockedCountByRecordsCount(int totalRecordCountInFile){
    	int returnTotalBlockCount = totalRecordCountInFile;
    	long blockAdjustmentAmount = 10 - ( returnTotalBlockCount % 10);
		if (blockAdjustmentAmount == 10) {
			blockAdjustmentAmount = 0;
			returnTotalBlockCount = returnTotalBlockCount / 10;
		} else {
			returnTotalBlockCount = 1 + (returnTotalBlockCount / 10);
		}
		return returnTotalBlockCount;
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
    
    public static String lPad(String s, int len, char padChar) {
  	  if (s == null)
        s = "";
  		String retString = s.trim();
  		if (retString.length() > len)
  			return retString.substring(retString.length() - len, retString
  					.length());
  		else {
  			StringBuffer sb = new StringBuffer("");
  			for (int i = retString.length(); i < len; i++) {
  				sb.append(padChar);
  			}
  			return sb.toString() + retString;
  		}
  	}
    
    
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
