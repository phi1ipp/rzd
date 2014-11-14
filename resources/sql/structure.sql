.echo on
.open clients.db

create table countries(
	country_key		integer primary key autoincrement,
	name			char(128) not null,
	val				integer not null
);

create table doctypes(
	doctype_key		integer primary key autoincrement,
	name			char(64) not null
);

create table customers(
	cust_key		integer primary key autoincrement,
	name			char(256) not null
);

create table contacts(
	contact_key 	integer	primary key autoincrement,
	lastname 		char(128) not null,
	firstname 		char(64) not null,
	middlename 		char(128),
	doctype_key 	integer,
	docnumber 		char(32) not null,
	gender			char(1) not null,
	doccountry_key	integer,
	birthplace 		char(128) not null,
	birthdate 		real not null,
	cust_key		integer,
	foreign key 	(doctype_key) 		references doctypes(doctype_key),
	foreign key 	(doccountry_key) 	references countries(country_key),
	foreign key 	(cust_key) 			references customers(cust_key)
);