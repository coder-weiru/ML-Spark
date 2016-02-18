package org.mlspark.examples.ex1;

import java.awt.Color;
import java.awt.Shape;
import java.util.Arrays;
import java.util.function.ToDoubleFunction;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.ShapeUtilities;

public class DataPlot {
	private static DataPlot instance = new DataPlot();

	private static final String DATA_PATH = "src/test/resources/data/ex1";
	
	private static final FlatMapFunction<String, String> DATA_EXTRACTOR = new FlatMapFunction<String, String>() {
		private static final long serialVersionUID = 1L;
		public Iterable<String> call(final String s) throws Exception {
			return Arrays.asList(s.split("\n"));
		}
	};
	
	private static final ToDoubleFunction<String> X_EXTRACTOR = s -> {
		String[] ls = s.split(",");
		double x = Double.parseDouble(ls[0]);
		return x;
	};

	private static final ToDoubleFunction<String> Y_EXTRACTOR = s -> {
		String[] ls = s.split(",");
		double y = Double.parseDouble(ls[1]);
		return y;
	};
	
	public void plot() {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Charts");

                frame.setSize(600, 400);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

                XYDataset ds = createChartDataset();
				JFreeChart chart = ChartFactory.createScatterPlot("Scatter plot of training data",
						"Population of City in 10,000s", "Profit in $10,000s", ds, PlotOrientation.VERTICAL, true, true,
                        false);

				Shape cross = ShapeUtilities.createDiagonalCross(5, 0.1f);
				XYPlot xyPlot = (XYPlot) chart.getPlot();
				xyPlot.setDomainCrosshairVisible(true);
				xyPlot.setRangeCrosshairVisible(true);
				XYItemRenderer renderer = xyPlot.getRenderer();
				renderer.setSeriesShape(0, cross);
				renderer.setSeriesPaint(0, Color.red);

                ChartPanel cp = new ChartPanel(chart);

                frame.getContentPane().add(cp);
            }
        });
	}

	public static void main(String[] args) {
		instance.plot();
    }

	private XYDataset createChartDataset() {
		SparkConf conf = new SparkConf().setAppName("org.mlspark.examples.ex1.DataPlot").setMaster("local");
		JavaSparkContext context = new JavaSparkContext(conf);

		JavaRDD<String> file = context.textFile(DATA_PATH + "/ex1data1.txt");
		JavaRDD<String> data = file.flatMap(DATA_EXTRACTOR);

		double[] lx = data.collect().stream().mapToDouble(X_EXTRACTOR).toArray();

		double[] ly = data.collect().stream().mapToDouble(Y_EXTRACTOR).toArray();

		double[][] series = new double[][] { lx, ly };
		context.close();

		DefaultXYDataset ds = new DefaultXYDataset();

		ds.addSeries("training data", series);

        return ds;
    }
}
