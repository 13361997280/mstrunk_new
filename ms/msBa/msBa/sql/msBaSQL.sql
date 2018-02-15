
CREATE TABLE `admin_ass_users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `enabled` bit(1) NOT NULL COMMENT '是否有效 1: 有效,0:无效',
  `realname` varchar(20) DEFAULT NULL COMMENT '昵称',
  `department_id` int(11) DEFAULT NULL COMMENT '部门Id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_username` varchar(50) DEFAULT NULL COMMENT '创建人',
  `is_modify_pwd` tinyint(4) DEFAULT '1' COMMENT '是否需要修改密码',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户信息表';


insert into `admin_ass_users` (`user_id`, `username`, `password`, `enabled`, `realname`, `department_id`, `create_time`, `create_username`, `is_modify_pwd`) values('1','admin','743e3a656b5649136cbf2b75d7107844',0x1,NULL,NULL,NULL,NULL,'1');


CREATE TABLE `admin_ass_authorities` (
  `authority_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `authority` varchar(50) NOT NULL DEFAULT '' COMMENT '权限码',
  PRIMARY KEY (`authority_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='权限表';

insert into `admin_ass_authorities` (`authority_id`, `username`, `authority`) values('2','admin','ROLE_ADMIN');
insert into `admin_ass_authorities` (`authority_id`, `username`, `authority`) values('3','admin','ROLE_CONFIG');
insert into `admin_ass_authorities` (`authority_id`, `username`, `authority`) values('4','admin','ROLE_FINANCE_ADMIN');
insert into `admin_ass_authorities` (`authority_id`, `username`, `authority`) values('5','admin','ROLE_USER');

