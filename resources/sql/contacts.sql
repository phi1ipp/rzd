.echo on
.open test.db

insert into contacts(lastname, firstname, middlename, doctype_key, docnumber, gender, doccountry_key, birthplace, birthdate)
	values("Григорьев", "Филипп", "Вячеславович", 1, "1111 111111","М", 176, "Севастополь", date("1974-11-23"));
insert into contacts(lastname, firstname, middlename, doctype_key, docnumber, gender, doccountry_key, birthplace, birthdate)
	values("Габидуллин", "Айдар", "Ибрагимович", 1, "2222 222222","М", 176, "Казань", date("1970-01-01"));
insert into contacts(lastname, firstname, doctype_key, docnumber, gender, doccountry_key, birthplace, birthdate)
	values("Suram", "Suresh", 1, "4646464664","М", 176, "Hyderabad", date("1970-01-01"));