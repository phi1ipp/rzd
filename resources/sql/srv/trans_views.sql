create or replace view sale_transactions as
select 
	t.num ticket_num, o.createdon as trans_date, 
	t.lastname as passenger, u.name sales_user, cl.name client_name
from 
	tickets t, users u, clients cl, orders o, order_tickets ot
where 
	t.id = ot.ticket_id and ot.order_id = o.id and o.createdby = u.id and u.client_id = cl.id;
	
create or replace view refund_transactions as
select 
	t.num ticket_num, ref.refundedon as trans_date,
	t.lastname as passenger, u.name sales_user, cl.name client_name
from 
	tickets t, users u, clients cl, refunds ref
where 
	t.id = ref.ticket_id and ref.refundedby = u.id 
	and ref.ticket_id = t.id and u.client_id = cl.id;
	
create view all_transactions as
select 
	t.num ticket_num, o.createdon as trans_date, 'sale' as trans_type,
	t.lastname as passenger, u.name sales_user, cl.name client_name
from 
	tickets t, users u, clients cl, orders o, order_tickets ot
where 
	t.id = ot.ticket_id and ot.order_id = o.id and o.createdby = u.id and u.client_id = cl.id
union all	
select 
	t.num ticket_num, ref.refundedon as trans_date, 'refund' as trans_type,
	t.lastname as passenger, u.name sales_user, cl.name client_name
from 
	tickets t, users u, clients cl, refunds ref
where 
	t.id = ref.ticket_id and ref.refundedby = u.id 
	and ref.ticket_id = t.id and u.client_id = cl.id;	
	