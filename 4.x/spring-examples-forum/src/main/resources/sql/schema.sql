DROP TABLE IF EXISTS `accounts`;
CREATE TABLE `accounts` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(255) NOT NULL default '' UNIQUE,
    `password_salt` CHAR(4) NOT NULL default '',
    `password_hash` CHAR(40) NOT NULL default '',
    `nickname` VARCHAR(255) NOT NULL default '',
    `status` TINYINT(1) NOT NULL default 0,
    PRIMARY KEY (`id`)
);
CREATE INDEX `accounts_email` ON `accounts` (`email`);
--INSERT INTO `accounts` (`email`, `password_salt`, `password_hash`, `nickname`, `status`)
--    VALUES ('alice@example.net', '0000', '70352f41061eda4ff3c322094af068ba70c3b38b', 'Alice', 1);
--INSERT INTO `accounts` (`email`, `password_salt`, `password_hash`, `nickname`, `status`)
--    VALUES ('bob@example.net', '0000', '70352f41061eda4ff3c322094af068ba70c3b38b', 'Bob', 1);
--INSERT INTO `accounts` (`email`, `password_salt`, `password_hash`, `nickname`, `status`)
--    VALUES ('carol@example.net', '0000', '70352f41061eda4ff3c322094af068ba70c3b38b', 'Carol', 1);
--INSERT INTO `accounts` (`email`, `password_salt`, `password_hash`, `nickname`, `status`)
--    VALUES ('ellen@example.net', '0000', '70352f41061eda4ff3c322094af068ba70c3b38b', 'Ellen', 1);
--INSERT INTO `accounts` (`email`, `password_salt`, `password_hash`, `nickname`, `status`)
--    VALUES ('frank@example.net', '0000', '70352f41061eda4ff3c322094af068ba70c3b38b', 'Frank', 1);
