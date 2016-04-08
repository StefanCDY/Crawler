package com.zhihucrawler.database;

import com.zhihucrawler.utils.ClassUtil;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: DBPool.java
 * @Description: 指定数据库连接池配置文件 
 * @Date 2016-3-15 下午7:53:56
 */
public class DBPool {
	private String poolPath;//数据库连接池的配置文件路径

	/**
	 * @author Stefan
	 * @version V1.0
	 * @ClassName: DBPool.java
	 * @Description: 静态内部类实现单例模式
	 * @Date 2016-3-15 下午8:07:09
	 */
	private static class DBPoolDao {
		private static DBPool dbPool = new DBPool();
	}
	
	/** 
	 * @author Stefan
	 * @Title getDbPool
	 * @Description 获取DBPool对象
	 * @return DBPool
	 * @Date 2016-3-15 下午8:07:42
	 */
	public static DBPool getDbPool() {
		return DBPoolDao.dbPool;
	}
	
	/** 
	 * @author Stefan
	 * @Title getPoolPath
	 * @Description 获取数据库连接池的配置文件路径
	 * @return String
	 * @Date 2016-3-15 下午8:08:15
	 */
	public String getPoolPath() {
		if (poolPath == null) {
			//如果poolPath为空，赋值为默认值
			poolPath = ClassUtil.getClassRootPath(DBPool.class) + "proxool.xml";
		}
		return poolPath;
	}
	
	/** 
	 * @author Stefan
	 * @Title setPoolPath
	 * @Description 设置数据库连接池的配置文件路径
	 * @param poolPath
	 * @Date 2016-3-15 下午8:09:21
	 */
	public void setPoolPath(String poolPath) {
		this.poolPath = poolPath;
	}

	public static void main(String[] args) {
		DBPool dbPool = new DBPool();
		System.out.println(dbPool.getPoolPath());
	}
}
