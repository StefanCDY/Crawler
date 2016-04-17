package com.zhihucrawler.filter;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: LinkFilter.java
 * @Description: TODO
 * @Date 2016-4-15 下午3:32:08
 */
public class UserFilter {
	
	private String userFilter = "www.zhihu.com/people/";
	
	public boolean accept(String url) {
		if (url == null || "".equals(url)) {
			return false;
		}
		if(url.contains(userFilter)) {
			return true;
		}
		return false;
	}
	
}
