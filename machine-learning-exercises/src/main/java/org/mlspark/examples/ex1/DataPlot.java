package org.mlspark.examples.ex1;

import java.awt.Color;
import java.awt.Shape;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToDoubleFunction;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.apache.commons.io.IOUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.util.ShapeUtilities;
import org.mlspark.examples.Config;

public class DataPlot {
	private static DataPlot instance = new DataPlot();

	private static Config config = Config.getInstance();
	private static final String DATA_FILE = config.getLocalDataPath() + "/ex1/ex1data1.txt";

	static final ToDoubleFunction<String> X_EXTRACTOR = s -> {
		String[] ls = s.split(",");
		double x = Double.parseDouble(ls[0]);
		return x;
	};

	static final ToDoubleFunction<String> Y_EXTRACTOR = s -> {
		String[] ls = s.split(",");
		double y = Double.parseDouble(ls[1]);
		return y;
	};
	
	public void plot() {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Machine Learning - Linear Regression");

                frame.setSize(600, 400);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);

				XYDataset ds = createChartDataset(DATA_FILE);
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

	public static DataPlot getInstance() {
		return instance;
	}
	public static void main(String[] args) {
		getInstance().plot();
    }

	public XYDataset createChartDataset(String dataFile) {
		String file = "";
		try {
			file = IOUtils.toString(new FileInputStream(dataFile));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		List<String> data = Arrays.asList(file.split("\n"));

		double[] lx = data.stream().mapToDouble(X_EXTRACTOR).toArray();

		double[] ly = data.stream().mapToDouble(Y_EXTRACTOR).toArray();

		double[][] series = new double[][] { lx, ly };

		DefaultXYDataset ds = new DefaultXYDataset();

		ds.addSeries("training data", series);

        return ds;
    }
}
