-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`philipp`@`localhost` PROCEDURE `save_ticket_ordered`(order_num bigint, ticket_num bigint, 
										lastname varchar(64), ticket_price float, 
										tripdate varchar(32),
										createdon varchar(32), createdby varchar(32), 
										out retcode int)
proc:BEGIN
	declare userid int;
	declare ticketid int;
	declare orderid int;
	declare createdDT datetime;
	declare triponDT datetime;
	declare cnt int;
	
	-- convert dates
	select str_to_date(createdon, '%d.%m.%Y %H:%i:%s') into createdDT;
	select str_to_date(tripdate, '%d.%m.%Y %H:%i:%s') into triponDT;

	-- TODO check a date of a trip > curdate

	set userid = 0;
	select id into userid from users where name = createdby and disabled = false;

	-- if no enabled user found - return 1
	if (userid = 0) then 
		set retcode = 1;
		leave proc;
	end if;
	
	-- check if we have that order with the ticket already
	set cnt = 0;
	select count(*) into cnt from tickets_view 
		where ordernum = order_num and ticketnum = ticket_num;
	if (cnt > 0) then
		set retcode = 0;
		leave proc;
	end if;

	set autocommit = 0;
	-- begin of transaction
	start transaction;

	-- search for a ticket
	set ticketid = 0;
	select id into ticketid from tickets where num = ticket_num;

	-- if not found -> insert
	if (ticketid = 0) then
		-- insert ticket data
		insert into tickets(num, created, lastname, price, tripon)
			values(ticket_num, date(createdDT), lastname, ticket_price, triponDT);
		select last_insert_id() into ticketid;
	end if;
	

	-- check if order already exists so there is a valid order_id
	set orderid = 0;
	select id into orderid from orders where num = order_num;
		
	-- if not exists -> insert order data into DB
	if (orderid = 0) then
		insert into orders(num, createdon, createdby)
			values(order_num, createdDT, userid);
		select last_insert_id() into orderid;
	end if;

	-- check if 
	select count(id) from order_tickets 
		where order_id = orderid and ticket_id = ticketid;
	insert into order_tickets(order_id, ticket_id)
		values(orderid, ticketid);

	commit;
	-- end transaction

	set retcode = 0;
END

-- --------------------------------------------------------------------------------
-- Routine DDL
-- Note: comments before and after the routine body will not be stored by the server
-- --------------------------------------------------------------------------------
DELIMITER $$

CREATE DEFINER=`philipp`@`localhost` PROCEDURE `save_refund`(ticketNum bigint, userLogin varchar(32), 
								refundedon varchar(32), out retCode int)
proc:BEGIN
	declare userid int;
	declare ticketid int;
	declare refundedDt datetime;

	select str_to_date(refundedon, '%d.%m.%Y %H:%i:%s') into refundedDt;

	-- get user id
	set userid = 0;
	select id into userid from users where name = userLogin and disabled = 0;

	if (userid = 0) then
		set retCode = 1;
		leave proc;
	end if;

	-- get ticket id
	set ticketid = 0;
	select id into ticketid from tickets where num = ticketNum;
	
	if (ticketid = 0) then
		set retcode = 1;
		leave proc;
	end if;

	insert into refunds(ticket_id, refundedby, refundedon)
		values(ticketid, userid, refundedDt);
	set retcode = 0;
END
