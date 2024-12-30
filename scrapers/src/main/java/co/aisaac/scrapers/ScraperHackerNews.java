package co.aisaac.scrapers;

import co.aisaac.webapp.Job;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

public class ScraperHackerNews {
    private final WebDriver driver;
    private final Database db;

    private final String url;

    public static void main(String[] args) {
        String url = "https://hnhiring.com/december-2024";
        new ScraperHackerNews(url).run();
    }

    public ScraperHackerNews(String url) {
        driver = new ChromeDriver();
        db = new Database();
        this.url = url;
    }


    public void run() {
        driver.manage().window().setPosition(new Point(100, 50));
        driver.manage().window().setSize(new Dimension(1400, 900));
        driver.get(url);

        Utils.sleepRandom(5);

        driver.findElement(By.cssSelector("a[data-show-all]")).click();

        // save text to data folder
        String md5Hex = hashMD5(url);

        System.out.println("Scraping main page");
        List<WebElement> jobElements = driver.findElements(By.cssSelector("li.job>.container>.body"));
        System.out.println("Found " + jobElements.size() + " jobElements");

        for (WebElement element : jobElements) {
            try {
                Job job = new Job();
                job.title = element.getText().split("\n")[0];
                job.description = element.getAttribute("innerHTML");
                job.url = hashMD5(job.title);
                job.status = "new";
                job.job_site = "hacker_news";
                job.job_posting_date = LocalDateTime.now();

                job.company = "";
                job.searchTerm = "";
                job.location = "";
                job.subtitle = "";

                if (!db.hrefExists(md5Hex)) {
                    db.storeJob(job);
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());

            }
        }

        driver.quit();
    }

    public static String hashMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            return new BigInteger(messageDigest).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}