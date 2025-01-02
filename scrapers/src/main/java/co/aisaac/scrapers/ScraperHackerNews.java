package co.aisaac.scrapers;

import co.aisaac.webapp.Job;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.List;

public class ScraperHackerNews {

    public static void main(String[] args) {
        new ScraperHackerNews().run();
    }

    public void run() {
        WebDriver driver = new ChromeDriver();
        try {
            String url = "https://hnhiring.com/january-2025";
            driver.manage().window().setPosition(new Point(100, 50));
            driver.manage().window().setSize(new Dimension(1400, 900));
            driver.get(url);

            Utils.sleepRandom(5);

            driver.findElement(By.cssSelector("a[data-show-all]")).click();

            System.out.println("Scraping main page");
            List<WebElement> jobElements = driver.findElements(By.cssSelector("li.job>.container>.body"));
            System.out.println("Found " + jobElements.size() + " jobElements");

            Database db = new Database();

            for (WebElement element : jobElements) {
                try {
                    Job job = new Job();
                    job.title = element.getText().split("\n")[0];
                    job.description = element.getAttribute("innerHTML");
                    job.url = hashMD5(job.title);
                    job.status = "new";
                    job.job_site = "hacker_news";
                    job.job_posting_date = LocalDate.now();

                    job.company = "";

                    db.storeJob(job);

                } catch (Exception e) {
                    System.out.println(e.getMessage());

                }
            }
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
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