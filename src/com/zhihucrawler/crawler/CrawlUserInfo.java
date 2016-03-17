package com.zhihucrawler.crawler;

import com.zhihucrawler.config.Const;
import com.zhihucrawler.model.UserInfo;
import com.zhihucrawler.parser.ParserUser;

public class CrawlUserInfo extends CrawlBase {
	private String url;
	private String charset;
	
	public CrawlUserInfo() {
		super();
	}

	public CrawlUserInfo(String url) {
		this.url = url;
	}
	
	public CrawlUserInfo(String url, String charset) {
		this.url = url;
		this.charset = charset;
	}
	
	/**
	 * @description 抓取用户信息
	 * @return
	 */
	public UserInfo crawlUserInfo() {
		if (charset == null || "".equals(charset)) {
			charset = Const.CHARSET;
		}
		this.crawlPageByGet(url, charset);
		ParserUser parser = new ParserUser();
		UserInfo userInfo = parser.parserUserInfo(url, this.getPageSourceCode(), charset);
		return userInfo;
	}
	
}
