# Job search program

A program that allows you to quickly filter through 1000's of job postings to find the very exact jobs you're interested
in.

Here's some of the problems I had with job boards in general when job hunting or looking for clients.

- Can't mark a job posting as seen, or ignore that job posting.
- Lots of irrelevant job postings that just matched a keyword in your search.
- You won't end up seeing some jobs. A board may have 3,000 remote java jobs, but you're not going to make it to the end
  page of results.
- Most job portals use a ridiculous amount of JavaScript, things are lagging all over the place, nothing is snappy or crisp.

## Design goals

For the design, I was going for simple, easy to use, effective.

- The styling of the program is optimal for quickly reading lots of data.
  - I find this design to be best, minimal without distraction, just show me the data.
- I don't want any extra clutter on the page, just the job description, and a few buttons.
- Should run very fast. I run everything locally on the same machine to reduce latency. Pure JavaScript is a must, no frameworks or libraries. 
- Small amount of code, no abstractions, no low value features.
  - Less code means I can make an update to logic in about 10 minutes.
  - I can add a new scraper in a single file, using a single function as pure Java.
  - No low value features. 
  - I had spring security setup for auth, but auth isn't useful for local web apps, so I removed it.
  - The scrapers used to have shared code in the form of inheritence, but there was no value in this.

## Features

There's only a few features. 
- One is the main page, where you can see all the job listings in your database.
![501B9044-D739-4C63-81D9-30AB90CAD34B_1_201_a](https://github.com/user-attachments/assets/2f18cc67-3e96-4e7b-bbab-26d2927dcb9b)

- Underneath the job title, you can click one of the status buttons to categorize the job posting. As soon as you click, the job posting status in the database is updated, and the posting is removed from your viewpoint so you can immediately look at the next job posting.
![AEBE8FC9-4901-4256-B9CB-78BEE0A68E03_1_201_a](https://github.com/user-attachments/assets/f023b7c4-9db9-4917-a877-1ec5ab057b1c)

- Clicking on a title opens the job description in a new tab so you can see the original post, or apply to the job.

- You can filter by job posting source, job posting status, title search, company search, and keyword search. The keyword search will do highlighting. Separate keyword search terms with a comma to search on many terms.
![B8C40972-ABCD-4DE6-B6B7-4EE9EEC287FE_1_201_a](https://github.com/user-attachments/assets/0d742be5-c657-44ad-b9d7-816938e602cb)

- The job status is a good way to track applications throughout the process. 

- The title feature is a way to rapidly cut through 1000's of job postings in a short amount of time. As soon as you click "declined" or "interested", the program will update this posting and it disappears from your view, shifting to the next title. Running everything on your machine, this will take about 20ms. So you can just "click click click click click". Clicking the title will open the original posting in a new tab. 
![50A958F0-D16D-4EF6-88AE-29222C0BCD27_1_201_a](https://github.com/user-attachments/assets/bb4a2403-b8ec-45a2-a78a-c7e74bce127a)

- The app is very minimal and fast. Here is all that is loaded when you visit the page.
![image](https://github.com/user-attachments/assets/6e05f90d-218a-4ef4-bfc0-f11aa1010e32)


## Usage

Using this program, I'm able to pull down 4,000 job postings or more at a time. After the job postings are in the database on my machine, I go to the "titles" page, where I can spend a few hours and rapidly categorize those postings. After this, I'll start looking through the actual job postings on the main page. I like to look at all the postings from one site, then move on to the next site. HackerNews, AngelCo, and Indeed all have very different types of postings, so doing one site at a time is optimal.

## Installing

I usually run this program directly from my IDE so I can debug in real time and fix bugs that appear as I'm using the program. You'll need to set up Intellij or some Java IDE. Also will need to set up MySQL. I use docker for mysql, but with persistent storage. There's a docker compose file you can use. For the schema, there's a single database table called "jobs" that you can find in the sql folder. You'll need to change paths in the program, so do a search on "aaron" and update accordingly. Probably you should just read all the code first to get an idea of how the program works, it's really not very much code.

The code uses thymeleaf for templating, basic spring boot, spring jpa, some regular java code for scrapers (though you can write those in any language and store the jobs directly in the jobs table). The project has 2 maven modules, refer to the .pom files to understand. One of the modules relies on the other. I may merge these into a single module for simplicity.

## Todo

- Clean up and sanitize html from scrapers, remove class names, inline styles, any scripts, etc.
- Move filtering and search logic out of Java and into MySQL for performance.
