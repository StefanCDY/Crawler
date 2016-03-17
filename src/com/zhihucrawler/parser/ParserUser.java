package com.zhihucrawler.parser;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.zhihucrawler.model.UserInfo;
import com.zhihucrawler.utils.EncryptUtil;

public class ParserUser {

	public ParserUser() {
		super();
	}

	/** 
	 * @author Stefan
	 * @Title parserUserName
	 * @Description 解析用户姓名
	 * @param info
	 * @param charset
	 * @return String
	 * @throws ParserException
	 * @Date 2016-3-16 下午7:13:40
	 */
	private String parserUserName(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter nameFilter = new HasAttributeFilter("class", "name");
		NodeList nodeList = parser.extractAllNodesThatMatch(nameFilter);
		if (nodeList.size() > 0) {
			return nodeList.elementAt(0).toPlainTextString().trim();
		}
		return null;
	}

	// 解析用户性别
	private String parserUserGender(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter genderFilter = new HasAttributeFilter("class", "item gender");
		NodeList nodeList = parser.extractAllNodesThatMatch(genderFilter);
		if (nodeList.size() > 0) {
			if (nodeList.elementAt(0).toHtml().contains("icon-profile-female")) {
				return "女";
			} else if (nodeList.elementAt(0).toHtml()
					.contains("icon-profile-male")) {
				return "男";
			}
		}
		return "未知";
	}

	// 解析用户描述
	private String parserUserHeadLine(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter headlineFilter = new HasAttributeFilter("class", "bio");
		NodeList nodeList = parser.extractAllNodesThatMatch(headlineFilter);
		if (nodeList.size() > 0) {
			return nodeList.elementAt(0).toPlainTextString().trim();
		}
		return null;
	}

	// 解析用户描述
	private String parserUserContent(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter descFilter = new HasAttributeFilter("class", "content");
		NodeList nodeList = parser.extractAllNodesThatMatch(descFilter);
		if (nodeList.size() > 0) {
			return nodeList.elementAt(0).toPlainTextString().trim();
		}
		return null;
	}

	// 解析用户居住地
	private String parserUserLocation(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter localFilter = new HasAttributeFilter("class",
				"location item");
		NodeList nodeList = parser.extractAllNodesThatMatch(localFilter);
		if (nodeList.size() > 0) {
			return nodeList.elementAt(0).toPlainTextString().trim();
		}
		return null;
	}

	// 解析用户所在行业
	private String parserUserBusiness(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter busFilter = new HasAttributeFilter("class", "business item");
		NodeList nodeList = parser.extractAllNodesThatMatch(busFilter);
		if (nodeList.size() > 0) {
			return nodeList.elementAt(0).toPlainTextString().trim();
		}
		return null;
	}

	// 解析用户职业经历
	private String parserUserEmployment(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter emplFilter = new HasAttributeFilter("class",
				"employment item");
		NodeList nodeList = parser.extractAllNodesThatMatch(emplFilter);
		if (nodeList.size() > 0) {
			return nodeList.elementAt(0).toPlainTextString().trim();
		}
		return null;
	}

	// 解析用户职位
	private String parserUserPosition(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter positFilter = new HasAttributeFilter("class",
				"position item");
		NodeList nodeList = parser.extractAllNodesThatMatch(positFilter);
		if (nodeList.size() > 0) {
			return nodeList.elementAt(0).toPlainTextString().trim();
		}
		return null;
	}

	// 解析教育经历
	private String parserUserEducation(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter eduFilter = new HasAttributeFilter("class",
				"education item");
		NodeList nodeList = parser.extractAllNodesThatMatch(eduFilter);
		if (nodeList.size() > 0) {
			return nodeList.elementAt(0).toPlainTextString().trim();
		}
		return null;
	}

	// 解析专业方向
	private String parserUserMajor(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter majorFilter = new HasAttributeFilter("class",
				"education-extra item");
		NodeList nodeList = parser.extractAllNodesThatMatch(majorFilter);
		if (nodeList.size() > 0) {
			return nodeList.elementAt(0).toPlainTextString().trim();
		}
		return null;
	}

	// 解析用户赞同数
	private int parserUserAgree(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter agreeFilter = new HasAttributeFilter("class",
				"zm-profile-header-user-agree");
		NodeList nodeList = parser.extractAllNodesThatMatch(agreeFilter);
		if (nodeList.size() > 0) {
			return Integer.parseInt(nodeList.elementAt(0).toPlainTextString().replace("赞同", "").trim());
		}
		return 0;
	}

	// 解析用户感谢数
	private int parserUserThanks(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter thxFilter = new HasAttributeFilter("class",
				"zm-profile-header-user-thanks");
		NodeList nodeList = parser.extractAllNodesThatMatch(thxFilter);
		if (nodeList.size() > 0) {
			return Integer.parseInt(nodeList.elementAt(0).toPlainTextString().replace("感谢", "").trim());
		}
		return 0;
	}

	// 解析用户提问数
	private int parserUserAsks(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter navFilter = new HasAttributeFilter("class", "item ");
		NodeList nodeList = parser.extractAllNodesThatMatch(navFilter);
		if (nodeList.size() > 0) {
			return Integer.parseInt(nodeList.elementAt(0).toPlainTextString().replace("提问", "").trim());
		}
		return 0;
	}
	
	// 解析用户回答数
	private int parserUserAnswers(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter navFilter = new HasAttributeFilter("class", "item ");
		NodeList nodeList = parser.extractAllNodesThatMatch(navFilter);
		if (nodeList.size() > 0) {
			return Integer.parseInt(nodeList.elementAt(1).toPlainTextString().replace("回答", "").trim());
		}
		return 0;
	}
	
	// 解析用户专栏文章数
	private int parserUserPosts(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter navFilter = new HasAttributeFilter("class", "item ");
		NodeList nodeList = parser.extractAllNodesThatMatch(navFilter);
		if (nodeList.size() > 0) {
			return Integer.parseInt(nodeList.elementAt(2).toPlainTextString().replace("专栏文章", "").trim());
		}
		return 0;
	}
	
	// 解析用户收藏数
	private int parserUserCollections(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter navFilter = new HasAttributeFilter("class", "item ");
		NodeList nodeList = parser.extractAllNodesThatMatch(navFilter);
		if (nodeList.size() > 0) {
			return Integer.parseInt(nodeList.elementAt(3).toPlainTextString().replace("收藏", "").trim());
		}
		return 0;
	}
	
	// 解析用户公共编辑数
	private int parserUserLogs(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter navFilter = new HasAttributeFilter("class", "item ");
		NodeList nodeList = parser.extractAllNodesThatMatch(navFilter);
		if (nodeList.size() > 0) {
			return Integer.parseInt(nodeList.elementAt(4).toPlainTextString().replace("公共编辑", "").trim());
		}
		return 0;
	}
	
	// 解析用户头像地址
	private String parserUserImage(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter imageFilter = new NodeClassFilter(ImageTag.class);
		NodeList nodeList = parser.extractAllNodesThatMatch(imageFilter);
		if (nodeList.size() > 0) {
			ImageTag imageTag = (ImageTag) nodeList.elementAt(0);
			return imageTag.getImageURL().trim();
		}
		return null;
	}
	
	// 解析用户微博地址
	private String parserUserWeibo(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter weiboFilter = new HasAttributeFilter("class", "zm-profile-header-user-weibo");
		NodeList nodeList = parser.extractAllNodesThatMatch(weiboFilter);
		if (nodeList.size() > 0) {
			LinkTag linkTag = (LinkTag) nodeList.elementAt(0);
			return linkTag.getLink().trim();
		}
		return null;
	}
	
	// 解析用户关注了的人数
	private int parserUserFollowees(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter followeesFilter = new HasAttributeFilter("class", "zm-profile-side-following zg-clear");
		NodeList nodeList = parser.extractAllNodesThatMatch(followeesFilter);
		if (nodeList.size() > 0) {
			return Integer.valueOf(nodeList.elementAt(0).getChildren().elementAt(1).getChildren().elementAt(5).toPlainTextString().trim());
		}
		return 0;
	}
	
	// 解析用户受关注的人数
	private int parserUserFollowers(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter filter = new HasAttributeFilter("class", "zm-profile-side-following zg-clear");
		NodeList nodeList = parser.extractAllNodesThatMatch(filter);
		if (nodeList.size() > 0) {
			return Integer.valueOf(nodeList.elementAt(0).getChildren().elementAt(3).getChildren().elementAt(5).toPlainTextString().trim());
		}
		return 0;
	}
	
	// 解析用户关注专栏数
	private int parserUserFollowed(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter followedFilter = new HasAttributeFilter("class", "zm-profile-side-section-title");
		NodeList nodeList = parser.extractAllNodesThatMatch(followedFilter);
		if (nodeList.size() > 0) {
			return Integer.valueOf(nodeList.elementAt(0).getChildren().elementAt(1).toPlainTextString().replace("个专栏", "").trim());
		}
		return 0;
	}
	
	// 解析用户关注话题数
	private int parserUserTopics(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter topicsFilter = new HasAttributeFilter("class", "zm-profile-side-section-title");
		NodeList nodeList = parser.extractAllNodesThatMatch(topicsFilter);
		if (nodeList.size() > 0) {
			return Integer.valueOf(nodeList.elementAt(1).getChildren().elementAt(1).toPlainTextString().replace("个话题", "").trim());
		}
		return 0;
	}
	
	// 解析用户主页被浏览人数
	private int parserUserPv(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter followeesFilter = new HasAttributeFilter("class", "zg-gray-normal");
		NodeList nodeList = parser.extractAllNodesThatMatch(followeesFilter);
		if (nodeList.size() > 0) {
			return Integer.valueOf(nodeList.elementAt(2).getChildren().elementAt(2).toPlainTextString().trim());
		}
		return 0;
	}

	/** 
	 * @author Stefan
	 * @Title parserUserInfo
	 * @Description 解析并封装用户信息
	 * @param url
	 * @param html
	 * @param charset
	 * @return UserInfo
	 * @Date 2016-3-16 下午7:19:45
	 */
	public UserInfo parserUserInfo(String url, String html, String charset) {
		UserInfo userinfo = new UserInfo();
		userinfo.setId(EncryptUtil.parseStrToMD5(url));
		userinfo.setUrl(url);
		try {
			Parser parser = Parser.createParser(html, charset);
			NodeFilter filter = new HasAttributeFilter("class", "zm-profile-header ProfileCard");
			NodeList nodeList = parser.extractAllNodesThatMatch(filter);
			if (nodeList.size() < 1) {
				return userinfo;
			}
			String info = nodeList.elementAt(0).toHtml();
			userinfo.setName(this.parserUserName(info, charset));
			userinfo.setGender(this.parserUserGender(info, charset));
			userinfo.setHeadline(this.parserUserHeadLine(info, charset));
			userinfo.setDescription(this.parserUserContent(info, charset));
			userinfo.setLocation(this.parserUserLocation(info, charset));
			userinfo.setBusiness(this.parserUserBusiness(info, charset));
			userinfo.setEmployment(this.parserUserEmployment(info, charset));
			userinfo.setPosition(this.parserUserPosition(info, charset));
			userinfo.setEducation(this.parserUserEducation(info, charset));
			userinfo.setMajor(this.parserUserMajor(info, charset));
			userinfo.setAgree(this.parserUserAgree(info, charset));
			userinfo.setThanks(this.parserUserThanks(info, charset));
			userinfo.setWeibo(this.parserUserWeibo(info, charset));
			userinfo.setHeadimage(this.parserUserImage(info, charset));
			userinfo.setAsks(this.parserUserAsks(info, charset));
			userinfo.setAnswers(this.parserUserAnswers(info, charset));
			userinfo.setPosts(this.parserUserPosts(info, charset));
			userinfo.setCollections(this.parserUserCollections(info, charset));
			userinfo.setLogs(this.parserUserLogs(info, charset));
			
			parser = Parser.createParser(html, charset);
			filter = new HasAttributeFilter("class", "zu-main-sidebar");
			nodeList = parser.extractAllNodesThatMatch(filter);
			if (nodeList.size() < 1) {
				return userinfo;
			}
			info = nodeList.elementAt(0).toHtml();
			userinfo.setFollowees(this.parserUserFollowees(info, charset));
			userinfo.setFollowers(this.parserUserFollowers(info, charset));
			userinfo.setFollowed(this.parserUserFollowed(info, charset));
			userinfo.setTopics(this.parserUserTopics(info, charset));
			userinfo.setPv(this.parserUserPv(info, charset));
			
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return userinfo;
	}

}
