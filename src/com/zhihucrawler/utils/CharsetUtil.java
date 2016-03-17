package com.zhihucrawler.utils;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

/**
 * @author Stefan
 *
 */
public class CharsetUtil {
	//编码探测器
	private static final CodepageDetectorProxy detector;

	//初始化编码探测器
	static {
		detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		detector.add(JChardetFacade.getInstance());
	}
	
	/**
	 * @param url
	 * @param defaultCharset
	 * @return String 检测URL下的编码方式，建议用于检测文件
	 */
	public static String getStreamCharset(URL url, String defaultCharset) {
		if (url == null) {
			return defaultCharset;
		}
		try {
			Charset charset = detector.detectCodepage(url);
			if (charset != null) {
				return charset.name();
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultCharset;
	}
	
	/**
	 * @param inputStream
	 * @param defaultCharset
	 * @return String 检测InputStream的编码方式
	 */
	public static String getStreamChaset(InputStream inputStream, String defaultCharset) {
		if (inputStream == null) {
			return defaultCharset;
		}
		try {
			int count = 200;
			count = inputStream.available();
			Charset charset = detector.detectCodepage(inputStream, count);
			if (charset != null) {
				return charset.name();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return defaultCharset;
	}

}
