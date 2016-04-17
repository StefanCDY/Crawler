package com.zhihucrawler.config;

import java.util.HashSet;
import java.util.Set;

public class LinkQueue {

	private static Set<Object> visitedUrl = new HashSet<Object>();// 已访问的URL集合
	private static Queue unVisitedUrl = new Queue();// 待访问的URL集合
	
	//移除访问过的URL
//	public static void removeVisitedUrl(String url){
//		visitedUrl.remove(url);
//	}
	
	//保证每个URL只被访问一次
	synchronized public static void addUnVisitedUrl(String url){
		if(url != null && !url.trim().equals("")){
//			if (!visitedUrl.contains(url) && !unVisitedUrl.contains(url)) {
				unVisitedUrl.enQueue(url);
//			}
		}
	}
	
	//添加到访问过的URL队列
	synchronized public static void addVisitedUrl(String url){
		visitedUrl.add(url);
	}
	
	//未访问过的URL出队列
	synchronized public static Object unVisitedUrlDeQueue(){
		return unVisitedUrl.deQueue();
	}
	
	//获得已经访问的URL数目
	synchronized public static int getVisitedUrlNum(){
		return visitedUrl.size();
	}
	
	//判断未访问的URL队列是否为空
	synchronized public static boolean unVisitedUrlIsEmpty(){
		return unVisitedUrl.isEmpty();
	}
	
	//获得未访问的URL数目
	public static int getUnVisitedUrlNum(){
		return unVisitedUrl.getSize();
	}
	
}
