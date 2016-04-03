package test;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

import com.zhihucrawler.chart.GenderChart;
import com.zhihucrawler.dao.UserInfoDao;
import com.zhihucrawler.database.ZhihuCrawlerDB;
import com.zhihucrawler.model.UserInfo;
import com.zhihucrawler.utils.JsonUtil;

public class testDatabase {
	
	@Test
	public void testSchemaExport() {
		//创建hibernate配置对象
		Configuration config = new Configuration().configure();
		//创建服务注册对象
		StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
		//生成SessionFactory
		SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
//		Session session  = sessionFactory.openSession();

//		Metadata metadata = new MetadataSources(serviceRegistry).buildMetadata();
//		SchemaExport schemaExport = new SchemaExport();
//		schemaExport.create(EnumSet.of(TargetType.DATABASE), metadata);
		
		SchemaExport export = new SchemaExport(config);
		export.create(true, true);
		
	}
	
	@Test
	public void testGetUserInfo() {
		String id = "751edc1d04945ede33c2ba6d9a6f482d";
		ZhihuCrawlerDB crawlerDB = new ZhihuCrawlerDB();
		UserInfo userinfo = crawlerDB.getUserInfo(id);
		System.out.println(JsonUtil.parseJson(userinfo));
	}
	
	@Test
	public void testUpdateUserInfo() {
		String id = "751edc1d04945ede33c2ba6d9a6f482d";
		ZhihuCrawlerDB crawlerDB = new ZhihuCrawlerDB();
		UserInfo userInfo = crawlerDB.getUserInfo(id);
		userInfo.setAsks(2);
		userInfo.setGender("男");
		crawlerDB.updateUserInfo(userInfo);
		System.out.println(JsonUtil.parseJson(userInfo));
	}
	
	@Test
	public void testHasUserInfo() {
		String id = "751edc1d04945ede33c2ba6d9a6f482d";
		ZhihuCrawlerDB crawlerDB = new ZhihuCrawlerDB();
		boolean has = crawlerDB.hasUserInfo(id);
		System.out.println(has);
	}
	
	@Test
	public void testFindById() {
		String id = "751edc1d04945ede33c2ba6d9a6f482d";
		UserInfoDao userInfoDao = new UserInfoDao();
		UserInfo userInfo = userInfoDao.findById(id);
		System.out.println(JsonUtil.parseJson(userInfo));
	}

	@Test
	public void getUserGenderRatio() {
		UserInfoDao dao = new UserInfoDao();
		List<Object[]> list = dao.getUserGenderRatio();
		System.out.println(JsonUtil.parseJson(list));
	}
	
	@Test
	public void genterateGenderChart() {
		UserInfoDao dao = new UserInfoDao();
		List<Object[]> list = dao.getUserGenderRatio();
		GenderChart infoChart = new GenderChart();
//		infoChart.generateGenderPieChart(list);
	}
	
}
