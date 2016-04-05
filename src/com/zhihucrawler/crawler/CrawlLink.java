package com.zhihucrawler.crawler;

import java.util.HashSet;
import java.util.Set;

import com.zhihucrawler.config.Const;
import com.zhihucrawler.parser.HtmlParserTool;
import com.zhihucrawler.utils.JsonUtil;

public class CrawlLink extends CrawlBase {
	private String url;
	private String charset;
	
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
//		this.crawlPageByGet(url);
		
		
//		ParserLink link = new ParserLink();
//		Set<String> links = link.parserUserLink(url, this.getPageSourceCode(), charset);
//		System.out.println("links:" + links.size());
//		for (int i = 0; i < links.size(); i++) {
//			System.out.println(links.toArray()[i]);
//		}
		return null;
	}
	
	/**
	 * @description 抓取所有链接
	 * @return
	 */
	public Set<String> crawlLinks() {
		if (charset == null || "".equals(charset)) {
			charset = Const.CHARSET;
		}
		String pageSource = this.crawlByGet(url);
		if (pageSource == null || "".equals(pageSource)) {
			return null;
		}
//		if (pageSource.contains("Stefan")) {
//			System.out.println("登录成功.");
//		}
//		System.out.println(pageSource);
		Set<String> links = new HashSet<String>();
		HtmlParserTool parserTool = new HtmlParserTool(charset);
		links = parserTool.extracLinks(url, pageSource);
		if (this.url.startsWith("https://www.zhihu.com/people/")) {// 抓取用户主页信息
//			UserInfo userInfo = ParserUser.parserUserInfo(url, this.getPageSourceCode(), charset);
//			ZhihuCrawlerDB crawlerDB = new ZhihuCrawlerDB();
//			crawlerDB.saveUserInfo(userInfo);
//			System.out.println(JsonUtil.parseJson(userInfo));
			System.out.println(this.url);
		}
		return links;
	}
	
}
