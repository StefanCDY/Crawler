package com.zhihucrawler.database;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.zhihucrawler.model.CrawlUrl;
import com.zhihucrawler.model.UserInfo;

public class ZhihuCrawlerDB {
	private static final String POOLNAME = "proxool.DBPool";
	
	public CrawlUrl getUrl() {
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			String sql = "select * from url where state = '" + 0 + "' order by rand() limit 1";
			ResultSet resultSet = dbServer.select(sql);
			while (resultSet.next()) {
				CrawlUrl url = new CrawlUrl();
				url.setAddTime(resultSet.getLong("addTime"));
				url.setDepth(resultSet.getInt("depth"));
				url.setId(resultSet.getInt("id"));
				url.setState(resultSet.getInt("state"));
				url.setUpdateTime(resultSet.getLong("updateTime"));
				url.setUrl(resultSet.getString("url"));
				return url;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
		return null;
	}
	
	public void saveUrl(CrawlUrl url) {
		if (url == null || "".equals(url)) {
			return;
		}
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			if (!this.hasUrl(url.getUrl())) {// url是否已存在
				HashMap<Integer, Object> params = new HashMap<Integer, Object>();
				int i = 1;
				params.put(i++, url.getUrl());
				params.put(i++, url.getDepth());
				params.put(i++, url.getState());
				params.put(i++, System.currentTimeMillis());
				params.put(i++, System.currentTimeMillis());
				String columns = "url,depth,state,addTime,updateTime";
				dbServer.insert("url", columns, params);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
	}
	
	public void saveUrl(List<CrawlUrl> urlList) {
		if (urlList == null || urlList.size() < 1) {
			return;
		}
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			for (CrawlUrl url : urlList) {
				if (!this.hasUrl(url.getUrl())) {// url是否已存在
					HashMap<Integer, Object> params = new HashMap<Integer, Object>();
					int i = 1;
					params.put(i++, url.getUrl());
					params.put(i++, url.getDepth());
					params.put(i++, url.getState());
					params.put(i++, System.currentTimeMillis());
					params.put(i++, System.currentTimeMillis());
					String columns = "url,depth,state,addTime,updateTime";
					dbServer.insert("url", columns, params);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
	}
	
	public void updateUrl(long id, int state) {
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			String sql = "update url set `state` = '" + state + "', `updateTime` = '" + System.currentTimeMillis() + "'  where id = '" + id + "'";
			dbServer.update(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}		
	}
	
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
			String columns = "url,name,gender,headline,description,headimage,weibo,location,business,employment,position,education,major,agree,thanks,asks,answers,posts,collections,logs,followees,followers,followed,topics,pv,state,createtime,updatetime";
			dbServer.insert("userinfo", columns, params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
	}
	
	public void updateUserInfo(UserInfo userInfo) {
		if (userInfo == null) {
			return;
		}
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			HashMap<Integer, Object> params = new HashMap<Integer, Object>();
			int i = 1;
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
			String columns = "name,gender,headline,description,headimage,weibo,location,business,employment,position,education,major,agree,thanks,asks,answers,posts,collections,logs,followees,followers,followed,topics,pv,state,updatetime";
			String condition = "where id = '" + userInfo.getId() + "'";
			dbServer.update("userinfo", columns, condition, params);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
	}
	
//	public UserInfo getUserInfo(String id) {
//		DBServer dbServer = new DBServer(POOLNAME);
//		UserInfo userInfo = new UserInfo();
//		try {
//			String sql = "select * from userinfo where id = '" + id + "' limit 1";
//			ResultSet resultSet = dbServer.select(sql);
//			while (resultSet.next()) {
//				userInfo.setId(resultSet.getString("id"));
//				userInfo.setName(resultSet.getString("name"));
//				userInfo.setUrl(resultSet.getString("url"));
//				userInfo.setAgree(resultSet.getInt("agree"));
//				userInfo.setAnswers(resultSet.getInt("answers"));
//				userInfo.setAsks(resultSet.getInt("asks"));
//				userInfo.setBusiness(resultSet.getString("business"));
//				userInfo.setCollections(resultSet.getInt("collections"));
//				userInfo.setDescription(resultSet.getString("description"));
//				userInfo.setEducation(resultSet.getString("education"));
//				userInfo.setEmployment(resultSet.getString("employment"));
//				userInfo.setFollowed(resultSet.getInt("followed"));
//				userInfo.setFollowees(resultSet.getInt("followees"));
//				userInfo.setFollowers(resultSet.getInt("followers"));
//				userInfo.setGender(resultSet.getString("gender"));
//				userInfo.setHeadimage(resultSet.getString("headimage"));
//				userInfo.setLocation(resultSet.getString("location"));
//				userInfo.setLogs(resultSet.getInt("logs"));
//				userInfo.setMajor(resultSet.getString("major"));
//				userInfo.setPosition(resultSet.getString("position"));
//				userInfo.setPosts(resultSet.getInt("posts"));
//				userInfo.setPv(resultSet.getInt("pv"));
//				userInfo.setThanks(resultSet.getInt("thanks"));
//				userInfo.setTopics(resultSet.getInt("topics"));
//				userInfo.setWeibo(resultSet.getString("weibo"));
//				userInfo.setState(resultSet.getInt("state"));
//				userInfo.setCreatetime(resultSet.getLong("createtime"));
//				userInfo.setUpdatetime(resultSet.getLong("updatetime"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			dbServer.close();
//		}
//		return userInfo;
//	}
//	
//	public boolean hasUserInfo(String id) {
//		DBServer dbServer = new DBServer(POOLNAME);
//		try {
//			String sql = "select sum(1) as count from userinfo where id = '" + id + "'";
//			ResultSet resultSet = dbServer.select(sql);
//			if (resultSet.next()) {
//				int count = resultSet.getInt("count");
//				return count > 0;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			dbServer.close();
//		}
//		return false;
//	}

	public List<Object[]> countGenderRatio() {
		DBServer dbServer = new DBServer(POOLNAME);
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			String sql = "select gender, count(1) as count from userinfo group by gender;";
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
	
	public List<Object[]> countLocationRatio() {
		DBServer dbServer = new DBServer(POOLNAME);
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			String sql = "select location, count(1) as count from userinfo group by location order by count desc limit 10;";
			ResultSet resultSet = dbServer.select(sql);
			while (resultSet.next()) {
				Object[] objects = new Object[2];
				objects[0] = resultSet.getString("location");
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
	
	public List<Object[]> countBusinessRatio() {
		DBServer dbServer = new DBServer(POOLNAME);
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			String sql = "select business, count(1) as count from userinfo group by business order by count desc limit 10;";
			ResultSet resultSet = dbServer.select(sql);
			while (resultSet.next()) {
				Object[] objects = new Object[2];
				objects[0] = resultSet.getString("business");
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
	
	public List<Object[]> countEducationRatio() {
		DBServer dbServer = new DBServer(POOLNAME);
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			String sql = "select education, count(1) as count from userinfo group by education order by count desc limit 10;";
			ResultSet resultSet = dbServer.select(sql);
			while (resultSet.next()) {
				Object[] objects = new Object[2];
				objects[0] = resultSet.getString("education");
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
	
	public List<Object[]> countMajorRatio() {
		DBServer dbServer = new DBServer(POOLNAME);
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			String sql = "select major, count(1) as count from userinfo group by major order by count desc limit 10;";
			ResultSet resultSet = dbServer.select(sql);
			while (resultSet.next()) {
				Object[] objects = new Object[2];
				objects[0] = resultSet.getString("major");
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
	
	public List<Object[]> countEmploymentRatio() {
		DBServer dbServer = new DBServer(POOLNAME);
		List<Object[]> list = new ArrayList<Object[]>();
		try {
			String sql = "select employment, count(1) as count from userinfo group by employment order by count desc limit 10;";
			ResultSet resultSet = dbServer.select(sql);
			while (resultSet.next()) {
				Object[] objects = new Object[2];
				objects[0] = resultSet.getString("employment");
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


	
	public long getVisitedUrlNum() {
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			String sql = "select count(1) as count from url where state = 1";
			ResultSet resultSet = dbServer.select(sql);
			if (resultSet.next()) {
				int count = resultSet.getInt("count");
				return count;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
		return 0;
	}
	
	private boolean hasUrl(String url) {
		DBServer dbServer = new DBServer(POOLNAME);
		try {
			String sql = "select sum(1) as count from url where url = '" + url + "'";
			ResultSet resultSet = dbServer.select(sql);
			if (resultSet.next()) {
				int count = resultSet.getInt("count");
				return count > 0;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbServer.close();
		}
		return false;
	}


	
}
