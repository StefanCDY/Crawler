package com.zhihucrawler.chart;

import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import com.zhihucrawler.config.Const;
import com.zhihucrawler.database.ZhihuCrawlerDB;

public class UserInfoChart {
	
	private PieDataset createDataset(List<Object[]> list) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		long sum = 0;
		for (Object[] objects : list) {
			sum += Integer.parseInt(objects[1].toString());
		}
		for (Object[] objects : list) {
			dataset.setValue(objects[0].toString(), Integer.parseInt(objects[1].toString()) * 100 / sum);
		}
		return dataset;
	}
	
	private JFreeChart createChart(String title, PieDataset dataset) {
		
		// 创建主题样式
		StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
		// 设置标题字体
		standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));
		// 设置图例的字体
		standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));
		// 设置轴向的字体
		standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));
		// 应用主题样式
		ChartFactory.setChartTheme(standardChartTheme);

		// 第一个参数是标题，第二个参数是一个数据集，第三个参数表示是否显示Legend，第四个参数表示是否显示提示，第五个参数表示图中是否存在URL
		JFreeChart jfreechart = ChartFactory.createPieChart(title, dataset, true, true, false);
		PiePlot pieplot = (PiePlot) jfreechart.getPlot(); // 通过JFreeChart对象获得plot：PiePlot
		pieplot.setNoDataMessage("No data available"); // 没有数据的时候显示的内容
		pieplot.setCircular(true);
		
		// 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
		pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}:{2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
		
		// 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
//		pieplot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}:{2}"));
		
		return jfreechart;
	}
	
	public void generateGenderPieChart(List<Object[]> list) {
		try {
			
			PieDataset dataset = createDataset(list);
			JFreeChart chart = createChart(Const.genderTitle, dataset);
//			chart.set
			OutputStream outputStream = new FileOutputStream("chart/gender.jpeg");
			ChartUtilities.writeChartAsJPEG(outputStream, chart, Const.genderWidth, Const.genderHeight);
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ZhihuCrawlerDB crawlerDB = new ZhihuCrawlerDB();
		List<Object[]> list = crawlerDB.countGender();
		UserInfoChart infoChart = new UserInfoChart();
		infoChart.generateGenderPieChart(list);
	}
	
	private static CategoryDataset createDataset1() {
		String series1 = "First"; 
		String series2 = "Second"; 
		String series3 = "Third"; 
		String category1 = "Category 1"; 
		String category2 = "Category 2"; 
		String category3 = "Category 3"; 
		String category4 = "Category 4"; 
		String category5 = "Category 5"; 
		DefaultCategoryDataset defaultcategorydataset = new DefaultCategoryDataset(); 
		defaultcategorydataset.addValue(1.0D, series1, category1); 
		defaultcategorydataset.addValue(4D, series1, category2); 
		defaultcategorydataset.addValue(3D, series1, category3); 
		defaultcategorydataset.addValue(5D, series1, category4); 
		defaultcategorydataset.addValue(5D, series1, category5); 

		defaultcategorydataset.addValue(5D, series2, category1); 
		defaultcategorydataset.addValue(7D, series2, category2); 
		defaultcategorydataset.addValue(6D, series2, category3); 
		defaultcategorydataset.addValue(8D, series2, category4); 
		defaultcategorydataset.addValue(4D, series2, category5); 

		defaultcategorydataset.addValue(4D, series3, category1); 
		defaultcategorydataset.addValue(3D, series3, category2); 
		defaultcategorydataset.addValue(2D, series3, category3); 
		defaultcategorydataset.addValue(3D, series3, category4); 
		defaultcategorydataset.addValue(6D, series3, category5); 
		return defaultcategorydataset; 
	}

}
