package co.aisaac.scrapers;

import co.aisaac.webapp.Job;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScrapeAngelCo {

    private static class Company {
        String name;
        String href;
        String description;
        List<JobPosting> jobPostings = new ArrayList<>();

        public String asHtml() {
            StringBuilder b = new StringBuilder();
            b.append("<div><h2><a href=\"" + href + "\" rel=\"noopener noreferrer\" target=\"_blank\">" + name + "</a></h2></div>");
//            b.append("<div>" + description + "</div>");
            return b.toString();
        }
    }

    private static class JobPosting {
        String title;
        String href;
        String description;
        String companyDescription;

        public String asHtml() {
            StringBuilder b = new StringBuilder();

            b.append("<div><h4><a href=\"" + href + "\" rel=\"noopener noreferrer\" target=\"_blank\">" + title + "</a></h4></div>");
            b.append("<div>" + description + "</div>");
            b.append("<div>" + companyDescription + "</div>");
            return b.toString();
        }
    }

    public static void main(String[] args) {
        ScrapeAngelCo scrapeAngelCo = new ScrapeAngelCo();
        scrapeAngelCo.run();
    }

    public void run() {

        File input = new File("./scrapers/data/angel_co.html");
        try {
            Document document = Jsoup.parse(input, "UTF-8", "https://angel.co/");

            String file = "./scrapers/data/new.html";
            Path path = Path.of(file);
            if (Files.exists(path)) {
                Files.delete(path);
            }

            // remove SVG
            Elements elements = document.selectXpath("//svg");
            for (Element svg : elements) {
                svg.remove();
            }

            // remove images
            elements = document.selectXpath("//img");
            for (Element img : elements) {
                img.remove();
            }

            // remove buttons
            Elements buttons = document.getElementsByAttributeValue("data-test", "Button");
            for (Element button : buttons) {
                button.remove();
            }
            buttons = document.getElementsByAttributeValue("data-test", "LearnMoreButton");
            for (Element button : buttons) {
                button.remove();
            }

            // remove promoted startups
            Elements startupEntries = document.getElementsByAttributeValue("data-test", "StartupResult");
            int promotedCount = 0;
            for (Element startup : startupEntries) {
                if (is_promoted(startup)) {
                    startup.remove();
                    promotedCount++;
                    continue;
                }

                // angel.co will put 4 promoted posts in a row. we can skip the promoted posts for now.
                // may later decide to grab these, but I suspect these companies have postings elsewhere in the page.
                // initial testing shows that this pattern holds for 1600+ entries.
                if (promotedCount > 0 && promotedCount != 4) {
                    System.out.println("Was expecting 4 promoted companies, but found " + promotedCount + ", check the css selectors in code.");
                }
                promotedCount = 0;
            }

            startupEntries = document.getElementsByAttributeValue("data-test", "StartupResult");

            List<Company> companies = new ArrayList<>();
            for (Element startup : startupEntries) {
                Company company = new Company();
                company.name = find_company_name(startup);
                company.href = find_company_href(startup);
                company.description = find_company_description(startup);

                List<JobPosting> jobs = find_jobs(startup, company);
                jobs.forEach(jobPosting -> jobPosting.companyDescription = company.description);
                company.jobPostings.addAll(jobs);
                companies.add(company);
            }

//            print_companies_as_html(file, companies);
//            print_startup_entries_as_html(file, startupEntries);

            save_jobs_to_database(companies);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void save_jobs_to_database(List<Company> companies) {
        Database database = new Database();
        for (Company company : companies) {
            for(JobPosting jobPosting : company.jobPostings){
                Job job = new Job();
                job.company = company.name;
                job.title = jobPosting.title;
                job.url = jobPosting.href;
                job.status = "new";
                job.description = "<div>" + jobPosting.description + "</div>" + "<div>" + company.description + "</div>";
                job.job_site = "angelco";
                job.job_posting_date = LocalDate.now();
                database.storeJob(job);
            }
        }
    }

    private List<JobPosting> find_jobs(Element startup, Company company) {
        Elements jobListElements = Selector.select("div[class*=styles_jobListingList_]", startup);
        if (jobListElements.isEmpty()) {
            System.out.println("Was expecting div[class*=styles_jobListingList_], but it doesn't exist.");
            return new ArrayList<>();
        }
        if (jobListElements.size() >= 2) {
            System.out.println("Was expecting div[class*=styles_jobListingList_] to match 1 element, but it matches more.");
        }
        Element jobList = jobListElements.getFirst();


        List<JobPosting> jobPostings = new ArrayList<>();
        Elements jobsHrefs = Selector.select("a[href*=/jobs/]", jobList);
        for (Element jobs : jobsHrefs) {
            Element parent = jobs.parent();
            if (parent == null) {
                continue;
            }

            JobPosting jobPosting = new JobPosting();

            Element titleElement = Selector.selectFirst("span[class*=styles_title__]", parent);
            if (titleElement == null) {
                System.out.println("Expecting div[class*=styles_title__] to be size 1.");
            } else {
                jobPosting.title = titleElement.ownText();
            }

            Element infoElement = Selector.selectFirst("div[class*=styles_info__]", parent);
            if (infoElement == null) {
                System.out.println("Expecting div[class*=styles_info__] to be size 1.");
            } else {
                jobPosting.description = infoElement.stream()
                        .map(Element::ownText)
                        .filter(s -> !s.isBlank())
                        .map(s -> "<div>" + s + "</div>")
                        .collect(Collectors.joining());

            }

            Element tagsElement = Selector.selectFirst("div[class*=styles_tags__]", parent);
            if (tagsElement == null) {
                System.out.println("Expecting div[class*=styles_tags__] to be size 1.");
            } else {
                jobPosting.description += "<ul>"
                        + tagsElement.stream()
                        .map(Element::ownText)
                        .filter(s -> !s.isBlank())
                        .map(s -> "<li>" + s + "</li>")
                        .collect(Collectors.joining())
                        + "</ul>";
            }


            Attribute href = jobs.attribute("href");
            if (href != null) {
                jobPosting.href = "https://wellfound.com" + href.getValue();
            }

            jobPostings.add(jobPosting);

        }

        return jobPostings;
    }

    private String find_company_description(Element startup) {
        // in the first child div with
        Elements headerContainers = Selector.select("div[class*=styles_headerContainer_]", startup);
        if (headerContainers.isEmpty()) {
            System.out.println("Was expecting div[class*=styles_headerContainer_], but it doesn't exist.");
            return "";
        }
        Element companyDiv = headerContainers.getFirst();
        String textElements = companyDiv.stream()
                .map(Element::ownText)
                .filter(s -> !s.isBlank())
                .map(s -> "<div>" + s + "</div>")
                .collect(Collectors.joining());

        // the second child div, may not exist some of the times
        Elements secondDiv = Selector.select("div[class=p-4 pt-0]", startup);
        if (secondDiv.isEmpty()) {
            System.out.println("Was expecting div[class=p-4 pt-0], but it doesn't exist.");
            return textElements;
        }
        Element div = secondDiv.getFirst();
        String secondTextElements = div.stream()
                .map(Element::ownText)
                .filter(s -> !s.isBlank())
                .map(s -> "<li>" + s + "</li>")
                .collect(Collectors.joining());

        return textElements + "<div><ul>" + secondTextElements + "</ul></div>";
    }

    private String find_company_href(Element startup) {
        Elements aElements = startup.select("a");
        for (Element a : aElements) {

            Elements h2s = a.select("h2");
            if (h2s.isEmpty()) {
                continue;
            }

            for (Element h2 : h2s) {
                if (h2.hasText()) {
                    Attribute href = a.attribute("href");
                    if (href == null) {
                        continue;
                    }
                    String url = href.getValue();
                    if (url.startsWith("/company/")) {
                        return "https://wellfound.com" + href.getValue();
                    }
                }
            }

        }
        return "";
    }

    private String find_company_name(Element startup) {
        Elements aElements = startup.select("a");
        for (Element a : aElements) {

            Elements h2s = a.select("h2");
            if (h2s.isEmpty()) {
                continue;
            }

            for (Element h2 : h2s) {
                if (h2.hasText()) {
                    Attribute href = a.attribute("href");
                    if (href == null) {
                        continue;
                    }
                    String url = href.getValue();
                    if (url.startsWith("/company/")) {
                        return h2.text();
                    }
                }
            }
        }
        return "";
    }

    private boolean is_promoted(Element startup) {
        Elements headerContainers = Selector.select("div[class*=styles_headerContainer_]", startup);

        if (headerContainers.size() >= 2) {
            Elements aElements = startup.select("a");

            for (Element a : aElements) {
                Elements promoted = a.getElementsContainingOwnText("promoted");
                if (!promoted.isEmpty()) {
                    return true;
                }
            }
        }

        return false;
    }

}
