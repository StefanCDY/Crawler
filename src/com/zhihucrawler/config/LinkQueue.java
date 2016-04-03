package com.zhihucrawler.config;

import java.util.HashSet;
import java.util.Set;

public class LinkQueue {

	private static Set<Object> visitedUrl;// 已访问的URL集合
	private static Queue unVisitedUrl;// 待访问的URL集合
	public static final Object visitedSignal = new Object();   // 线程间通信变量
	public static final Object unVisitedSignal = new Object();   // 线程间通信变量
	
	//获得已访问的URL集合
	public static Set<Object> getVisitedUrl(){
		if (visitedUrl == null) {
			synchronized (visitedSignal) {
				if (visitedUrl == null) {
					visitedUrl = new HashSet<Object>();
				}
			}
		}
		return visitedUrl;
	}
	
	//获得未访问的URL队列
	public static Queue getUnVisitedUrl(){
		if (unVisitedUrl == null) {
			synchronized (unVisitedSignal) {
				if (unVisitedUrl == null) {
					unVisitedUrl = new Queue();
				}
			}
		}
		return unVisitedUrl;
	}
	
	
	//添加到访问过的URL队列
	public static void addVisitedUrl(String url){
		getVisitedUrl().add(url);
	}
	
	//移除访问过的URL
	public static void removeVisitedUrl(String url){
		visitedUrl.remove(url);
	}
	
	//未访问过的URL出队列
	public static Object unVisitedUrlDeQueue(){
		return getUnVisitedUrl().deQueue();
	}
	
	//保证每个URL只被访问一次
	public static void addUnVisitedUrl(String url){
		if(url != null && !url.trim().equals("")){
			if(!getVisitedUrl().contains(url) && !getUnVisitedUrl().contains(url)){
				getUnVisitedUrl().enQueue(url);
			}
		}
	}
	
	//获得已经访问的URL数目
	public static int getVisitedUrlNum(){
		return getVisitedUrl().size();
	}
	
	//获得未访问的URL数目
	public static int getUnVisitedUrlNum(){
		return unVisitedUrl.getSize();
	}
	
	//判断未访问的URL队列是否为空
	public static boolean unVisitedUrlIsEmpty(){
		return getUnVisitedUrl().isEmpty();
	}
}
