create sequence job_scraper.jobs_id_seq;
alter sequence job_scraper.jobs_id_seq owner to aaron;

create table job_scraper.jobs
(
    id               bigint primary key not null default nextval('jobs_id_seq'::regclass),
    title            text,
    url              text,
    company          text,
    subtitle         text,
    description      text,
    status           text,
    search_term      text,
    location         text,
    job_site         text               not null default ''::text,
    job_posting_date timestamp without time zone
);

create index jobs_company_index on jobs using btree (company);
create index jobs_job_site_index on jobs using btree (job_site);
create index jobs_status_index on jobs using btree (status);

alter sequence job_scraper.jobs_id_seq owned by jobs.id;
