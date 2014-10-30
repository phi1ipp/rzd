grant select on ticket_service.users to php@localhost;
grant select on ticket_service.* to to php@localhost;
grant insert on ticket_service.order_tickets to php@localhost;
grant insert on ticket_service.orders to php@localhost;
grant insert on ticket_service.refunds to php@localhost;
grant insert on ticket_service.tickets to php@localhost;

grant execute on procedure ticket_service.save_ticket_ordered to php@localhost;
grant execute on procedure ticket_service.save_refund to php@localhost;
