package com.zhihucrawler.spider;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.zhihucrawler.crawler.HttpClient;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: TaskMaster.java
 * @Description: TODO
 * @Date 2016-4-15 下午6:06:05
 */
public class TaskMaster {
	
	private int threadCount;
	private CountDownLatch countDownLatch;
	private ExecutorService executorService;
	private HttpClient httpClient;
	
	public TaskMaster(int count) {
		this.threadCount = count > 1 ? count : 1;
	}
	
	private void init(String[] seeds) {
		// 初始化种子
		for (int i = 0; i < seeds.length; i++) {
			LinkQueue.addWaitUrl(seeds[i]);
		}
		// 初始化操作
		this.executorService = Executors.newFixedThreadPool(this.threadCount);
		this.countDownLatch = new CountDownLatch(this.threadCount);
		this.executorService = Executors.newFixedThreadPool(this.threadCount);
		this.httpClient = new HttpClient();
		this.httpClient.login();
	}
	
	public void crawling() {
//		System.out.println(Thread.currentThread().getName() + ":" + httpClient.hashCode());
		// 启动线程抓取
		for (int i = 0; i < this.threadCount; i++) {
			executorService.execute(new WorkThread(httpClient, countDownLatch));
		}
		
		executorService.shutdown();
		
		try {
			boolean loop = true;
			do {// 等待所有任务完成
				loop = executorService.awaitTermination(3, TimeUnit.SECONDS);// 阻塞,直到线程池里所有任务结束,每隔n秒监测一次
			} while (!loop);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * @author Stefan
	 * @Title main
	 * @Description TODO
	 * @param args
	 * @Date 2016-4-15 下午6:06:05
	 */
	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
		// https://www.zhihu.com/people/stefan-77
		String url = "https://www.zhihu.com/";
		int threadPoolNum = 1;
		String[] seeds = {url};
		
		TaskMaster taskMaster = new TaskMaster(threadPoolNum);
		
		// 初始化
		taskMaster.init(seeds);
		
		// 抓取......
		taskMaster.crawling();
		
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("程序总共耗时:" + time);

	}

}
