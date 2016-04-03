package com.zhihucrawler.crawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.zhihucrawler.config.LinkQueue;

/**
 * @author Stefan
 *
 */
public class CrawlStart {
	
	private int threadPoolNum = 1;
	private ExecutorService executorService = null;
	
	public CrawlStart() {
		
	}
	
	public CrawlStart(int threadPoolNum) {
		if (threadPoolNum < 1) {
			threadPoolNum = 1;
		}
		this.threadPoolNum = threadPoolNum;
	}
	
	private void init(String[] seeds) {
		// 初始化种子duil
		for (int i = 0; i < seeds.length; i++) {
			LinkQueue.addUnVisitedUrl(seeds[i]);
		}
		// 初始化程序池
		this.executorService = Executors.newFixedThreadPool(this.threadPoolNum);
	}
	
	public void crawling() {
		for (int i = 0; i < this.threadPoolNum; i++) {
			executorService.execute(new CrawlThread("Thread:" + i));
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
		
//		while(!LinkQueue.unVisitedUrlIsEmpty() && LinkQueue.getVisitedUrlNum() < 1){
//			
//			String visitUrl = (String) LinkQueue.unVisitedUrlDeQueue();// 获取待访问的URL
//			if (visitUrl == null) {
//				continue;
//			}
//			System.out.println(i + "==>" + visitUrl);
//			CrawlLink crawlLink = new CrawlLink(visitUrl);
//			Set<String> links = crawlLink.crawlAllLink();// 抓取网页中的URL
//			System.out.println(i + "==>" + visitUrl + "==>抓取的URL数量:" + links.size());
//			for (String link : links) {// 新的未访问的URL入队列
//				LinkQueue.addUnVisitedUrl(link);
//			}
//			System.out.println(i + "==>URL队列中未访问的数量:" + LinkQueue.getUnVisitedUrl().getSize());
//			LinkQueue.addVisitedUrl(visitUrl);// 添加已访问URL
//			i++;
//		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		// http://eol.zhbit.com/
		// https://www.zhihu.com/people/stefan-77
		// https://www.zhihu.com/people/Kirio
		// https://www.zhihu.com/terms#sec-licence-6  people/shi-yu-52-32
		CrawlBase base = new CrawlBase();
		base.login();
		
		String url = "https://www.zhihu.com/";
		int threadPoolNum = 20;
		String[] seeds = {url};
		
		CrawlStart crawlStart = new CrawlStart(threadPoolNum);
		// 初始化操作
		crawlStart.init(seeds);
		System.out.println("抓取前。。。。。。");
		System.out.println("已访问：" + LinkQueue.getVisitedUrlNum());
		System.out.println("未访问：" + LinkQueue.getUnVisitedUrlNum());
		
		// 抓取......
		crawlStart.crawling();
		System.out.println("抓取后。。。。。。");
		System.out.println("已访问：" + LinkQueue.getVisitedUrlNum());
		System.out.println("未访问：" + LinkQueue.getUnVisitedUrlNum());
		
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("程序总共耗时:" + time);
	}

}
