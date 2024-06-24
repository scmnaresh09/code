@UP
DROP TABLE IF EXISTS `qwick`.`users`;
CREATE TABLE  `qwick`.`users` (
  `id` bigint(20) NOT NULL auto_increment,
  `email` varchar(150) default NULL,
  `first_name` varchar(30) default NULL,
  `last_name` varchar(30) default NULL,
  `password` varchar(200) default NULL,
  `userid` varchar(200),
  `signup_date` datetime default NULL,
  `status` char(1) default NULL,
  `verification_token` bigint(20) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `qwick`.`users` ADD UNIQUE (`verification_token`);

@UP

@DOWN
DROP TABLE IF EXISTS `qwick`.`users`;
@DOWN