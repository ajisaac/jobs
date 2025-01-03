package co.aisaac.scrapers;

import co.aisaac.webapp.Job;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScraperRemotive {

    private WebDriver driver;
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

        List<WebElement> elements = driver.findElements(By.cssSelector("a[href*='/remote-jobs/software-dev/']"));
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
                job.setUrl(href);
                job.setTitle(title);
                job.setCompany(company);

                jobs.put(href, job);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }


        int i = 0, j = 0;
        for (Job job : jobs.values()) {
            System.out.println("\nScraping attempt: " + i++ + " - Actual: " + j + " - " + job.url);

            if (db.hrefExists(job.url)) {
                System.out.println(job.url + " already exists");
                continue;
            }

            boolean success = scrapeJob(job);
            if (success) {
                j++;
            }
        }
    }

    public boolean scrapeJob(Job job) {
        try {
            System.out.println("Scraping " + job.url);
            resetDriver();
            driver.get(job.url);
            Utils.sleepRandom(6);

            WebElement jobElement = driver.findElement(By.cssSelector("div.left>div"));
            String description = jobElement.getAttribute("innerHTML");

            job.job_site = "remotive";
            job.status = "new";
            job.description = description;
            job.job_posting_date = LocalDate.now();

            return db.storeJob(job);

        } catch (Exception e) {
            System.out.println("Failed to scrape job " + job.url);
            System.out.println(e.getMessage());
            return false;

        }

    }

    private void resetDriver() {
        driver.quit();
        driver = new ChromeDriver();
        driver.manage().window().setPosition(new Point(100, 50));
        driver.manage().window().setSize(new Dimension(1400, 900));
    }


}