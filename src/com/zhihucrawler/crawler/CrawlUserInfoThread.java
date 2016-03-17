package com.zhihucrawler.crawler;

import com.zhihucrawler.dao.UserInfoDao;
import com.zhihucrawler.model.UserInfo;

public class CrawlUserInfoThread extends Thread {
	private boolean flag = false;
	
	public CrawlUserInfoThread(String name) {
		super(name);
	}

	@Override
	public void run() {
		UserInfoDao userInfoDao = new UserInfoDao();
		while (flag) {
			String url = userInfoDao.getRandUserInfo(0);
			if (url == null || "".equals(url)) {
				continue;
			}
			synchronized (this) {
				System.out.println("当前在执行的线程为：" + Thread.currentThread().getName());
				System.out.println("当前在解析的URL为：" + url);				
			}
			CrawlUserInfo crawlUserInfo = new CrawlUserInfo(url, null);
			UserInfo userInfo = crawlUserInfo.crawlUserInfo();
			if (userInfo == null) {
				continue;
			}
			userInfo.setState(1);
			userInfo.setUpdatetime(System.currentTimeMillis());
			userInfoDao.update(userInfo);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
