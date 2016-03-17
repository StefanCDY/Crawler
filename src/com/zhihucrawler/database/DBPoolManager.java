package com.zhihucrawler.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.logicalcobwebs.proxool.configuration.JAXPConfigurator;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: DBManager.java
 * @Description: 数据库连接池管理类
 * @Date 2016-3-15 下午8:06:37
 */
public class DBPoolManager {
	
	private DBPoolManager() {
		try {
			JAXPConfigurator.configure(DBPool.getDbPool().getPoolPath(), false);
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author Stefan
	 * @version V1.0
	 * @ClassName: DBManager.java
	 * @Description: 内部静态类实现单例模式
	 * @Date 2016-3-15 下午8:06:09
	 */
	private static class DBManagerDao {
		private static DBPoolManager dbManager = new DBPoolManager();
	}
	
	/**
	 * @author Stefan
	 * @Title getDbManager
	 * @Description 获取数据库连接池管理类
	 * @return DBManager
	 * @Date 2016-3-15 下午8:05:03
	 */
	public static DBPoolManager getDbManager() {
		return DBManagerDao.dbManager;
	}
	
	/**
	 * @author Stefan
	 * @Title getConnection
	 * @Description 获取数据库连接
	 * @param poolName
	 * @return Connection
	 * @throws SQLException
	 * @Date 2016-3-15 下午8:04:13
	 */
	public Connection getConnection(String poolName) throws SQLException {
		return DriverManager.getConnection(poolName);
	}

}