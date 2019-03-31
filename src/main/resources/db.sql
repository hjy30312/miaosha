CREATE TABLE `miaosha_user` (
                                  `id` bigint(20) NOT NULL COMMENT '用户ID，手机号码',
                                  `nickname` varchar(255) NOT NULL,
                                  `password` varchar(32) DEFAULT NULL COMMENT 'MD5(MD5(pass铭文+固定salt) + sakt)',
                                  `salt` varchar(10) DEFAULT NULL,
                                  `head` varchar(120) DEFAULT NULL COMMENT '头像，云存储的ID',
                                  `register_data` datetime DEFAULT NULL COMMENT '注册时间',
                                  `last_login_date` datetime DEFAULT NULL COMMENT '上次登录时间',
                                  `login_count` int(11) DEFAULT '0' COMMENT '登录次数',
                                  PRIMARY KEY(`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;


CREATE TABLE `goods`(
                          `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
                          `goods_name` varchar(16) DEFAULT NULL COMMENT '商品名称',
                          `goods_title` varchar(64) DEFAULT NULL COMMENT '商品标题',
                          `goods_img` varchar(64) DEFAULT NULL COMMENT '商品的图片',
                          `goods_detail` longtext COMMENT '商品的详情信息',
                          `goods_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品单价',
                          `goods_stock` int(11) DEFAULT '0' COMMENT '商品库存，-1表示没有限制',
                          PRIMARY KEY(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

INSERT INTO `goods` VALUE(1,'iphoneX','Apple iphone X(A 1865) 64GB 银色 移动联通电信4G手机','/img/iphonex.png',
                          'Apple iphone X(A 1865) 64GB 银色 移动联通电信4G手机',12,11);

CREATE TABLE `miaosha_goods`(
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀商品ID',
                                  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品id',
                                  `miaosha_price` decimal(10,2) DEFAULT NULL COMMENT '库存价格',
                                  `stock_count` int(11) DEFAULT NULL COMMENT '库存数量',
                                  `start_date` datetime DEFAULT NULL COMMENT '秒杀开始时间',
                                  `end_date` datetime DEFAULT NULL COMMENT '秒杀结束时间',
                                  PRIMARY KEY(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `order_info`(
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
                               `goods_id` bigint(20) DEFAULT NULL COMMENT '商品id',
                               `delivery_addr_id` bigint(20) DEFAULT NULL COMMENT '收货地址id',
                               `goods_name` varchar(16) DEFAULT NULL COMMENT '冗余过来的商品名称',
                               `goods_count` int(11) DEFAULT '0' COMMENT '商品数量',
                               `goods_price` decimal(10,2) DEFAULT '0.00' COMMENT '商品单价',
                               `order_channel` tinyint(4) DEFAULT '0' COMMENT '1pc,2android,3ios',
                               `status` tinyint(4) DEFAULT '0' COMMENT '订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
                               `create_date` datetime DEFAULT NULL COMMENT '订单的创建时间',
                               `pay_date` datetime DEFAULT NULL COMMENT '支付时间',
                               PRIMARY KEY(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;















