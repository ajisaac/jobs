package co.aisaac.scaper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;

@Controller
public class AddJobController {

	private final JobRepo repo;

	public AddJobController(JobRepo repo) {
		this.repo = repo;
	}

	@GetMapping("/add-job")
	public String addJob(Model model, JobUrl jobUrl, BindingResult result) {
		if (jobUrl == null)
			jobUrl = new JobUrl();

		if (model.getAttribute("err") != null) {
			String err = (String) Objects.requireNonNull(model.getAttribute("err"));
			result.addError(new ObjectError("jobUrl", err));
		}

		model.addAttribute("jobUrl", jobUrl);

		return "add-job";
	}

	@PostMapping("/add-job")
	public String scrapeJob(Model model, JobUrl jobUrl, RedirectAttributes attrs) {
		if (!jobUrl.isValid()) {
			attrs.addFlashAttribute("err", jobUrl.errorMsg());
			attrs.addFlashAttribute("jobUrl", jobUrl);
			return "redirect:/add-job";
		}

		// todo scrape the job page
		return "redirect:/add-job";
	}
}
