package com.zhihucrawler.filter;

import java.util.BitSet;

import com.zhihucrawler.model.CrawlUrl;

public class BloomFilter {
	
	private static final int DEFAULT_SIZE = 2 << 24;// BitSet初始分配2 << 24个bit
	private static final int[] seeds = new int[] {7, 11, 13, 31, 37, 61};// 不同哈希函数的种子,一般应取质数
	private BitSet bits = new BitSet(DEFAULT_SIZE);
	private SimpleHash[] func = new SimpleHash[seeds.length];// 哈希函数对象
	
	public BloomFilter() {
		for (int i = 0; i < seeds.length; i++) {
			func[i] = new SimpleHash(DEFAULT_SIZE, seeds[i]);
		}
	}

	// 覆盖方法,把URL添加进来
//	public void add(CrawlUrl value) {
//		if (value != null) {
//			add(value.getUrl());
//		}
//	}
	
	// 覆盖方法,是否包含URL
//	public boolean contains(CrawlUrl value) {
//		return contains(value.getUrl());
//	}
	
	// 覆盖方法,把URL添加进来
	public void add(String value) {
		for (SimpleHash f : func) {
			bits.set(f.hash(value), true);
		}
	}
    
    // 覆盖方法,是否包含URL
	public boolean contains(String value) {
		if (value == null) {
			return false;
		}
		boolean ret = true;
		for (SimpleHash f : func) {
			ret = ret && bits.get(f.hash(value));
		}
		return ret;
	}
    
    // 哈希函数类
    public static class SimpleHash {
    	
    	private int cap;
    	private int seed;
    	
		public SimpleHash(int cap, int seed) {
			this.cap = cap;
			this.seed = seed;
		}
    	
		// Hash函数,采用简单的加权和hash
		public int hash(String value) {
			
			int result = 0;
			int len = value.length();
			for (int i = 0; i < len; i++) {
				result = seed * result + value.charAt(i);
			}
			return (cap - 1) & result;
		}
    }
    
    public static void main(String[] args) {
		BloomFilter bloomFilter = new BloomFilter();
		if (bloomFilter.contains("https://www.zhihu.com/")) {
			System.out.println("1");
		}
		bloomFilter.add("https://www.zhihu.com/");
		if (bloomFilter.contains("https://www.zhihu.com/")) {
			System.out.println("2");
		}
		bloomFilter.add("https://www.zhihu.com/");
		if (bloomFilter.contains("https://www.zhihu.com/")) {
			System.out.println("3");
		}
		bloomFilter.add("https://www.zhihu.com/");
		if (bloomFilter.contains("https://www.zhihu.com/")) {
			System.out.println("4");
		}
	}
    
}
