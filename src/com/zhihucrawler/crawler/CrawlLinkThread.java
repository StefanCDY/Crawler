package com.zhihucrawler.crawler;

import java.util.Set;

import com.zhihucrawler.config.LinkQueue;
import com.zhihucrawler.dao.UrlListDao;
import com.zhihucrawler.dao.UserInfoDao;
import com.zhihucrawler.model.UrlList;
import com.zhihucrawler.model.UserInfo;
import com.zhihucrawler.utils.EncryptUtil;

public class CrawlLinkThread extends Thread {

	public CrawlLinkThread(String name) {
		super(name);
	}

	@Override
	public void run() {
		while (!LinkQueue.unVisitedUrlIsEmpty() && LinkQueue.getVisitedUrlNum() < 1000) {
			String url = null;
			synchronized (this) {
				System.out.println("当前在执行的线程为：" + Thread.currentThread().getName());
				System.out.println("未访问的URL数量为：" + LinkQueue.getUnVisitedUrlNum());
				System.out.println("已访问的URL数量为：" + LinkQueue.getVisitedUrlNum());
				url = (String) LinkQueue.unVisitedUrlDeQueue();// 队头URL出队列
				LinkQueue.addVisitedUrl(url);// 该URL放入已访问的URL中
				System.out.println("当前所抓取的链接为:" + url);
			}
			if (url == null || "".equals(url)) {
				continue;
			}
			if (url.startsWith("https://www.zhihu.com/people/")) {
				synchronized (this) {					
					UserInfoDao userInfoDao = new UserInfoDao();
					UserInfo userInfo = new UserInfo();
					userInfo.setId(EncryptUtil.parseStrToMD5(url));
					userInfo.setUrl(url);
					userInfo.setState(0);
					userInfo.setCreatetime(System.currentTimeMillis());
					userInfo.setUpdatetime(System.currentTimeMillis());
					userInfoDao.save(userInfo);
				}
			}

			// 提取出网页中的URL链接
			CrawlLink crawlLink = new CrawlLink(url);
			Set<String> links = crawlLink.crawlAllLink();
			for (String link : links) {
				LinkQueue.addUnVisitedUrl(link);
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		String url = "https://www.zhihu.com/";
		CrawlStart start = new CrawlStart();
//		start.initCrawlerWithSeeds(new String[]{url});
//		CrawlLinkThread thread = new CrawlLinkThread("aa");
//		thread.start();
	}
}