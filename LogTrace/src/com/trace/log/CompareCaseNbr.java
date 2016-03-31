package com.trace.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class CompareCaseNbr {
    
    
    
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
            
            /* д��Txt�ļ� */  
            File writename = new File(outgoingFile); // ���·�������û����Ҫ����һ���µ�output��txt�ļ�  
            writename.createNewFile(); // �������ļ�  
            out = new BufferedWriter(new FileWriter(writename));  
            int count = 0;
            String tmpString = "";
            String[] targetStrings = {"194701291",
                    "270502847",
                    "287900486",
                    "293908401",
                    "294848534",
                    "303041768",
                    "303047977",
                    "303748587",
                    "303941107",
                    "303949034",
                    "303961726",
                    "304043989",
                    "304044079",
                    "304087018",
                    "304087254",
                    "304130590",
                    "304846090",
                    "304944744",
                    "305025985",
                    "305043408",
                    "305044421",
                    "305084815",
                    "305645728",
                    "305903408",
                    "305943644",
                    "305947567",
                    "305980011",
                    "305988803",
                    "306114456",
                    "306547043",
                    "306764723",
                    "306943402",
                    "307067612",
                    "307067984",
                    "307087734",
                    "307089302",
                    "307829600",
                    "308061887",
                    "308663396",
                    "308908755",
                    "308984574",
                    "309040144",
                    "309042201",
                    "309063411",
                    "309089679",
                    "309118246",
                    "309728645",
                    "309886339",
                    "309981426",
                    "310067517",
                    "310704174",
                    "310749639",
                    "311045217",
                    "311045730",
                    "311060058",
                    "311064122",
                    "311085569",
                    "311584088",
                    "311600120",
                    "311704753",
                    "311801820",
                    "311849042",
                    "311867277",
                    "311968555",
                    "312046541",
                    "312047829",
                    "312064846",
                    "312083858",
                    "312088873",
                    "312880302",
                    "313024668",
                    "313040813",
                    "313689059",
                    "313982516",
                    "314085265",
                    "314085987",
                    "314907427",
                    "314948750",
                    "315062208",
                    "315665131",
                    "315680863",
                    "315767425",
                    "315942015",
                    "316065371",
                    "316067239",
                    "316068070",
                    "316083810",
                    "316747266",
                    "316922968",
                    "316964250",
                    "317067248",
                    "317083858",
                    "317707154",
                    "317761497",
                    "317866287",
                    "317887556",
                    "317905424",
                    "317966601",
                    "317982911",
                    "320788827",
                    "327649734",
                    "350583974",
                    "350689555",
                    "365887820",
                    "369923047",
                    "371969407",
                    "400396017",
                    "401136053",
                    "405338001",
                    "425394854",
                    "432592720",
                    "443943801",
                    "491929727",
                    "512885557"};
            
            
            do {
                line = br.readLine(); 
                if (line == null || line.trim().length() == 0) {
                    break;
                }
                //split
                tmpArr = line.split(",");
                tmpArr[0] = tmpArr[0].trim();
                
                System.err.println("case_nbr: " +tmpArr[0] + ", line: "+ ++count);
                
                for (int i = 0; i < targetStrings.length; i++) {
                    if (tmpArr[0].equals(targetStrings[i])) {
                        System.err.println("Dup happened: "+tmpArr[0]);
                        out.write( tmpArr[0]+","+ tmpString); // ��Ϊ����
                        out.write("\n");
                    }
                    
                }
                
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
            } catch (Exception e2) {
                // TODO: handle exception
                e2.printStackTrace();
            }
            
        }
    }
    
    
    public static void main(String[] args) {
        process("D:\\working\\0625\\2015_Fee_Adj_list.csv", "D:\\working\\0626\\result_file_check.csv");
    }
}
