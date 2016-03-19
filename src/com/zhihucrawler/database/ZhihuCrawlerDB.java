package com.zhihucrawler.database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.zhihucrawler.crawler.CrawlLink;
import com.zhihucrawler.crawler.CrawlUserInfo;
import com.zhihucrawler.dao.UserInfoDao;
import com.zhihucrawler.model.UrlList;
import com.zhihucrawler.model.UserInfo;
import com.zhihucrawler.utils.JsonUtil;

public class ZhihuCrawlerDB {
	private static final String POOLNAME = "proxool.DBPool";
	
	/** 
	 * @author Stefan
	 * @Title saveUserInfo
	 * @Description 保存用户信息
	 * @param userInfo
	 * @Date 2016-3-19 下午10:56:21
	 */
	public void saveUserInfo(UserInfo userInfo) {
		if (userInfo == null) {
			return;
		}
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			HashMap<Integer, Object> params = new HashMap<Integer, Object>();
			int i = 1;
			params.put(i++, userInfo.getId());
			params.put(i++, userInfo.getUrl());
			params.put(i++, userInfo.getName());
			params.put(i++, userInfo.getGender());
			params.put(i++, userInfo.getHeadline());
			params.put(i++, userInfo.getDescription());
			params.put(i++, userInfo.getHeadimage());
			params.put(i++, userInfo.getWeibo());
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
			params.put(i++, userInfo.getFollowees());
			params.put(i++, userInfo.getFollowers());
			params.put(i++, userInfo.getFollowed());
			params.put(i++, userInfo.getTopics());
			params.put(i++, userInfo.getPv());
			params.put(i++, 1);
			params.put(i++, System.currentTimeMillis());
			params.put(i++, System.currentTimeMillis());
			String columns = "id,url,name,gender,headline,description,headimage,weibo,location,business,employment,position,education,major,agree,thanks,asks,answers,posts,collections,logs,followees,followers,followed,topics,pv,state,createtime,updatetime";
			dbServer.insert("userinfo", columns, params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
	}

	
	public List<Object[]> countGender() {
		DBServer dbServer = new DBServer(POOLNAME);
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			String sql = "select gender, count(1) as count from userinfo group by gender";
			ResultSet resultSet = dbServer.select(sql);
			while (resultSet.next()) {
				Object[] objects = new Object[2];
				objects[0] = resultSet.getString("gender");
				objects[1] = resultSet.getInt("count");
				list.add(objects);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
		return list;
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
