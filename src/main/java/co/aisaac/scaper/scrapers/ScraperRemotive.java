package co.aisaac.scaper.scrapers;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScraperRemotive {

	private final WebDriver driver;
	private final Database db;

	private static final String url = "https://remotive.com/remote-jobs/software-dev";

	public static void main(String[] args) {
		new ScraperRemotive().run();
	}

	ScraperRemotive() {
		driver = new ChromeDriver();
		db = new Database();
	}

	public void run() {
		driver.manage().window().setPosition(new Point(100, 50));
		driver.manage().window().setSize(new Dimension(1400, 900));
		driver.get(url);

		Utils.sleepRandom(5);

		scrape();

		driver.quit();
	}

	public void scrape() {
		System.out.println("Scraping main page");

		List<WebElement> elements = driver.findElements(By.cssSelector("a[href^='/remote-jobs/software-dev/']"));
		Map<String, Job> jobs = new HashMap<>();


		for (WebElement element : elements) {
			try {
				String href = element.getAttribute("href");
				String[] titleAndCompany = element.getText().split("\n");
				if (titleAndCompany.length == 0) {
					continue;
				}
				String title = titleAndCompany[0];
				String company = titleAndCompany[2];

				Job job = new Job();
				job.href = href;
				job.title = title;
				job.company = company;

				jobs.put(href, job);

			} catch (Exception e) {
				System.out.println(e.getMessage());

			}
		}


		for (Job job : jobs.values()) {

			if (db.hrefExists(job.href)) {
				System.out.println(job.href + " already exists");
				continue;
			}
			System.out.println("Scraping " + job.href);

			try {
				scrapeJob(job);
			} catch (Exception e) {
				System.out.println("Failed to scrape job " + job.href);
				System.out.println(e.getMessage());
			}

		}
	}

	public void scrapeJob(Job job) {
		driver.get(job.href);
		WebElement jobElement = driver.findElement(By.cssSelector("div.left>div"));
		String description = jobElement.getAttribute("innerHTML");

		job.jobSite = "remotive";
		job.status = "new";
		job.description = description;
		job.jobPostingDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		db.storeJob(job);
	}


}