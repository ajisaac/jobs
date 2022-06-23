package co.aisaac.scaper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
