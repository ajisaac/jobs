package co.aisaac.scrapers;

import co.aisaac.webapp.Job;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ScrapeWeWorkRemotely {

	WebDriver driver = new ChromeDriver();
	Database db = new Database();

    public static void main(String[] args) {
        var scraper = new ScrapeWeWorkRemotely();
        scraper.run();
    }

    public void run() {
        scrape("https://weworkremotely.com/categories/remote-full-stack-programming-jobs");
        scrape("https://weworkremotely.com/categories/remote-back-end-programming-jobs");
        driver.quit();
    }

	public void scrape(String url) {
		driver.manage().window().setPosition(new Point(100, 50));
		driver.manage().window().setSize(new Dimension(1400, 900));
		driver.get(url);

		List<WebElement> lis = driver.findElements(By.cssSelector("section.jobs>article>ul>li"));
		List<String> hrefs = new ArrayList<>();
		for (WebElement li : lis) {
			if (li.getAttribute("id").contains("one-signal-subscription-form")) {
				System.out.println("found subscription form");
				continue;
			}

			if (li.findElements(By.cssSelector("a.view-all")).size() > 0) {
				System.out.println("last li is view all jobs button");
				continue;
			}

			List<WebElement> hrefElements = li.findElements(By.cssSelector("a"));
			if (hrefElements.size() < 2) {
				System.out.println("failed to find job link a element");
				continue;
			}

			String href = hrefElements.get(1).getAttribute("href");
			if (href == null) {
				System.out.println("failed to find job link href");
				continue;
			}

			if (!href.startsWith("https://weworkremotely.com/remote-jobs/")) {
				System.out.println("href " + href + " does not start with https://weworkremotely.com/remote-jobs/");
				continue;
			}

			if (db.hrefExists(href)) {
				System.out.println("href " + href + " already exists");
				continue;
			}

			System.out.println("valid href: " + href);
			hrefs.add(href);
		}

		for (String href : hrefs) {
			try {
				scrapeJob(href);
			} catch (Exception e) {
				System.out.println(e);
				continue;
			}
		}

		System.out.println("finished");
	}


    public void scrapeJob(String href) {
        Utils.sleepRandom(5);

        driver.get(href);

        Job job = new Job();

        WebElement titleElement = driver.findElement(By.cssSelector("div.listing-header-container h1"));
        job.title = titleElement != null ? titleElement.getText().trim() : "undefined";

        WebElement companyElement = driver.findElement(By.cssSelector("div.company-card>h2>a"));
        job.company = companyElement != null ? companyElement.getText().trim() : "unknown";

        WebElement descriptionElement = driver.findElement(By.cssSelector("div.listing-container"));
        job.description = descriptionElement != null ? descriptionElement.getAttribute("innerHTML") : "No Description Found";

        job.job_site = "we_work_remotely";
        job.status = "new";
        job.url = href;
        job.job_posting_date = LocalDate.now();

        db.storeJob(job);
    }

}