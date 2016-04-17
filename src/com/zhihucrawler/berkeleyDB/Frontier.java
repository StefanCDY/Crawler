package com.zhihucrawler.berkeleyDB;

import com.zhihucrawler.model.CrawlUrl;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: Frontier.java
 * @Description: TODO
 * @Date 2016-4-13 下午6:04:33
 */
public interface Frontier {
	public CrawlUrl getNext()throws Exception;  
    public boolean putUrl(CrawlUrl url) throws Exception;  
    //public boolean visited(CrawlUrl url);

}
