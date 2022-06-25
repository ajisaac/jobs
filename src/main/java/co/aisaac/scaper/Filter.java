package co.aisaac.scaper;

import java.util.List;

public class Filter {
	/*
			Statuses

			- New
			- Decline
			- Interested
			- Applied
			- Interviewing
			- Rejected

	Show if the company already has other postings that you have assigned non new status
	to. This way we can see what we already thought of some of their job postings and
	it might jog our memory.

	Filter by status. We should definitely want to see jobs of a certain status, like applied.

	Filter by company. To see all jobs by company regardless of status.

	Filter by companies with a certain status. To see all of the companies that have a certain status.

	Filter by search term. Search makes sense, even better if added highlighting.

	Notes on the job, should be easy to add notes to keep track.


	 */


	// only show jobs that have one of these
	public List<String> containsStatuses;

	// only show jobs with these companies
	public List<String> containsCompanies;

	// don't show any of these companies
	public List<String> blacklistCompanies;

	// show jobs with these search terms
	public List<String> searchTerms;

	// show jobs by companies that have a certain status
	// for instance, show us all jobs that belong to companies we have already applied
	public List<String> companyStatuses;

}
