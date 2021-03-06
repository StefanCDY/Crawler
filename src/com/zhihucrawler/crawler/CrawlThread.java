package com.zhihucrawler.crawler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.zhihucrawler.config.LinkQueue;
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
public class CrawlThread extends Thread {
	
	private final String charset = "UTF-8";// 网页解析默认编码方式
	private final String peopleUrl = "https://www.zhihu.com/people/";// 用户主页
	
	public CrawlThread() {
		super();
	}

	@Override
	public void run() {
//		System.out.println(Thread.currentThread().getId() + ":" + Thread.currentThread().getName() + " is running.");
		HtmlParserTool parserTool = new HtmlParserTool(this.charset);
		ZhihuCrawlerDB crawlerDB = new ZhihuCrawlerDB();
		HttpClient httpClient = new HttpClient();
		httpClient.login();
		
		while (!LinkQueue.unVisitedUrlIsEmpty() && LinkQueue.getVisitedUrlNum() < 100) {
//			System.out.println(Thread.currentThread().getId() + " is crawling.");

			String visitUrl = (String) LinkQueue.unVisitedUrlDeQueue();// 获取待访问的URL
			if (visitUrl == null || "".equals(visitUrl)) {
				continue;
			}
			
			String pageCode = httpClient.crawlPage(visitUrl);
			if (pageCode == null || "".equals(pageCode)) {
				continue;
			}
//			System.out.println(Thread.currentThread().getId() + " 正在抓取页面链接:" + visitUrl);
			
			// 提取链接——List队列
			List<String> links = parserTool.extracLinks(visitUrl, pageCode);
			if (links != null && links.size() > 0) {
				for (String link : links) {// 新的未访问的URL入队列
					LinkQueue.addUnVisitedUrl(link);
				}
			}
			LinkQueue.addVisitedUrl(visitUrl);// 添加到已访问URL队列
			
			if (visitUrl.startsWith(this.peopleUrl) && visitUrl.lastIndexOf("/") == (this.peopleUrl.length() - 1)) {
				// 解析用户信息
//				System.out.println(Thread.currentThread().getId() + " is crawling userinfo:" + visitUrl);
				UserInfo userInfo = parserTool.parserUserInfo(visitUrl, pageCode);
				crawlerDB.saveUserInfo(userInfo);
				System.out.println(userInfo.toString());
			}
//			try {
//				Thread.currentThread().sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}

}
