DROP TABLE IF EXISTS `item`;
CREATE TABLE IF NOT EXISTS `item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `price` decimal(19,2) NOT NULL,
  `quantity` bigint(20) NOT NULL,
  `category` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
);
