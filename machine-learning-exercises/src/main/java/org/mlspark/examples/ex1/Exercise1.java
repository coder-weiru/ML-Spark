package org.mlspark.examples.ex1;

import org.mlspark.examples.Config;

public class Exercise1 {

	private static Config config = Config.getInstance();
	private static final String DATA_FILE = config.getDataPath() + "/ex1/ex1data1.txt";

	public static void main(String[] args) {
		
		LinearRegressionGradientDescentModel model = new LinearRegressionGradientDescentModel();

		System.out.println("datafile absolute path: " + DATA_FILE);

		model.train(DATA_FILE, 1500);

		System.out.println(String.format("[%1$s]Training Data Mean Squared Error = %2$d", Exercise1.class.getName(),
				model.computeMeanSquaredError()));
		
		System.out.println(String.format("[%1$s]For population = 35,000, we predict a profit of %d\n",
				Exercise1.class.getName(), model.predict(3.5) * 10000));
		
		System.out.println(String.format("[%1$s]For population = 35,000, we predict a profit of %d\n",
				Exercise1.class.getName(), model.predict(7.0) * 10000));
	}


}
