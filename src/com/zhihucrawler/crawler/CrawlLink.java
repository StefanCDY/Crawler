package com.zhihucrawler.crawler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zhihucrawler.config.Const;
import com.zhihucrawler.model.UrlList;
import com.zhihucrawler.model.UserInfo;
import com.zhihucrawler.parser.ParserLink;
import com.zhihucrawler.parser.ParserUser;
import com.zhihucrawler.utils.JsonUtil;

public class CrawlLink extends CrawlBase {
	private String url;
	private String charset;
	
	public CrawlLink() {
		super();
	}

	public CrawlLink(String url) {
		this.url = url;
	}
	
	public CrawlLink(String url, String charset) {
		this.url = url;
		this.charset = charset;
	}
	
	/**
	 * @description 抓取用户链接
	 * @return
	 */
	public Set<String> crawlUserLink() {
		if (charset == null || "".equals(charset)) {
			charset = Const.CHARSET;
		}
		this.crawlPageByGet(url, charset);
		
		
		ParserLink link = new ParserLink();
		Set<String> links = link.parserUserLink(url, this.getPageSourceCode(), charset);
//		System.out.println("links:" + links.size());
//		for (int i = 0; i < links.size(); i++) {
//			System.out.println(links.toArray()[i]);
//		}
		return links;
	}
	
	/**
	 * @description 抓取所有链接
	 * @return
	 */
	public Set<String> crawlAllLink() {
		if (charset == null || "".equals(charset)) {
			charset = Const.CHARSET;
		}
		Set<String> links = new HashSet<String>();
		if (this.crawlPageByGet(url, charset)) {
//			System.out.println(this.getPageSourceCode());
			links = ParserLink.parserAllLink(url, this.getPageSourceCode(), charset);
			
			if (this.url.startsWith("https://www.zhihu.com/people/")) {// 抓取用户主页信息
//				System.out.println(url);
//				ParserUser parserUser = new ParserUser();
//				UserInfo userInfo = parserUser.parserUserInfo(url, this.getPageSourceCode(), charset);
//				System.out.println(JsonUtil.parseJson(userInfo));
			}
		}
		
		return links;
	}
	
}
