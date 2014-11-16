CREATE VIEW `sale_transactions` 
AS select `t`.`num` AS `ticket_num`,`o`.`createdon` AS `trans_date`,`t`.`lastname` AS `passenger`,
	`u`.`name` AS `sales_user`,`cl`.`name` AS `client_name` 
from ((((`tickets` `t` join `users` `u`) join `clients` `cl`) join `orders` `o`) join `order_tickets` `ot`) 
where ((`t`.`id` = `ot`.`ticket_id`) and (`ot`.`order_id` = `o`.`id`) and (`o`.`createdby` = `u`.`id`) 
	and (`u`.`client_id` = `cl`.`id`));

CREATE VIEW `tickets_view` AS
    select 
        `o`.`num` AS `ordernum`,
        `t`.`num` AS `ticketnum`,
        `t`.`lastname` AS `lastname`,
        `t`.`tripon` AS `tripon`,
        `t`.`price` AS `price`,
        `o`.`createdon` AS `createdon`,
        `u`.`name` AS `name`
    from
        (((`orders` `o`
        join `tickets` `t`)
        join `users` `u`)
        join `order_tickets` `ot`)
    where
        ((`o`.`id` = `ot`.`order_id`)
            and (`t`.`id` = `ot`.`ticket_id`)
            and (`o`.`createdby` = `u`.`id`));