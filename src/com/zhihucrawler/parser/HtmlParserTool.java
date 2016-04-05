package com.zhihucrawler.parser;

import java.util.HashSet;
import java.util.Set;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.LinkStringFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.zhihucrawler.model.UserInfo;
import com.zhihucrawler.utils.EncryptUtil;
import com.zhihucrawler.utils.RegexUtil;

public class HtmlParserTool {
	
	private String charset = "UTF-8";// 网页解析默认编码方式
	private String validateUrl = "https://www.zhihu.com/";
	
	public HtmlParserTool(String charset) {
		this.charset = charset;
	}
	
	public Set<String> extracLinks(String url, String html) {
		Set<String> links = new HashSet<String>();
		try {
			Parser parser = Parser.createParser(html, this.charset);
			NodeFilter filter = new NodeClassFilter(LinkTag.class);
			NodeList nodeList  = parser.extractAllNodesThatMatch(filter);
			
			for (int i = 0; i < nodeList.size(); i++) {
				LinkTag linkTag = (LinkTag) nodeList.elementAt(i);
				String link = RegexUtil.getHttpUrl(linkTag.getLink(), url);
				if (!link.startsWith(this.validateUrl)) {
					continue;
				}
				int index = link.indexOf("#");
				if (index != -1) {// 含有"#"
					link = link.substring(0, index);
				}
				links.add(link);
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return links;
	}
	
	public UserInfo parserUserInfo(String url, String html) {
		UserInfo userinfo = new UserInfo();
		userinfo.setId(EncryptUtil.parseStrToMD5(url));
		userinfo.setUrl(url);
		try {
			// zm-profile-header-main
			Parser parser = Parser.createParser(html, this.charset);
			NodeFilter filter = new HasAttributeFilter("class", "zm-profile-header-main");
			NodeList nodeList = parser.extractAllNodesThatMatch(filter);
			if (nodeList.size() > 0) {
				String info = nodeList.elementAt(0).toHtml();
				userinfo.setName(parserUserName(info, this.charset));
				userinfo.setGender(parserUserGender(info, this.charset));
				userinfo.setHeadline(parserUserHeadLine(info, this.charset));
				userinfo.setDescription(parserUserDescription(info, this.charset));
				userinfo.setLocation(parserUserLocation(info, this.charset));
				userinfo.setBusiness(parserUserBusiness(info, this.charset));
				userinfo.setEmployment(parserUserEmployment(info, this.charset));
				userinfo.setPosition(parserUserPosition(info, this.charset));
				userinfo.setEducation(parserUserEducation(info, this.charset));
				userinfo.setMajor(parserUserMajor(info, this.charset));
				userinfo.setWeibo(parserUserWeibo(info, this.charset));
				userinfo.setHeadimage(parserUserImage(info, this.charset));
			}
			
			// zm-profile-header-info-list
			parser = Parser.createParser(html, this.charset);
			filter = new HasAttributeFilter("class", "zm-profile-header-info-list");
			nodeList = parser.extractAllNodesThatMatch(filter);
			if (nodeList.size() > 0) {
				nodeList = nodeList.elementAt(0).getChildren();
				userinfo.setAgree(Integer.parseInt(nodeList.elementAt(3).toPlainTextString().replace("赞同", "").trim()));
				userinfo.setThanks(Integer.parseInt(nodeList.elementAt(5).toPlainTextString().replace("感谢", "").trim()));
			}
			
			// profile-navbar clearfix
			parser = Parser.createParser(html, this.charset);
			filter = new HasAttributeFilter("class", "profile-navbar clearfix");
			nodeList = parser.extractAllNodesThatMatch(filter);
			if (nodeList.size() > 0) {
				nodeList = nodeList.elementAt(0).getChildren();
				userinfo.setAsks(Integer.parseInt(nodeList.elementAt(3).toPlainTextString().replace("提问", "").trim()));
				userinfo.setAnswers(Integer.parseInt(nodeList.elementAt(5).toPlainTextString().replace("回答", "").trim()));
				userinfo.setPosts(Integer.parseInt(nodeList.elementAt(7).toPlainTextString().replace("文章", "").trim()));
				userinfo.setCollections(Integer.parseInt(nodeList.elementAt(9).toPlainTextString().replace("收藏", "").trim()));
				userinfo.setLogs(Integer.parseInt(nodeList.elementAt(11).toPlainTextString().replace("公共编辑", "").trim()));
			}
			
			// zu-main-sidebar
			parser = Parser.createParser(html, this.charset);
			filter = new HasAttributeFilter("class", "zu-main-sidebar");
			nodeList = parser.extractAllNodesThatMatch(filter);
			if (nodeList.size() > 0) {
				String info = nodeList.elementAt(0).toHtml().trim();
				userinfo.setFollowees(parserUserFollowees(info, charset));
				userinfo.setFollowers(parserUserFollowers(info, charset));
				userinfo.setFollowed(parserUserFollowed(info, charset));
				userinfo.setTopics(parserUserTopics(info, charset));
				userinfo.setPv(parserUserPv(info, charset));
			}
			
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return userinfo;
	}
	
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
			} else if (nodeList.elementAt(0).toHtml().contains("icon-profile-male")) {
				return "男";
			}
		}
		return "未填";
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

	// 解析用户简介
	private String parserUserDescription(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter descFilter = new HasAttributeFilter("class", "content");
		NodeList nodeList = parser.extractAllNodesThatMatch(descFilter);
		if (nodeList.size() > 0) {
			String description = nodeList.elementAt(0).toPlainTextString().trim();
			if (description.length() > 255) {
				description = description.substring(0, 250) + "...";
			}
			return description;
//			return nodeList.elementAt(0).toPlainTextString().trim();
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
		NodeFilter filter = new LinkStringFilter("followees");
//		NodeFilter filter = new HasAttributeFilter("class", "zm-profile-side-following zg-clear");
		NodeList nodeList = parser.extractAllNodesThatMatch(filter);
		if (nodeList.size() > 0) {
			return Integer.valueOf(nodeList.elementAt(0).toPlainTextString().replace("关注了", "").replace("人", "").trim());
		}
		return 0;
	}
	
	// 解析用户受关注的人数
	private int parserUserFollowers(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter filter = new LinkStringFilter("followers");
//		NodeFilter filter = new HasAttributeFilter("class", "zm-profile-side-following zg-clear");
		NodeList nodeList = parser.extractAllNodesThatMatch(filter);
		if (nodeList.size() > 0) {
			return Integer.valueOf(nodeList.elementAt(0).toPlainTextString().replace("关注者", "").replace("人", "").trim());
		}
		return 0;
	}
	
	// 解析用户关注专栏数
	private int parserUserFollowed(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter filter = new LinkStringFilter("followed");
//		NodeFilter filter = new HasAttributeFilter("class", "zm-profile-side-section-title");
		NodeList nodeList = parser.extractAllNodesThatMatch(filter);
		if (nodeList.size() > 0) {
			return Integer.valueOf(nodeList.elementAt(0).toPlainTextString().replace("个专栏", "").trim());
		}
		return 0;
	}
	
	// 解析用户关注话题数
	private int parserUserTopics(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter filter = new LinkStringFilter("topics");
//		NodeFilter filter = new HasAttributeFilter("class", "zm-profile-side-section-title");
		NodeList nodeList = parser.extractAllNodesThatMatch(filter);
		if (nodeList.size() > 0) {
			return Integer.valueOf(nodeList.elementAt(0).toPlainTextString().replace("个话题", "").trim());
		}
		return 0;
	}
	
	// 解析用户主页被浏览人数
	private int parserUserPv(String info, String charset) throws ParserException {
		Parser parser = Parser.createParser(info, charset);
		NodeFilter filter = new HasAttributeFilter("class", "zg-gray-normal");
		NodeList nodeList = parser.extractAllNodesThatMatch(filter);
		if (nodeList.size() > 0) {
			return Integer.valueOf(nodeList.elementAt(2).toPlainTextString().replace("个人主页被", "").replace("人浏览", "").trim());
		}
		return 0;
	}

}
