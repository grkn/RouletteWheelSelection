package grkn.generic.algorithm.selection;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

public class RouletteWheelSelection {

	private static class Population {
		private String population;
		private double fitness;

		public Population(String population) {
			this.population = population;
		}

		public String getPopulation() {
			return population;
		}

		public void setPopulation(String population) {
			this.population = population;
		}

		@Override
		public String toString() {
			return "Population : " + population + " -- Fitness : " + fitness;
		}

		public double getFitness() {
			return fitness;
		}

		public void setFitness(double fitness) {
			this.fitness = fitness;
		}
	}

	public static String[] selection(List<Population> populations) {
		populations.parallelStream().forEach(str -> str.setFitness(fitness(str.getPopulation().toCharArray())));
		int randomValue = createRandomInteger();
		List<String> list = populations.parallelStream()
				.filter(population -> ((double) randomValue / 1000) < population.getFitness())
				.map(item -> item.getPopulation()).collect(Collectors.toList());

		return list.toArray(new String[list.size()]);
	}

	private static int createRandomInteger() {
		Random random = new Random();
		int randomValue = random.nextInt(100);
		return randomValue;
	}

	private static int calculateDecimal(char[] binaryArrayAsChar) {
		int pow = 0;
		int decimalValue = 0;
		for (int i = binaryArrayAsChar.length - 1; i >= 0; i--) {
			int value = binaryArrayAsChar[i] - '0';
			decimalValue += (value * Math.pow(2, pow));
			pow++;
		}
		return decimalValue;
	}

	private static double fitness(char[] gen) {
		Double fitness = 0.0;
		for (int i = gen.length - 1; i >= 0; i--) {
			fitness += ((double) gen[i] - '0') / calculateDecimal(gen);
		}
		return calculateDecimalFormat(fitness);
	}

	private static double calculateDecimalFormat(Double fitness) {
		DecimalFormat d = new DecimalFormat("000.####");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("tr"));
		symbols.setDecimalSeparator('.');
		symbols.setGroupingSeparator(',');
		d.setDecimalFormatSymbols(symbols);

		return Double.valueOf(d.format(fitness));
	}

	public static void main(String[] args) {
		List<Population> list = new ArrayList<>();

		System.out.println("INITIALIZE BEGINS");
		int maxValue = 5000000;
		for (int i = 0; i < maxValue; i++) {
			list.add(new Population(Integer.toBinaryString(i)));
		}
		System.out.println("INITIALIZE ENDS");
		long begin = System.currentTimeMillis();
		System.out.println("SELECTION BEGINS " + new Date(begin));
		selection(list);
		long end = System.currentTimeMillis();
		System.out.println("SELECTION ENDS " + new Date(end));
	}
}
