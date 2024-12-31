package co.aisaac.scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScrapeAngelCo {

    public static void main(String[] args) {
        ScrapeAngelCo scrapeAngelCo = new ScrapeAngelCo();
        scrapeAngelCo.run();
    }

    public void run() {

        File input = new File("./scrapers/data/angel_co.html");
        try {
            Document document = Jsoup.parse(input, "UTF-8", "https://angel.co/");

            Path p1 = Paths.get("./scrapers/data/new1.html");
            Files.writeString(p1, document.toString(), StandardCharsets.UTF_8);

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

            Path p2 = Paths.get("./scrapers/data/new2.html");
            Files.writeString(p2, document.toString(), StandardCharsets.UTF_8);
            Elements startupEntries = document.getElementsByAttributeValue("data-test", "StartupResult");

            File f = new File("./scrapers/data/new3.html");
            FileWriter fr = new FileWriter(f, true);
            int i = 0;
            for (Element element : startupEntries) {
                System.out.println("count: " + ++i);
                fr.append(element.toString());
                fr.append("\n<hr>\n");
            }
            fr.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
