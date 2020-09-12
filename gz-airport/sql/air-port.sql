DROP TABLE IF EXISTS `sms_log`;
CREATE TABLE `sms_log` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `mobile` char(11) NOT NULL COMMENT '手机号',
  `content` varchar(200) DEFAULT NULL COMMENT '内容',
	`biz_type` varchar(10) DEFAULT NULL COMMENT '业务类型',
	`host` varchar(50) DEFAULT NULL COMMENT '主机',
  `gmt_create` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` varchar(50) DEFAULT NULL COMMENT '创建人',
  `gmt_modified` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `modifier` varchar(50) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  KEY `index_mobile` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COMMENT='短信记录';