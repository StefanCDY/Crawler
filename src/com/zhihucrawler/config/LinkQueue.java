package com.zhihucrawler.config;

import java.util.HashSet;
import java.util.Set;

public class LinkQueue {

	//已访问的URL集合
	private static Set<Object> visitedUrl = new HashSet<Object>();
	//待访问的URL集合
	private static Queue unVisitedUrl = new Queue();
	
	//获得URL队列
	public static Queue getUnVisitedUrl(){
		return unVisitedUrl;
	}
	
	//添加到访问过的URL队列
	public static void addVisitedUrl(String url){
		visitedUrl.add(url);
	}
	
	//移除访问过的URL
	public static void removeVisitedUrl(String url){
		visitedUrl.remove(url);
	}
	
	//未访问过的URL出队列
	public static Object unVisitedUrlDeQueue(){
		return unVisitedUrl.deQueue();
	}
	
	//保证每个URL只被访问一次
	public static void addUnVisitedUrl(String url){
		if(url != null && !url.trim().equals("")){
			if(!visitedUrl.contains(url) && !unVisitedUrl.contains(url)){
				unVisitedUrl.enQueue(url);
			}
		}
	}
	
	//获得已经访问的URL数目
	public static int getVisitedUrlNum(){
		return visitedUrl.size();
	}
	
	//获得未访问的URL数目
	public static int getUnVisitedUrlNum(){
		return unVisitedUrl.getSize();
	}
	
	//判断未访问的URL队列是否为空
	public static boolean unVisitedUrlIsEmpty(){
		return unVisitedUrl.isEmpty();
	}
}
