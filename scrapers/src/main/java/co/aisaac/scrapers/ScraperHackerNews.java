package co.aisaac.scrapers;

import co.aisaac.webapp.Job;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ScraperHackerNews {
    private final WebDriver driver;
    private final Database db;

    private final String url;

    public static void main(String[] args) {
        String url = "https://hnhiring.com/january-2024";
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
        String htmlPath = savePage(md5Hex);

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
//				job.htmlPath = htmlPath;
                job.job_posting_date = LocalDateTime.now();

                if (!db.hrefExists(md5Hex)) {
                    db.storeJob(job);
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());

            }
        }

        driver.quit();
    }

    // Saves page text to file
    private String savePage(String md5Hex) {
        String htmlPath = "/Users/aaron/Code/scraper-backend/data/" + md5Hex + ".html";
        try {
            Files.write(Paths.get(htmlPath), driver.getPageSource().getBytes());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Unable to save page for hackernews.");
        }
        return htmlPath;
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