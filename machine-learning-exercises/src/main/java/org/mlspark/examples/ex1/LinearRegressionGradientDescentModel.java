package org.mlspark.examples.ex1;

import java.io.Serializable;

import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;

import scala.Tuple2;

public class LinearRegressionGradientDescentModel implements Serializable {

	private static final long serialVersionUID = -2351862905095309501L;

	private LinearRegressionModel model;

	private JavaRDD<LabeledPoint> parsedData;

	public static final Function<String, LabeledPoint> LABELEDPOINT_DATA_EXTRACTOR = new Function<String, LabeledPoint>() {
		
		private static final long serialVersionUID = -1167483833573007220L;

		public LabeledPoint call(String line) {
			String[] parts = line.split(",");
			String[] features = parts[1].split(" ");
			double[] v = new double[features.length];
			for (int i = 0; i < features.length - 1; i++)
				v[i] = Double.parseDouble(features[i]);
			return new LabeledPoint(Double.parseDouble(parts[0]), Vectors.dense(v));
		}

	};

	public LinearRegressionGradientDescentModel() {
		super();
	}

	public void train(JavaSparkContext sparkContext, String datafile, int numIterations) {
		JavaRDD<String> data = sparkContext.textFile(datafile);
		parsedData = data.map(LABELEDPOINT_DATA_EXTRACTOR);
		parsedData.cache();

		model = LinearRegressionWithSGD.train(JavaRDD.toRDD(parsedData), numIterations);

	}

	public double computeMeanSquaredError() {
		// Evaluate model on training examples and compute training error
		JavaRDD<Tuple2<Double, Double>> valuesAndPreds = parsedData
				.map(new Function<LabeledPoint, Tuple2<Double, Double>>() {
					private static final long serialVersionUID = 1L;

					public Tuple2<Double, Double> call(LabeledPoint point) {
						double prediction = model.predict(point.features());
						return new Tuple2<Double, Double>(prediction, point.label());
					}
				});
		double MSE = new JavaDoubleRDD(valuesAndPreds.map(new Function<Tuple2<Double, Double>, Object>() {
			private static final long serialVersionUID = 1L;

			public Object call(Tuple2<Double, Double> pair) {
				return Math.pow(pair._1() - pair._2(), 2.0);
			}
		}).rdd()).mean();

		return MSE;
	}

	public double predict(double... features) {
		return model.predict(Vectors.dense(features));
	}

}
