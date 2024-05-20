package co.aisaac.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class TitlesController {

    private final JobRepo jobRepo;

    private TitlesFilter filter;

    public TitlesController(JobRepo jobRepo) {
        this.jobRepo = jobRepo;
        this.filter = new TitlesFilter();
    }

    @GetMapping("/titles")
    public String all(Model model) {
        List<Job> jobs = jobRepo.findAllByStatusOrderByTitle("new");


        // Remove indeed from title
        for (Job job : jobs) {
            String indeed = " - Indeed.com";
            if (!job.title.endsWith(indeed)) {
                continue;
            }

            int trimTo = job.title.lastIndexOf("-", job.title.length() - indeed.length() - 1);
            job.setTitle(job.title.substring(0, trimTo));
        }

        if (!filter.titleSearch.isBlank()) {
            jobs = jobs
                    .stream()
                    .filter(job -> job.title.toLowerCase().contains(filter.titleSearch.toLowerCase()))
                    .toList();
        }

        model.addAttribute("jobs", jobs);
        model.addAttribute("filter", filter);

        return "titles";
    }


    @PostMapping("/titles")
    public String searchTitles(TitlesFilter filter) {
        this.filter = filter;
        return "redirect:/titles";
    }
}
