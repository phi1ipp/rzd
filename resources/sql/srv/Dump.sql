CREATE DATABASE  IF NOT EXISTS `ticket_service` DEFAULT CHARACTER SET utf8;
USE `ticket_service`;
-- MySQL dump 10.13  Distrib 5.5.40, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: ticket_service
-- ------------------------------------------------------
-- Server version	5.5.40-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Temporary table structure for view `all_transactions`
--

DROP TABLE IF EXISTS `all_transactions`;
/*!50001 DROP VIEW IF EXISTS `all_transactions`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `all_transactions` (
  `ticket_num` tinyint NOT NULL,
  `trans_date` tinyint NOT NULL,
  `trans_type` tinyint NOT NULL,
  `passenger` tinyint NOT NULL,
  `sales_user` tinyint NOT NULL,
  `client_name` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clients` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idclients_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_tickets`
--

DROP TABLE IF EXISTS `order_tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_tickets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `ticket_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `ticket_id_UNIQUE` (`ticket_id`),
  KEY `fk_order_tickets_order_idx` (`order_id`),
  CONSTRAINT `fk_order_tickets_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_tickets_tickets` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7571 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'row id',
  `num` int(11) NOT NULL COMMENT 'Номер заказа в нотификации rzd.ru',
  `createdon` datetime NOT NULL,
  `createdby` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ordernum_UNIQUE` (`num`),
  KEY `fk_orders_createdby_idx` (`createdby`),
  CONSTRAINT `fk_orders_createdby` FOREIGN KEY (`createdby`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5773 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `refund_transactions`
--

DROP TABLE IF EXISTS `refund_transactions`;
/*!50001 DROP VIEW IF EXISTS `refund_transactions`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `refund_transactions` (
  `ticket_num` tinyint NOT NULL,
  `trans_date` tinyint NOT NULL,
  `passenger` tinyint NOT NULL,
  `sales_user` tinyint NOT NULL,
  `client_name` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `refunds`
--

DROP TABLE IF EXISTS `refunds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `refunds` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ticket_id` int(11) NOT NULL,
  `refundedby` int(11) NOT NULL,
  `refundedon` date NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ticket_id_UNIQUE` (`ticket_id`),
  KEY `fk_refunds_users_idx` (`refundedby`),
  CONSTRAINT `fk_refunds_tickets` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_refunds_users` FOREIGN KEY (`refundedby`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=526 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `sale_transactions`
--

DROP TABLE IF EXISTS `sale_transactions`;
/*!50001 DROP VIEW IF EXISTS `sale_transactions`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `sale_transactions` (
  `ticket_num` tinyint NOT NULL,
  `trans_date` tinyint NOT NULL,
  `passenger` tinyint NOT NULL,
  `sales_user` tinyint NOT NULL,
  `client_name` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS `tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tickets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `num` bigint(20) NOT NULL COMMENT 'ticket number as it appears on paper tickets',
  `created` date NOT NULL,
  `lastname` varchar(128) NOT NULL,
  `price` float NOT NULL,
  `tripon` datetime NOT NULL COMMENT 'Date and time of a trip',
  `tripfrom` varchar(64) NOT NULL,
  `tripto` varchar(64) NOT NULL,
  `rzd_id` bigint(20) NOT NULL COMMENT 'external ticket id from RZD reference, used in web calls',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `num_UNIQUE` (`num`)
) ENGINE=InnoDB AUTO_INCREMENT=7585 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary table structure for view `tickets_view`
--

DROP TABLE IF EXISTS `tickets_view`;
/*!50001 DROP VIEW IF EXISTS `tickets_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `tickets_view` (
  `ordernum` tinyint NOT NULL,
  `ticketnum` tinyint NOT NULL,
  `rzd_id` tinyint NOT NULL,
  `lastname` tinyint NOT NULL,
  `tripon` tinyint NOT NULL,
  `from` tinyint NOT NULL,
  `to` tinyint NOT NULL,
  `price` tinyint NOT NULL,
  `createdon` tinyint NOT NULL,
  `name` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) NOT NULL,
  `disabled` bit(1) DEFAULT b'0',
  `key` blob,
  `client_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`),
  KEY `fk_users_client_idx` (`client_id`),
  CONSTRAINT `fk_users_client` FOREIGN KEY (`client_id`) REFERENCES `clients` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'ticket_service'
--
/*!50003 DROP PROCEDURE IF EXISTS `save_refund` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
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

	-- check if refund is already in 
	set @ref_cnt = 0;
	select count(1) into @ref_cnt from refunds where ticket_id = ticketid;
	if (@ref_cnt > 0) then
		set retcode = 0;
		leave proc;
	end if;
	
	-- save refund information
	insert into refunds(ticket_id, refundedby, refundedon)
		values(ticketid, userid, refundedDt);
	commit;
	set retcode = 0;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `save_ticket_ordered` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
CREATE DEFINER=`philipp`@`localhost` PROCEDURE `save_ticket_ordered`(
								order_num bigint, 
								ticket_num bigint, ticket_id bigint,
								lastname varchar(64), ticket_price float, 
								tripdate varchar(32),
								tripFrom varchar(64), tripTo varchar(64),		
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

	-- search for a user
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
		insert into tickets(num, created, lastname, price, tripon, tripfrom, tripto, rzd_id)
			values(ticket_num, date(createdDT), lastname, ticket_price, 
					triponDT, tripFrom, tripTo, ticket_id);
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

	-- insert order and ticket relation
	insert into order_tickets(order_id, ticket_id)
		values(orderid, ticketid);

	commit;
	-- end transaction

	set retcode = 0;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `search_tickets` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES' */ ;
DELIMITER ;;
CREATE DEFINER=`philipp`@`localhost` PROCEDURE `search_tickets`(ticketNum bigint, lastName varchar(128), 
	stationFrom varchar(64), stationTo varchar(64), dateFrom date, dateTo date,
	companyName varchar(128), allCriteria bool, runAs varchar(64), out retCode int)
proc: 
BEGIN 
	SET @strSQL = concat(
		"select tv.ordernum, tv.ticketnum, tv.rzd_id, ",
			"tv.lastname, tv.tripon, tv.from, tv.to, cl.name as client ",
			"from tickets_view tv ",
			"join users u on tv.name = u.name ",
			"join clients cl on cl.id = u.client_id");
	
	-- check if user is null, invalid or disabled
	if (runAs is null) then
		select 'runAs can''t be null';
		set retCode = -1;
		leave proc;
	else
		set @userid = 0;
		select id into @userid from users where name = runAs and disabled = false;
		if (@userid = 0) then
			set retCode = -1;
			select concat('user: ', runAs, ' is invalid or disabled');
			leave proc;
		else
			set @strSQL = 
				concat(@strSQL, " where cl.id = (select client_id from users where id=", @userid, ") ");
		end if;
	end if;

	if (allCriteria = 1) then
		set @strLogic = " and ";
	else
		set @strLogic = " or ";
	end if;

	set @strWhere = "";
	if (ticketNum is not null) then
		set @strWhere = concat(@strWhere, @strLogic, " tv.ticketnum = ", ticketNum);
	end if;

	if (lastName is not null and length(lastName) > 0) then
		set @strWhere = concat(@strWhere, @strLogic, " tv.lastname like '", upper(lastName), "%' ");
	end if;

	if (stationFrom is not null and length(stationFrom) > 0) then
		set @strWhere = concat(@strWhere, @strLogic, " tv.from like '", upper(stationFrom), "%' ");
	end if;

	if (stationTo is not null and length(stationTo) > 0) then
		set @strWhere = concat(@strWhere, @strLogic, " tv.to like '", upper(stationTo), "%' ");
	end if;

	if (dateFrom is not null) then
		set @strWhere = concat(@strWhere, @strLogic, " date(tv.tripon) >= '", date_format(dateFrom, '%Y-%m-%d'), "' ");
	end if;

	if (dateTo is not null) then
		set @strWhere = concat(@strWhere, @strLogic, " date(tv.tripon) <= '", date_format(dateTo, '%Y-%m-%d'), "' ");
	end if;

	if (length(@strWhere)) then
		if (allCriteria = 0) then
			set @strSQL = concat(@strSQL, " and (0 ", @strWhere, ")");
		else
			set @strSQL = concat(@strSQL, @strWhere);
		end if;
	end if;

	prepare stmt from @strSQL;
 	execute stmt;
	deallocate prepare stmt;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `all_transactions`
--

/*!50001 DROP TABLE IF EXISTS `all_transactions`*/;
/*!50001 DROP VIEW IF EXISTS `all_transactions`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`philipp`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `all_transactions` AS select `t`.`num` AS `ticket_num`,`o`.`createdon` AS `trans_date`,'sale' AS `trans_type`,`t`.`lastname` AS `passenger`,`u`.`name` AS `sales_user`,`cl`.`name` AS `client_name` from ((((`tickets` `t` join `users` `u`) join `clients` `cl`) join `orders` `o`) join `order_tickets` `ot`) where ((`t`.`id` = `ot`.`ticket_id`) and (`ot`.`order_id` = `o`.`id`) and (`o`.`createdby` = `u`.`id`) and (`u`.`client_id` = `cl`.`id`)) union all select `t`.`num` AS `ticket_num`,`ref`.`refundedon` AS `trans_date`,'refund' AS `trans_type`,`t`.`lastname` AS `passenger`,`u`.`name` AS `sales_user`,`cl`.`name` AS `client_name` from (((`tickets` `t` join `users` `u`) join `clients` `cl`) join `refunds` `ref`) where ((`t`.`id` = `ref`.`ticket_id`) and (`ref`.`refundedby` = `u`.`id`) and (`ref`.`ticket_id` = `t`.`id`) and (`u`.`client_id` = `cl`.`id`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `refund_transactions`
--

/*!50001 DROP TABLE IF EXISTS `refund_transactions`*/;
/*!50001 DROP VIEW IF EXISTS `refund_transactions`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`philipp`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `refund_transactions` AS select `t`.`num` AS `ticket_num`,`ref`.`refundedon` AS `trans_date`,`t`.`lastname` AS `passenger`,`u`.`name` AS `sales_user`,`cl`.`name` AS `client_name` from (((`tickets` `t` join `users` `u`) join `clients` `cl`) join `refunds` `ref`) where ((`t`.`id` = `ref`.`ticket_id`) and (`ref`.`refundedby` = `u`.`id`) and (`ref`.`ticket_id` = `t`.`id`) and (`u`.`client_id` = `cl`.`id`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `sale_transactions`
--

/*!50001 DROP TABLE IF EXISTS `sale_transactions`*/;
/*!50001 DROP VIEW IF EXISTS `sale_transactions`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`philipp`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `sale_transactions` AS select `t`.`num` AS `ticket_num`,`o`.`createdon` AS `trans_date`,`t`.`lastname` AS `passenger`,`u`.`name` AS `sales_user`,`cl`.`name` AS `client_name` from ((((`tickets` `t` join `users` `u`) join `clients` `cl`) join `orders` `o`) join `order_tickets` `ot`) where ((`t`.`id` = `ot`.`ticket_id`) and (`ot`.`order_id` = `o`.`id`) and (`o`.`createdby` = `u`.`id`) and (`u`.`client_id` = `cl`.`id`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `tickets_view`
--

/*!50001 DROP TABLE IF EXISTS `tickets_view`*/;
/*!50001 DROP VIEW IF EXISTS `tickets_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`philipp`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `tickets_view` AS select `o`.`num` AS `ordernum`,`t`.`num` AS `ticketnum`,`t`.`rzd_id` AS `rzd_id`,`t`.`lastname` AS `lastname`,`t`.`tripon` AS `tripon`,`t`.`tripfrom` AS `from`,`t`.`tripto` AS `to`,`t`.`price` AS `price`,`o`.`createdon` AS `createdon`,`u`.`name` AS `name` from (((`orders` `o` join `tickets` `t`) join `users` `u`) join `order_tickets` `ot`) where ((`o`.`id` = `ot`.`order_id`) and (`t`.`id` = `ot`.`ticket_id`) and (`o`.`createdby` = `u`.`id`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-11-22 23:03:45
