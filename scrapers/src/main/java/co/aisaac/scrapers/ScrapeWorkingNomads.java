package co.aisaac.scrapers;

import co.aisaac.webapp.Job;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ScrapeWorkingNomads {

    public static void main(String[] args) {
        new ScrapeWorkingNomads().run();
    }

    private void run() {
        WebDriver driver = new ChromeDriver();
        Database db = new Database();

        driver.manage().window().setPosition(new Point(100, 50));
        driver.manage().window().setSize(new Dimension(1400, 900));
        driver.get("https://www.workingnomads.com/remote-development-jobs");

        Utils.sleepRandom(1);

        // We have to click a "load more jobs" button on the bottom of the page over and over again, it's a single page app.

        System.out.println("scraping main page");
        WebElement showMore = driver.findElement(By.cssSelector("div.show-more"));

        while (!showMore.getAttribute("class").endsWith("ng-hide")) {
            showMore.click();
            Utils.sleepRandom(6);
        }

        // After all is loaded, find all the possible job links on the page.
        // Somehow this is finding 4000+ job postings, but there's not that man, so lots of duplication
        List<WebElement> linkElements = driver.findElements(By.cssSelector("a[href^='/jobs/']"));
        List<String> links = linkElements.stream().map(link -> link.getAttribute("href")).collect(Collectors.toList());

        System.out.println("Going to scrape " + links.size() + " hrefs.");
        // todo want to deduplicate these links
        // Scrape each individual page
        int i = 0;
        for (String link : links) {
            System.out.println("Scraping: " + i++ + " - " + link);
            if (db.hrefExists(link)) {
                System.out.println("Link already exists");
                continue;
            }

            Utils.sleepRandom(6);

            try {
                driver.get(link);

                WebElement jobElement = driver.findElement(By.cssSelector("div.job"));

                Job job = new Job();
                job.description = Objects.requireNonNullElse(jobElement.getAttribute("innerHTML"), "Undefined");

                WebElement element = jobElement.findElement(By.cssSelector("h1.job-title"));
                job.title = Objects.requireNonNullElse(element.getText(), "Undefined");

                element = jobElement.findElement(By.cssSelector("div.job-company"));
                job.company = Objects.requireNonNullElse(element.getText(), "Undefined");

                job.url = link;
                job.status = "new";
                job.job_site = "working_nomads";
                job.job_posting_date = LocalDate.now();

                db.storeJob(job);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        driver.quit();
    }


}
