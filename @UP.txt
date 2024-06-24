@UP
DROP TABLE IF EXISTS `qwick`.`user_login_ids`;
CREATE TABLE  `qwick`.`user_login_ids` (
  `id` bigint(20) NOT NULL auto_increment,
  `email` varchar(255) default NULL,
  `user_id` bigint(20) default NULL,
  `status` char(1) default NULL,
  `type` int(11) default NULL,
  `verification_token` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `qwick`.`user_login_ids` ADD FOREIGN KEY (`user_id`)  REFERENCES `users` (`id`);
ALTER TABLE `qwick`.`user_login_ids` ADD UNIQUE (`email`);
@UP

@DOWN
DROP TABLE IF EXISTS `qwick`.`user_login_ids`;
@DOWN