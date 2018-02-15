-- ----------------------------
--  Table structure for `bus_score`
-- ----------------------------
DROP TABLE IF EXISTS `bus_score`;
CREATE TABLE `bus_score` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `ratio` decimal(10,2) DEFAULT NULL COMMENT '行为系数',
  `total_score` decimal(10,2) DEFAULT NULL COMMENT '总分',
  `credit_score` decimal(10,2) DEFAULT NULL COMMENT '信用分',
  `add_score` decimal(10,2) DEFAULT NULL COMMENT '加分项（总加分）',
  `status` tinyint(1) DEFAULT NULL COMMENT '加分状态  0正常  1加分 -1减分',
  `add_time` datetime DEFAULT NULL COMMENT '加分时间',
  `adjust_score` decimal(10,2) DEFAULT NULL COMMENT '调整分数（总分）',
  `adjust_time` datetime DEFAULT NULL COMMENT '调整分数时间',
  `sign_score` decimal(10,2) DEFAULT NULL COMMENT '签到分',
  `task_score` decimal(10,2) DEFAULT NULL COMMENT '任务分',
  `sign_fre` int(11) DEFAULT NULL COMMENT '签到次数',
  `pt_score` decimal(10,2) DEFAULT NULL COMMENT '普通提现',
  `kt_score` decimal(10,2) DEFAULT NULL COMMENT '快速提现',
  `cz_score` decimal(10,2) DEFAULT NULL COMMENT '充值',
  `lbs_score` decimal(10,2) DEFAULT NULL COMMENT 'lbs分',
  `ky_score` decimal(10,2) DEFAULT NULL COMMENT '酷雅分',
  `ws_score` decimal(10,2) DEFAULT NULL COMMENT '微商分',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userid_index` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户信用信息';

-- ----------------------------
--  Table structure for `bus_score_detail`
-- ----------------------------
DROP TABLE IF EXISTS `bus_score_detail`;
CREATE TABLE `bus_score_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bus_id` int(11) DEFAULT NULL COMMENT '信用分业务id',
  `bus_type` varchar(255) DEFAULT NULL COMMENT '业务类型 对应业务接口producttype',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作账号',
  `score` decimal(10,2) DEFAULT NULL COMMENT '分数',
  `adjust_time` datetime DEFAULT NULL COMMENT '分数调整时间',
  `valid_start_time` datetime DEFAULT NULL COMMENT '有效期开始时间',
  `valid_end_time` datetime DEFAULT NULL COMMENT '有效期结束时间',
  `task_id` int(4) DEFAULT NULL COMMENT '任务id',
  `memo` varchar(255) DEFAULT NULL COMMENT '备注',
  `source` tinyint(4) DEFAULT NULL COMMENT '数据来源 0是各业务方  1是大数据推送过来',
  `order_id` varchar(255) DEFAULT NULL COMMENT '订单id',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `quantity` int(11) DEFAULT NULL COMMENT '数量',
  `c_order_id` varchar(255) DEFAULT NULL COMMENT '取消订单id  微商使用',
  `action` tinyint(4) DEFAULT NULL COMMENT '行为类型 0正常  1终止业务  比如手动终止任务',
  `company_id` int(11) DEFAULT NULL COMMENT '公司id',
  `product_id` int(11) DEFAULT NULL COMMENT '产品id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间 对用timestmp',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `userid_index` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='用户信用信息明细';

-- ----------------------------
--  Table structure for `conf_base_set`
-- ----------------------------
DROP TABLE IF EXISTS `conf_base_set`;
CREATE TABLE `conf_base_set` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `oneday_score_limit` decimal(10,2) DEFAULT NULL COMMENT '用户单日上限总分',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态  0正常  1已下架',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='信用分基础设置';

-- ----------------------------
--  Table structure for `conf_bus`
-- ----------------------------
DROP TABLE IF EXISTS `conf_bus`;
CREATE TABLE `conf_bus` (
  `bus_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '业务id(自增)',
  `bus_name` varchar(255) DEFAULT NULL COMMENT '业务名称',
  `memo` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态:0-正常,1-已下架',
  `link` varchar(255) DEFAULT NULL COMMENT '跳转链接',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`bus_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='业务信用分配置表';

-- ----------------------------
--  Table structure for `conf_item`
-- ----------------------------
DROP TABLE IF EXISTS `conf_item`;
CREATE TABLE `conf_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bus_name` varchar(255) DEFAULT NULL COMMENT '业务名称',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `sub_title` varchar(255) DEFAULT NULL COMMENT '副标题',
  `image_url` varchar(255) DEFAULT NULL COMMENT '图片链接',
  `link_url` varchar(255) DEFAULT NULL COMMENT '跳转链接',
  `status` tinyint(1) DEFAULT NULL COMMENT '上下架状态 1:已下架 0:上架中',
  `class_name` varchar(255) DEFAULT NULL COMMENT '跳转关键字  以逗号隔开，逗号前是ios，逗号后是android',
  `class_type` tinyint(1) DEFAULT NULL COMMENT '跳转类型',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='行为系数加分项';

-- ----------------------------
--  Table structure for `conf_ratio`
-- ----------------------------
DROP TABLE IF EXISTS `conf_ratio`;
CREATE TABLE `conf_ratio` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `list_name` varchar(255) DEFAULT NULL COMMENT '权重表名称',
  `start_score` decimal(10,2) DEFAULT NULL COMMENT '起始分数',
  `end_score` decimal(10,2) DEFAULT NULL COMMENT '上限分数',
  `status` tinyint(1) DEFAULT '0' COMMENT '权重状态 0为上家  1为下架',
  `ratio` decimal(10,2) DEFAULT NULL COMMENT '行为系数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作账号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='行为系数收益权重';

-- ----------------------------
--  Table structure for `conf_sign`
-- ----------------------------
DROP TABLE IF EXISTS `conf_sign`;
CREATE TABLE `conf_sign` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `score` decimal(10,2) DEFAULT NULL COMMENT '签到加分',
  `total_score_limit` decimal(10,2) DEFAULT NULL COMMENT '总分上限 无上限传空',
  `memo` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态 0-为正常,1-为已下架',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='签到配置';

-- ----------------------------
--  Table structure for `conf_task`
-- ----------------------------
DROP TABLE IF EXISTS `conf_task`;
CREATE TABLE `conf_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `task_id` int(11) NOT NULL COMMENT '任务id',
  `add_score` decimal(10,2) DEFAULT NULL COMMENT '任务加分',
  `sub_score` decimal(10,2) DEFAULT NULL COMMENT '提前结束任务惩罚',
  `is_duplicate` char(50) DEFAULT NULL COMMENT '是否可重复 N为不重复，Y为重复',
  `memo` varchar(255) DEFAULT NULL COMMENT '备注',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作账号',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态 :0-正常, 1-下架',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `un_task_id` (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='任务配置';

-- ----------------------------
--  Table structure for `conf_task_set`
-- ----------------------------
DROP TABLE IF EXISTS `conf_task_set`;
CREATE TABLE `conf_task_set` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `oneday_score` decimal(10,2) DEFAULT NULL COMMENT '单日上限 无上限值传-1',
  `total_score` decimal(10,2) DEFAULT NULL COMMENT '总分上限 无上限值传-1',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `operator` varchar(30) DEFAULT NULL COMMENT '操作人',
  `status` tinyint(2) DEFAULT NULL COMMENT '状态,0正常，1历史',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='任务基础设置';

-- ----------------------------
--  Table structure for `sys_config`
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `sys_key` varchar(32) NOT NULL DEFAULT '' COMMENT '系统配置key',
  `sys_desc` varchar(32) NOT NULL DEFAULT '' COMMENT '配置描述',
  `sys_value` varchar(1000) NOT NULL DEFAULT '' COMMENT '系统配置值',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `edit` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可修改：0-不可修改，1-可修改',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_sys_config_key` (`sys_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='系统配置';
INSERT INTO `sys_config` VALUES ('1', 'creditScore', '信用分数', '401', '2017-11-23 17:20:13', '2017-11-24 08:35:29', '1'), ('2', 'operator', '账号字典', '[{\"key\":\"admin\",\"value\":\"admin\"}]', '2017-11-24 08:28:32', '2017-11-24 09:30:55', '1'), ('3', 'busType', '业务字典', '[{\"key\":\"签到\",\"value\":\"签到\"},{\"key\":\"任务\",\"value\":\"任务\"},{\"key\":\"人工调整\",\"value\":\"人工调整\"}]', '2017-11-24 08:31:13', '2017-11-24 08:52:51', '1');

-- ----------------------------
--  Table structure for `conf_cash`
-- ----------------------------
DROP TABLE IF EXISTS `conf_cash`;
CREATE TABLE `conf_cash` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ratio` decimal(10,2) DEFAULT NULL COMMENT '充值系数',
  `total_score_limit` decimal(10,2) DEFAULT NULL COMMENT '总分上限: -1-无上限传空,其他-上限值',
  `memo` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态 0-为正常,1-为已下架',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='提现配置';

-- ----------------------------
--  Table structure for `conf_deposit`
-- ----------------------------
DROP TABLE IF EXISTS `conf_deposit`;
CREATE TABLE `conf_deposit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ratio` decimal(10,2) DEFAULT NULL COMMENT '充值系数',
  `total_score_limit` decimal(10,2) DEFAULT NULL COMMENT '总分上限: -1-无上限传空,其他-上限值',
  `memo` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态 0-为正常,1-为已下架',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='充值配置';

-- ----------------------------
--  Table structure for `conf_quick_cash`
-- ----------------------------
DROP TABLE IF EXISTS `conf_quick_cash`;
CREATE TABLE `conf_quick_cash` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ratio` decimal(10,2) DEFAULT NULL COMMENT '充值系数',
  `total_score_limit` decimal(10,2) DEFAULT NULL COMMENT '总分上限: -1-无上限传空,其他-上限值',
  `memo` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态 0-为正常,1-为已下架',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='快速提现配置';