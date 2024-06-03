package co.aisaac.webapp.util;

import co.aisaac.webapp.Job;
import co.aisaac.webapp.JobRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

// can run this as scheduled if we need to load this again
public class LoadJobs {

//    @Autowired
    private JobRepo jobRepo;

    public void loadJobs() {
        ObjectMapper om = new ObjectMapper();

        try {
            File file = new File("/Users/aaron/Documents/jobs.json");
            JsonNode arrayNode = om.readTree(file);

            // [subtitle, search_term, description, company, title, url, status]
            for (JsonNode node : arrayNode) {
                Job job = new Job();
                job.subtitle = node.get("subtitle").asText();
                job.searchTerm = node.get("search_term").asText();
                job.description = node.get("description").asText();
                job.company = node.get("company").asText();
                job.title = node.get("title").asText();
                job.url = node.get("url").asText();
                job.status = node.get("status").asText();
                jobRepo.save(job);
                System.out.println("done");
            }

            System.out.println("done");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
