package com.zhihucrawler.config;

public class Const {
	public static final int CONNECT_TIMEOUT = 5000;// 设置连接超时时间
	public static final int CONNECTION_REQUEST_TIMEOUT = 5000;// 设置请求超时时间
	public static final int SOCKET_TIMEOUT = 5000;// 设置读取超时时间
	
	public static final int MAX_TOTAL_CONNECTIONS = 1;// 设置最大连接数
	public static final int MAX_ROUTE_CONNECTIONS = 1;// 设置每个路由最大连接数
	
//	public static final int WAIT_TIMEOUT = 3000;// 设置获取连接的最大等待时间
//	public static final long CONN_MANAGER_TIMEOUT = 500L;// 连接不够用时等待超时时间
	
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
