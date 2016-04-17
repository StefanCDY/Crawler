package com.zhihucrawler.thread;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.zhihucrawler.config.LinkQueue;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: Run.java
 * @Description: TODO
 * @Date 2016-4-11 下午4:37:29
 */
public class Run {
	
	private int clawlThreadPoolNum = 1;
	private int parsethreadPoolNum = 1;
	private ExecutorService clawlExecutorService = null;
	private ExecutorService parseExecutorService = null;
//	private ThreadPoolExecutor clawlExecutorService = null;
//	private ThreadPoolExecutor parseExecutorService = null;
	private static PageCodeList pageCodeList = new PageCodeList();
	
	public Run(int threadPoolNum) {
		this.clawlThreadPoolNum = threadPoolNum > 1 ? threadPoolNum : 1;
		this.parsethreadPoolNum = this.clawlThreadPoolNum / 4;
	}
	
	private void init(String[] seeds) {
		// 初始化种子队列
		for (int i = 0; i < seeds.length; i++) {
			LinkQueue.addUnVisitedUrl(seeds[i]);
		}
		// 初始化程序池
//		this.clawlExecutorService = new ThreadPoolExecutor(clawlThreadPoolNum / 2, clawlThreadPoolNum, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
//		this.parseExecutorService = new ThreadPoolExecutor(parsethreadPoolNum / 2 , parsethreadPoolNum, 1000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
		this.clawlExecutorService = Executors.newFixedThreadPool(20);
		this.parseExecutorService = Executors.newFixedThreadPool(1);
	}
	
	public void crawling() {

		for (int i = 0; i < 1; i++) {
			parseExecutorService.execute(new CustomerThread(pageCodeList));
		}
		parseExecutorService.shutdown();
		
		for (int i = 0; i < 20; i++) {
			clawlExecutorService.execute(new ProducerThread(pageCodeList));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		clawlExecutorService.shutdown();// 不能再提交新任务,等待执行的任务不受影响
		
		try {
			boolean loop = true;
			do {// 等待所有任务完成
				loop = clawlExecutorService.awaitTermination(1, TimeUnit.SECONDS);// 阻塞,直到线程池里所有任务结束,每隔1秒监测一次
			} while (!loop);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
//		try {
//			boolean loop = true;
//			do {// 等待所有任务完成
//				loop = parseExecutorService.awaitTermination(1, TimeUnit.SECONDS);// 阻塞,直到线程池里所有任务结束,每隔1秒监测一次
//			} while (!loop);
//			System.out.println("parseExecutorService:" + loop);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
	}

	/** 
	 * @author Stefan
	 * @Title main
	 * @Description TODO
	 * @param args
	 * @Date 2016-4-11 下午4:37:29
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		String url1 = "https://www.zhihu.com/";
		String url2 = "https://www.zhihu.com/people/stefan-77";
		int threadPoolNum = 10;
		String[] seeds = {url1, url2};
//		String[] seeds = new String[100];/*{url1, url2}*/
//		for (int i = 0; i < 100; i++) {
//			seeds[i] = url1;
//			seeds[++i] = url2;
//		}
		
		Run run = new Run(threadPoolNum);
		// 初始化操作
		run.init(seeds);
//		crawlStart.initDatabase(seeds);
		
		System.out.println("抓取前。。。。。。");
		System.out.println("已访问：" + LinkQueue.getVisitedUrlNum());
		System.out.println("未访问：" + LinkQueue.getUnVisitedUrlNum());
		
		// 抓取......
		run.crawling();
//		crawlStart.crawlingByDatabase();
		
		System.out.println("抓取后。。。。。。");
		System.out.println("已访问：" + LinkQueue.getVisitedUrlNum());
		System.out.println("未访问：" + LinkQueue.getUnVisitedUrlNum());
		
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("程序总共耗时:" + time);
		
		System.out.println("pageCodeSize:" + pageCodeList.count());
	}

}
