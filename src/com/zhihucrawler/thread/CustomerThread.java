/**
 * Stefan
 */
package com.zhihucrawler.thread;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.zhihucrawler.config.LinkQueue;
import com.zhihucrawler.database.ZhihuCrawlerDB;
import com.zhihucrawler.model.UserInfo;
import com.zhihucrawler.parser.HtmlParserTool;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: C_Thread.java
 * @Description: TODO
 * @Date 2016-4-11 下午4:35:48
 */
public class CustomerThread extends Thread {
	
	private PageCodeList pageCodeList;
	private final String charset = "UTF-8";// 网页解析默认编码方式
	private final String peopleUrl = "https://www.zhihu.com/people/";// 用户主页
	
	public CustomerThread(PageCodeList pageCodeList) {
		super();
		this.pageCodeList = pageCodeList;
	}
	
	@Override
	public void run() {
		
		HtmlParserTool parserTool = new HtmlParserTool(this.charset);
		ZhihuCrawlerDB crawlerDB = new ZhihuCrawlerDB();
		
		while (true) {
			System.out.println(Thread.currentThread().getName() + " is parsing.");
			
			Map<String, String> data = pageCodeList.pop();
			
			String visitUrl = null;
			String pageCode = null;
			Iterator<Entry<String, String>> iterator = data.entrySet().iterator();
			if (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				visitUrl = entry.getKey();
				pageCode = entry.getValue();
			}
			if (visitUrl == null || pageCode == null || "".equals(visitUrl) || "".equals(pageCode)) {
				continue;
			}
			// 提取链接——List队列
			List<String> links = parserTool.extracLinks(visitUrl, pageCode);
			if (links != null && links.size() > 0) {
				for (String link : links) {// 新的未访问的URL入队列
					LinkQueue.addUnVisitedUrl(link);
				}
			}
			
			// 解析用户信息
			if (visitUrl.startsWith(this.peopleUrl) && visitUrl.lastIndexOf("/") == (this.peopleUrl.length() - 1)) {
				UserInfo userInfo = parserTool.parserUserInfo(visitUrl, pageCode);
				crawlerDB.saveUserInfo(userInfo);
				System.out.println(Thread.currentThread().getName() + "-" + userInfo.toString());
			}
			
//			try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}

}
