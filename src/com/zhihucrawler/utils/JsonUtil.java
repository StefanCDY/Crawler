/**
 * 
 */
package com.zhihucrawler.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Stefan
 *
 */
public class JsonUtil {
	private static final String noData = "{\"result\" : null}";//默认json字符串，null值或错误的情况下返回该值
	private static ObjectMapper mapper = new ObjectMapper();;
	
	static {
		mapper.setSerializationInclusion(Include.NON_NULL);//如果属性值为空，直接忽略
	}

	/**
	 * @description 将 object对象转化为json字符串
	 * @param object
	 * @return String
	 */
	public static String parseJson(Object object) {
		if (object == null) {
			return noData;
		}
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return noData;
	}
	
	/**
	 * @description 给定object对象生成对应json,可以指定一个json的root名
	 * @param object
	 * @param root
	 * @return String
	 */
	public static String parseJson(Object object, String root) {
		if (object == null) {
			return noData;
		}
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("{\"");
			builder.append(root);
			builder.append("\":");
			builder.append(mapper.writeValueAsString(object));
			builder.append("}");
			return builder.toString();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return noData;
	}
	
	/**
	 * @param json
	 * @return JsonNode 将json字符串转化为JsonNode
	 */
	public static JsonNode jsonToObject(String json) {
		try {
			return mapper.readTree(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param json
	 * @param var 若传入var为null，则默认变量名为datas
	 * @return String 将json字符串包装成jsonp，例如var data={}方式
	 */
	public static String wrapperJsonp(String json, String var) {
		if (var == null) {
			var = "data";
		}
		return new StringBuilder().append("var ").append(var).append(" = ").append(json).toString();
	}
	
}
