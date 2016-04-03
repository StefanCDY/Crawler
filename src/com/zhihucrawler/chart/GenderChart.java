package com.zhihucrawler.chart;

import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import com.zhihucrawler.config.Const;
import com.zhihucrawler.database.ZhihuCrawlerDB;

public class GenderChart {
	
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
		// 设置周围的背景色
		standardChartTheme.setChartBackgroundPaint(Color.WHITE);
		// 应用主题样式
		ChartFactory.setChartTheme(standardChartTheme);

		// 第一个参数是标题，第二个参数是一个数据集，第三个参数表示是否显示Legend，第四个参数表示是否显示提示，第五个参数表示图中是否存在URL
		JFreeChart jfreechart = ChartFactory.createPieChart(title, dataset, false, true, false);
		PiePlot plot = (PiePlot) jfreechart.getPlot(); // 通过JFreeChart对象获得plot：PiePlot
		plot.setNoDataMessage("No data available"); // 没有数据的时候显示的内容
		plot.setBackgroundPaint(Color.white);// 生成图片的背景色
		plot.setCircular(true);
		
		// 图片中显示百分比:自定义方式，{0} 表示选项， {1} 表示数值， {2} 表示所占比例 ,小数点后两位
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}:{2}", NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
		
		// 图例显示百分比:自定义方式， {0} 表示选项， {1} 表示数值， {2} 表示所占比例
//		pieplot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0}:{2}"));
		
		return jfreechart;
	}
	
	public void generateGenderChart(List<Object[]> list) {
		try {
			
			PieDataset dataset = createDataset(list);
			JFreeChart chart = createChart(Const.GENDER_TITLE, dataset);
			OutputStream outputStream = new FileOutputStream("chart/gender.jpeg");
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
	 * @Date 2016-3-21 下午4:40:52
	 */
	public static void main(String[] args) {
		ZhihuCrawlerDB crawlerDB = new ZhihuCrawlerDB();
		List<Object[]> list = crawlerDB.countGenderRatio();
		GenderChart chart = new GenderChart();
		chart.generateGenderChart(list);
	}
	
}
