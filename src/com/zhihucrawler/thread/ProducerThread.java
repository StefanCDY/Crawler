package com.zhihucrawler.thread;

import com.zhihucrawler.config.LinkQueue;
import com.zhihucrawler.crawler.HttpClient;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: P_Thread.java
 * @Description: TODO
 * @Date 2016-4-11 下午4:33:41
 */
public class ProducerThread extends Thread {
	
	private PageCodeList pageCodeList;
	
	public ProducerThread(PageCodeList pageCodeList) {
		super();
		this.pageCodeList = pageCodeList;
	}
	
	@Override
	public void run() {
		HttpClient httpClient = new HttpClient();
		httpClient.login();
//		System.out.println(Thread.currentThread().getName() + " is running.");
		
		while (!LinkQueue.unVisitedUrlIsEmpty() && LinkQueue.getVisitedUrlNum() < 1000) {
			System.out.println(Thread.currentThread().getName() + " is crawling.");
			
			String visitUrl = (String) LinkQueue.unVisitedUrlDeQueue();// 获取待访问的URL
			if (visitUrl == null || "".equals(visitUrl)) {
				continue;
			}
			String pageCode = httpClient.crawlPage(visitUrl);
			pageCodeList.push(visitUrl, pageCode);// 将抓取到的pageCode放入pageCodeList
			
			LinkQueue.addVisitedUrl(visitUrl);// 添加到已访问URL队列
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
