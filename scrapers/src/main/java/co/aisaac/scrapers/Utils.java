package co.aisaac.scrapers;

import java.util.Random;

public class Utils {
	public static void sleepRandom(int maxSeconds) {
		sleep(new Random().nextInt(maxSeconds) + 1);
	}

	public static void sleep(long seconds) {
		try {
			System.out.println("Sleeping for " + seconds + " seconds");
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
