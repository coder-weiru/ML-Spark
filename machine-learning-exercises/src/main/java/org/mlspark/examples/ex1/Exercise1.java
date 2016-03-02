package org.mlspark.examples.ex1;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.mlspark.examples.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Exercise1 {

	private static Config config = Config.getInstance();
	private static final String DATA_FILE = config.getDataPath() + "/ex1/ex1data1.txt";

	private static final Logger LOGGER = LoggerFactory.getLogger(Exercise1.class);

	public static void main(String[] args) {
		
		SparkConf conf = new SparkConf().setAppName(Exercise1.class.getName()).setMaster("local");

		JavaSparkContext sparkContext = new JavaSparkContext(conf);

		LinearRegressionGradientDescentModel model = new LinearRegressionGradientDescentModel();

		LOGGER.info("datafile absolute path: " + DATA_FILE);

		model.train(sparkContext, DATA_FILE, 1500);

		LOGGER.info(String.format("[%1$s]Training Data Mean Squared Error = %2$f", Exercise1.class.getName(),
				model.computeMeanSquaredError()));
		
		LOGGER.info(String.format("[%1$s]For population = 35,000, we predict a profit of %2$f\n",
				Exercise1.class.getName(), model.predict(3.5d) * 10000));
		
		LOGGER.info(String.format("[%1$s]For population = 70,000, we predict a profit of %2$f\n",
				Exercise1.class.getName(), model.predict(7.0d) * 10000));

		sparkContext.close();
	}


}
