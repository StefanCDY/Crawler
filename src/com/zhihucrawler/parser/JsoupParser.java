/**
 * Stefan
 */
package com.zhihucrawler.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zhihucrawler.crawler.HttpClient;
import com.zhihucrawler.model.CrawlUrl;
import com.zhihucrawler.utils.RegexUtil;


/**
 * @author Stefan
 * @version V1.0
 * @ClassName: JsoupParser.java
 * @Description: TODO
 * @Date 2016-4-15 下午3:37:54
 */
public class JsoupParser {
	
	public static void main(String[] args) throws IOException {
		HttpClient httpClient = new HttpClient();
		String html = httpClient.crawlPage("https://www.zhihu.com");
		int num = 100000;
		long start = System.currentTimeMillis();
		for (int i = 0; i < num; i++) {
			Document doc = Jsoup.parse(html, "https://www.zhihu.com/");
//			Elements links = doc.getElementsByTag("a");			
		}
//		for (Element link : links) {
//		  String linkHref = link.attr("abs:href");
//		  System.out.println(linkHref);
//		}
		System.out.println("jsoup耗费时间：" + (System.currentTimeMillis() - start));
		
			start = System.currentTimeMillis();
			for (int i = 0; i < num; i++) {
				Parser parser = Parser.createParser(html, "UTF-8");
//				NodeFilter filter = new NodeClassFilter(LinkTag.class);
//				NodeList nodeList  = parser.extractAllNodesThatMatch(filter);				
			}
//			for (int i = 0; i < nodeList.size(); i++) {
//				LinkTag linkTag = (LinkTag) nodeList.elementAt(i);
//				String link = RegexUtil.getHttpUrl(linkTag.getLink(), "https://www.zhihu.com/");
//				System.out.println(link);
//			}
		System.out.println("parser耗费时间：" + (System.currentTimeMillis() - start));
	}
	
}
