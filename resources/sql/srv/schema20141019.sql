SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `ticket_service` DEFAULT CHARACTER SET utf8 ;
USE `ticket_service` ;

-- -----------------------------------------------------
-- Table `ticket_service`.`clients`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ticket_service`.`clients` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `idclients_UNIQUE` (`id` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ticket_service`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ticket_service`.`users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64) NOT NULL,
  `disabled` BIT(1) NULL DEFAULT b'0',
  `key` BLOB NULL DEFAULT NULL,
  `client_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  INDEX `fk_users_client_idx` (`client_id` ASC),
  CONSTRAINT `fk_users_client`
    FOREIGN KEY (`client_id`)
    REFERENCES `ticket_service`.`clients` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ticket_service`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ticket_service`.`orders` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'row id',
  `num` INT(11) NOT NULL COMMENT 'Номер заказа в нотификации rzd.ru',
  `createdon` DATETIME NOT NULL,
  `createdby` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `ordernum_UNIQUE` (`num` ASC),
  INDEX `fk_orders_createdby_idx` (`createdby` ASC),
  CONSTRAINT `fk_orders_createdby`
    FOREIGN KEY (`createdby`)
    REFERENCES `ticket_service`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 66
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ticket_service`.`tickets`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ticket_service`.`tickets` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `num` BIGINT(20) NOT NULL,
  `created` DATE NOT NULL,
  `lastname` VARCHAR(128) NOT NULL,
  `price` FLOAT NOT NULL,
  `tripon` DATETIME NOT NULL COMMENT 'Date and time of a trip',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `num_UNIQUE` (`num` ASC))
ENGINE = InnoDB
AUTO_INCREMENT = 84
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ticket_service`.`order_tickets`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ticket_service`.`order_tickets` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `order_id` INT(11) NOT NULL,
  `ticket_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `ticket_id_UNIQUE` (`ticket_id` ASC),
  INDEX `fk_order_tickets_order_idx` (`order_id` ASC),
  CONSTRAINT `fk_order_tickets_order`
    FOREIGN KEY (`order_id`)
    REFERENCES `ticket_service`.`orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_tickets_tickets`
    FOREIGN KEY (`ticket_id`)
    REFERENCES `ticket_service`.`tickets` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 70
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `ticket_service`.`refunds`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ticket_service`.`refunds` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `ticket_id` INT(11) NOT NULL,
  `refundedby` INT(11) NOT NULL,
  `refundedon` DATE NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `ticket_id_UNIQUE` (`ticket_id` ASC),
  INDEX `fk_refunds_users_idx` (`refundedby` ASC),
  CONSTRAINT `fk_refunds_tickets`
    FOREIGN KEY (`ticket_id`)
    REFERENCES `ticket_service`.`tickets` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_refunds_users`
    FOREIGN KEY (`refundedby`)
    REFERENCES `ticket_service`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 13
DEFAULT CHARACTER SET = utf8;

USE `ticket_service` ;

-- -----------------------------------------------------
-- Placeholder table for view `ticket_service`.`tickets_view`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ticket_service`.`tickets_view` (`ordernum` INT, `ticketnum` INT, `price` INT, `createdon` INT, `name` INT);

-- -----------------------------------------------------
-- procedure save_refund
-- -----------------------------------------------------

DELIMITER $$
USE `ticket_service`$$
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
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure save_ticket_ordered
-- -----------------------------------------------------

DELIMITER $$
USE `ticket_service`$$
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
END$$

DELIMITER ;

-- -----------------------------------------------------
-- View `ticket_service`.`tickets_view`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `ticket_service`.`tickets_view`;
USE `ticket_service`;
CREATE  OR REPLACE ALGORITHM=UNDEFINED DEFINER=`philipp`@`localhost` SQL SECURITY DEFINER VIEW `ticket_service`.`tickets_view` AS select `o`.`num` AS `ordernum`,`t`.`num` AS `ticketnum`,`t`.`price` AS `price`,`o`.`createdon` AS `createdon`,`u`.`name` AS `name` from (((`ticket_service`.`orders` `o` join `ticket_service`.`tickets` `t`) join `ticket_service`.`users` `u`) join `ticket_service`.`order_tickets` `ot`) where ((`o`.`id` = `ot`.`order_id`) and (`t`.`id` = `ot`.`ticket_id`) and (`o`.`createdby` = `u`.`id`));

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

CREATE 
    ALGORITHM = UNDEFINED 
    DEFINER = `philipp`@`localhost` 
    SQL SECURITY DEFINER
VIEW `tickets_view` AS
    select 
        `o`.`num` AS `ordernum`,
        `t`.`num` AS `ticketnum`,
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
            and (`o`.`createdby` = `u`.`id`))


