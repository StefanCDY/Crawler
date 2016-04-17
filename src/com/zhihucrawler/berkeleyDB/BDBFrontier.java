package com.zhihucrawler.berkeleyDB;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.collections.StoredMap;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.OperationStatus;
import com.zhihucrawler.database.ZhihuCrawlerDB;
import com.zhihucrawler.model.CrawlUrl;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: BDBFrontier.java
 * @Description: TODO
 * @Date 2016-4-13 下午6:13:35
 */
public class BDBFrontier extends AbstractFrontier implements Frontier {

//	private StoredMap pendingUrisDB = null;
	
	// 使用默认的路径和缓存大小构造函数
	public BDBFrontier(String homeDirectory) {
		super(homeDirectory);
//		DatabaseEntry databaseEntry;
//		EntryBinding keyBinding = new SerialBinding(classCatalog, String.class);
//		EntryBinding valueBinding = new SerialBinding(classCatalog, CrawlUrl.class);
//		this.pendingUrisDB = new StoredMap(database, keyBinding, valueBinding, true);
	}


	// 获得下一条记录
	public CrawlUrl getNext() {
		CrawlUrl result = null;
//		if (!pendingUrisDB.isEmpty()) {
//			Entry<String, CrawlUrl> entry = (Entry<String, CrawlUrl>) pendingUrisDB.entrySet().iterator().next();
//			result = entry.getValue();
//			System.out.println(entry.getKey().toString() + " : " + entry.getValue().toString());
//		}
		return result;
	}

	// 存入URL对象
	public boolean putUrl(CrawlUrl url) {
		this.put(url.getUrl(), url);;
		return true;
	}

	// 存入数据库的方法
	protected void put(Object key, Object value) {
		
		try {
			DatabaseEntry keyEntry = new DatabaseEntry(key.toString().getBytes("utf-8"));
			DatabaseEntry valEntry = new DatabaseEntry(value.toString().getBytes("utf-8"));
			OperationStatus status = this.database.put(null, keyEntry, valEntry);
//			System.out.println(" ---- &gt; put status:" + status);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
//		this.pendingUrisDB.put(key, value);
	}

	// 取出
	protected Object get(Object key) {
//		return pendingUrisDB.get(key);
		return null;
	}

	// 删除
	protected Object delete(Object key) {
//		return pendingUrisDB.remove(key);
		return null;
	}

	// 根据URL计算键值，可以使用各种压缩算法，包括MD5等压缩算法
//	private String caculateUrl(String url) {
//		return url;
//	}

	// 测试函数
	public static void main(String[] strs) {
		long start = 0;
		long end = 0;
		CrawlUrl url = new CrawlUrl();
		url.setUrl("https://www.zhihu.com");
		int num = 1000;
		try {
			start = System.currentTimeMillis();
			BDBFrontier frontier = new BDBFrontier("d:\\bdb");
			for (int i = 0; i < num; i++) {
				frontier.putUrl(url);
			}
			frontier.close();
			end = System.currentTimeMillis();
			System.out.println("Data Size:" + num);
			System.out.println("Berkely总共耗时:" + (end - start)); // 程序总共耗时:1092532
			
			start = System.currentTimeMillis();
			ZhihuCrawlerDB crawlerDB = new ZhihuCrawlerDB();
			for (int i = 0; i < num; i++) {
				crawlerDB.saveUrl(url);				
			}
			end = System.currentTimeMillis();
			System.out.println("MySql总共耗时:" + (end - start));
			
			start = System.currentTimeMillis();
			List<CrawlUrl> list = new ArrayList<CrawlUrl>();
			for (int i = 0; i < num; i++) {
				list.add(url);
			}
			end = System.currentTimeMillis();
			System.out.println("ArrayList总共耗时:" + (end - start));
			
			
			System.out.println("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
