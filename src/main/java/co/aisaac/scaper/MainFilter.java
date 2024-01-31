package co.aisaac.scaper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainFilter {
	public MainFilter() {
	}

	String status = "NEW";
	String jobSite = "ALL";

	public String getJobSite() {
		return jobSite.toUpperCase();
	}

	public String getStatus() {
		return status.toUpperCase();
	}

	String companySearch = "";

	String searchTerms = "";

	String titleSearch = "";

}
