package co.aisaac.scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScrapeAngelCo {

    public static void main(String[] args) {
        ScrapeAngelCo scrapeAngelCo = new ScrapeAngelCo();
        scrapeAngelCo.run();
    }

    public void run() {

        File input = new File("./scrapers/data/angel_co.html");
        try {
            Document document = Jsoup.parse(input, "UTF-8", "https://angel.co/");

            String file = "./scrapers/data/new.html";
            if (Files.exists(Path.of(file))) {
                Files.delete(Path.of(file));
            }

            // remove SVG
            Elements elements = document.selectXpath("//svg");
            for (Element element : elements) {
                element.remove();
            }

            // remove images
            elements = document.selectXpath("//img");
            for (Element element : elements) {
                element.remove();
            }

            // remove buttons
            Elements buttons = document.getElementsByAttributeValue("data-test", "Button");
            for (Element element : buttons) {
                element.remove();
            }
            buttons = document.getElementsByAttributeValue("data-test", "LearnMoreButton");
            for (Element element : buttons) {
                element.remove();
            }



            // find class styles_headerContainer_*, if there are two of these, it's a promoted post
            // otherwise it's a normal non-promoted post
            // we should have 4 of these in a row, then a bunch of non promoted
            // they should have the words "Promoted" somewhere in there
            // root should have an additional styles_compactComponent

            // company name is likely text in h2
            // can look for company href based upon /company/company-name

            // for the rest of the company header, just grab any text and put it in bullet points

            // div.p-4 pt-0 may be an information pane, it should be second child of root
            // there should be 3 children total

            // third child of root should be .styles_jobListingList_*

            // styles_info
            // and styles_tags
            // for the actual job posting data

            // Probably make a separate page just for angel.co jobs




            Elements startupEntries = document.getElementsByAttributeValue("data-test", "StartupResult");

            File f = new File(file);
            FileWriter fr = new FileWriter(f, true);
            int i = 0;
            for (Element element : startupEntries) {
                System.out.println("count: " + ++i);
                fr.append(element.toString());
                fr.append("\n<hr>\n");
                fr.append("\n\n\n\n\n\n\n\n");
            }
            fr.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
