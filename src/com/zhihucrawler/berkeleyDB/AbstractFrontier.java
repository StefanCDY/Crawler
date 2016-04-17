/**
 * Stefan
 */
package com.zhihucrawler.berkeleyDB;

import java.io.File;
import java.io.FileNotFoundException;

import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: AbstractFrontier.java
 * @Description: TODO
 * @Date 2016-4-13 下午6:10:49
 */
public abstract class AbstractFrontier {
	
	private Environment environment = null;
	private static final String DatabaseName = "Url.db";
	protected Database database;// 存储数据
//	private static final String Class_Catalog = "class.Db";
//	protected StoredClassCatalog classCatalog;
//	protected Database catalogdatabase;// 存储类信息
	
    public AbstractFrontier(String envDir) {
        // 打开Environment
//        System.out.println("Opening Environment in: " + envDir);
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);// 如果设置了true则表示当数据库环境不存在时候重新创建一个数据库环境,默认为false.
        envConfig.setTransactional(true);// 事务支持,如果为true,则表示当前环境支持事务处理,默认为false.
        envConfig.setCacheSize(1024 * 1024 * 20);// 设置当前环境能够使用的缓存大小,单位Byte.
//        envConfig.setReadOnly(false);// 以只读方式打开,默认为false.
//      envConfig.setCachePercent(percent);// 设置当前环境能够使用的RAM占整个JVM内存的百分比.
		
        environment = new Environment(new File(envDir), envConfig);
        
        // 设置DatabaseConfig
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);// 如果是true的话,则当不存在此数据库的时候创建一个.
        dbConfig.setTransactional(true);// 如果设置为true,则支持事务处理,默认是false,不支持事务.
        dbConfig.setSortedDuplicates(true);// 设置一个key是否允许存储多个值，true代表允许，默认false.
//		dbConfig.setExclusiveCreate(true);// 以独占的方式打开，也就是说同一个时间只能有一实例打开这个database.
		
		//打开一个数据库，如果数据库不存在则创建一个
//		catalogdatabase = environment.openDatabase(null, Class_Catalog, dbConfig);
//		classCatalog = new StoredClassCatalog(catalogdatabase);
		
		database = environment.openDatabase(null, DatabaseName, dbConfig);
		
//		System.out.println(catalogdatabase.getDatabaseName() + ":" + catalogdatabase.count());
//		System.out.println(database.getDatabaseName() + ":" + database.count());
    }
    
    //关闭数据库，关闭环境  
	public void close() throws DatabaseException {
		if (database != null) {
			database.close();
		}
//		if (catalogdatabase != null) {
//			catalogdatabase.close();
//		}
//		if (classCatalog != null) {
//			classCatalog.close();
//		}
		if (environment != null) {
			environment.cleanLog();// 在关闭环境前清理下日志
			environment.close();
			environment = null;
		}
	}
	
    //put方法
    protected abstract void put(Object key,Object value);
    //get方法
    protected abstract Object get(Object key);
    //delete方法
    protected abstract Object delete(Object key);

}
