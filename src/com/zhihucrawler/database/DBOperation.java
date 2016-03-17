package com.zhihucrawler.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: DBOperation.java
 * @Description: 数据库操作封装类
 * @Date 2016-3-15 下午8:41:03
 */
public class DBOperation {
	private String poolName;// 数据库连接池别名
	private Connection connection = null;// 数据库连接

	public DBOperation(String poolName) {
		this.poolName = poolName;
	}
	
	/** 
	 * @author Stefan
	 * @Title close
	 * @Description 关闭数据库连接
	 * @Date 2016-3-15 下午8:42:45
	 */
	public void close() {
		try {
			if (this.connection != null) {
				this.connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * @author Stefan
	 * @Title open
	 * @Description 打开数据库连接
	 * @throws SQLException
	 * @Date 2016-3-15 下午8:45:35
	 */
	private void open() throws SQLException {
		this.close();// 先关闭后打开,防止数据库连接溢出
		this.connection = DBPoolManager.getDbManager().getConnection(this.poolName);
	}
	
	/** 
	 * @author Stefan
	 * @Title executeUpdate
	 * @Description 执行SQL语句，返回影响行数
	 * @param sql
	 * @return int
	 * @throws SQLException
	 * @Date 2016-3-15 下午8:54:48
	 */
	public int executeUpdate(String sql) throws SQLException {
		this.open();
		Statement statement = this.connection.createStatement();
		return statement.executeUpdate(sql);
	}
	
	/** 
	 * @author Stefan
	 * @Title executeUpdate
	 * @Description 执行带参数的SQL语句，返回影响行数
	 * @param sql
	 * @param params
	 * @return int
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @Date 2016-3-15 下午8:56:31
	 */
	public int executeUpdate(String sql, HashMap<Integer, Object> params) throws SQLException, ClassNotFoundException {
		this.open();
		PreparedStatement pres = this.setPres(sql, params);
		if (pres == null) {
			return 0;
		}
		return pres.executeUpdate();
	}
	
	/** 
	 * @author Stefan
	 * @Title executeQuery
	 * @Description 执行SQL语句，返回结果集
	 * @param sql
	 * @return ResultSet
	 * @throws SQLException
	 * @Date 2016-3-15 下午8:59:54
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		this.open();
		Statement statement = this.connection.createStatement();
		return statement.executeQuery(sql);
	}
	
	/** 
	 * @author Stefan
	 * @Title executeQuery
	 * @Description 执行带参数的SQL语句，返回结果集
	 * @param sql
	 * @param params
	 * @return ResultSet
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @Date 2016-3-15 下午9:00:45
	 */
	public ResultSet executeQuery(String sql, HashMap<Integer, Object> params) throws SQLException, ClassNotFoundException {
		this.open();
		PreparedStatement pres = this.setPres(sql, params);
		if (pres == null) {
			return null;
		}
		return pres.executeQuery();
	}
	

	/** 
	 * @author Stefan
	 * @Title setPre
	 * @Description SQL语句参数转化
	 * @param sql
	 * @param params
	 * @return PreparedStatement
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @Date 2016-3-15 下午8:49:09
	 */
	private PreparedStatement setPres(String sql, HashMap<Integer, Object> params) throws SQLException, ClassNotFoundException {
		if (params == null || params.size() < 1) {
			return null;
		}
		PreparedStatement pres = this.connection.prepareStatement(sql);
		for (int i = 1; i <= params.size(); i++) {
			Object object = params.get(i);
			if (object == null) {
				pres.setString(i, "");
			} else if(object.getClass() == Class.forName("java.lang.String")) {
				pres.setString(i, object.toString());
			} else if(object.getClass() == Class.forName("java.lang.Integer")) {
				pres.setInt(i, (Integer) object);
			} else if(object.getClass() == Class.forName("java.lang.Long")) {
				pres.setLong(i, (Long) object);
			} else if(object.getClass() == Class.forName("java.lang.Double")) {
				pres.setDouble(i, (Double) object);
			} else if(object.getClass() == Class.forName("java.lang.Float")) {
				pres.setFloat(i, (Float) object);
			} else if(object.getClass() == Class.forName("java.lang.Boolean")) {
				pres.setBoolean(i, (Boolean) object);
			} else if(object.getClass() == Class.forName("java.sql.Date")) {
				pres.setDate(i, java.sql.Date.valueOf(object.toString()));
			} else {
				return null;
			}
		}
		return pres;
	}
	
}
