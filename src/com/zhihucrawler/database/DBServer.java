package com.zhihucrawler.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: DBServer.java
 * @Description: 基于数据库连接池实现数据库的增删改查操作
 * @Date 2016-3-15 下午9:04:15
 */
public class DBServer {
	private DBOperation dbOperation;
	
	public DBServer(String poolName) {
		dbOperation = new DBOperation(poolName);
	}

	/** 
	 * @author Stefan
	 * @Title close
	 * @Description 关闭数据库连接
	 * @Date 2016-3-15 下午9:08:56
	 */
	public void close() {
		dbOperation.close();
	}
	
	/** 
	 * @author Stefan
	 * @Title insert
	 * @Description 数据库新增操作
	 * @param sql
	 * @return int
	 * @throws SQLException
	 * @Date 2016-3-15 下午9:09:15
	 */
	public int insert(String sql) throws SQLException {
		return dbOperation.executeUpdate(sql);
	}
	
	/** 
	 * @author Stefan
	 * @Title insert
	 * @Description 数据库新增操作
	 * @param tableName
	 * @param columns
	 * @param params
	 * @return int
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @Date 2016-3-15 下午9:09:53
	 */
	public int insert(String tableName, String columns, HashMap<Integer, Object> params) throws ClassNotFoundException, SQLException {
		String sql = this.insertSql(tableName, columns);
		return dbOperation.executeUpdate(sql, params);
	}
	
	/** 
	 * @author Stefan
	 * @Title delete
	 * @Description 数据库删除操作
	 * @param sql
	 * @return int
	 * @throws SQLException
	 * @Date 2016-3-15 下午9:15:54
	 */
	public int delete(String sql) throws SQLException {
		return dbOperation.executeUpdate(sql);
	}
	
	/** 
	 * @author Stefan
	 * @Title delete
	 * @Description 数据库删除操作
	 * @param tableName
	 * @param condition
	 * @return int
	 * @throws SQLException
	 * @Date 2016-3-15 下午9:17:30
	 */
	public int delete(String tableName, String condition) throws SQLException {
		if (tableName == null) {
			return 0;
		}
		String sql = "delete from " + tableName + " " + condition;
		return dbOperation.executeUpdate(sql);
	}
	
	/** 
	 * @author Stefan
	 * @Title update
	 * @Description 数据库更新操作
	 * @param sql
	 * @return int
	 * @throws SQLException
	 * @Date 2016-3-15 下午9:19:02
	 */
	public int update(String sql) throws SQLException {
		return dbOperation.executeUpdate(sql);
	}
	
	/** 
	 * @author Stefan
	 * @Title update
	 * @Description 数据库更新操作
	 * @param tableName
	 * @param columns
	 * @param condition
	 * @param params
	 * @return int
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @Date 2016-3-15 下午9:19:19
	 */
	public int update(String tableName, String columns, String condition, HashMap<Integer, Object> params) throws SQLException, ClassNotFoundException {
		String sql = this.updateSql(tableName, columns, condition);
		return dbOperation.executeUpdate(sql, params);
	}
	
	/** 
	 * @author Stefan
	 * @Title select
	 * @Description 数据库查询操作
	 * @param sql
	 * @return ResultSet
	 * @throws SQLException
	 * @Date 2016-3-15 下午9:23:10
	 */
	public ResultSet select(String sql) throws SQLException {
		return dbOperation.executeQuery(sql);
	}
	
	/** 
	 * @author Stefan
	 * @Title executeQuery
	 * @Description 数据库查询操作
	 * @param tableName
	 * @param columns
	 * @param condition
	 * @return ResultSet
	 * @throws SQLException
	 * @Date 2016-3-15 下午9:23:41
	 */
	public ResultSet executeQuery(String tableName, String columns, String condition) throws SQLException {
		String sql = "select " + columns + " from " + tableName + " " + condition;
		return dbOperation.executeQuery(sql);
	}
	
	/** 
	 * @author Stefan
	 * @Title insertSql
	 * @Description 组装 insert语句
	 * @param tableName
	 * @param columns
	 * @return String
	 * @Date 2016-3-15 下午9:12:06
	 */
	private String insertSql(String tableName, String columns) {
		if (tableName == null || columns == null) {
			return "";
		}
		int n = columns.split(",").length;
		StringBuilder builder = new StringBuilder("");
		builder.append("insert into ");
		builder.append(tableName);
		builder.append(" (");
		builder.append(columns);
		builder.append(") values (?");
		for (int i = 1; i < n; i++) {			
			builder.append(",?");
		}
		builder.append(")");
		return builder.toString();
	}
	
	/** 
	 * @author Stefan
	 * @Title updateSql
	 * @Description 组装 update语句
	 * @param tableName
	 * @param columns
	 * @param condition
	 * @return String
	 * @Date 2016-3-15 下午9:20:37
	 */
	private String updateSql(String tableName, String columns, String condition) {
		if (tableName == null || columns == null) {
			return "";
		}
		String[] column = columns.split(",");
		StringBuilder builder = new StringBuilder("");
		builder.append("update ");
		builder.append(tableName);
		builder.append(" set ");
		builder.append(column[0]);
		builder.append(" = ?");
		for (int i = 1; i < column.length; i++) {
			builder.append(", ");
			builder.append(column[i]);
			builder.append(" = ?");
		}
		builder.append(" ");
		builder.append(condition);
		return builder.toString();
	}
	
}
 