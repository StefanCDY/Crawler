package com.zhihucrawler.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Stefan
 *
 */
public class EncryptUtil {
	public static final String MD5 = "MD5";
	public static final String SHA1 = "SHA-1";
	public static final String SHA256 = "SHA-256";
	
	/**
	 * @param str
	 * @return String 32位小写MD5
	 */
	public static String parseStrToMD5(String str) {
		return encrypt(str, MD5);
	}
	
	/**
	 * @param str
	 * @return String 32位大写MD5
	 */
	public static String parseStrToUpperMD5(String str) {
		return encrypt(str, MD5).toUpperCase();
	}
	
	/**
	 * @param str
	 * @return String 16位小写MD5
	 */
	public static String parseStrTo16MD5(String str) {
		return encrypt(str, MD5).substring(8, 24);
	}
	
	/**
	 * @param str
	 * @return String 16位大写MD5
	 */
	public static String parseStrToUpper16MD5(String str) {
		return encrypt(str, MD5).substring(8, 24).toUpperCase();
	}
	
	/**
	 * @param str
	 * @param encName 加密种类名
	 * @return String 对字符串加密
	 */
	public static String encrypt(String str, String encName) {
		String reStr = null;
		try {
			MessageDigest digest;
			digest = MessageDigest.getInstance(encName);
			byte[] bytes = digest.digest(str.getBytes());
			StringBuffer buffer = new StringBuffer();
			for (byte b : bytes) {
				int i = b & 0xff;
				//如果小于16，补位一个0
				if (i < 16) {
					buffer.append(0);
				}
				buffer.append(Integer.toHexString(i));
			}
			reStr = buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return reStr;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "总有一天,你会强大起来.";
		System.out.println(parseStrToMD5("5oC75pyJ5LiA5aSpLOS9oOS8muW8uuWkp+i1t+adpS4="));// 84d3f1916bce38f324e9772ce780f666
		System.out.println(encrypt(parseStrToMD5(str),SHA1));// 49dd4378fd33cdf7aa353687cee39739e1e5f1f8
		System.out.println(parseStrToUpperMD5(str));
		System.out.println(parseStrTo16MD5(str));
		System.out.println(parseStrToUpper16MD5(str));
	}

}
