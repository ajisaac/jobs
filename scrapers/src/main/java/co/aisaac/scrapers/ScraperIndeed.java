package co.aisaac.scrapers;

import co.aisaac.webapp.Job;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScraperIndeed {
    private WebDriver driver;
    private final Database db;

    private final String term;
    private final String location;

    public static void main(String[] args) {
        new ScraperIndeed("Java", "Remote").run();
    }

    private void resetDriver() {
        if (driver != null) {
            driver.quit();
        }
        driver = new ChromeDriver();
        driver.manage().window().setPosition(new Point(100, 50));
        driver.manage().window().setSize(new Dimension(1400, 900));
    }

    public ScraperIndeed(String term, String location) {
        db = new Database();
        this.term = term;
        this.location = location;
    }

    public void run() {
        scrape();
        driver.quit();
    }


    private void scrape() {
        int start = 0;
        String url = prepareUrl(start);

        // if we hit 1000, we have done enough, we can stop
        while (start < 1000) {
            try {

                resetDriver();

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
                boolean hasMorePages = driver.findElements(By.cssSelector(".show_omitted_jobs")).isEmpty();

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

        resetDriver();

        System.out.println("Scraping description page: " + href);
        driver.get(href);


        // The driver will throw exception if it can't find the element, but we don't mind.

        // Parse title
        Job job = new Job();
        job.title = null;
        try {
            job.title = driver.findElement(By.cssSelector("h1.jobsearch-JobInfoHeader-title>span")).getText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (job.title == null) {
            try {
                job.title = driver.findElement(By.cssSelector("h2[data-testid=simpler-jobTitle]")).getText();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Parse company
        job.company = null;
        try {
            job.company = driver.findElement(By.cssSelector("div[data-company-name]>span>a")).getText();
        } catch (Exception e) {
            System.out.println("Company not found.");
        }

        if (job.company == null) {
            try {
                job.company = driver.findElement(By.className("jobsearch-JobInfoHeader-companyNameSimple")).getText();
            } catch (Exception e) {
                System.out.println("Company not found.");
            }
        }

        if (job.company == null) {
            try {
                job.company = driver.findElement(By.className("jobsearch-JobInfoHeader-companyNameLink")).getText();
            } catch (Exception e) {
                System.out.println("Company not found.");
            }
        }

        if (job.company == null) {
            System.out.println("");
        }

        // Parse description
        job.description = "No Description Found";
        try {
            job.description = driver.findElement(By.cssSelector("#jobDescriptionText")).getAttribute("innerHTML");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (job.title == null) {
            job.title = "undefined";
        }
        job.url = href;
        job.status = "new";
        job.job_site = "indeed";
        job.job_posting_date = LocalDate.now();

        // Save job
        db.storeJob(job);
    }

    /**
     * Gets the next url in the paginated series.
     */
    private String prepareUrl(int start) {
        Map<String, String> params = new HashMap<>();
        params.put("q", term);
        params.put("l", location);
        params.put("start", Integer.toString(start));

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