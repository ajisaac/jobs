package co.aisaac.scrapers;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ScrapeWorkingNomads {

    WebDriver driver = new ChromeDriver();
    Database db = new Database();

    public void scrapeJob(String href) {
        try {
            System.out.println("scraping " + href);
            int sleep = (int) (Math.random() * 6) + 1;
            TimeUnit.SECONDS.sleep(sleep);
            driver.get(href);

            WebElement jobElement = driver.findElement(By.cssSelector("div.job"));
            String title = jobElement.findElement(By.cssSelector("h1.job-title")).getText();
            String company = jobElement.findElement(By.cssSelector("div.job-company")).getText();
            String description = jobElement.getAttribute("innerHTML");

//            db.storeJob(title, company, href, "", description, "Working Nomads", "new", "", "working_nomads", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),"");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void scrape() {
        System.out.println("scraping main page");
        WebElement showMore = driver.findElement(By.cssSelector("div.show-more"));

        while (!showMore.getAttribute("class").endsWith("ng-hide")) {
            showMore.click();
            try {
                TimeUnit.SECONDS.sleep((int) (Math.random() * 6) + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<WebElement> linkElements = driver.findElements(By.cssSelector("a[href^='/jobs/']"));
        List<String> links = linkElements.stream().map(link -> link.getAttribute("href")).collect(Collectors.toList());

        for (String link : links) {
            if (!db.hrefExists(link)) {
                scrapeJob(link);
            }
        }
    }

    public void run() {
        driver.manage().window().setPosition(new Point(100, 50));
        driver.manage().window().setSize(new Dimension(1400, 900));
        driver.get("https://www.workingnomads.com/remote-development-jobs");

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        scrape();

        driver.quit();
    }
}
