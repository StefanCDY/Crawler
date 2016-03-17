package test;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;

import com.zhihucrawler.chart.UserInfoChart;
import com.zhihucrawler.dao.UserInfoDao;
import com.zhihucrawler.utils.JsonUtil;

public class testDatabase {
	
	@Test
	public void testSchemaExport() {
		//创建hibernate配置对象
		Configuration config = new Configuration().configure();
		//创建服务注册对象
		ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
		//生成SessionFactory
		SessionFactory sessionFactory = config.buildSessionFactory(serviceRegistry);
		
		SchemaExport export = new SchemaExport(config);
		export.create(true, true);
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
		UserInfoChart infoChart = new UserInfoChart();
		infoChart.generateGenderPieChart(list);
	}
	
}
