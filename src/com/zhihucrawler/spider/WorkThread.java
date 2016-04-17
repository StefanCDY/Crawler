package com.zhihucrawler.spider;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.http.client.methods.HttpGet;

import com.zhihucrawler.crawler.HttpClient;
import com.zhihucrawler.model.UserInfo;
import com.zhihucrawler.parser.HtmlParserTool;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: WorkThread.java
 * @Description: TODO
 * @Date 2016-4-15 下午6:08:44
 */
public class WorkThread implements Runnable {
	
	private final String charset = "UTF-8";// 网页解析默认编码方式
	private final String peopleUrl = "https://www.zhihu.com/people/";// 用户主页
    private final HttpClient httpClient;
    private CountDownLatch countDownLatch;

	public WorkThread(HttpClient httpClient, CountDownLatch countDownLatch) {
		this.httpClient = httpClient;
		this.countDownLatch = countDownLatch;
	}

	@Override
	public void run() {
//		System.out.println(Thread.currentThread().getName() + " is running.");		
		HtmlParserTool parserTool = new HtmlParserTool(charset);
		while (!LinkQueue.isEmpty() && LinkQueue.getVisitedNum() < 5) {
			System.out.println(Thread.currentThread().getName() + " is crawling.");
			// 获取待访问的URL
			String url = (String) LinkQueue.getWaitUrl();
			if (url == null || "".equals(url)) {
				continue;
			}
			
//			System.out.println(Thread.currentThread().getName() + " : " + url);
			
			// 抓取网页信息
			String pageCode = httpClient.crawlPage(url);
			if (pageCode == null || "".equals(pageCode)) {
				continue;
			}
			
			System.out.println(Thread.currentThread().getName() + " : " + pageCode);
			
			// 提取页面链接
			List<String> links = parserTool.extracLinks(url, pageCode);
			if (links != null && links.size() > 0) {
				for (String link : links) {// 新的未访问的URL入队列
					LinkQueue.addWaitUrl(link);
				}
			}
			System.out.println(Thread.currentThread().getName() + " : " + links.size());
			
			// 添加已访问URL
			LinkQueue.addVisitedUrl(url);
			
			// 解析用户信息
			if (url.startsWith(this.peopleUrl) && url.lastIndexOf("/") == (this.peopleUrl.length() - 1)) {
				UserInfo userInfo = parserTool.parserUserInfo(url, pageCode);
				System.out.println(userInfo.toString());
			}
			
		}
		
		this.countDownLatch.countDown();
		
	}

}
