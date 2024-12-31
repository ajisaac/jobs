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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScrapeSkipTheDrive {
    private static final String url = "https://www.skipthedrive.com/";
    private final WebDriver driver;
    private final Database db;

    public static void main(String[] args) {
        new ScrapeSkipTheDrive().run();
    }

    private ScrapeSkipTheDrive() {
        driver = new ChromeDriver();
        db = new Database();
    }

    private void run() {
        driver.manage().window().setPosition(new Point(100, 50));
        driver.manage().window().setSize(new Dimension(1400, 900));
        driver.get(url);

        Utils.sleepRandom(5);

        scrape();

        driver.quit();
    }


    private void scrape() {
        // todo, copilot wrote this, it is odd
        int start = 1;
        Integer end = null;
        String url = prepareUrl(start);
        while (true) {
            try {
                System.out.println("Scraping main page: " + url);
                driver.get(url);

                if (end == null) {
                    WebElement lastp = driver.findElement(By.cssSelector("a.lastp"));
                    if (lastp != null) {
                        String href = lastp.getAttribute("href");
                        if (href != null) {
                            href = href.replace("https://www.skipthedrive.com/job-category/remote-software-development-jobs/page/", "");
                            href = href.replace("/", "");
                            end = Integer.parseInt(href);
                        }
                    }
                }

                List<WebElement> results = driver.findElements(By.cssSelector(".post-content"));
                List<Map<String, String>> hrefs = parseListingPage(results);
                System.out.println("Found " + hrefs.size() + " URLs");

                for (Map<String, String> href : hrefs) {
                    scrapeJob(href);
                }

                if (start < end) {
                    start += 1;
                    url = prepareUrl(start);
                } else {
                    System.out.println("Done scraping");
                    break;
                }

            } catch (Exception e) {
                start += 1;
                url = prepareUrl(start);
            }

            Utils.sleepRandom(6);
        }
    }

    private String prepareUrl(int start) {
        String base = "https://www.skipthedrive.com/job-category/remote-software-development-jobs/";
        if (start <= 1) {
            return base;
        } else {
            return base + "page/" + start;
        }
    }

    private List<Map<String, String>> parseListingPage(List<WebElement> results) {
        List<Map<String, String>> jobs = new ArrayList<>();
        for (WebElement result : results) {
            String href = result.findElement(By.tagName("a")).getAttribute("href");
            Map<String, String> job = new HashMap<>();
            job.put("href", href);
            jobs.add(job);
        }
        return jobs;
    }

    private void scrapeJob(Map<String, String> url) {
        String href = url.get("href");
        if (href == null) {
            return;
        }
        if (!db.hrefExists(href)) {
            Job job = parseDescriptionPage(href);
            if (job != null) {
                System.out.println("Saving job - " + job.title);
                saveJob(job);
            }
        }
    }

    private Job parseDescriptionPage(String href) {
        Utils.sleepRandom(6);

        Job job = new Job();
        System.out.println("Scraping description page: " + href);
        driver.get(href);

        job.url = href;

        WebElement jobTitleElement = driver.findElement(By.className("post-title"));
        job.title = jobTitleElement != null ? jobTitleElement.getText().trim() : "undefined";

        WebElement compLinkElement = driver.findElement(By.className("custom_fields_company_name_display"));
        job.company = compLinkElement != null ? compLinkElement.getText().trim() : "unknown";

        WebElement jdElement = driver.findElement(By.className("entry-content"));
        job.description = jdElement != null ? jdElement.getAttribute("innerHTML") : "No Description Found";

        return job;
    }


    private void saveJob(Job job) {
        job.status = "new";
        job.job_site = "skip-the-drive";
        job.job_posting_date = LocalDate.now();

        db.storeJob(job);

    }

}