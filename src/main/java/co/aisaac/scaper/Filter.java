package co.aisaac.scaper;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Filter {
	public Filter() {
	}

	boolean statusNew = true;
	boolean declined = true;
	boolean interested = true;
	boolean applied = true;
	boolean interviewing = true;
	boolean rejected = true;

	List<String> getStatuses() {
		List<String> statuses = new ArrayList<>();
		if (statusNew)
			statuses.add("new");
		if (declined)
			statuses.add("declined");
		if (interested)
			statuses.add("interested");
		if (applied)
			statuses.add("applied");
		if (interviewing)
			statuses.add("interviewing");
		if (rejected)
			statuses.add("rejected");

		return statuses;
	}

	String companySearch = "";

	String searchTerms = "";

}
