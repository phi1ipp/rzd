.echo on
.open test.db

create table countries(
	country_key		integer primary key autoincrement,
	name			char(128) not null,
	val				integer not null
);

create table doctypes(
	doctype_key		integer primary key autoincrement,
	name			char(64) not null
);

create table contacts(
	contact_key 	integer	primary key autoincrement,
	lastname 		char(128) not null,
	firstname 		char(64) not null,
	middlename 		char(128),
	doctype_key 		integer not null,
	docnumber 		char(32) not null,
	gender			char(1) not null,
	doccountry_key		integer not null,
	birthplace 		char(128) not null,
	birthdate 		real not null
);