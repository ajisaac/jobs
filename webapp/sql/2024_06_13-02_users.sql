create sequence job_scraper.users_id_seq;
alter sequence job_scraper.users_id_seq owner to aaron;

create table job_scraper.users
(
    id                  bigint primary key not null default nextval('users_id_seq'::regclass),
    name                text                        default '',
    email               text unique        not null,
    password            text               not null,
    authorities         text               not null default 'USER',
    account_expired     boolean                     default false,
    account_locked      boolean                     default false,
    credentials_expired boolean                     default false,
    enabled             boolean                     default true
);

create index users_email_index on users using btree (email);

alter sequence job_scraper.users_id_seq owned by users.id;
