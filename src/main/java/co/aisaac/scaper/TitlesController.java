package co.aisaac.scaper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TitlesController {

    private final JobRepo jobRepo;

    public TitlesController(JobRepo jobRepo) {
        this.jobRepo = jobRepo;
    }

    @GetMapping("/titles")
    public String all(Model model) {
        List<Job> jobs = jobRepo.findAllByStatusOrderByTitle("new");

        for (Job job : jobs) {
            String indeed = " - Indeed.com";
            int trimTo = job.title.lastIndexOf("-", job.title.length() - indeed.length() - 1);
            job.setTitle(job.title.substring(0, trimTo));
        }

        model.addAttribute("jobs", jobs);
        return "titles";
    }
}
