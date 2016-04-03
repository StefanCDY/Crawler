package com.zhihucrawler.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Stefan
 *
 */
public class RegexUtil {
	private static String rootUrlRegex = "(https://.*?/)";
	private static String currentUrlRegex = "(https://.*?/)";
	private static String chRegex = "([\u4e00-\u9fa5]+)";

	/**
	 * @param dealStr
	 * @param regexStr
	 * @param splitStr
	 * @param n
	 * @return String 正则匹配结果，每条记录用splitStr分割
	 */
	public static String getString(String dealStr, String regexStr, String splitStr, int n) {
		String reStr = "";
		if (dealStr == null || regexStr == null || n < 1 || dealStr.isEmpty()) {
			return reStr;
		}
		splitStr = (splitStr == null) ? "" : splitStr;
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		StringBuffer stringBuffer = new StringBuffer();
		while (matcher.find()) {
			stringBuffer.append(matcher.group(n).trim());
			stringBuffer.append(splitStr);
		}
		reStr = stringBuffer.toString();
		if (splitStr != "" && reStr.endsWith(splitStr)) {
			reStr = reStr.substring(0, reStr.length() - splitStr.length());
		}
		return reStr;
	}
	
	/**
	 * @param dealStr
	 * @param regexStr
	 * @param n
	 * @return String 正则匹配结果，将所有匹配记录组装成字符串
	 */
	public static String getString(String dealStr, String regexStr, int n) {
		return getString(dealStr, regexStr, null, n);
	}
	
	/**
	 * @param dealStr
	 * @param regexStr
	 * @param n
	 * @return String 正则匹配第一条结果
	 */
	public static String getFirstString(String dealStr, String regexStr, int n) {
		if (dealStr == null || regexStr == null || n < 1) {
			return "";
		}
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while (matcher.find()) {
			return matcher.group(n).trim();
		}
		return "";
	}
	
	/**
	 * @param dealStr
	 * @param regexStr
	 * @param n
	 * @return List<String> 正则匹配结果，将匹配结果组装成数组
	 */
	public static List<String> getList(String dealStr, String regexStr, int n) {
		if (dealStr == null || regexStr == null || n < 1) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while (matcher.find()) {
			list.add(matcher.group(n).trim());
		}
		return list;
	}
	
	/**
	 * @param dealStr
	 * @param regexStr
	 * @param array
	 * @return List<String[]> 获取全部正则匹配结果
	 */
	public static List<String[]> getList(String dealStr, String regexStr, int[] array) {
		if (dealStr == null || regexStr == null || array == null) {
			return null;
		}
		for (int i = 0; i < array.length; i++) {
			if (array[i] < 1) {
				return null;
			}
		}
		List<String[]> list = new ArrayList<String[]>();
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while (matcher.find()) {
			String[] ss = new String[array.length];
			for (int i = 0; i < array.length; i++) {
				ss[i] = matcher.group(array[i]).trim();
			}
			list.add(ss);
		}
		return list;
	}
	
	/**
	 * @param dealStr
	 * @param regexStr
	 * @param array
	 * @return List<String> 获取全部正则匹配结果
	 */
	public static List<String> getListArray(String dealStr, String regexStr, int[] array) {
		if (dealStr == null || regexStr == null || array == null) {
			return null;
		}
		for (int i = 0; i < array.length; i++) {
			if (array[i] < 1) {
				return null;
			}
		}
		List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while (matcher.find()) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; i++) {
				sb.append(matcher.group(array[i]).trim());
			}
			list.add(sb.toString());
		}
		return list;
	}
	
	/**
	 * @param dealStr
	 * @param regexStr
	 * @param array
	 * @return String[] 获取第一个正则匹配结果
	 */
	public static String[] getFirstArray(String dealStr, String regexStr, int[] array) {
		if (dealStr == null || regexStr == null || array == null) {
			return null;
		}
		for (int i = 0; i < array.length; i++) {
			if (array[i] < 1) {
				return null;
			}
		}
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while (matcher.find()) {
			String[] string = new String[array.length];
			for (int i = 0; i < array.length; i++) {
				string[i] = matcher.group(array[i]).trim();
			}
			return string;
		}
		return null;
	}
	
	/**
	 * @param dealStr
	 * @param regexStr
	 * @param currentUrl
	 * @param n
	 * @return List<String> 获取和正则匹配的绝对链接地址
	 */
	public static List<String> getArrayList(String dealStr, String regexStr, String currentUrl, int n) {
		if (dealStr == null || regexStr == null || n < 1 || dealStr.isEmpty()) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		Pattern pattern = Pattern.compile(regexStr, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		Matcher matcher = pattern.matcher(dealStr);
		while (matcher.find()) {
			list.add(getHttpUrl(matcher.group(n).trim(), currentUrl));
		}
		return list;
	}
	
	/**
	 * @param url
	 * @param currentUrl 当前所处的url地址
	 * @return String 组装网址，网页的url
	 */
	public static String getHttpUrl(String url, String currentUrl) {
		try {
			//新增的replaceAll 转化有些地址接口中的转化地址，如： \/test\/1.html
			url = encodeUrlch(url).replaceAll("\\\\/", "/");
			if (url.indexOf("http") == 0) {
				return url;
			} else if (url.indexOf("/") == 0) {
				return getFirstString(currentUrl, rootUrlRegex, 1) + url.substring(1);
			} else if (url.indexOf("\\/") == 0) {
				return getFirstString(currentUrl, rootUrlRegex, 1) + url.substring(2);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return getFirstString(currentUrl, currentUrlRegex, 1) + url;
	}
	
	/**
	 * @param url
	 * @return String 将连接地址中的中文进行编码处理
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeUrlch(String url) throws UnsupportedEncodingException {
		while (true) {
			String s = getFirstString(url, chRegex, 1);
			if ("".equals(s)) {
				return url;
			}
			url = url.replaceAll(s, URLEncoder.encode(s, "UTF-8"));
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dealStr = "ab1234asdvahdckhka";
		String regexStr = "a(.*?)a";
		System.out.println(RegexUtil.getFirstString(dealStr, regexStr, 1));
	}

}
