package co.aisaac.scaper.scrapers;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScraperIndeed {
	private final WebDriver driver;
	private final Database db;

	private final String term;
	private final String location;

	public static void main(String[] args) {
		new ScraperIndeed("Java", "Remote").run();
	}

	public ScraperIndeed(String term, String location) {
		driver = new ChromeDriver();
		db = new Database();
		this.term = term;
		this.location = location;
	}

	public void run() {
		driver.manage().window().setPosition(new Point(100, 50));
		driver.manage().window().setSize(new Dimension(1400, 900));
		driver.get("https://www.indeed.com");

		Utils.sleep(1);
		scrape();

		driver.quit();
	}


	private void scrape() {
		int start = 0;
		String url = prepareUrl(start);

		// if we hit 1000, we have done enough, we can stop
		while (start < 1000) {
			try {
				System.out.println("Scraping main page: " + url);
				driver.get(url);

				// Find all the links on the main page
				List<String> links = new ArrayList<>();
				List<WebElement> hrefs = driver.findElements(By.cssSelector("h2.jobTitle>a.jcs-JobTitle"));
				for (WebElement hrefElement : hrefs) {
					String href = hrefElement.getAttribute("href");
					if (href != null && !db.hrefExists(href)) {
						links.add(href);
					}
				}
				System.out.println("Found " + hrefs.size() + " URLs");

				// Check if there are more pages
				boolean hasMorePages = driver.findElements(By.cssSelector(".show_omitted_jobs")).size() > 0;

				// Parse all the job pages
				for (String href : links) {
					if (href != null && !db.hrefExists(href)) {
						parseDescriptionPage(href);
					}
				}

				// Find the next index to scrape
				if (hasMorePages) {
					start += 10;
					url = prepareUrl(start);
				} else {
					System.out.println("Done scraping");
					break;
				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
				start += 10;
				url = prepareUrl(start);
			}

			Utils.sleepRandom(6);
		}
	}

	/**
	 * This is a single job description page.
	 */
	private void parseDescriptionPage(String href) {

		Utils.sleepRandom(6);

		System.out.println("Scraping description page: " + href);
		driver.get(href);

		// Hash the href to use as a filename
		String md5Hex = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(href.getBytes());
			BigInteger no = new BigInteger(1, messageDigest);
			md5Hex = no.toString(16);
			while (md5Hex.length() < 32) {
				md5Hex = "0" + md5Hex;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		// Save text
		String htmlPath = "/Users/aaron/Code/scraper-backend/data/" + md5Hex + ".html";
		try {
			Files.write(Paths.get(htmlPath), driver.getPageSource().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Parse title
		String title = "undefined";
		try {
			title = driver.findElement(By.cssSelector("h1.jobsearch-JobInfoHeader-title>span")).getText();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// Parse company
		String company = "";
		try {
			company = driver.findElement(By.cssSelector("div[data-company-name]>span>a")).getText();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// Parse description
		String jd = "No Description Found";
		try {
			jd = driver.findElement(By.cssSelector("#jobDescriptionText")).getAttribute("innerHTML");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		// Save job
		System.out.println("Saving job - " + title + " - " + company + " - " + href);
		db.storeJob(title, company, href, "", jd, "Java - Remote", "new", "", "indeed", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), htmlPath);
	}

	/**
	 * Gets the next url in the paginated series.
	 */
	private String prepareUrl(int start) {
		Map<String, String> params = new HashMap<>();
		params.put("q", term);
		params.put("l", location);
		params.put("start", Integer.toString(start));

		// todo change this to proper url builder
		StringBuilder url = new StringBuilder("https://www.indeed.com/jobs?");
		for (Map.Entry<String, String> entry : params.entrySet()) {
			if (!url.isEmpty()) {
				url.append('&');
			}
			url.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
			url.append('=');
			url.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
		}

		return url.toString();
	}


}