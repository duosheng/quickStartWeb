-- auto Generated on 2017-05-12 18:05:13 
-- DROP TABLE IF EXISTS `biz_system`; 
CREATE TABLE `biz_system`(
    `id` VARCHAR (50) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `number` VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'number',
    `name` VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'name',
    `owner` VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'owner',
    `owner_contact` VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'ownerContact',
    `port` INT (11) NOT NULL DEFAULT -1 COMMENT 'port',
    `http_port` INT (11) NOT NULL DEFAULT -1 COMMENT 'httpPort',
    `description` VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'description',
    `product_id` INT (11) NOT NULL DEFAULT -1 COMMENT 'productId',
    `zk` VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'zk',
    `dimension` BIGINT (15) NOT NULL DEFAULT -1 COMMENT 'dimension',
    `domain` VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'domain',
    `product` VARCHAR (50) NOT NULL DEFAULT '' COMMENT 'product',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '`biz_system`';
