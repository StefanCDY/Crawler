package com.zhihucrawler.chart;

import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.zhihucrawler.config.Const;
import com.zhihucrawler.database.ZhihuCrawlerDB;

/**
 * @author Stefan
 * @version V1.0
 * @ClassName: LocationChart.java
 * @Description: TODO
 * @Date 2016-3-21 下午2:57:49
 */
public class EmploymentChart {

	private static CategoryDataset createDataset(List<Object[]> list) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (Object[] objects : list) {
			int count = Integer.parseInt(objects[1].toString());
			String location = objects[0].toString().isEmpty() ? "未填": objects[0].toString();
			dataset.setValue(count, "", location);
		}
		return dataset;
	}

	public static JFreeChart createChart(String title, CategoryDataset dataset) {
		// 创建主题样式
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		// 设置标题字体
		standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
		// 设置图例的字体
		standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
		// 设置轴向的字体
		standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));
		// 设置周围的背景色
		standardChartTheme.setChartBackgroundPaint(Color.WHITE);
		// 应用主题样式
		ChartFactory.setChartTheme(standardChartTheme);
		
		// 第一个参数是标题,第二个参数是x轴,第三个参数是y轴,第四个参数是数据集,第五个参数表示定位,VERTICAL：垂直,第六个参数表示是否显示图例注释,第七个参数表示是否生成工具,第八个参数表示图中是否生成URL
		JFreeChart chart = ChartFactory.createBarChart(title, "就业分布", "用户数量", dataset, PlotOrientation.VERTICAL,false, false, false);
		CategoryPlot plot = (CategoryPlot) chart.getPlot();//获得图标中间部分,即plot
		plot.setNoDataMessage("No data available");
		plot.setBackgroundPaint(Color.white);// 生成图片的背景色
		plot.setRangeGridlinePaint(Color.BLACK);// 行线的颜色

		CategoryAxis domainAxis = plot.getDomainAxis();
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);// 倾斜45度
        
		BarRenderer renderer = new BarRenderer();// 设置柱子的相关属性
		renderer.setShadowVisible(false);// 是否显示阴影
//		renderer.setShadowPaint(Color.white);// 设置阴影颜色
		renderer.setDrawBarOutline(false);// 设置柱子边框可见
//		renderer.setBaseOutlinePaint(Color.BLACK);// 设置柱子边框颜色
//		renderer.setMaximumBarWidth(0.9);// 设置柱子宽度
//		renderer.setMinimumBarLength(0.5);// 设置柱子高度
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelsVisible(true);// 设置在柱子上显示对应的数值
//		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT));
		plot.setRenderer(renderer);

		return chart;
	}
	
	public void generateLocationChart(List<Object[]> list) {
		try {
			CategoryDataset dataset = createDataset(list);
			JFreeChart chart = createChart(Const.EMPLOYMENT_TITLE, dataset);
			OutputStream outputStream = new FileOutputStream("chart/employment.jpeg");
			ChartUtilities.writeChartAsJPEG(outputStream, chart, Const.genderWidth, Const.genderHeight);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * @author Stefan
	 * @Title main
	 * @Description TODO
	 * @param args
	 * @Date 2016-3-21 下午2:57:49
	 */
	public static void main(String[] args) {
		ZhihuCrawlerDB crawlerDB = new ZhihuCrawlerDB();
		List<Object[]> list = crawlerDB.countEmploymentRatio();
		EmploymentChart chart = new EmploymentChart();
		chart.generateLocationChart(list);
	}

}
