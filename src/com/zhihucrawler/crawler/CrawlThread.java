/**
 * Stefan
 */
package com.zhihucrawler.crawler;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zhihucrawler.config.LinkQueue;
import com.zhihucrawler.dao.UserInfoDao;
import com.zhihucrawler.model.UserInfo;
import com.zhihucrawler.utils.EncryptUtil;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: ThreedPool.java
 * @Description: TODO
 * @Date 2016-3-29 下午4:05:26
 */
public class CrawlThread extends Thread {
	
	public CrawlThread(String name) {
		super(name);
	}

	@Override
	public void run() {
		CrawlBase base = new CrawlBase();
		base.login();
//		System.out.println(Thread.currentThread().getName() + " base " + base.hashCode());
//		System.out.println(Thread.currentThread().getName() + " httpclient " + base.getHttpClient().hashCode());
		while(!LinkQueue.unVisitedUrlIsEmpty() && LinkQueue.getVisitedUrlNum() < 100){
			String visitUrl = (String) LinkQueue.unVisitedUrlDeQueue();// 获取待访问的URL
			if (visitUrl == null || "".equals(visitUrl)) {
				continue;
			}
			CrawlLink crawlLink = new CrawlLink(visitUrl);
			Set<String> links = crawlLink.crawlLinks();// 抓取网页中的URL
//			System.out.println(Thread.currentThread().getName() + ":" + visitUrl + "==>抓取的URL数量:" + links.size());
			if (links != null && links.size() > 0) {
				for (String link : links) {// 新的未访问的URL入队列
					LinkQueue.addUnVisitedUrl(link);
				}				
			}
			LinkQueue.addVisitedUrl(visitUrl);// 添加已访问URL
		}
	}

}
