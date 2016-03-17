package com.zhihucrawler.database;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.zhihucrawler.crawler.CrawlLink;
import com.zhihucrawler.crawler.CrawlUserInfo;
import com.zhihucrawler.dao.UserInfoDao;
import com.zhihucrawler.model.UrlList;
import com.zhihucrawler.model.UserInfo;
import com.zhihucrawler.utils.JsonUtil;

public class ZhihuCrawlerDB {
	private static final String POOLNAME = "proxool.zhihucrawler";
	
	/**
	 * @description 保存用户信息
	 * @param userInfo
	 */
	public void saveUserInfo(UserInfo userInfo) {
		if (userInfo == null) {
			return;
		}
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			HashMap<Integer, Object> params = new HashMap<Integer, Object>();
			int i = 1;
			params.put(i++, userInfo.getUrl());
			params.put(i++, userInfo.getName());
			params.put(i++, userInfo.getGender());
			params.put(i++, userInfo.getHeadline());
			params.put(i++, userInfo.getDescription());
			params.put(i++, userInfo.getLocation());
			params.put(i++, userInfo.getBusiness());
			params.put(i++, userInfo.getEmployment());
			params.put(i++, userInfo.getPosition());
			params.put(i++, userInfo.getEducation());
			params.put(i++, userInfo.getMajor());
			params.put(i++, userInfo.getAgree());
			params.put(i++, userInfo.getThanks());			
			params.put(i++, userInfo.getAsks());
			params.put(i++, userInfo.getAnswers());
			params.put(i++, userInfo.getPosts());
			params.put(i++, userInfo.getCollections());
			params.put(i++, userInfo.getLogs());
			params.put(i++, System.currentTimeMillis());
			String columns = "url,name,gender,headline,description,location,business,employment,position,education,major,agree,thanks,asks,answers,posts,collections,logs,updatetime";
			dbServer.insert("userinfo", columns, params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
	}

	public void saveUser(UserInfo userInfo) {
		if (userInfo == null) {
			return;
		}
		UserInfoDao userInfoDao = new UserInfoDao();
		userInfoDao.save(userInfo);
	}
	
	public void saveAllLink(String url) {
		CrawlLink crawlLink = new CrawlLink(url);
		Set<String> links = crawlLink.crawlAllLink();
		System.out.println("links:" + links.size());
		Iterator<String> iterator = links.iterator();
		while(iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "https://www.zhihu.com/";
//		String user = "https://www.zhihu.com/people/zhouyuan";
		String user = "https://www.zhihu.com/people/05lai-zi";
		CrawlLink crawlLink = new CrawlLink(url);
		crawlLink.crawlAllLink();
		System.out.println("=============================");
		crawlLink.crawlAllLink();
//		crawlLink.getResponseHeaders();
//		System.out.println("=============================");
//		crawlLink.getRequestHeaders();
//		System.out.println(crawlLink.getPageSourceCode());
//		CrawlUserInfo crawlUserInfo = new CrawlUserInfo(user);
//		UserInfo userInfo = crawlUserInfo.crawlUserInfo();
//		UserInfoDao userInfoDao = new UserInfoDao();
//		userInfoDao.save(userInfo);
//		System.out.println(JsonUtil.parseJson(userInfo, "userInof"));
	}
}
