create table users
(
    id            serial,
    name          character varying(32)  not null,
    email         character varying(64)  not null,
    password      character varying(100) not null,
    description   character varying(1024) null,
    role          character varying(16)  not null,
    creation_date timestamp without time zone not null,
    constraint users_pkey primary key (id),
    constraint users_email_unique unique (email),
    constraint users_name_unique unique (name)
);

create table links
(
    id              serial,
    short           character varying(32)   not null,
    url             character varying(2048) not null,
    owner_id        integer null,
    creation_date   timestamp without time zone not null,
    expiration_date timestamp without time zone null,
    constraint links_pkey primary key (id),
    constraint links_short_key unique (short),
    constraint fk_links_owner_id__id foreign key (owner_id) references users (id) on update restrict on delete restrict
);

