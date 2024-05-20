package co.aisaac.scaper.scrapers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Job {

	public String href;
	public String title;
	public String company;
	public String subtitle;
	public String description;
	public String searchTerm;
	public String status;
	public String location;
	public String jobSite;
	public String jobPostingDate;
	public String htmlPath;

	public Job() {

	}

}
