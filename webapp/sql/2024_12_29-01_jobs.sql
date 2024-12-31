create table db.jobs
(
    id               int auto_increment
        primary key,
    title            text null,
    url              text null,
    company          text null,
    description      text null,
    status           text null,
    job_site         text null,
    job_posting_date date null
);

