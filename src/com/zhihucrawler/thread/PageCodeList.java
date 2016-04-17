package com.zhihucrawler.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: PageCode.java
 * @Description: TODO
 * @Date 2016-4-11 下午3:52:18
 */
public class PageCodeList {
	
	private List<Map<String, String>> list = new ArrayList<Map<String,String>>();
	
	// 添加PageCode
	synchronized public void push(String url, String pageCode) {
		try {
			while (list.size() > 1000) {
				this.wait();
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put(url, pageCode);
			list.add(map);
			this.notifyAll();
			System.out.println(Thread.currentThread().getName() + " push=" + list.size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// 获取PageCode
	synchronized Map<String, String> pop() {
		Map<String, String> result = null;
		try {
			while (list.size() == 0) {
				System.out.println("pop操作中的：" + Thread.currentThread().getName() + " 线程呈wait状态");
				this.wait();
			}
			result = list.remove(0);
			this.notifyAll();
			System.out.println(Thread.currentThread().getName() + " pop=" + list.size());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int count() {
		return list.size();
	}

}
