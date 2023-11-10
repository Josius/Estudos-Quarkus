CREATE DATABASE quarkus_social;

create table USERS(
	id bigserial PRIMARY KEY,
	name varchar(100) not null,
	age integer not null
);

