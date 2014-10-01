.open test.db
.echo on

select count(*) from contacts;
select * from contacts where lastname like "Гр%";
select * from contacts where lastname like "Su%";