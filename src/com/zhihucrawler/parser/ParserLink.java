package com.zhihucrawler.parser;

import java.util.HashSet;
import java.util.Set;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.zhihucrawler.utils.RegexUtil;

public class ParserLink {
	
	public ParserLink() {
		super();
	}
	
	/**
	 * @description 解析网页链接
	 * @param url 当前地址
	 * @param html 待解析的HTML
	 * @param charset 解析编码
	 * @return
	 */
	public static Set<String> parserAllLink(String url, String html, String charset) {
		Set<String> links = new HashSet<String>();
		try {
			Parser parser = Parser.createParser(html, charset);
			NodeFilter filter = new NodeClassFilter(LinkTag.class);
			NodeList nodeList  = parser.extractAllNodesThatMatch(filter);
			for (int i = 0; i < nodeList.size(); i++) {
				LinkTag linkTag = (LinkTag) nodeList.elementAt(i);
				String link = RegexUtil.getHttpUrl(linkTag.getLink(), url);
				int index = link.indexOf("#");
				if (index != -1) {// 含有"#"
					link = link.substring(0, index);
				}
				if (link.contains("www.zhihu.com") && !link.contains("careers")) {					
					links.add(link);
				}
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return links;
	}

	/**
	 * @description 解析用户主页链接
	 * @param url 当前地址
	 * @param html 待解析的HTML
	 * @param charset 解析编码
	 * @return
	 */
	public Set<String> parserUserLink(String url, String html, String charset) {
		Set<String> links = new HashSet<String>();
		try {
			Parser parser = Parser.createParser(html, charset);
			NodeFilter filter = new HasAttributeFilter("class", "author-link");
			NodeList nodeList  = parser.extractAllNodesThatMatch(filter);
			for (int i = 0; i < nodeList.size(); i++) {
				LinkTag linkTag = (LinkTag) nodeList.elementAt(i);
				String link = RegexUtil.getHttpUrl(linkTag.getLink(), url);//获取标签链接
				links.add(link);
			}
		} catch (ParserException e) {
			e.printStackTrace();
		}
		return links;
	}

}
