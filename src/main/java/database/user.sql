create table user
(
	account int auto_increment,
	username varchar(20) not null,
	password integer not null,
	email varchar(20) not null,
	friends varchar(256) null,
	`groups` varchar(256) null,
	status int default 0 not null,
	constraint user_pk
		primary key (account)
);