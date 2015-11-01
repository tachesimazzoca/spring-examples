DROP TABLE accounts;
CREATE TABLE `accounts` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL default '' UNIQUE,
  `status` TINYINT(1) NOT NULL default 0,
   PRIMARY KEY (`id`)
);
