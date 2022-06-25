package co.aisaac.scaper;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

@Controller
public class WebController {

	private final JobRepo repo;

	private Filter filter;

	public WebController(JobRepo repo) {
		this.repo = repo;
		this.filter = new Filter();
	}

	@GetMapping("/")
	public String all(Model model) {
		List<Job> jobs = filterJobs(filter, repo.findAll());
		model.addAttribute("jobs", jobs);
		model.addAttribute("filter", filter);
		return "jobs";
	}

	@PostMapping("/")
	public String allFiltered(Filter filter) {
		this.filter = filter;
		return "redirect:/";
	}

	@ResponseBody
	@PostMapping("/status/{id}/{status}")
	public ResponseEntity<String> updateStatus(@PathVariable String id, @PathVariable String status) {
		Long idd = Long.parseLong(id);
		Optional<Job> optJob = repo.findById(idd);
		if (optJob.isEmpty())
			return ResponseEntity.notFound().build();

		Job job = optJob.get();
		job.setStatus(status);
		repo.save(job);
		return ResponseEntity.ok().build();
	}

	private List<Job> filterJobs(Filter filter, List<Job> all) {


		List<String> statuses = filter.getStatuses();
		List<Job> filtered = new ArrayList<>(all);
		if (!statuses.isEmpty()) {
			filtered = filtered.stream().filter(job -> {
				for (String status : statuses)
					if (job.status.trim().equalsIgnoreCase(status.trim()))
						return true;

				return false;
			}).toList();
		}

		List<String> companies = Arrays.stream(filter.companySearch.split(",")).filter(s -> !s.isBlank()).toList();
		if (!companies.isEmpty()) {
			filtered = filtered.stream().filter(job -> {
				for (String company : companies)
					if (job.company.trim().contains(company.trim()))
						return true;

				return false;
			}).toList();
		}


		List<String> searchTerms = Arrays.stream(filter.searchTerms.split(",")).filter(s -> !s.isBlank()).toList();
		if (!searchTerms.isEmpty()) {
			filtered = filtered.stream().filter(job -> {
				var desc = job.description.toLowerCase();
				for (String term : searchTerms) {
					if (desc.contains(term.trim().toLowerCase()))
						return true;
					if (job.title.toLowerCase(Locale.ROOT).contains(term.trim().toLowerCase()))
						return true;
					if (job.subtitle.toLowerCase(Locale.ROOT).contains(term.trim().toLowerCase()))
						return true;
				}
				return false;
			}).toList();
		}

		filtered = highlight(searchTerms, filtered);

		// sort
		filtered = filtered.stream()
				.sorted((o1, o2) -> o1.company.compareToIgnoreCase(o2.company))
				.toList();

		return filtered;
	}

	private List<Job> highlight(List<String> searchTerms, List<Job> filtered) {
		if (searchTerms.isEmpty())
			return filtered;
		if (filtered.isEmpty())
			return filtered;

		for (String searchTerm : searchTerms) {
			String pattern = "(?i)" + Pattern.quote(searchTerm.trim());
			String st = "<span class=\"highlight\">" + searchTerm + "</span>";
			// for each search term
			for (Job job : filtered) {
				job.description = job.description.replaceAll(pattern, st);
			}
		}
		return filtered;
	}
}
