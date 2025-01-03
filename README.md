# Job search program

A program that allows you to quickly filter through 1000's of job postings to find the very exact jobs you're interested
in.

Here's some of the problems I had with job boards in general when job hunting or looking for clients.

- Can't mark a job posting as seen, or ignore that job posting.
- Lots of irrelevant job postings that just matched a keyword in your search.
- You won't end up seeing some jobs. A board may have 3,000 remote java jobs, but you're not going to make it to the end
  page of results.
- Job boards really spam you with all these extra features, it's annoying.

## Design goals

For the design, I was going for simple, easy to use, effective.

- The styling of the program is optimal for quickly reading lots of job postings. No animations, no distracting color scheme, no UI, just pure data.
- I don't want any extra clutter on the page, just the job description, and a few links.
- Should run very fast. I run everything locally on the same machine to reduce latency. Pure JavaScript is a must, no frameworks or libraries. 
- Small amount of code, no abstractions, no low value features.
  - Less code means I can make an update to logic in about 10 minutes.
  - I can add a new scraper in a single file, using a single function in simple Java.
  - The scrapers used to have shared code in the form of inheritence, but there was no value in this.

## Features

There's only a few features. 
- One is the main page, where you can see all the job listings in your database.
![6E518C71-877C-4388-A276-600196E2CF77](https://github.com/user-attachments/assets/88515f76-784b-4939-9de7-f4b111e50f5a)
![501B9044-D739-4C63-81D9-30AB90CAD34B_1_201_a](https://github.com/user-attachments/assets/1b967d48-7d4b-468d-a732-6207813625f5)

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

## Installing

I don't intend for anyone to actually use this software, so it's not published. To use it, you'd need to update any file paths on your machine, then set up your database, configure everything. Best to read through the code to understand the app.

## Todo

- Clean up and sanitize html from scrapers, remove class names, inline styles, any scripts, etc.
- Move filtering and search logic out of Java and into MySQL for performance.
