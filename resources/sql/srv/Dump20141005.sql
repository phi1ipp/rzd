SET NAMES utf8;

CREATE DATABASE  IF NOT EXISTS `ticket_service` 
	DEFAULT CHARACTER SET utf8
	DEFAULT collate utf8_general_ci;
USE `ticket_service`;
-- MySQL dump 10.13  Distrib 5.5.38, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: ticket_service
-- ------------------------------------------------------
-- Server version	5.5.38-0ubuntu0.14.04.1

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
CREATE TABLE `clients` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idclients_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL COMMENT 'row id',
  `num` int(11) NOT NULL COMMENT 'Номер заказа в нотификации rzd.ru',
  `createdon` datetime NOT NULL,
  `createdby` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ordernum_UNIQUE` (`num`),
  KEY `fk_orders_createdby_idx` (`createdby`),
  CONSTRAINT `fk_orders_createdby` FOREIGN KEY (`createdby`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Table structure for table `tickets`
--

DROP TABLE IF EXISTS `tickets`;

CREATE TABLE `tickets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `num` int(11) NOT NULL,
  `created` date NOT NULL,
  `lastname` varchar(128) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `num_UNIQUE` (`num`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

--
-- Table structure for table `order_tickets`
--

DROP TABLE IF EXISTS `order_tickets`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dump completed on 2014-10-05 17:45:09
