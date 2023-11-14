CREATE DATABASE quarkus_social;

create table USERS(
	id bigserial PRIMARY KEY,
	name varchar(100) not null,
	age integer not null
);

create table POSTS(
    id bigserial PRIMARY KEY,
    post_text varchar(150) not null,
    dateTime timestamp not null,
    user_id bigint not null references USERS(id)
);