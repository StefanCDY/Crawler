package com.zhihucrawler.crawler;

import java.util.List;

import com.zhihucrawler.database.ZhihuCrawlerDB;
import com.zhihucrawler.model.CrawlUrl;
import com.zhihucrawler.model.UserInfo;
import com.zhihucrawler.parser.HtmlParserTool;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: ThreedPool.java
 * @Description: TODO
 * @Date 2016-3-29 下午4:05:26
 */
public class CrawlMySqlThread extends Thread {
	
	private final String charset = "UTF-8";// 网页解析默认编码方式
	private final String peopleUrl = "https://www.zhihu.com/people/";// 用户主页
	
	public CrawlMySqlThread() {
		super();
	}

	@Override
	public void run() {
		System.out.println(Thread.currentThread().getId() + ":" + Thread.currentThread().getName() + " is running.");
		HtmlParserTool parserTool = new HtmlParserTool(this.charset);
		ZhihuCrawlerDB crawlerDB = new ZhihuCrawlerDB();
		HttpClient httpClient = new HttpClient();
		httpClient.login();
		while (true) {
			// 获取URL对象
			CrawlUrl url = crawlerDB.getUrl();
			if (url == null) {// 数据库暂时没有可有数据
				Thread.currentThread().yield();
			}
			if (url.getUrl() == null || "".equals(url.getUrl())) {// 获取的url地址为空
				continue;
			}
			String pageCode = httpClient.crawlPage(url.getUrl());
			if (pageCode == null || "".equals(pageCode)) {
				continue;
			}
			System.out.println(Thread.currentThread().getId() + ":" + Thread.currentThread().getName() + " 正在抓取页面链接:" + url.getUrl());
			
			// 提取链接
			List<CrawlUrl> urls = parserTool.extracLinks(url, pageCode);
			if (urls != null && urls.size() > 0) {
				crawlerDB.saveUrl(urls);
			}
			crawlerDB.updateUrl(url.getId(), 1);// 更新已访问URL对象
			
			if (url.getUrl().startsWith(this.peopleUrl) && url.getUrl().lastIndexOf("/") == (this.peopleUrl.length() - 1)) {
				// 解析用户信息
				System.out.println(Thread.currentThread().getId() + ":" + Thread.currentThread().getName() + " 正在抓取用户主页:" + url.getUrl());
				UserInfo userInfo = parserTool.parserUserInfo(url.getUrl(), pageCode);
				crawlerDB.saveUserInfo(userInfo);
				System.out.println(userInfo.toString());
			}
			if (crawlerDB.getVisitedUrlNum() > 100) {
				break;
			}
		}
	}

}
