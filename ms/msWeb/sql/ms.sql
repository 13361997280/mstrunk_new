
-- ----------------------------
--  Table structure for `act_send`
-- ----------------------------
DROP TABLE IF EXISTS `act_send`;
CREATE TABLE `act_send` (
  `id` int(11) DEFAULT NULL COMMENT '推送id',
  `name` varchar(255) DEFAULT NULL COMMENT '活动名称',
  `send_tunnel` tinyint(4) DEFAULT NULL COMMENT '推送渠道 1短信  2h5',
  `tunnel_id` int(11) DEFAULT NULL COMMENT '通道',
  `tunnel_name` varchar(255) DEFAULT NULL COMMENT '通道名称',
  `select_num` int(11) DEFAULT NULL COMMENT '目标客群数量',
  `act_num` int(11) DEFAULT NULL COMMENT '激活用户数量',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `message_id` int(11) DEFAULT NULL COMMENT '消息id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动推送表';

-- ----------------------------
--  Table structure for `message`
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
  `id` int(11) NOT NULL COMMENT '消息id',
  `title` varchar(255) DEFAULT NULL COMMENT '消息标题',
  `content` varchar(255) DEFAULT NULL COMMENT '消息内容',
  `type` tinyint(4) DEFAULT NULL COMMENT '消息类型',
  `link` varchar(255) DEFAULT NULL COMMENT '跳转链接',
  `logo` varchar(255) DEFAULT NULL COMMENT 'logo链接',
  `short_content` varchar(255) DEFAULT NULL COMMENT '短文',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `tunnel`
-- ----------------------------
DROP TABLE IF EXISTS `tunnel`;
CREATE TABLE `tunnel` (
  `id` int(11) NOT NULL COMMENT '通道设置id',
  `name` varchar(255) DEFAULT NULL COMMENT '通道设置名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='通道设置表';

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL COMMENT 'userid',
  `name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `enable` tinyint(4) DEFAULT NULL COMMENT '有效性',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `mobile` varchar(255) DEFAULT NULL COMMENT '手机号',
  `group_ids` varchar(11) DEFAULT NULL COMMENT '组id',
  `update_time` date DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
--  Table structure for `user_group`
-- ----------------------------
DROP TABLE IF EXISTS `user_group`;
CREATE TABLE `user_group` (
  `id` int(11) NOT NULL COMMENT '组id',
  `name` varchar(255) DEFAULT NULL COMMENT '组名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户组表';

-- ----------------------------
--  Table structure for `user_label`
-- ----------------------------
DROP TABLE IF EXISTS `user_label`;
CREATE TABLE `user_label` (
  `e_user_id` bigint(20) NOT NULL COMMENT '用户id',
  `user_channel` tinyint(1) DEFAULT NULL COMMENT '用户群体',
  `base_sex` char(50) DEFAULT NULL COMMENT '性别',
  `base_age` int(11) DEFAULT NULL COMMENT '年龄',
  `base_marriage` tinyint(4) DEFAULT NULL COMMENT '婚姻',
  `base_girl` int(11) DEFAULT NULL COMMENT '子女－女',
  `base_son` int(11) DEFAULT NULL COMMENT '子女－男',
  `base_bacholr` tinyint(4) DEFAULT NULL COMMENT '学历',
  `base_career` varchar(32) DEFAULT NULL COMMENT '职业',
  `base_born_addr` varchar(32) DEFAULT NULL COMMENT '身份证归属地',
  `base_mob_addr` varchar(32) DEFAULT NULL COMMENT '手机归属地',
  `base_regit_time` date DEFAULT NULL COMMENT '注册时间',
  `base_hobbie` varchar(32) DEFAULT NULL COMMENT '爱好',
  `base_phone_brand` tinyint(4) DEFAULT NULL COMMENT '手机品牌',
  `base_phone_type` varchar(255) DEFAULT NULL COMMENT '手机型号',
  `base_phone_sys` tinyint(4) DEFAULT NULL COMMENT '操作系统',
  `base_phone_screen` int(11) DEFAULT NULL COMMENT '屏幕尺寸',
  `base_pc_brower` tinyint(4) DEFAULT NULL COMMENT 'pc浏览器',
  `base_car_brand` varchar(255) DEFAULT NULL COMMENT '车辆型号／品牌',
  `base_email` char(4) DEFAULT NULL COMMENT '是否有email',
  `base_weixin` char(4) DEFAULT NULL COMMENT '是否有微信',
  `base_qq` char(4) DEFAULT NULL COMMENT '是否有qq',
  `base_weibo` char(4) DEFAULT NULL COMMENT '是否有微博',
  `base_time_on_net` varchar(255) DEFAULT NULL COMMENT '集中上网时段',
  `base_lbs_live` varchar(255) DEFAULT NULL COMMENT '第一生活位置',
  `base_lbs_home` varchar(255) DEFAULT NULL COMMENT '居住位置',
  `base_lbs_home_code` varchar(255) DEFAULT NULL COMMENT '居住位置行政区码',
  `base_lbs_home_addr` varchar(255) DEFAULT NULL COMMENT '居住位置具体门牌号',
  `base_lbs_work_xy` varchar(255) DEFAULT NULL COMMENT '工作位置',
  `base_lbs_work_code` varchar(255) DEFAULT NULL COMMENT '工作位置行政区码',
  `base_lbs_work_addr` varchar(255) DEFAULT NULL COMMENT '工作位置具体门牌号',
  `buy_goods_prefer` varchar(255) DEFAULT NULL COMMENT '品类偏好',
  `buy_avg_order_pric` varchar(255) DEFAULT NULL COMMENT '平均客单价',
  `buy_order_count` int(11) DEFAULT NULL COMMENT '总购买次数',
  `buy_order_sum` decimal(10,0) DEFAULT NULL COMMENT '总购买金额',
  `buy_last_time` date DEFAULT NULL COMMENT '最近一次支付时间',
  `buy_last_amount` decimal(10,0) DEFAULT NULL COMMENT '最近一次购买金额',
  `buy_max_time_gap` int(11) DEFAULT NULL COMMENT '消费最大间隔时间',
  `buy_min_time_gap` int(11) DEFAULT NULL COMMENT '消费最小间隔时间',
  `buy_refund_count` int(11) DEFAULT NULL COMMENT '退单总次数',
  `buy_complain_count` int(11) DEFAULT NULL COMMENT '投诉总次数',
  `buy_payment_rate` decimal(10,0) DEFAULT NULL COMMENT '订单支付率',
  `buy_card_count` int(11) DEFAULT NULL COMMENT '绑定银行卡数',
  `buy_sim_user10` varchar(255) DEFAULT NULL COMMENT '同购行为最相似用户10个',
  `seller_is_certifer` char(50) DEFAULT NULL COMMENT '是否认证商家',
  `seller_history_level` varchar(255) DEFAULT NULL COMMENT '商家历史最高等级',
  `selller_sales_month` int(11) DEFAULT NULL COMMENT '商家平均月销量',
  `online_collect_count` int(11) DEFAULT NULL COMMENT '三个月收藏次数',
  `online_applaud_count` int(11) DEFAULT NULL COMMENT '三个月点赞次数',
  `online_share_count` int(11) DEFAULT NULL COMMENT '1个月转发次数',
  `online_search_key` varchar(255) DEFAULT NULL COMMENT '60天搜索关键字top10',
  `online_search_count` int(11) DEFAULT NULL COMMENT '60天搜索次数',
  `online_searh_rate` varchar(255) DEFAULT NULL COMMENT '60天搜索下单转化率',
  `online_active_score` int(11) DEFAULT NULL COMMENT '活跃值',
  `online_brw_fenxiao` int(11) DEFAULT NULL COMMENT '分销大厅／有',
  `online_brw_guangao` int(11) DEFAULT NULL COMMENT '广告大厅',
  `onlian_brw_baogou` int(11) DEFAULT NULL COMMENT '宝购',
  `onlian_brw_leipai` int(11) DEFAULT NULL COMMENT '雷拍',
  `onlian_brw_baoyue` int(11) DEFAULT NULL COMMENT '宝约',
  `onlian_brw_hongbao` int(11) DEFAULT NULL COMMENT '红包',
  `onlian_brw_yisheng` int(11) DEFAULT NULL COMMENT '医生',
  `onlian_brw_data_ass` int(11) DEFAULT NULL COMMENT '数据助手',
  `onlian_brw_kuya` int(11) DEFAULT NULL COMMENT '酷雅',
  `onlian_brw_haohuo` int(11) DEFAULT NULL COMMENT '有好货',
  `onlian_brw_youhuo` int(11) DEFAULT NULL COMMENT '优活',
  `onlian_brw_youpiao` int(11) DEFAULT NULL COMMENT '电影票',
  `onlian_brw_qzhibao` int(11) DEFAULT NULL COMMENT 'Q宝直播',
  `onlian_brw_jingedun` int(11) DEFAULT NULL COMMENT '金戈盾',
  `onlian_brw_market` int(11) DEFAULT NULL COMMENT '超市',
  `onlian_brw_h5game` int(11) DEFAULT NULL COMMENT 'h5游戏',
  `invest_mechant_sum` decimal(10,0) DEFAULT NULL COMMENT '商家资产',
  `invest_qb` int(11) DEFAULT NULL COMMENT '宝币',
  `invest_quan` int(11) DEFAULT NULL COMMENT '宝券',
  `invest_coupon` varchar(255) DEFAULT NULL COMMENT '优惠券使用／失效比［90天］',
  `invest_output_rate` varchar(255) DEFAULT NULL COMMENT '当天减持比率',
  `invest_input_rate` varchar(255) DEFAULT NULL COMMENT '当天追加资金比率',
  `invest_is_assurance` char(50) DEFAULT NULL COMMENT '是否购买保险',
  `invest_is_data_ass` char(50) DEFAULT NULL COMMENT '是否订阅数据助手',
  `invest_account_assure` char(50) DEFAULT NULL COMMENT '是否订阅账户安全险',
  `invest_is_jingedun` char(50) DEFAULT NULL COMMENT '是否订阅金戈盾',
  `invest_rate_30_all` varchar(255) DEFAULT NULL COMMENT '30天总的投资回报率［宝币］',
  `invest_type_buy` char(50) DEFAULT NULL COMMENT '消费型',
  `invest_type_sure` char(50) DEFAULT NULL COMMENT '稳健型',
  `invest_type_risk` char(50) DEFAULT NULL COMMENT '赌博型',
  `invest_type_analy` char(50) DEFAULT NULL COMMENT '数据型',
  `invest_period_balance` char(50) DEFAULT NULL COMMENT '持平阶段',
  `invest_period_addin` char(50) DEFAULT NULL COMMENT '追加投资阶段',
  `invest_period_updown` char(50) DEFAULT NULL COMMENT '减持阶段',
  `invest_period_inlook` char(50) DEFAULT NULL COMMENT '观望阶段',
  `invest_charg_max_90` decimal(10,0) DEFAULT NULL COMMENT '最大充值金额［3个月］',
  `invest_charg_max` decimal(10,0) DEFAULT NULL COMMENT '最大充值金额［开户以来］',
  `invest_charg_min_90` decimal(10,0) DEFAULT NULL COMMENT '最小充值金额［3个月］',
  `invest_charg_min` decimal(10,0) DEFAULT NULL COMMENT '最小充值金额［开户以来］',
  `nvest_refund-max_90` decimal(10,0) DEFAULT NULL COMMENT '最大提现金额［3个月］',
  `invest_refund-max` decimal(10,0) DEFAULT NULL COMMENT '最大提现金额［开户以来］',
  `invest_refund_count_90` int(11) DEFAULT NULL COMMENT '提现次数［3个月］',
  `invest_refund_count` int(11) DEFAULT NULL COMMENT '提现次数［开户以来］',
  `invest_refund_rate` varchar(255) DEFAULT NULL COMMENT '提现资金比',
  `invest_refund_month` decimal(10,0) DEFAULT NULL COMMENT '月平均提现次数',
  `invest_fast_refund` int(11) DEFAULT NULL COMMENT '快速提现次数［开户以来］',
  `invest_fast_refund_90` int(11) DEFAULT NULL COMMENT '快速提现次数［3个月］',
  `invest_refund_min_gap` int(11) DEFAULT NULL COMMENT '最小提现间隔时间[开户以来］',
  `invest_refund_min_gap_90` int(11) DEFAULT NULL COMMENT '最小提现间隔时间［3个月］',
  `invest_refund_max_gap` int(11) DEFAULT NULL COMMENT '最大充值间隔时间［开户以来］',
  `invest_charg_max_gap_90` int(11) DEFAULT NULL COMMENT '最大充值间隔时间［3个月］',
  `invest_refund_time` date DEFAULT NULL COMMENT '最近一次提现时间',
  `invest_forbiden_time` date DEFAULT NULL COMMENT '最近一次封号时间［单独放风控］',
  `leisure_film_30` int(11) DEFAULT NULL COMMENT '30天观看电影场次＋演出',
  `leisure_travel_30` int(11) DEFAULT NULL COMMENT '最近30天旅游产品消费次数',
  `leisure_show_times` int(11) DEFAULT NULL COMMENT '最近30天做主播次数',
  `leisure_tv_follow_count` int(11) DEFAULT NULL COMMENT '关注的主播数量',
  `leisure_qdou_out` int(11) DEFAULT NULL COMMENT '30天q豆消费数',
  `leisure_qdou_count` int(11) DEFAULT NULL COMMENT '30天q豆充值数',
  `leisure_gift_count` int(11) DEFAULT NULL COMMENT '30天收到的礼物次数',
  `leisure_tv_fans_all` int(11) DEFAULT NULL COMMENT '今天粉丝总数量',
  `leisure_fans_add` int(11) DEFAULT NULL COMMENT '30天粉丝增长数量',
  `leisure_tv_watch_count` int(11) DEFAULT NULL COMMENT '最近30天进入直播间次数',
  `commut_present_count` int(11) DEFAULT NULL COMMENT '邀请好友数',
  `commut_present_count_90` int(11) DEFAULT NULL COMMENT '90天邀请好友数',
  `commut_product_share` int(11) DEFAULT NULL COMMENT '分销次数',
  `commut_product_share_90` int(11) DEFAULT NULL COMMENT '90天分销次数',
  `commut_product_share_price_avg` decimal(10,0) DEFAULT NULL COMMENT '90天分销产品平均保证金',
  `commut_product_share_price_avg_90` decimal(10,0) DEFAULT NULL COMMENT '分销产品平均保证金',
  `lbs_qbike_count_7` int(11) DEFAULT NULL COMMENT '7天骑行次数',
  `lbs_qbike_distence_avg` decimal(10,0) DEFAULT NULL COMMENT '骑行平均距离',
  `lbs_zone_top_5` varchar(255) DEFAULT NULL COMMENT '骑行商圈位置top',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`e_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户标签表';

-- ----------------------------
--  Table structure for `user_lable_dict`
-- ----------------------------
DROP TABLE IF EXISTS `user_lable_dict`;
CREATE TABLE `user_lable_dict` (
  `id` int(11) NOT NULL COMMENT '字典id',
  `p_type` varchar(4) DEFAULT NULL COMMENT '所属类别',
  `name` varchar(255) DEFAULT NULL COMMENT '标签名称',
  `option` varchar(255) DEFAULT NULL COMMENT '选项组合json字段（labelType:value,option:{key:value,key1:value,}）下拉框类型',
  `sort` int(11) DEFAULT NULL COMMENT '排序字段',
  `color` tinyint(255) DEFAULT NULL COMMENT '柱状图颜色',
  `graf_type` tinyint(4) DEFAULT NULL COMMENT '图形结构（1,柱状图；2,饼图）',
  `code` varchar(255) DEFAULT NULL COMMENT '标签code',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='标签字典';

-- ----------------------------
--  Table structure for `user_select_detail`
-- ----------------------------
DROP TABLE IF EXISTS `user_select_detail`;
CREATE TABLE `user_select_detail` (
  `id` int(11) NOT NULL COMMENT '主健id',
  `select_id` int(11) DEFAULT NULL COMMENT '客群id',
  `user_ids` blob COMMENT '用户id串',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客群明细';

-- ----------------------------
--  Table structure for `user_select_group`
-- ----------------------------
DROP TABLE IF EXISTS `user_select_group`;
CREATE TABLE `user_select_group` (
  `id` int(11) NOT NULL COMMENT '客群id',
  `name` varchar(255) DEFAULT NULL COMMENT '客群名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `num` int(11) DEFAULT NULL COMMENT '客群数量',
  `user_id` int(11) DEFAULT NULL COMMENT '操作用户',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `desc` varchar(255) DEFAULT NULL COMMENT '客群描述',
  `message_id` int(11) DEFAULT NULL COMMENT '消息id',
  `user_select_id` int(11) DEFAULT NULL COMMENT '客群id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户客群组';
