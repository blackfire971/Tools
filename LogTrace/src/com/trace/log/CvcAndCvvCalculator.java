package com.trace.log;

import com.acs.eppic.security.Cryptor;
import com.acs.eppic.security.IEppicSecurityManager;
import com.acs.eppic.security.impl.EppicSecurityManagerImpl;
import com.tps.childsupport.CSUtil;
import com.tps.util.StringUtil;

public class CvcAndCvvCalculator {
	
	private static Cryptor cryptorA = null;
	  private static Cryptor cryptorB = null;
	  private static String CVC_KEY_A = "CVC_KEY_A";
	  private static String CVC_KEY_B = "CVC_KEY_B";
	  private static String YYMM = "yyMM";
	  
	  private static String MMYY = "MMyy";
	
	public static String CVCcalculateCVC(String PAN, String yymm, String extSvcCode)
	  {
	    // Check for input errors
	    if (PAN.length() < 16)
	      return null;
	    if (yymm.length() < 4)
	      return null;
	    if (extSvcCode.length() != 3)
	      return null;

	    PAN = PAN.trim();

	    // Step 1 & 2
	    int index = 0;
	    byte[] block = new byte[16];
	    index = CSUtil.catRightFourBits(block, index, PAN.getBytes(), PAN.length() - 16, PAN.length());
	    index = CSUtil.catRightFourBits(block, index, yymm.getBytes(), 0, 4);
	    index = CSUtil.catRightFourBits(block, index, extSvcCode.getBytes(), 0, 2);
	    block[index++] = (byte)((extSvcCode.getBytes()[2] & 0x0F) << 4);
	    for (int i = index; i < 16; i++) {
	      block[i] = 0;
	    }
	    byte[] block1 = new byte[8];
	    byte[] block2 = new byte[8];
	    System.arraycopy(block, 0, block1, 0, 8);
	    System.arraycopy(block, 8, block2, 0, 8);
	    // Step 3
	    IEppicSecurityManager esm = EppicSecurityManagerImpl.getSoleInstance();
	    cryptorA = esm.getKeyByName(CVC_KEY_A);
	    cryptorB = esm.getKeyByName(CVC_KEY_B);
	    byte[] encBlock1 = cryptorA.encrypt(block1);
	    // Step 4
	    byte[] xorBlock = CSUtil.xorBytes(encBlock1, block2);
	    byte[] encXorBlock = cryptorA.encrypt(xorBlock);
	    // Step 5
	    xorBlock = cryptorB.decrypt(encXorBlock);
	    // Step 6
	    encXorBlock = cryptorA.encrypt(xorBlock);
	    // Steps 7, 8, 9
	    String hex = StringUtil.bytes2HexString(encXorBlock);
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < hex.length(); i++) {
	      if (Character.isDigit(hex.charAt(i)))
	        sb.append(hex.charAt(i));
	    }
	    for (int i = 0; i < hex.length(); i++) {
	      if (Character.isLetter(hex.charAt(i)))
	        sb.append((char)(hex.charAt(i) - 'A' + '0'));
	    }
	    // Step 10
	    return sb.substring(0, 3);
	  }

	public static String CVVcalculate(String PAN, String YYMMorMMYY, String extSvcCode)
	  {
	    // Check for input errors
	    if (PAN.length() < 16)
	      return null;
	    if (YYMMorMMYY.length() < 4)
	      return null;
	    if (extSvcCode.length() != 3)
	      return null;

	    PAN = PAN.trim();

	    // Step 1 & 2
	    int index = 0;
	    byte[] block = new byte[16];
	    index = CSUtil.catRightFourBits(block, index, PAN.getBytes(), PAN.length() - 16, PAN.length());
	    index = CSUtil.catRightFourBits(block, index, YYMMorMMYY.getBytes(), 0, 4);
	    index = CSUtil.catRightFourBits(block, index, extSvcCode.getBytes(), 0, 2);
	    block[index++] = (byte)((extSvcCode.getBytes()[2] & 0x0F) << 4);
	    for (int i = index; i < 16; i++) {
	      block[i] = 0;
	    }
	    byte[] block1 = new byte[8];
	    byte[] block2 = new byte[8];
	    System.arraycopy(block, 0, block1, 0, 8);
	    System.arraycopy(block, 8, block2, 0, 8);
	    // Step 3
	    IEppicSecurityManager esm = EppicSecurityManagerImpl.getSoleInstance();
	    cryptorA = esm.getKeyByName(CVC_KEY_A);
	    cryptorB = esm.getKeyByName(CVC_KEY_B);
	    byte[] encBlock1 = cryptorA.encrypt(block1);
	    // Step 4
	    byte[] xorBlock = CSUtil.xorBytes(encBlock1, block2);
	    byte[] encXorBlock = cryptorA.encrypt(xorBlock);
	    // Step 5
	    xorBlock = cryptorB.decrypt(encXorBlock);
	    // Step 6
	    encXorBlock = cryptorA.encrypt(xorBlock);
	    // Steps 7, 8, 9
	    String hex = StringUtil.bytes2HexString(encXorBlock);
	    StringBuffer sb = new StringBuffer();
	    for (int i = 0; i < hex.length(); i++) {
	      if (Character.isDigit(hex.charAt(i)))
	        sb.append(hex.charAt(i));
	    }
	    for (int i = 0; i < hex.length(); i++) {
	      if (Character.isLetter(hex.charAt(i)))
	        sb.append((char)(hex.charAt(i) - 'A' + '0'));
	    }
	    // Step 10
	    return sb.substring(0, 3);
	  }
	
	public static void main(String[] args) {
//		String PAN = "5115590908553481";
		String PAN = "2AE1B9F96013067F";
	    String cardExpDate = "1810";
	    String extSvcCode = "101";
	    String CVC1 = CVCcalculateCVC(PAN, cardExpDate, extSvcCode);
	    System.out.println("CVC1=" + CVC1);
	    String CVC2 = CVCcalculateCVC(PAN, cardExpDate, "000");
	    System.out.println("CVC2=" + CVC2);
	}
	
	
}
