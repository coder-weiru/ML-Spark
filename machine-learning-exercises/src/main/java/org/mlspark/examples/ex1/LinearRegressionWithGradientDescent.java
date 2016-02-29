package org.mlspark.examples.ex1;

import java.io.Serializable;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;
import org.mlspark.examples.Config;

import scala.Tuple2;

public class LinearRegressionWithGradientDescent implements Serializable {

	private static final long serialVersionUID = -5634788839704971874L;

	private static LinearRegressionWithGradientDescent instance = new LinearRegressionWithGradientDescent();

	private LinearRegressionModel model;

	private static final String DATA_FILE = "/ex1/ex1data1.txt";

	final Function<String, LabeledPoint> DATA_EXTRACTOR = new Function<String, LabeledPoint>() {
		private static final long serialVersionUID = 1L;

		public LabeledPoint call(String line) {
			String[] parts = line.split(",");
			String[] features = parts[1].split(" ");
			double[] v = new double[features.length];
			for (int i = 0; i < features.length - 1; i++)
				v[i] = Double.parseDouble(features[i]);
			return new LabeledPoint(Double.parseDouble(parts[0]), Vectors.dense(v));
		}

	};

	final Function<LabeledPoint, Tuple2<Double, Double>> EVAL_TRAINING_DATA = new Function<LabeledPoint, Tuple2<Double, Double>>() {
		private static final long serialVersionUID = 1L;

		public Tuple2<Double, Double> call(LabeledPoint point) {
			double prediction = model.predict(point.features());
			return new Tuple2<Double, Double>(prediction, point.label());
		}
	};

	final Function<Tuple2<Double, Double>, Object> EVAL_TRAINING_ERR = new Function<Tuple2<Double, Double>, Object>() {
		private static final long serialVersionUID = 1L;

		public Object call(Tuple2<Double, Double> pair) {
			return Math.pow(pair._1() - pair._2(), 2.0);
		}
	};

	public void compute() {

		SparkConf conf = new SparkConf().setAppName(DataPlot.class.getName()).setMaster("local");
		JavaSparkContext context = new JavaSparkContext(conf);

		String path = Config.getInstance().getDataPath() + DATA_FILE;
		System.out.println("datafile absolute path: " + path);
		JavaRDD<String> data = context.textFile(path);
		JavaRDD<LabeledPoint> parsedData = data.map(DATA_EXTRACTOR);
		parsedData.cache();

		// Building the model
		int numIterations = 100;
		model = LinearRegressionWithSGD.train(JavaRDD.toRDD(parsedData), numIterations);

		// Evaluate model on training examples and compute training error
		JavaRDD<Tuple2<Double, Double>> valuesAndPreds = parsedData.map(EVAL_TRAINING_DATA);
		double MSE = new JavaDoubleRDD(valuesAndPreds.map(EVAL_TRAINING_ERR).rdd()).mean();
		System.out.println("training Mean Squared Error = " + MSE);

		// Save model
		// model.save(context.sc(),
		// LinearRegressionWithGradientDescent.class.getName());
		context.close();

	}

	public static LinearRegressionWithGradientDescent getInstance() {
		return instance;
	}

	public static void main(String[] args) {
		getInstance().compute();
	}

}
