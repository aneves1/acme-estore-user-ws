CREATE DATABASE `online_store` /*!40100 DEFAULT CHARACTER SET latin1 */;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(250) DEFAULT NULL,
  `user_first_name` varchar(45) DEFAULT NULL,
  `user_last_name` varchar(45) DEFAULT NULL,
  `user_password` varchar(250) DEFAULT NULL,
  `user_email` varchar(45) DEFAULT NULL,
  `user_email_verification_token` varchar(50) DEFAULT NULL,
  `user_email_verification_status` tinyint(1) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `email_verification_token` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id_unique` (`user_id`),
  UNIQUE KEY `user_email_unique` (`user_email`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=latin1;

CREATE TABLE `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_entity_id` int(11) NOT NULL,
  `address_id` varchar(30) NOT NULL,
  `address_city` varchar(30) NOT NULL,
  `address_country` varchar(15) NOT NULL,
  `address_street` varchar(15) NOT NULL,
  `address_zipcode` varchar(7) NOT NULL,
  `address_type` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_entity_id` (`user_entity_id`),
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`user_entity_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=86 DEFAULT CHARSET=latin1;

CREATE TABLE `customers` (
  `customer_id` int(11) NOT NULL AUTO_INCREMENT,
  `org_or_person` varchar(45) DEFAULT NULL,
  `org_name` varchar(45) DEFAULT NULL,
  `gender` varchar(45) DEFAULT NULL,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `email_address` varchar(45) DEFAULT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  `address_line_1` varchar(45) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `country` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


