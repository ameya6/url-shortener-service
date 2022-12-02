use url_shortner

drop table if exists url_info

create table url_info(
    id BIGSERIAL primary key not null,
    uuid UUID unique not null,
    long_url text unique not null,
    url_hash text unique not null,
    created_at timestamptz not null default now(),
    short_code text unique not null,
    distributed_uid BIGINT unique not null,
    expire_at timestamptz not null
);