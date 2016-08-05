package com.zhihucrawler.crawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.zhihucrawler.config.LinkQueue;
import com.zhihucrawler.database.ZhihuCrawlerDB;
import com.zhihucrawler.model.CrawlUrl;

/**
 * @author Stefan
 *
 */
public class CrawlStart {
	
	private int threadPoolNum = 1;
	private ExecutorService executorService = null;
	
	public CrawlStart(int threadPoolNum) {
		this.threadPoolNum = threadPoolNum > 1 ? threadPoolNum : 1;
	}
	
	private void init(String[] seeds) {
		// 初始化种子duil
		for (int i = 0; i < seeds.length; i++) {
			LinkQueue.addUnVisitedUrl(seeds[i]);
		}
		// 初始化程序池
		this.executorService = Executors.newFixedThreadPool(this.threadPoolNum);
	}
	
	private void initDatabase(String[] seeds) {
		ZhihuCrawlerDB crawlerDB = new ZhihuCrawlerDB();
		// 初始化数据库种子
		for (int i = 0; i < seeds.length; i++) {
			CrawlUrl url = new CrawlUrl();
			url.setUrl(seeds[i]);
			url.setDepth(0);
			url.setState(0);
//			url.setAddTime(System.currentTimeMillis());
//			url.setUpdateTime(System.currentTimeMillis());
			crawlerDB.saveUrl(url);
		}
		// 初始化程序池
		this.executorService = Executors.newFixedThreadPool(this.threadPoolNum);
	}
	
	public void crawlingByDatabase() {
		for (int i = 0; i < this.threadPoolNum; i++) {
			executorService.execute(new CrawlMySqlThread());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		executorService.shutdown();// 不能再提交新任务,等待执行的任务不受影响
		
		try {
			boolean loop = true;
			do {// 等待所有任务完成
				loop = executorService.awaitTermination(1, TimeUnit.SECONDS);// 阻塞,直到线程池里所有任务结束,每隔1秒监测一次
			} while (!loop);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void crawling() {
		for (int i = 0; i < this.threadPoolNum; i++) {
//			new CrawlThread().start();
			executorService.execute(new CrawlThread());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		executorService.shutdown();// 不能再提交新任务,等待执行的任务不受影响
		
		try {
			boolean loop = true;
			do {// 等待所有任务完成
				loop = executorService.awaitTermination(1, TimeUnit.SECONDS);// 阻塞,直到线程池里所有任务结束,每隔1秒监测一次
			} while (!loop);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		// https://www.zhihu.com/people/stefan-77
		String url = "https://www.zhihu.com/";
		int threadPoolNum = 5;
		String[] seeds = {url};
		
		CrawlStart crawlStart = new CrawlStart(threadPoolNum);
		// 初始化操作
		crawlStart.init(seeds);
//		crawlStart.initDatabase(seeds);
		
		System.out.println("抓取前。。。。。。");
		System.out.println("已访问：" + LinkQueue.getVisitedUrlNum());
		System.out.println("未访问：" + LinkQueue.getUnVisitedUrlNum());
		
		// 抓取......
		crawlStart.crawling();
//		crawlStart.crawlingByDatabase();
		
		System.out.println("抓取后。。。。。。");
		System.out.println("已访问：" + LinkQueue.getVisitedUrlNum());
		System.out.println("未访问：" + LinkQueue.getUnVisitedUrlNum());
		
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("程序总共耗时:" + time);
	}

}
