package com.zhihucrawler.crawler;

import java.util.Set;

import com.zhihucrawler.config.LinkQueue;

/**
 * @author Stefan
 *
 */
public class CrawlStart {
	
	private static boolean booleanCrawlLink = false;
	private static boolean booleanCrawlUserInfo = false;
	
	private static int crawlLinkThreadNum = 1;
	private static int crawlUserInfoThreadNum = 10;
	
	/**
	 * @description 初始化URL队列
	 * @param seeds
	 */
	private void initCrawlerWithSeeds(String[] seeds) {
		for (int i = 0; i < seeds.length; i++) {
			LinkQueue.addUnVisitedUrl(seeds[i]);
		}
	}
	
	/**
	 * @Description: 启动简介页采集线程
	 */
	public void startCrawlLink() {
		if (booleanCrawlLink) {
			return;
		}
		booleanCrawlLink = true;
		for (int i = 0; i < crawlLinkThreadNum; i++) {
			CrawlLinkThread thread = new CrawlLinkThread("LinkThread " + i);
			thread.start();
		}
	}
	
	/**
	 * @Description: 启动用户信息采集线程
	 */
	public void startCrawlUserinfo() {
		if (booleanCrawlUserInfo) {
			return;
		}
		booleanCrawlUserInfo = true;
		for (int i = 0; i < crawlUserInfoThreadNum; i++) {
			CrawlUserInfoThread thread = new CrawlUserInfoThread("UserInfoThread " + i);
			thread.start();
		}
	}
	
	public void crawling(String[] seeds){
		int i = 1;
		initCrawlerWithSeeds(seeds);// 初始化URL队列
		
		while(!LinkQueue.unVisitedUrlIsEmpty() && LinkQueue.getVisitedUrlNum() < 100){
			
			String visitUrl = (String) LinkQueue.unVisitedUrlDeQueue();// 获取待访问的URL
			if (visitUrl == null) {
				continue;
			}
			System.out.println(i + "==>" + visitUrl);
			CrawlLink crawlLink = new CrawlLink(visitUrl);
			Set<String> links = crawlLink.crawlAllLink();// 抓取网页中的URL
			System.out.println(i + "==>" + visitUrl + "==>抓取的URL数量:" + links.size());
			for (String link : links) {// 新的未访问的URL入队列
				LinkQueue.addUnVisitedUrl(link);
			}
//			System.out.println(i + "==>URL队列中未访问的数量:" + LinkQueue.getUnVisitedUrl().getSize());
			LinkQueue.addVisitedUrl(visitUrl);// 添加已访问URL
			i++;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long time1 = System.currentTimeMillis();
		// http://eol.zhbit.com/
		// https://www.zhihu.com/people/stefan-77
		// https://www.zhihu.com/people/Kirio
		// https://www.zhihu.com/terms#sec-licence-6
		String[] seeds = {"https://www.zhihu.com/"};
		CrawlStart start = new CrawlStart();
		start.crawling(seeds);
		
		long time2 = System.currentTimeMillis();
		long time = time2 - time1;
		System.out.println("程序总共耗时:" + time);
	}

}
