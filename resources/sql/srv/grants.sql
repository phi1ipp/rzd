grant select on ticket_service.* to webservice identified by '/8?hraT&6&!4~Z4';
grant insert on ticket_service.order_tickets to webservice;
grant insert on ticket_service.orders to webservice;
grant insert on ticket_service.refunds to webservice;
grant insert on ticket_service.tickets to webservice;

grant execute on procedure ticket_service.save_ticket_ordered to php@localhost;

grant execute on procedure ticket_service.save_refund to php@localhost;
