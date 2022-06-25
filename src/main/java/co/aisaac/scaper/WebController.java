package co.aisaac.scaper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebController {

	private final JobRepo repo;

	public WebController(JobRepo repo) {
		this.repo = repo;
	}

	@GetMapping("/")
	public String all(Model model) {
		model.addAttribute("jobs", repo.findAll());
		return "jobs";
	}

	@PostMapping("/")
	public String all(Model model, Filter filter) {
		List<Job> all = repo.findAll();
		all = filterJobs(filter, all);
		model.addAttribute("jobs", all);
		model.addAttribute("filter", filter);
		return "jobs";
	}

	private List<Job> filterJobs(Filter filter, List<Job> all) {
		List<Job> filtered = new ArrayList<>(all);

		if (!filter.containsStatuses.isEmpty()) {
			filtered = filtered.stream().filter(job -> {
				for (String status : filter.containsStatuses)
					if (job.status.equals(status))
						return true;

				return false;
			}).toList();
		}

		if (!filter.containsCompanies.isEmpty()) {
			filtered = filtered.stream().filter(job -> {
				for (String company : filter.containsCompanies)
					if (job.company.equals(company))
						return true;

				return false;
			}).toList();
		}

		if (!filter.blacklistCompanies.isEmpty()) {
			filtered = filtered.stream().filter(job -> {
				for (String company : filter.blacklistCompanies)
					if (job.company.equals(company))
						return false;

				return true;
			}).toList();
		}


		// show jobs with these search terms
		List<String> searchTerms;

		// show jobs by companies that have a certain status
		// for instance, show us all jobs that belong to companies we have already applied
		List<String> companyStatuses;

		return filtered;
	}
}
