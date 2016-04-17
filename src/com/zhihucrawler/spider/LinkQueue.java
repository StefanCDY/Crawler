package com.zhihucrawler.spider;

import java.util.LinkedList;

import com.zhihucrawler.filter.BloomFilter;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: WaitList.java
 * @Description: TODO
 * @Date 2016-4-15 下午6:32:10
 */
public class LinkQueue {
	
	private static LinkedList<Object> waitList = new LinkedList<Object>();
	private static BloomFilter bloomFilter = new BloomFilter();
	private static int visitedNum = 0;
	
	// 添加等待队列URL
	synchronized static public void addWaitUrl(String url){
		if (url == null || "".equals(url)) {
			return;
		}
		if (!bloomFilter.contains(url)) {// bloomFilter不包含,未访问过
			waitList.addLast(url);
		}
	}
	
	// 添加已访问URL
	synchronized static public void addVisitedUrl(String url){
		bloomFilter.add(url);
		visitedNum++;
	}
	
	// 获取等待队列URL
	synchronized static public Object getWaitUrl(){
		if (!waitList.isEmpty()) {
			return waitList.removeFirst();			
		}
		return null;
	}
	
	// 获取等待队列URL数量
	synchronized static public int getWaitListNum(){
		return waitList.size();
	}
	
	// 获取已访问URL数量
	synchronized static public int getVisitedNum(){
		return visitedNum;
	}
	
	// 判断等待队列是否为空
	synchronized static public boolean isEmpty(){
		return waitList.isEmpty();
	}
	
}
