package com.zhihucrawler.config;

public class Const {
	public static final int CONNECT_TIMEOUT = 5000;// 设置连接超时时间
	public static final int CONNECTION_REQUEST_TIMEOUT = 5000;// 设置请求超时时间
	public static final int SOCKET_TIMEOUT = 5000;// 设置读取超时时间
	
	public static final int MAX_TOTAL_CONNECTIONS = 1;// 设置最大连接数
	public static final int MAX_ROUTE_CONNECTIONS = 1;// 设置每个路由最大连接数
	
//	public static final int WAIT_TIMEOUT = 3000;// 设置获取连接的最大等待时间
//	public static final long CONN_MANAGER_TIMEOUT = 500L;// 连接不够用时等待超时时间
	
	public static final String COOKIE = "q_c1=6e908fb2156e44eeaa9200c7faaad076|1457511240000|1454752297000; cap_id=\"ODI1NzkwYjI1MDZkNDY2MmE3OGJlMjBkMzcyNDg1ODM=|1458372767|d9d96f1edd40b300d8a439d6718ab98a51167c11\"; _za=a7e8266d-324f-46fe-a243-70edcd17b647; __utma=51854390.613105210.1458200811.1458378449.1458381254.6; __utmz=51854390.1458381254.6.5.utmcsr=zhihu.com|utmccn=(referral)|utmcmd=referral|utmcct=/people/chen-zhu-92-47/followees; z_c0=\"QUJES0dUa2Zkd2tYQUFBQVlRSlZUYVNORkZmcDNiY2JCVmNJVG92MzBuLXZ0ZUV1c2NuSmtRPT0=|1458372772|121d7246f38f691293a7f4c316e2ebfb0cb80f8b\"; udid=\"AIAAPuxSlAmPTmyUrcrtwMkNfMU7KwsAB2s=|1457511256\"; __utmv=51854390.100--|2=registration_date=20160214=1^3=entry_date=20160206=1; _xsrf=717a1e813b50dc2e63502895275b540c; d_c0=\"AIAAxSQnoQmPTpM-EnDRTmGOjLsYhywkSBc=|1458213611\"; __utmc=51854390; n_c=1";
	
	public static final int EXECUTION_COUNT = 3;// 设置请求重试次数
	
	public static final String CHARSET = "ISO-8859-1";//网页默认编码方式
	
	public static final String IMAGE_SAVE_PATH = "picture/";//图片默认保存路径
	
	// 数据库连接池
	public static final String PROXOOL_Path = "proxool.xml";
	
	// 图表
	public static final String genderTitle = "知乎用户性别统计图";
	public static final int genderWidth = 500;
	public static final int genderHeight = 300;

}
