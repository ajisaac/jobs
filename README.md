# Job Scraper

This is like an applicant tracking system, but in reverse.

You scrape hundreds of job descriptions from a job search site into a database.
Then this application lets you rapidly sift through all the jobs, until you've 
narrowed it to the postings you are truly interested in.

### Statuses

These are the categories you make walk a job through, so you can track the entire 
job search.

- New
- Declined
- Interested
- Applied
- Interviewing
- Rejected

### Filtering and Search

Can search and highlight text of job descriptions, titles, and companies. Can also
search by status. 

todo - show statuses next to company

### Scraping

Check if job posting exists before scraping it

Put a flag on "new" jobs, marking them as "old"

Then for any "old" job that we see when scraping, mark it new

Delete old jobs that weren't seen

### Add multiple categories for scraping

Should have Java, Linux, C++, etc, though maybe that doesn't matter

	/*

	Show if the company already has other postings that you have assigned non new status
	to. This way we can see what we already thought of some of their job postings and
	it might jog our memory.

	Sort by company

	Filter by search term. Search makes sense, even better if added highlighting.

	Notes on the job, should be easy to add notes to keep track.
	 */
