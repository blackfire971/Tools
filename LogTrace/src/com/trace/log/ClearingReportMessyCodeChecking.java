package com.trace.log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClearingReportMessyCodeChecking {



                    public static void main(String[] args) throws IOException {
                        ClearingReportMessyCodeChecking generateTerminalIdFile = new ClearingReportMessyCodeChecking();
                                    String file250 = "D:\\workspace\\LogTrace\\src\\com\\trace\\log\\AL20150826001221PROD250";
                                    String resultFile = "D:\\workspace\\LogTrace\\src\\com\\trace\\log\\AL20150826001221PROD250_1";
                                    generateTerminalIdFile.generateFile(file250, resultFile);
                    }

                    public void generateFile(String file250, String resultFile)
                                                    throws IOException {

                                    BufferedReader reader = new BufferedReader(new FileReader(file250));
                                    PrintWriter pw = new PrintWriter(resultFile);

                                    String line;
                                    int i =0;
                                    while ((line = reader.readLine()) != null) {
                                                    int length = line.length();
                                                    i++;                                        
                                                    if (length !=250) {
                                                                    System.out.println(line);
                                                                    System.out.println(line);
                                                    }
                                                     pw.println(line);
                                    }
                                    reader.close();
                                    pw.close();
                    }           

}
