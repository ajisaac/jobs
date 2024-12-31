package co.aisaac.scrapers;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Utils {
	public static void sleepRandom(int maxSeconds) {
		sleep(new Random().nextInt(maxSeconds) + 1);
	}

	public static void sleep(long seconds) {
		try {
			System.out.println("Sleeping for " + seconds + " seconds");
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
