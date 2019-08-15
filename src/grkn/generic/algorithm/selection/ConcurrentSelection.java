package grkn.generic.algorithm.selection;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import grkn.generic.algorithm.selection.RouletteWheelSelection.Population;

public class ConcurrentSelection {

	public static Integer MOD = 100000;
	public static int MAX_VALUE = 5000000;
	public static ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 100, 0L, TimeUnit.MILLISECONDS,
			new SynchronousQueue<>());

	public static void main(String[] args) {

		List<Future<String[]>> results = new ArrayList<>();

		long begin = System.currentTimeMillis();
		System.out.println("SELECTION BEGINS " + new Date(begin));

		List<Population> list = initializeList();
		for (int i = 0; i < MAX_VALUE; i++) {

			list.add(new Population(Integer.toBinaryString(i)));

			if (firstModNotIncluded(i) && isModEqualToZero(i)) {
				submitAndAddToResultListOfFuture(results, list);
				list = initializeList();
			} else if (firstModNotIncluded(i) && isLatestElement(MAX_VALUE, i)) {
				submitAndAddToResultListOfFuture(results, list);
				list = initializeList();
			}
		}

		getResultsFromCallable(results);
		executor.shutdown();
		long end = System.currentTimeMillis();
		System.out.println("SELECTION ENDS " + new Date(end));
	}

	private static void submitAndAddToResultListOfFuture(List<Future<String[]>> results, List<Population> list) {
		Future<String[]> future = executor.submit(RouletteWheelSelection.selectionConcurrent(list));
		results.add(future);
	}

	private static List<Population> initializeList() {
		List<Population> list;
		list = new LinkedList<>();
		return list;
	}

	private static void getResultsFromCallable(List<Future<String[]>> results) {
		for (Future<String[]> future : results) {
			try {
				String[] arr = future.get();
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static boolean isLatestElement(int maxValue, int i) {
		return i == maxValue - 1;
	}

	private static boolean isModEqualToZero(int i) {
		return i % MOD == 0;
	}

	private static boolean firstModNotIncluded(int i) {
		return i != 0;
	}

}
