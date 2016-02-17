package org.mlspark.examples.ex1;

import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.PairFunction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import scala.Tuple2;

public class DataPlot {
	private static final String DATA_PATH = "src/test/resources/data/ex1";
	
	private static final FlatMapFunction<String, String> DATA_EXTRACTOR = new FlatMapFunction<String, String>() {	
		private static final long serialVersionUID = 1L;
		public Iterable<String> call(final String s) throws Exception {
			return Arrays.asList(s.split("\n"));
		}
	};

	private static final PairFunction<String, Double, Double> DATA_MAPPER = new PairFunction<String, Double, Double>() {
		private static final long serialVersionUID = 1L;
		public Tuple2<Double, Double> call(String s) throws Exception {
			String[] ls = s.split(",");
			double x = Double.parseDouble(ls[0]);
			double y = Double.parseDouble(ls[1]);
			return new Tuple2<Double, Double>(x, y);
		}
	};

	
	private static JavaPairRDD<Double, Double> loadData() {
		
		SparkConf conf = new SparkConf().setAppName("org.mlspark.examples.ex1.DataPlot").setMaster("local");
		JavaSparkContext context = new JavaSparkContext(conf);

		JavaRDD<String> file = context.textFile(DATA_PATH + "/ex1data1.txt");
		JavaRDD<String> data = file.flatMap(DATA_EXTRACTOR);
		//JavaPairRDD<Double, Double> pairs = data.mapToPair(DATA_MAPPER);
		
		data.collect().stream().mapToDouble((String s) -> {
			String[] ls = s.split(",");
			double x = Double.parseDouble(ls[0]);
			return x;
		});
		
		double[][] data = { {0.1, 0.2, 0.3}, {1, 2, 3} };
		
		context.close();
		
		return pairs;
	}
	
	public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Charts");

                frame.setSize(600, 400);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

                XYDataset ds = createChartDataset();
                JFreeChart chart = ChartFactory.createXYLineChart("Test Chart",
                        "x", "y", ds, PlotOrientation.VERTICAL, true, true,
                        false);

                ChartPanel cp = new ChartPanel(chart);

                frame.getContentPane().add(cp);
            }
        });

    }

    private static XYDataset createChartDataset() {
    	JavaPairRDD<Double, Double> pairs = loadData();
        DefaultXYDataset ds = new DefaultXYDataset();

        pairs.collect().stream().mapToDouble(mapper)
        
        double[][] data = { {0.1, 0.2, 0.3}, {1, 2, 3} };

        ds.addSeries("series1", data);

        return ds;
    }
}
