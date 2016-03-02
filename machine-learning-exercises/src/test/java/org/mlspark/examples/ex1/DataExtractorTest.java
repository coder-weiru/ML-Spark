package org.mlspark.examples.ex1;

import static org.junit.Assert.assertEquals;

import org.apache.spark.mllib.regression.LabeledPoint;
import org.junit.Assert;
import org.junit.Test;

public class DataExtractorTest {

	public static String DATA = "6.1101,17.592";

	@Test
	public final void testLABELEDPOINT_DATA_EXTRACTOR() throws Exception {
		LabeledPoint parsedData = LinearRegressionGradientDescentModel.LABELEDPOINT_DATA_EXTRACTOR.call(DATA);
		assertEquals(6.1101, parsedData.label(), 0);
		Assert.assertArrayEquals(new double[] { 17.592 }, parsedData.features().toArray(), 0);
	}

}
