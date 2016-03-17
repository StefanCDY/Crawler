package com.zhihucrawler.config;

import java.util.LinkedList;

/**
 * @author Stefan 
 * @description 队列，保存即将要访问的URL
 */
public class Queue {
	
	//使用链表实现队列
	private LinkedList<Object> queue = new LinkedList<Object>();

	//入队列
	public void enQueue(Object object){
		queue.addLast(object);
	}
	
	//出队列
	public Object deQueue(){
		return queue.removeFirst();
	}
	
	//判断队列是否为空
	public boolean isEmpty(){
		return queue.isEmpty();
	}
	
	//判断队列是否包含object
	public boolean contains(Object object){
		return queue.contains(object);
	}
	
	//获取url数量
	public int getSize() {
		return queue.size();
	}
}

/*
ArrayList 和 LinkedList的区别
ArrayList 采用的是数组形式来保存对象的，这种方式将对象放在连续的位置中，所以最大的缺点就是插入删除时非常麻烦
LinkedList 采用的将对象存放在独立的空间中，而且在每个空间中还保存下一个链接的索引  但是缺点就是查找非常麻烦,要丛第一个索引开始
*/