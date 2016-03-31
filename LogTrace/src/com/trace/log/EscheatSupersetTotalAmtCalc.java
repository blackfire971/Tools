package com.trace.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class EscheatSupersetTotalAmtCalc {
    
    
    
    public static void process(String incomingFile, String outgoingFile) {
        BufferedReader br = null;
        BufferedWriter out = null;
        try { // ��ֹ�ļ��������ȡʧ�ܣ���catch��׽���󲢴�ӡ��Ҳ����throw  
            
            /* ����TXT�ļ� */  
            String pathname = incomingFile; // ����·�������·�������ԣ������Ǿ���·����д���ļ�ʱ��ʾ���·��  
            File filename = new File(pathname); // Ҫ��ȡ����·����input��txt�ļ�  
            InputStreamReader reader = new InputStreamReader(  
                    new FileInputStream(filename)); // ����һ������������reader  
            br = new BufferedReader(reader); // ����һ�����������ļ�����ת�ɼ�����ܶ���������  
            String line = ""; 
            String[] tmpArr = null;
            int inNbrCount= 0;
            long totalAmt = 0;
            
            
            /* д��Txt�ļ� */  
            File writename = new File(outgoingFile); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�  
            writename.createNewFile(); // �������ļ�  
            out = new BufferedWriter(new FileWriter(writename));  
            Map<String, String> map = new HashMap<>();
            
            do {
                line = br.readLine(); 
                if (line == null || line.trim().length() == 0) {
                    break;
                }
                //split
                tmpArr = line.split("\\|");
                tmpArr[20] = tmpArr[20].trim();
                map.put(tmpArr[6], tmpArr[1]);
                
                totalAmt += (Double.parseDouble(tmpArr[20]));
                inNbrCount++;
                
            } while (line != null && line.trim().length() != 0);
            
            DecimalFormat df = new DecimalFormat("############.00");
            System.out.println("inNbrCount:::"+inNbrCount);
            System.out.println(df.format(totalAmt));
            System.out.println(map.size());
//            out.write(String.valueOf(totalAmt)); // ��Ϊ����
//            out.write("\r\n");
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
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
            
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
    
    
    public static void main(String[] args) {
        process("c:\\working\\0318\\XEROXVA_CMRA_20160316160407EscheatSuperSet", "c:\\working\\0318\\result.txt");
    }
}
