package org.mlspark.examples.ex1;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.function.ToDoubleFunction;

import org.jfree.data.xy.XYDataset;
import org.junit.Assert;
import org.junit.Test;

public class DataPlotTest {

	DataPlot dataPlot = DataPlot.getInstance();
	
	static final String DATA_PATH = "src/test/resources/junit/data/ex1";
	
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
	
	@Test
	public final void testX_EXTRACTOR() {
		List<String> input = Arrays.asList("1.1,2.2", "3.3,4.4");
	    double[] result = input.stream().mapToDouble(X_EXTRACTOR).toArray();
	    assertEquals(result.length, 2);
	    Assert.assertArrayEquals(new double[]{1.1, 3.3}, result, 0);
	}
	
	@Test
	public final void testY_EXTRACTOR() {
		List<String> input = Arrays.asList("1.1,2.2", "3.3,4.4");
	    double[] result = input.stream().mapToDouble(Y_EXTRACTOR).toArray();
	    assertEquals(result.length, 2);
	    Assert.assertArrayEquals(new double[]{2.2, 4.4}, result, 0); 
	}
	
	
	@Test
	public final void testCreateChartDataset() {
		XYDataset ds = dataPlot.createChartDataset(DATA_PATH + "/data1.txt");
		assertEquals(ds.getSeriesCount(), 1);
		assertEquals(ds.getXValue(0, 0), 6.1101, 0);
		assertEquals(ds.getXValue(0, 1), 5.5277, 0);
		assertEquals(ds.getYValue(0, 0), 17.592, 0);
		assertEquals(ds.getYValue(0, 1), 9.1302, 0);		
	}

}
