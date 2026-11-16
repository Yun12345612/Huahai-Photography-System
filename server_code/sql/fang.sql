/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50735
Source Host           : localhost:3306
Source Database       : fang

Target Server Type    : MYSQL
Target Server Version : 50735
File Encoding         : 65001

Date: 2025-06-05 17:47:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for baomingxinxi
-- ----------------------------
DROP TABLE IF EXISTS `baomingxinxi`;
CREATE TABLE `baomingxinxi` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `huodongmingcheng` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动名称',
  `huodongtupian` longtext COLLATE utf8mb4_unicode_ci COMMENT '活动图片',
  `kaishishijian` datetime DEFAULT NULL COMMENT '开始时间',
  `jieshushijian` datetime DEFAULT NULL COMMENT '结束时间',
  `huodongdidian` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活动地点',
  `yonghuzhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户账号',
  `yonghuxingming` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户姓名',
  `crossuserid` bigint(20) DEFAULT NULL COMMENT '跨表用户id',
  `crossrefid` bigint(20) DEFAULT NULL COMMENT '跨表主键id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='报名信息';

-- ----------------------------
-- Records of baomingxinxi
-- ----------------------------
INSERT INTO `baomingxinxi` VALUES ('1', '2025-05-22 16:27:01', '活动名称1', 'file/baomingxinxiHuodongtupian1.jpg,file/baomingxinxiHuodongtupian2.jpg,file/baomingxinxiHuodongtupian3.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点1', '用户账号1', '用户姓名1', '1', '1');
INSERT INTO `baomingxinxi` VALUES ('2', '2025-05-22 16:27:01', '活动名称2', 'file/baomingxinxiHuodongtupian2.jpg,file/baomingxinxiHuodongtupian3.jpg,file/baomingxinxiHuodongtupian4.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点2', '用户账号2', '用户姓名2', '2', '2');
INSERT INTO `baomingxinxi` VALUES ('3', '2025-05-22 16:27:01', '活动名称3', 'file/baomingxinxiHuodongtupian3.jpg,file/baomingxinxiHuodongtupian4.jpg,file/baomingxinxiHuodongtupian5.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点3', '用户账号3', '用户姓名3', '3', '3');
INSERT INTO `baomingxinxi` VALUES ('4', '2025-05-22 16:27:01', '活动名称4', 'file/baomingxinxiHuodongtupian4.jpg,file/baomingxinxiHuodongtupian5.jpg,file/baomingxinxiHuodongtupian6.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点4', '用户账号4', '用户姓名4', '4', '4');
INSERT INTO `baomingxinxi` VALUES ('5', '2025-05-22 16:27:01', '活动名称5', 'file/baomingxinxiHuodongtupian5.jpg,file/baomingxinxiHuodongtupian6.jpg,file/baomingxinxiHuodongtupian7.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点5', '用户账号5', '用户姓名5', '5', '5');
INSERT INTO `baomingxinxi` VALUES ('6', '2025-05-22 16:27:01', '活动名称6', 'file/baomingxinxiHuodongtupian6.jpg,file/baomingxinxiHuodongtupian7.jpg,file/baomingxinxiHuodongtupian8.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点6', '用户账号6', '用户姓名6', '6', '6');
INSERT INTO `baomingxinxi` VALUES ('7', '2025-05-22 16:27:01', '活动名称7', 'file/baomingxinxiHuodongtupian7.jpg,file/baomingxinxiHuodongtupian8.jpg,file/baomingxinxiHuodongtupian9.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点7', '用户账号7', '用户姓名7', '7', '7');
INSERT INTO `baomingxinxi` VALUES ('8', '2025-05-22 16:27:01', '活动名称8', 'file/baomingxinxiHuodongtupian8.jpg,file/baomingxinxiHuodongtupian9.jpg,file/baomingxinxiHuodongtupian10.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点8', '用户账号8', '用户姓名8', '8', '8');
INSERT INTO `baomingxinxi` VALUES ('9', '2025-05-22 16:27:01', '活动名称9', 'file/baomingxinxiHuodongtupian9.jpg,file/baomingxinxiHuodongtupian10.jpg,file/baomingxinxiHuodongtupian11.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点9', '用户账号9', '用户姓名9', '9', '9');
INSERT INTO `baomingxinxi` VALUES ('10', '2025-05-22 16:27:01', '活动名称10', 'file/baomingxinxiHuodongtupian10.jpg,file/baomingxinxiHuodongtupian11.jpg,file/baomingxinxiHuodongtupian12.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点10', '用户账号10', '用户姓名10', '10', '10');

-- ----------------------------
-- Table structure for chat
-- ----------------------------
DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `adminid` bigint(20) DEFAULT NULL COMMENT '管理员id',
  `ask` longtext COLLATE utf8mb4_unicode_ci COMMENT '提问内容',
  `reply` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  `isreply` int(11) DEFAULT NULL COMMENT '是否回复',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='在线咨询';

-- ----------------------------
-- Records of chat
-- ----------------------------
INSERT INTO `chat` VALUES ('1', '2025-05-22 16:27:00', '1', '提问内容1', '回复内容1', '1', '1');
INSERT INTO `chat` VALUES ('2', '2025-05-22 16:27:00', '2', '提问内容2', '回复内容2', '2', '2');
INSERT INTO `chat` VALUES ('3', '2025-05-22 16:27:00', '3', '提问内容3', '回复内容3', '3', '3');
INSERT INTO `chat` VALUES ('4', '2025-05-22 16:27:00', '4', '提问内容4', '回复内容4', '4', '4');
INSERT INTO `chat` VALUES ('5', '2025-05-22 16:27:00', '5', '提问内容5', '回复内容5', '5', '5');
INSERT INTO `chat` VALUES ('6', '2025-05-22 16:27:00', '6', '提问内容6', '回复内容6', '6', '6');
INSERT INTO `chat` VALUES ('7', '2025-05-22 16:27:00', '7', '提问内容7', '回复内容7', '7', '7');
INSERT INTO `chat` VALUES ('8', '2025-05-22 16:27:00', '8', '提问内容8', '回复内容8', '8', '8');
INSERT INTO `chat` VALUES ('9', '2025-05-22 16:27:00', '9', '提问内容9', '回复内容9', '9', '9');
INSERT INTO `chat` VALUES ('10', '2025-05-22 16:27:00', '10', '提问内容10', '回复内容10', '10', '10');
INSERT INTO `chat` VALUES ('11', '2025-05-30 23:49:52', null, '你好', null, '0', '61');
INSERT INTO `chat` VALUES ('12', '2025-05-30 23:50:31', '1', null, '好的', '0', '61');
INSERT INTO `chat` VALUES ('13', '2025-05-30 23:50:31', '1', null, '好的', '0', '61');
INSERT INTO `chat` VALUES ('14', '2025-06-04 23:38:26', null, '你好，请问都有哪些活动？\n', null, '0', '1748678347218');
INSERT INTO `chat` VALUES ('15', '2025-06-04 23:39:00', '1', null, '你好\n', '0', '1748678347218');

-- ----------------------------
-- Table structure for config
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '名称',
  `value` longtext COLLATE utf8mb4_unicode_ci COMMENT '值',
  `url` longtext COLLATE utf8mb4_unicode_ci COMMENT '链接',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='轮播图';

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES ('1', '2025-05-22 16:27:01', 'swiper1', 'file/1749096747704.jpg', 'https://www.mnust.edu.cn/index.htm');
INSERT INTO `config` VALUES ('2', '2025-05-22 16:27:01', 'swiper2', 'file/1749096076196.jpg', 'https://www.mnust.edu.cn/index.htm');
INSERT INTO `config` VALUES ('3', '2025-05-22 16:27:01', 'swiper3', 'file/1749095772903.png', 'https://www.mnust.edu.cn/index.htm');

-- ----------------------------
-- Table structure for discusssheyingshixinxi
-- ----------------------------
DROP TABLE IF EXISTS `discusssheyingshixinxi`;
CREATE TABLE `discusssheyingshixinxi` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `refid` bigint(20) NOT NULL COMMENT '关联表id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `avatarurl` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `nickname` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `score` double DEFAULT NULL COMMENT '评分',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `reply` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='摄影师信息评论表';

-- ----------------------------
-- Records of discusssheyingshixinxi
-- ----------------------------

-- ----------------------------
-- Table structure for fenlei
-- ----------------------------
DROP TABLE IF EXISTS `fenlei`;
CREATE TABLE `fenlei` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `fenlei` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分类';

-- ----------------------------
-- Records of fenlei
-- ----------------------------
INSERT INTO `fenlei` VALUES ('1', '2025-05-22 16:27:01', '浪漫唯美');
INSERT INTO `fenlei` VALUES ('2', '2025-05-22 16:27:01', '情绪意境');
INSERT INTO `fenlei` VALUES ('3', '2025-05-22 16:27:01', '艺术创意');
INSERT INTO `fenlei` VALUES ('4', '2025-05-22 16:27:01', '清新淡雅');
INSERT INTO `fenlei` VALUES ('5', '2025-05-22 16:27:01', '清冷古风');
INSERT INTO `fenlei` VALUES ('6', '2025-05-22 16:27:01', '极简主义');
INSERT INTO `fenlei` VALUES ('7', '2025-05-22 16:27:01', '复古港风');
INSERT INTO `fenlei` VALUES ('8', '2025-05-22 16:27:01', '网感流行');
INSERT INTO `fenlei` VALUES ('9', '2025-05-22 16:27:01', '纪实街拍');
INSERT INTO `fenlei` VALUES ('11', '2025-06-05 09:20:04', '自然风光');

-- ----------------------------
-- Table structure for forum
-- ----------------------------
DROP TABLE IF EXISTS `forum`;
CREATE TABLE `forum` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `content` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '帖子内容',
  `parentid` bigint(20) DEFAULT NULL COMMENT '父节点id',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `username` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名',
  `avatarurl` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `isdone` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '状态',
  `is_top` int(11) DEFAULT '0' COMMENT '是否置顶',
  `top_time` datetime DEFAULT NULL COMMENT '置顶时间',
  `title` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '帖子标题',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='互动社交';

-- ----------------------------
-- Records of forum
-- ----------------------------
INSERT INTO `forum` VALUES ('1', '2025-05-22 16:27:00', '帖子内容1', '0', '1', '用户名1', 'file/forumAvatarurl1.jpg,file/forumAvatarurl2.jpg,file/forumAvatarurl3.jpg', '开放', '0', '2025-06-05 11:48:32', '帖子标题1');
INSERT INTO `forum` VALUES ('2', '2025-05-22 16:27:00', '帖子内容2', '0', '2', '用户名2', 'file/forumAvatarurl2.jpg,file/forumAvatarurl3.jpg,file/forumAvatarurl4.jpg', '开放', '0', '2025-06-05 11:48:32', '帖子标题2');
INSERT INTO `forum` VALUES ('3', '2025-05-22 16:27:00', '帖子内容3', '0', '3', '用户名3', 'file/forumAvatarurl3.jpg,file/forumAvatarurl4.jpg,file/forumAvatarurl5.jpg', '开放', '0', '2025-06-05 11:48:32', '帖子标题3');
INSERT INTO `forum` VALUES ('4', '2025-05-22 16:27:00', '帖子内容4', '0', '4', '用户名4', 'file/forumAvatarurl4.jpg,file/forumAvatarurl5.jpg,file/forumAvatarurl6.jpg', '开放', '0', '2025-06-05 11:48:32', '帖子标题4');
INSERT INTO `forum` VALUES ('5', '2025-05-22 16:27:00', '帖子内容5', '0', '5', '用户名5', 'file/forumAvatarurl5.jpg,file/forumAvatarurl6.jpg,file/forumAvatarurl7.jpg', '开放', '0', '2025-06-05 11:48:32', '帖子标题5');
INSERT INTO `forum` VALUES ('6', '2025-05-22 16:27:00', '帖子内容6', '0', '6', '用户名6', 'file/forumAvatarurl6.jpg,file/forumAvatarurl7.jpg,file/forumAvatarurl8.jpg', '开放', '0', '2025-06-05 11:48:32', '帖子标题6');
INSERT INTO `forum` VALUES ('7', '2025-05-22 16:27:00', '帖子内容7', '0', '7', '用户名7', 'file/forumAvatarurl7.jpg,file/forumAvatarurl8.jpg,file/forumAvatarurl9.jpg', '开放', '0', '2025-06-05 11:48:32', '帖子标题7');
INSERT INTO `forum` VALUES ('8', '2025-05-22 16:27:00', '帖子内容8', '0', '8', '用户名8', 'file/forumAvatarurl8.jpg,file/forumAvatarurl9.jpg,file/forumAvatarurl10.jpg', '开放', '0', '2025-06-05 11:48:32', '帖子标题8');
INSERT INTO `forum` VALUES ('9', '2025-05-22 16:27:00', '帖子内容9', '0', '9', '用户名9', 'file/forumAvatarurl9.jpg,file/forumAvatarurl10.jpg,file/forumAvatarurl11.jpg', '开放', '0', '2025-06-05 11:48:32', '帖子标题9');
INSERT INTO `forum` VALUES ('10', '2025-05-22 16:27:00', '帖子内容10', '0', '10', '用户名10', 'file/forumAvatarurl10.jpg,file/forumAvatarurl11.jpg,file/forumAvatarurl12.jpg', '开放', '0', '2025-06-05 11:48:32', '帖子标题10');
INSERT INTO `forum` VALUES ('11', '2025-06-05 12:16:26', '<p>特别喜欢叶老师拍的照片，非常好看</p><p><img src=\"http://localhost:8080/cl10989512/file/1749096976308.jpg\" alt=\"\" data-href=\"\" style=\"width: 164.00px;height: 218.21px;\"/></p>', '0', '1748678347218', '10086', '', '开放', '0', null, '“复古港风”');

-- ----------------------------
-- Table structure for friendship_link
-- ----------------------------
DROP TABLE IF EXISTS `friendship_link`;
CREATE TABLE `friendship_link` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `link_name` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '链接名称',
  `link_image` longtext COLLATE utf8mb4_unicode_ci COMMENT '链接图片',
  `link_url` longtext COLLATE utf8mb4_unicode_ci COMMENT '链接地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='友情链接';

-- ----------------------------
-- Records of friendship_link
-- ----------------------------
INSERT INTO `friendship_link` VALUES ('1', '2025-05-22 16:27:01', 'CSDN', 'file/fl1.jpg', 'https://www.csdn.net/');
INSERT INTO `friendship_link` VALUES ('2', '2025-05-22 16:27:01', '凤凰资讯', 'file/fl2.jpg', 'https://news.ifeng.com/');
INSERT INTO `friendship_link` VALUES ('3', '2025-05-22 16:27:01', '网易云音乐', 'file/fl3.jpg', 'https://music.163.com/');
INSERT INTO `friendship_link` VALUES ('4', '2025-05-22 16:27:01', '抖音', 'file/fl4.jpg', 'https://www.douyin.com/');
INSERT INTO `friendship_link` VALUES ('5', '2025-05-22 16:27:01', '麦当劳', 'file/fl5.jpg', 'https://www.mcdonalds.com.cn/');
INSERT INTO `friendship_link` VALUES ('6', '2025-05-22 16:27:01', '芒果TV', 'file/fl6.jpg', 'https://www.mgtv.com/');
INSERT INTO `friendship_link` VALUES ('7', '2025-05-22 16:27:01', '天猫', 'file/fl7.jpg', 'https://www.tmall.com/');
INSERT INTO `friendship_link` VALUES ('8', '2025-05-22 16:27:01', '携程', 'file/fl8.jpg', 'https://www.ctrip.com/');
INSERT INTO `friendship_link` VALUES ('9', '2025-05-22 16:27:01', '优酷', 'file/fl9.jpg', 'https://www.youku.com/');
INSERT INTO `friendship_link` VALUES ('10', '2025-05-22 16:27:01', 'Bilibili', 'file/fl10.jpg', 'https://www.bilibili.com/');
INSERT INTO `friendship_link` VALUES ('11', '2025-06-02 23:18:26', '中国视觉网', 'file/1748877504461.jpg', 'https://www.vcg.com/');
INSERT INTO `friendship_link` VALUES ('12', '2025-06-02 23:34:43', '中国摄影网', 'file/1748878481825.png', 'https://www.cnphotos.net/');
INSERT INTO `friendship_link` VALUES ('13', '2025-06-02 23:40:14', '中国国家地理网', 'file/1748878813429.png', 'http://www.dili360.com/');
INSERT INTO `friendship_link` VALUES ('14', '2025-06-02 23:47:02', '中国摄影家协会', 'file/1748880659252.png', 'https://www.cpanet.org.cn/zpjzsx.html');
INSERT INTO `friendship_link` VALUES ('15', '2025-06-03 00:14:43', '闽南理工学院', 'file/1748880881873.webp', 'https://www.mnust.edu.cn/ssjgy.jsp?wbtreeid=1002');

-- ----------------------------
-- Table structure for fyf_ddb
-- ----------------------------
DROP TABLE IF EXISTS `fyf_ddb`;
CREATE TABLE `fyf_ddb` (
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `order_id` varchar(255) DEFAULT NULL COMMENT '订单唯一id',
  `order_status` tinyint(4) DEFAULT NULL COMMENT '订单状态：0-待支付，1-已支付',
  `order_price` decimal(10,2) DEFAULT NULL COMMENT '订单价格',
  `order_time` datetime DEFAULT NULL COMMENT '下单时间',
  `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式: 1-支付宝, 2-微信支付, 3-银行卡, 4-现金'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fyf_ddb
-- ----------------------------

-- ----------------------------
-- Table structure for fyf_zfdd
-- ----------------------------
DROP TABLE IF EXISTS `fyf_zfdd`;
CREATE TABLE `fyf_zfdd` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` varchar(20) DEFAULT NULL COMMENT '用户账号',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payment_status` tinyint(4) DEFAULT NULL COMMENT '支付状态:0-未支付，1-支付成功，2-支付失败，3-已退款',
  `is_audited` tinyint(1) DEFAULT NULL COMMENT '是否审核:：0-未审核，1-已审核',
  `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式:wechat-微信，alipay-支付宝，unionpay-银联等',
  `order_status` varchar(20) DEFAULT NULL COMMENT '订单状态:：0-待处理，1-已完成，2-已取消',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of fyf_zfdd
-- ----------------------------

-- ----------------------------
-- Table structure for huodongxinxi
-- ----------------------------
DROP TABLE IF EXISTS `huodongxinxi`;
CREATE TABLE `huodongxinxi` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `huodongmingcheng` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动名称',
  `huodongtupian` longtext COLLATE utf8mb4_unicode_ci COMMENT '活动图片',
  `kaishishijian` datetime DEFAULT NULL COMMENT '开始时间',
  `jieshushijian` datetime DEFAULT NULL COMMENT '结束时间',
  `huodongdidian` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '活动地点',
  `huodongneirong` longtext COLLATE utf8mb4_unicode_ci COMMENT '活动内容',
  `fabushijian` datetime DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动信息';

-- ----------------------------
-- Records of huodongxinxi
-- ----------------------------
INSERT INTO `huodongxinxi` VALUES ('1', '2025-05-22 16:27:01', '捕捉自然之美摄影大赛', 'file/1749095518009.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '云景山', '<p><span style=\"color: rgb(34, 34, 34); background-color: rgb(255, 255, 255); font-size: 16px;\">本次摄影大赛旨在鼓励摄影爱好者走进自然，用镜头记录下大自然的美丽瞬间。无论是晨曦中的森林，还是夕阳下的山川，我们都期待您用独特的视角和创意，展现自然的壮丽与宁静。</span></p>', '2025-05-22 16:27:01');
INSERT INTO `huodongxinxi` VALUES ('2', '2025-05-22 16:27:01', '活动名称2', 'file/huodongxinxiHuodongtupian2.jpg,file/huodongxinxiHuodongtupian3.jpg,file/huodongxinxiHuodongtupian4.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点2', '活动内容2', '2025-05-22 16:27:01');
INSERT INTO `huodongxinxi` VALUES ('3', '2025-05-22 16:27:01', '活动名称3', 'file/huodongxinxiHuodongtupian3.jpg,file/huodongxinxiHuodongtupian4.jpg,file/huodongxinxiHuodongtupian5.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点3', '活动内容3', '2025-05-22 16:27:01');
INSERT INTO `huodongxinxi` VALUES ('4', '2025-05-22 16:27:01', '活动名称4', 'file/huodongxinxiHuodongtupian4.jpg,file/huodongxinxiHuodongtupian5.jpg,file/huodongxinxiHuodongtupian6.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点4', '活动内容4', '2025-05-22 16:27:01');
INSERT INTO `huodongxinxi` VALUES ('5', '2025-05-22 16:27:01', '活动名称5', 'file/huodongxinxiHuodongtupian5.jpg,file/huodongxinxiHuodongtupian6.jpg,file/huodongxinxiHuodongtupian7.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点5', '活动内容5', '2025-05-22 16:27:01');
INSERT INTO `huodongxinxi` VALUES ('6', '2025-05-22 16:27:01', '活动名称6', 'file/huodongxinxiHuodongtupian6.jpg,file/huodongxinxiHuodongtupian7.jpg,file/huodongxinxiHuodongtupian8.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点6', '活动内容6', '2025-05-22 16:27:01');
INSERT INTO `huodongxinxi` VALUES ('7', '2025-05-22 16:27:01', '活动名称7', 'file/huodongxinxiHuodongtupian7.jpg,file/huodongxinxiHuodongtupian8.jpg,file/huodongxinxiHuodongtupian9.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点7', '活动内容7', '2025-05-22 16:27:01');
INSERT INTO `huodongxinxi` VALUES ('8', '2025-05-22 16:27:01', '活动名称8', 'file/huodongxinxiHuodongtupian8.jpg,file/huodongxinxiHuodongtupian9.jpg,file/huodongxinxiHuodongtupian10.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点8', '活动内容8', '2025-05-22 16:27:01');
INSERT INTO `huodongxinxi` VALUES ('9', '2025-05-22 16:27:01', '活动名称9', 'file/huodongxinxiHuodongtupian9.jpg,file/huodongxinxiHuodongtupian10.jpg,file/huodongxinxiHuodongtupian11.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点9', '活动内容9', '2025-05-22 16:27:01');
INSERT INTO `huodongxinxi` VALUES ('10', '2025-05-22 16:27:01', '活动名称10', 'file/huodongxinxiHuodongtupian10.jpg,file/huodongxinxiHuodongtupian11.jpg,file/huodongxinxiHuodongtupian12.jpg', '2025-05-22 16:27:01', '2025-05-22 16:27:01', '活动地点10', '活动内容10', '2025-05-22 16:27:01');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `menujson` longtext COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '菜单',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单';

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('1', '2025-05-22 16:27:01', '[{\"backMenu\":[{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"审核\",\"查看评论\",\"预约\"],\"appFrontIcon\":\"cuIcon-clothes\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"审核\"],\"classname\":\"sheyingshixinxi\",\"menu\":\"摄影师信息\",\"menuJump\":\"列表\",\"tableName\":\"sheyingshixinxi\"}],\"fontClass\":\"icon-common41\",\"menu\":\"摄影师信息管理\",\"unicode\":\"&#xeede;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"审核\",\"支付\"],\"appFrontIcon\":\"cuIcon-skin\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"审核\"],\"classname\":\"sheyingshiyuyue\",\"menu\":\"摄影师预约\",\"menuJump\":\"列表\",\"tableName\":\"sheyingshiyuyue\"}],\"fontClass\":\"icon-common50\",\"menu\":\"摄影师预约管理\",\"unicode\":\"&#xef96;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-copy\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"sheyingzuopin\",\"menu\":\"摄影作品\",\"menuJump\":\"列表\",\"tableName\":\"sheyingzuopin\"}],\"fontClass\":\"icon-common12\",\"menu\":\"摄影作品管理\",\"unicode\":\"&#xedf4;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-camera\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"fenlei\",\"menu\":\"分类\",\"menuJump\":\"列表\",\"tableName\":\"fenlei\"}],\"fontClass\":\"icon-common46\",\"menu\":\"分类管理\",\"unicode\":\"&#xef3d;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-keyboard\",\"buttons\":[\"查看\",\"修改\",\"删除\"],\"classname\":\"forum\",\"menu\":\"互动社交\",\"tableName\":\"forum\"}],\"fontClass\":\"icon-common24\",\"menu\":\"互动社交管理\",\"unicode\":\"&#xee07;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-flashlightopen\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"chat\",\"menu\":\"在线咨询\",\"menuJump\":\"列表\",\"tableName\":\"chat\"}],\"fontClass\":\"icon-common29\",\"menu\":\"在线咨询管理\",\"unicode\":\"&#xee2e;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\"],\"appFrontIcon\":\"cuIcon-brand\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"baomingxinxi\",\"menu\":\"报名信息\",\"menuJump\":\"列表\",\"tableName\":\"baomingxinxi\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"报名\"],\"appFrontIcon\":\"cuIcon-vipcard\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"huodongxinxi\",\"menu\":\"活动信息\",\"menuJump\":\"列表\",\"tableName\":\"huodongxinxi\"}],\"fontClass\":\"icon-common11\",\"menu\":\"活动信息管理\",\"unicode\":\"&#xeded;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-form\",\"buttons\":[\"查看\",\"修改\"],\"classname\":\"config\",\"menu\":\"轮播图\",\"menuJump\":\"列表\",\"tableName\":\"config\"}],\"fontClass\":\"icon-common19\",\"menu\":\"轮播图管理\",\"unicode\":\"&#xee00;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-form\",\"buttons\":[\"新增\",\"查看\",\"修改\"],\"classname\":\"users\",\"menu\":\"管理员\",\"menuJump\":\"列表\",\"tableName\":\"users\"}],\"fontClass\":\"icon-user5\",\"menu\":\"管理员管理\",\"unicode\":\"&#xef9b;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-goodsnew\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"friendshipLink\",\"menu\":\"友情链接\",\"menuJump\":\"列表\",\"tableName\":\"friendship_link\"}],\"fontClass\":\"icon-common16\",\"menu\":\"友情链接管理\",\"unicode\":\"&#xedfd;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-cardboard\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"yonghu\",\"menu\":\"用户\",\"menuJump\":\"列表\",\"tableName\":\"yonghu\"},{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"审核\"],\"appFrontIcon\":\"cuIcon-list\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"审核\"],\"classname\":\"sheyingshi\",\"menu\":\"摄影师\",\"menuJump\":\"列表\",\"tableName\":\"sheyingshi\"}],\"fontClass\":\"icon-user7\",\"menu\":\"用户管理\",\"unicode\":\"&#xef9d;\"}],\"frontMenu\":[{\"child\":[{\"appFrontIcon\":\"cuIcon-goods\",\"buttons\":[\"查看\",\"报名\"],\"classname\":\"huodongxinxi\",\"menu\":\"活动信息\",\"menuJump\":\"列表\",\"tableName\":\"huodongxinxi\"}],\"menu\":\"活动信息管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-time\",\"buttons\":[\"查看\",\"预约\"],\"classname\":\"sheyingshixinxi\",\"menu\":\"摄影师信息\",\"menuJump\":\"列表\",\"tableName\":\"sheyingshixinxi\"}],\"menu\":\"摄影师信息管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-similar\",\"buttons\":[\"查看\"],\"classname\":\"sheyingzuopin\",\"menu\":\"摄影作品\",\"menuJump\":\"列表\",\"tableName\":\"sheyingzuopin\"}],\"menu\":\"摄影作品管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-present\",\"buttons\":[\"查看\",\"新增\"],\"classname\":\"forum\",\"menu\":\"互动社交\",\"menuJump\":\"列表\",\"tableName\":\"forum\"}],\"menu\":\"论坛交流\"}],\"hasBackLogin\":\"是\",\"hasBackRegister\":\"否\",\"hasFrontLogin\":\"否\",\"hasFrontRegister\":\"否\",\"pathName\":\"users\",\"roleName\":\"管理员\",\"tableName\":\"users\"},{\"backMenu\":[{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"审核\",\"支付\"],\"appFrontIcon\":\"cuIcon-skin\",\"buttons\":[\"查看\",\"支付\"],\"classname\":\"sheyingshiyuyue\",\"menu\":\"摄影师预约\",\"menuJump\":\"列表\",\"tableName\":\"sheyingshiyuyue\"}],\"fontClass\":\"icon-common50\",\"menu\":\"摄影师预约管理\",\"unicode\":\"&#xef96;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-keyboard\",\"buttons\":[\"查看\",\"修改\",\"删除\"],\"classname\":\"forum\",\"menu\":\"我的发布\",\"menuJump\":\"14\",\"tableName\":\"forum\"}],\"fontClass\":\"icon-common24\",\"menu\":\"互动社交管理\",\"unicode\":\"&#xee07;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"导出\"],\"appFrontIcon\":\"cuIcon-brand\",\"buttons\":[\"查看\"],\"classname\":\"baomingxinxi\",\"menu\":\"报名信息\",\"menuJump\":\"列表\",\"tableName\":\"baomingxinxi\"}],\"fontClass\":\"icon-common11\",\"menu\":\"活动信息管理\",\"unicode\":\"&#xeded;\"}],\"frontMenu\":[{\"child\":[{\"appFrontIcon\":\"cuIcon-goods\",\"buttons\":[\"查看\",\"报名\"],\"classname\":\"huodongxinxi\",\"menu\":\"活动信息\",\"menuJump\":\"列表\",\"tableName\":\"huodongxinxi\"}],\"menu\":\"活动信息管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-time\",\"buttons\":[\"查看\",\"预约\"],\"classname\":\"sheyingshixinxi\",\"menu\":\"摄影师信息\",\"menuJump\":\"列表\",\"tableName\":\"sheyingshixinxi\"}],\"menu\":\"摄影师信息管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-similar\",\"buttons\":[\"查看\"],\"classname\":\"sheyingzuopin\",\"menu\":\"摄影作品\",\"menuJump\":\"列表\",\"tableName\":\"sheyingzuopin\"}],\"menu\":\"摄影作品管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-present\",\"buttons\":[\"查看\",\"新增\"],\"classname\":\"forum\",\"menu\":\"互动社交\",\"menuJump\":\"列表\",\"tableName\":\"forum\"}],\"menu\":\"论坛交流\"}],\"hasBackLogin\":\"否\",\"hasBackRegister\":\"否\",\"hasFrontLogin\":\"是\",\"hasFrontRegister\":\"是\",\"pathName\":\"yonghu\",\"roleName\":\"用户\",\"tableName\":\"yonghu\"},{\"backMenu\":[{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"审核\",\"查看评论\",\"预约\"],\"appFrontIcon\":\"cuIcon-clothes\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"sheyingshixinxi\",\"menu\":\"摄影师信息\",\"menuJump\":\"列表\",\"tableName\":\"sheyingshixinxi\"}],\"fontClass\":\"icon-common41\",\"menu\":\"摄影师信息管理\",\"unicode\":\"&#xeede;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\",\"审核\",\"支付\"],\"appFrontIcon\":\"cuIcon-skin\",\"buttons\":[\"查看\",\"审核\"],\"classname\":\"sheyingshiyuyue\",\"menu\":\"摄影师预约\",\"menuJump\":\"列表\",\"tableName\":\"sheyingshiyuyue\"}],\"fontClass\":\"icon-common50\",\"menu\":\"摄影师预约管理\",\"unicode\":\"&#xef96;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-copy\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"sheyingzuopin\",\"menu\":\"摄影作品\",\"menuJump\":\"列表\",\"tableName\":\"sheyingzuopin\"}],\"fontClass\":\"icon-common12\",\"menu\":\"摄影作品管理\",\"unicode\":\"&#xedf4;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-camera\",\"buttons\":[\"查看\"],\"classname\":\"fenlei\",\"menu\":\"分类\",\"menuJump\":\"列表\",\"tableName\":\"fenlei\"}],\"fontClass\":\"icon-common46\",\"menu\":\"分类管理\",\"unicode\":\"&#xef3d;\"},{\"child\":[{\"allButtons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"appFrontIcon\":\"cuIcon-flashlightopen\",\"buttons\":[\"新增\",\"查看\",\"修改\",\"删除\"],\"classname\":\"chat\",\"menu\":\"在线咨询\",\"menuJump\":\"列表\",\"tableName\":\"chat\"}],\"fontClass\":\"icon-common29\",\"menu\":\"在线咨询管理\",\"unicode\":\"&#xee2e;\"}],\"frontMenu\":[{\"child\":[{\"appFrontIcon\":\"cuIcon-goods\",\"buttons\":[\"查看\",\"报名\"],\"classname\":\"huodongxinxi\",\"menu\":\"活动信息\",\"menuJump\":\"列表\",\"tableName\":\"huodongxinxi\"}],\"menu\":\"活动信息管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-time\",\"buttons\":[\"查看\",\"预约\"],\"classname\":\"sheyingshixinxi\",\"menu\":\"摄影师信息\",\"menuJump\":\"列表\",\"tableName\":\"sheyingshixinxi\"}],\"menu\":\"摄影师信息管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-similar\",\"buttons\":[\"查看\"],\"classname\":\"sheyingzuopin\",\"menu\":\"摄影作品\",\"menuJump\":\"列表\",\"tableName\":\"sheyingzuopin\"}],\"menu\":\"摄影作品管理\"},{\"child\":[{\"appFrontIcon\":\"cuIcon-present\",\"buttons\":[\"查看\",\"新增\"],\"classname\":\"forum\",\"menu\":\"互动社交\",\"menuJump\":\"列表\",\"tableName\":\"forum\"}],\"menu\":\"论坛交流\"}],\"hasBackLogin\":\"是\",\"hasBackRegister\":\"否\",\"hasFrontLogin\":\"否\",\"hasFrontRegister\":\"否\",\"pathName\":\"sheyingshi\",\"roleName\":\"摄影师\",\"tableName\":\"sheyingshi\"}]');

-- ----------------------------
-- Table structure for sheyingshi
-- ----------------------------
DROP TABLE IF EXISTS `sheyingshi`;
CREATE TABLE `sheyingshi` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `sheyingzhanghao` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '摄影账号',
  `mima` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `sheyingxingming` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '摄影姓名',
  `touxiang` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `xingbie` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `lianxidianhua` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
  `sfsh` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '待审核' COMMENT '是否审核',
  `shhf` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sheyingzhanghao` (`sheyingzhanghao`)
) ENGINE=InnoDB AUTO_INCREMENT=81 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='摄影师';

-- ----------------------------
-- Records of sheyingshi
-- ----------------------------
INSERT INTO `sheyingshi` VALUES ('71', '2025-05-22 16:27:00', '摄影账号1', '123456', '摄影姓名1', 'file/sheyingshiTouxiang1.jpg', '男', '19819881111', '是', '');
INSERT INTO `sheyingshi` VALUES ('72', '2025-05-22 16:27:00', '摄影账号2', '123456', '摄影姓名2', 'file/sheyingshiTouxiang2.jpg', '男', '19819881112', '是', '');
INSERT INTO `sheyingshi` VALUES ('73', '2025-05-22 16:27:00', '摄影账号3', '123456', '摄影姓名3', 'file/sheyingshiTouxiang3.jpg', '男', '19819881113', '是', '');
INSERT INTO `sheyingshi` VALUES ('74', '2025-05-22 16:27:00', '摄影账号4', '123456', '摄影姓名4', 'file/sheyingshiTouxiang4.jpg', '男', '19819881114', '是', '');
INSERT INTO `sheyingshi` VALUES ('75', '2025-05-22 16:27:00', '摄影账号5', '123456', '摄影姓名5', 'file/sheyingshiTouxiang5.jpg', '男', '19819881115', '是', '');
INSERT INTO `sheyingshi` VALUES ('76', '2025-05-22 16:27:00', '摄影账号6', '123456', '摄影姓名6', 'file/sheyingshiTouxiang6.jpg', '男', '19819881116', '是', '');
INSERT INTO `sheyingshi` VALUES ('77', '2025-05-22 16:27:00', '摄影账号7', '123456', '摄影姓名7', 'file/sheyingshiTouxiang7.jpg', '男', '19819881117', '是', '');
INSERT INTO `sheyingshi` VALUES ('78', '2025-05-22 16:27:00', '摄影账号8', '123456', '摄影姓名8', 'file/sheyingshiTouxiang8.jpg', '男', '19819881118', '是', '');
INSERT INTO `sheyingshi` VALUES ('79', '2025-05-22 16:27:00', '摄影账号9', '123456', '摄影姓名9', 'file/sheyingshiTouxiang9.jpg', '男', '19819881119', '是', '');
INSERT INTO `sheyingshi` VALUES ('80', '2025-05-22 16:27:00', '摄影账号10', '123456', '摄影姓名10', 'file/sheyingshiTouxiang10.jpg', '男', '198198811110', '是', '');

-- ----------------------------
-- Table structure for sheyingshixinxi
-- ----------------------------
DROP TABLE IF EXISTS `sheyingshixinxi`;
CREATE TABLE `sheyingshixinxi` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `sheyingzhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '摄影账号',
  `sheyingxingming` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '摄影姓名',
  `sheyingshizhaopian` longtext COLLATE utf8mb4_unicode_ci COMMENT '摄影师照片',
  `fenlei` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类',
  `jianjie` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '简介',
  `yuyuejiage` double DEFAULT NULL COMMENT '预约价格',
  `gongzuojingli` longtext COLLATE utf8mb4_unicode_ci COMMENT '工作经历',
  `zhuanyejineng` longtext COLLATE utf8mb4_unicode_ci COMMENT '专业技能',
  `gerentezhang` longtext COLLATE utf8mb4_unicode_ci COMMENT '个人特长',
  `sfsh` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '待审核' COMMENT '是否审核',
  `shhf` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  `discuss_number` int(11) DEFAULT '0' COMMENT '评论数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='摄影师信息';

-- ----------------------------
-- Records of sheyingshixinxi
-- ----------------------------
INSERT INTO `sheyingshixinxi` VALUES ('1', '2025-05-22 16:27:00', '摄影账号1', '叶悬光', 'file/sheyingshixinxiSheyingshizhaopian1.jpg,file/sheyingshixinxiSheyingshizhaopian2.jpg,file/sheyingshixinxiSheyingshizhaopian3.jpg', '复古港风', '一位以复古港风著称的摄影师，他擅长通过浓郁的色彩、独特的构图和细腻的情感表达，重现20世纪80-90年代香港的独特韵味和市井风情。', '2000', '2012-2014：签约摄影师 - 《城市画报》杂志\n作为签约摄影师，陈启铭参与了多个以“城市记忆”为主题的专题拍摄，如“旧日香港”、“市井人生”等。\n他的作品多次被选为杂志封面，并参与了多期专题报道的策划与拍摄，赢得了读者的广泛好评。', 'Nikon（尼康） F6：经典的胶片相机，适合复古港风摄影，能够捕捉到细腻的颗粒感和浓郁的色彩。\nCanon（佳能） EOS-1V：高性能胶片相机，具备出色的对焦和连拍性能，适合捕捉动态的城市景观和人文瞬间。', '色彩与光影掌控：陈启铭擅长通过浓郁的色彩和独特的光影效果，营造出浓郁的怀旧氛围，使作品充满视觉冲击力。\n情感表达：他注重通过镜头表达情感，使作品不仅具有视觉美感，更能引发观者的共鸣和深思。', '是', '', '1');
INSERT INTO `sheyingshixinxi` VALUES ('2', '2025-05-22 16:27:00', '摄影账号2', '宋停云', 'file/sheyingshixinxiSheyingshizhaopian2.jpg,file/sheyingshixinxiSheyingshizhaopian3.jpg,file/sheyingshixinxiSheyingshizhaopian4.jpg', '情绪意境', '擅长通过细腻的光影运用、柔和的色彩搭配和独特的构图方式，将自然与人文景观转化为充满诗意和灵气的视觉作品。她的作品常常展现出宁静、悠远和纯净的氛围，仿佛将观者带入一个远离尘嚣的清新世界。', '2600', '2018-2020：文化与自然纪录片摄影师\n叶清岚参与了多部关于中国传统文化和自然风光的纪录片拍摄，如《诗意中国》、《秘境探幽》等。\n在拍摄过程中，她深入各地，捕捉到了许多珍贵的自然景观和人文风情影像。', 'Fujifilm（富士）X-T5：以其出色的色彩还原和胶片模拟效果著称，非常适合营造清新意境的视觉效果。\nSony（索尼） A7C：轻便的全画幅相机，适合各种环境下的拍摄，提供高质量的图像和视频。\nCanon（佳能） EOS R6：高性能相机，具备出色的对焦和连拍性能，适合捕捉动态的自然与人文瞬间。', '细腻光影运用：叶清岚擅长通过细腻的光影运用，营造出宁静、悠远的氛围，使作品充满诗意和灵气。\n柔和色彩搭配：她擅长运用柔和的色彩和独特的构图方式，使作品呈现出清新、纯净的视觉效果。\n情感与自然融合：叶清岚注重将情感与自然景观融合，使作品不仅具有视觉美感，更能引发观者的共鸣和深思。', '是', '', '2');
INSERT INTO `sheyingshixinxi` VALUES ('3', '2025-05-22 16:27:00', '摄影账号3', '许空山', 'file/sheyingshixinxiSheyingshizhaopian3.jpg,file/sheyingshixinxiSheyingshizhaopian4.jpg,file/sheyingshixinxiSheyingshizhaopian5.jpg', '极简主义', '极简主义风格著称的摄影师，他擅长通过简洁的构图、克制的色彩和留白的运用，传达出一种宁静、纯粹和深邃的视觉体验。他的作品常常去除多余的视觉元素，专注于捕捉事物本质的美，展现出一种超越物质的精神境界。', '2200', '2015-2017：签约摄影师 - 《新视线》杂志\n作为签约摄影师，赵无尘参与了多个以“简约之美”为主题的专题拍摄，如“都市极简”、“自然之韵”等。\n他的作品多次被选为杂志封面，并参与了多期专题报道的策划与拍摄，赢得了读者的广泛好评。', 'Leica （徕卡）M10 Monochrom：经典的黑白相机，适合极简主义摄影，能够捕捉到细腻的灰度层次和纯净的影像。\nFujifilm（富士） X-T4：以其出色的色彩科学和胶片模拟效果著称，常用于营造极简主义风格的视觉效果。', '简洁构图：赵无尘擅长通过简洁的构图和留白的运用，去除多余的视觉元素，专注于捕捉事物本质的美。\n克制色彩：他擅长运用克制的色彩和纯净的色调，营造出一种宁静、纯粹和深邃的视觉体验。\n哲学思考：赵无尘注重在作品中融入对极简主义的哲学思考，使作品不仅具有视觉美感，更能引发观者的深思。', '是', '', '3');
INSERT INTO `sheyingshixinxi` VALUES ('4', '2025-05-22 16:27:00', '摄影账号4', '杨自在', 'file/sheyingshixinxiSheyingshizhaopian4.jpg,file/sheyingshixinxiSheyingshizhaopian5.jpg,file/sheyingshixinxiSheyingshizhaopian6.jpg', '浪漫唯美', '一位以浪漫唯美风格著称的摄影师，擅长通过柔和的光线、梦幻的色彩和细腻的情感表达，将日常生活转化为充满诗意与浪漫的视觉盛宴。', '2400', '2016-2018：签约摄影师 - 《时尚芭莎》杂志\n作为签约摄影师，林若曦参与了多个以“浪漫之旅”为主题的时尚大片拍摄，如“巴黎之恋”、“爱在日落黄昏时”等。\n她的作品多次被选为杂志封面，并参与了多期专题报道的策划与拍摄，赢得了读者的广泛喜爱。', 'Canon EOS R5：高性能相机，具备出色的色彩还原和动态范围，非常适合拍摄浪漫唯美的场景。\nSony A7S III：高感光度性能优越，适合低光环境下的拍摄，能够捕捉到细腻的情感和梦幻的氛围。\nFujifilm X-T5：以其出色的色彩科学和胶片模拟效果著称，常用于营造浪漫唯美的视觉效果。', '情感捕捉：林若曦擅长捕捉和表达情感，她的作品常常能够引发观者的共鸣，让人们感受到爱情的甜蜜和生活的美好。\n梦幻氛围营造：她擅长通过光线和色彩的运用，营造出如梦似幻的浪漫氛围，使作品充满诗意和想象力', '是', '', '4');
INSERT INTO `sheyingshixinxi` VALUES ('5', '2025-05-22 16:27:01', '摄影账号5', '陆星河', 'file/sheyingshixinxiSheyingshizhaopian5.jpg,file/sheyingshixinxiSheyingshizhaopian6.jpg,file/sheyingshixinxiSheyingshizhaopian7.jpg', '清冷古风', '一位以清冷古风见长的摄影师，他擅长通过独特的镜头语言和精致的构图，将古典美学与现代摄影技术相结合，展现出一种超凡脱俗、宁静致远的艺术境界。', '2000', '2019-2021：文化纪录片摄影师\n萧逸尘参与了多部关于中国传统文化的纪录片拍摄，如《古韵流芳》、《匠心传承》等。\n在拍摄过程中，他深入各地，捕捉到了许多珍贵的传统文化和人文景观影像。', 'Nikon（尼康） Z7 II：高像素全画幅相机，适合精细的古风摄影，能够捕捉到丰富的细节和色彩。\nCanon（佳能） EOS R5：高性能相机，具备出色的对焦和连拍性能，适合捕捉动态的古风场景。\nSony（索尼） A7R IV：以其出色的分辨率和动态范围著称，非常适合拍摄具有复杂光影的古风作品。', '古典美学理解：萧逸尘对古典美学有着深刻的理解和独特的诠释，能够在作品中完美再现古代文化的韵味。\n精致构图：他的作品以精致的构图和简洁的线条著称，能够引导观者深入思考和感受作品的意境。\n淡雅色调运用：萧逸尘擅长运用淡雅的色调和深邃的意境，营造出一种宁静致远、淡雅脱俗的视觉氛围。', '是', '', '5');
INSERT INTO `sheyingshixinxi` VALUES ('6', '2025-05-22 16:27:01', '摄影账号6', '沈青野', 'file/1749090867938.jpg', '清新淡雅', '一位以清新淡雅风格著称的摄影师，她擅长通过细腻的镜头语言和柔和的色调，捕捉生活中那些宁静、美好而富有诗意的瞬间。她的作品常常展现出自然与人文的和谐美，营造出一种宁静致远、淡雅脱俗的视觉氛围。', '1600', '2016-2018：签约摄影师 - 《生活月刊》杂志\n作为签约摄影师，苏清婉参与了多个以“自然与人文”为主题的专题拍摄，如“四季之美”、“乡村拾遗”等。\n她的作品多次被选为杂志封面，并参与了多期专题报道的策划与拍摄，赢得了读者的广泛喜爱。', 'Sony（索尼） A7C：轻便的全画幅相机，适合各种环境下的拍摄，提供高质量的图像和视频。\nCanon （佳能）EOS R6：高性能相机，具备出色的对焦和连拍性能，适合捕捉生活中的动态瞬间。', '细腻的观察力：擅长发现和捕捉生活中那些容易被忽视的美好瞬间，展现出独特的审美眼光。\n柔和的色调运用：她的作品以柔和的色调和细腻的光影效果著称，能够营造出宁静、平和的视觉氛围。\n情感表达：苏清婉注重通过摄影传达情感，她的作品常常能够引发观者的共鸣，让人们感受到生活中的美好与宁静', '是', '', '6');
INSERT INTO `sheyingshixinxi` VALUES ('7', '2025-05-22 16:27:01', '摄影账号7', '林听风', 'file/sheyingshixinxiSheyingshizhaopian7.jpg,file/sheyingshixinxiSheyingshizhaopian8.jpg,file/sheyingshixinxiSheyingshizhaopian9.jpg', '纪实街拍', '一位充满人文关怀的艺术纪实街拍摄影师，他以敏锐的观察力和独特的视角记录城市生活中的瞬间与故事。他的作品常常捕捉到人们在日常生活中的真实情感和细微瞬间，展现出城市的多样性和人性的复杂。', '2700', '2021-至今：国际纪实摄影协会（IDPA）会员\n成为国际纪实摄影协会会员，积极参与协会组织的各类纪实摄影活动和展览。\n他的作品在多个国际摄影展上展出，并获得了多项纪实摄影奖项。\n', 'Leica（徕卡） Summilux-M 35mm f/1.4 ASPH：经典的人文镜头，适合街头摄影，能够在各种光线条件下拍摄出色画质。\nSony （索尼）FE 24-70mm f/2.8 GM：多功能变焦镜头，适合各种场景的拍摄，提供灵活的构图选择。', '敏锐的观察力：李卓然擅长在瞬息万变的街头环境中捕捉到最具代表性的瞬间和细节。\n人文关怀：他的作品充满了对城市居民和生活的关怀，能够通过镜头传达出深刻的人文情感。\n故事讲述能力：李卓然不仅关注单个画面的美感，更注重通过一组照片或一个视频讲述一个完整的故事，揭示社会现象，引发观者思考。', '是', '', '7');
INSERT INTO `sheyingshixinxi` VALUES ('8', '2025-05-22 16:27:01', '摄影账号8', '陈家鑫', 'file/sheyingshixinxiSheyingshizhaopian8.jpg', '网感流行', '陈老师是一位极具网感和流行嗅觉的摄影师，她擅长捕捉当代年轻人生活中的时尚与潮流元素，并以独特的艺术视角将其转化为视觉盛宴。她的作品充满了活力与创意，常常在社交媒体上引发热议和模仿。', '1800', '2018-2020：签约摄影师 - 《Vogue Me》杂志\n作为签约摄影师，苏沐晴参与了多个时尚大片的拍摄，与多位知名模特和设计师合作。\n她的作品多次被选为杂志封面，并参与了多期专题报道的策划与拍摄，主题涵盖“都市潮流”、“未来时尚”等。', 'Sony A7R IV：高像素全画幅相机，适合精细的时尚和人像摄影。\nFujifilm X-T4：经典的胶片模拟效果，常用于营造独特的艺术氛围。\nCanon EOS R5：高性能相机，适合动态场景和视频拍摄。', '创意视觉设计：苏沐晴擅长将摄影与图形设计结合，创造出独特的视觉艺术作品。\n时尚潮流洞察：她对时尚潮流有着敏锐的嗅觉，能够准确捕捉和预测流行趋势。\n社交媒体运营：苏沐晴在各大社交媒体平台上拥有大量粉丝，擅长通过社交媒体传播和推广自己的作品。', '是', '', '8');
INSERT INTO `sheyingshixinxi` VALUES ('9', '2025-05-22 16:27:01', '摄影账号9', '叶知秋', 'file/sheyingshixinxiSheyingshizhaopian9.jpg,file/sheyingshixinxiSheyingshizhaopian10.jpg,file/sheyingshixinxiSheyingshizhaopian11.jpg', '自然风光', '叶老师是中国当代自然风光摄影的代表人物之一，擅长以东方美学视角捕捉山川、云雾、水流的诗意瞬间。他的作品融合了传统水墨画的留白意境与现代摄影技术，被《国家地理》评价为“最具禅意的自然影像”。', '2000', '2020-至今 | 自由摄影师，合作机构：国家地理\n2018-2020 | 《夏日地理》特约摄影师，完成“四季”专题拍摄  \n 2016-2018 | 高原生态摄影项目首席记录者，作品入选联合国环境署展览', '富士 GFX100S（中画幅细腻画质）  索尼 A7R IV（高像素细节捕捉）   哈苏 X2D（极致色彩还原）\n核心技法：  黄金时段预判（日出日落前30分钟精准拍摄）  全景接片（单张亿级像素合成）  慢门水流（ND1000减光镜长曝光）\n后期工具：Lightroom（自然色调优化）、Photoshop（景深堆栈）', '“气象预言家”：能通过云层变化预判最佳拍摄时机  \n 极端环境适应：曾在-25℃的雪山连续蹲守3天  \n轻量化大师：仅用一台无反相机+三脚架完成高原拍摄', '是', '', '9');
INSERT INTO `sheyingshixinxi` VALUES ('10', '2025-05-22 16:27:01', '摄影账号10', '林见川', 'file/sheyingshixinxiSheyingshizhaopian10.jpg,file/sheyingshixinxiSheyingshizhaopian11.jpg,file/sheyingshixinxiSheyingshizhaopian12.jpg', '艺术创意', '“用相机解构现实，用光影写诗。” 林老师是中国新锐超现实摄影师，擅长将梦境、神话与科技融合，打造充满隐喻的视觉叙事。作品曾入选国际摄影双年展，并被《艺术世界》评为“最具哲学意味的影像创作者”。', '1500', '2021-至今 | 自由艺术家，合作品牌：云系列（概念广告）、云音乐（专辑视觉）  \n2019-2021 | M50艺术区驻地摄影师，举办个展《浮云记忆》  \n2017-2019 | 《新视线》杂志视觉编辑，主导“数字禅意”专题', '索尼 A7R IV（高解析力）  \n哈苏 X1D II 50C（中画幅质感）  \n富士 GFX100S（胶片模拟）', '“幻境建筑师”：能在普通天台或废弃仓库搭建超现实场景  \n色彩炼金术：独创“赛博水墨”色调（冷蓝+暖橙碰撞）  \n即兴创作：随机捡到的物品（如玻璃碎片、旧玩具）都能成为拍摄道具', '是', '', '10');

-- ----------------------------
-- Table structure for sheyingshiyuyue
-- ----------------------------
DROP TABLE IF EXISTS `sheyingshiyuyue`;
CREATE TABLE `sheyingshiyuyue` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `sheyingzhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '摄影账号',
  `sheyingxingming` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '摄影姓名',
  `yuyuejiage` double DEFAULT NULL COMMENT '预约价格',
  `sfsh` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '是否审核',
  `shhf` longtext COLLATE utf8mb4_unicode_ci COMMENT '回复内容',
  `yonghuzhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户账号',
  `yonghuxingming` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户姓名',
  `yuyuesheyingriqi` datetime DEFAULT NULL COMMENT '预约摄影日期',
  `yuyueshijianduan` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ispay` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '未支付' COMMENT '是否支付',
  `crossuserid` bigint(20) DEFAULT NULL COMMENT '跨表用户id',
  `crossrefid` bigint(20) DEFAULT NULL COMMENT '跨表主键id',
  `lianxidianhua` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='摄影师预约';

-- ----------------------------
-- Records of sheyingshiyuyue
-- ----------------------------
INSERT INTO `sheyingshiyuyue` VALUES ('25', '2025-06-04 18:00:50', '摄影账号1', '摄影姓名1', '1', '是', '好的', '123456', '123456', '2025-06-04 00:00:00', '08:00-09:00', '未支付', '1748791211018', '1', '19978778787');
INSERT INTO `sheyingshiyuyue` VALUES ('26', '2025-06-04 18:05:36', '摄影账号1', '摄影姓名1', '1', '', '', '235022125', '小城', '2025-06-04 00:00:00', '09:00-10:00', '未支付', '1748621434959', '1', '15659394051');

-- ----------------------------
-- Table structure for sheyingzuopin
-- ----------------------------
DROP TABLE IF EXISTS `sheyingzuopin`;
CREATE TABLE `sheyingzuopin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `zuopinmingcheng` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '作品名称',
  `sheyingzhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '摄影账号',
  `sheyingxingming` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '摄影姓名',
  `zuopinjianjie` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '作品简介',
  `zuopintupian` longtext COLLATE utf8mb4_unicode_ci COMMENT '作品图片',
  `zuopingaishu` longtext COLLATE utf8mb4_unicode_ci COMMENT '作品概述',
  `chuangzuobeijing` longtext COLLATE utf8mb4_unicode_ci COMMENT '创作背景',
  `fabushijian` datetime DEFAULT NULL COMMENT '发布时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='摄影作品';

-- ----------------------------
-- Records of sheyingzuopin
-- ----------------------------
INSERT INTO `sheyingzuopin` VALUES ('1', '2025-05-22 16:27:01', '“复古港风”', '1', '叶悬光', '作品采用了复古港风的艺术风格，融合了现代摄影技术，创造出既有怀旧感又不失现代感的视觉体验。', 'file/1749093302497.jpg', '探索和表现香港这座不夜城的独特魅力。这一系列作品捕捉了香港夜晚的繁华与宁静，通过光影和色彩的巧妙运用，展现了都市夜生活的多面性。', '这座城市的夜晚有着深刻的情感和理解。他希望通过这组作品，让观者感受到香港夜幕下的温度，体验到都市夜生活中的真实情感和故事。', '2025-05-22 16:27:01');
INSERT INTO `sheyingzuopin` VALUES ('2', '2025-05-22 16:27:01', '“艺术创意”', '10', '宋停云', '这组作品通过捕捉夜晚中光绘（Light Painting）的瞬间，展现了光影交织下的动态之美，将时间与空间的转换定格为永恒的视觉艺术。', 'file/1749093417965.jpg', '展现了光影交织下的动态之美，将时间与空间的转换定格为永恒的视觉艺术。', '夜间摄影充满热情，尤其对光影效果的创造和捕捉充满兴趣。利用长时间曝光技术，将光的轨迹记录在镜头中，创作出这一系列充满动感和节奏感的作品。', '2025-05-23 16:27:01');
INSERT INTO `sheyingzuopin` VALUES ('4', '2025-05-22 16:27:01', '“元气网感”', '8', '陈家鑫', '《晨曦之舞》是一组捕捉清晨第一缕阳光的人像摄影作品，摄影师通过镜头展现了自然光线与人物的和谐共舞，传递出一种宁静而又充满希望的清晨氛围。', 'file/1749093772433.jpg', '组作品不仅仅是视觉上的享受，更是情感上的触动。', '在一次偶然的日出时刻获得灵感，决定记录下人们在破晓时分与自然共舞的瞬间。她认为早晨的阳光具有一种独特的魔力，能够赋予人们新的活力和希望。', '2025-05-24 16:27:01');
INSERT INTO `sheyingzuopin` VALUES ('5', '2025-05-22 16:27:01', '“纪实街拍”', '7', ' 林听风', '这组作品不仅仅是对城市风光的记录，更是对人们日常生活的观察和感悟。江晨希望通过这些作品，让观者感受到早晨的平和与希望，以及生活的美好。', 'file/1749093990127.jpg', '《晨光之旅》是一组捕捉城市清晨第一缕阳光的摄影作品，摄影师通过对光影的捕捉，展现了城市从沉睡中苏醒的瞬间，以及人们开始新一天的旅程。', '城市清晨的宁静和活力所吸引，决定记录下这些珍贵的时刻。他认为，晨光不仅照亮了城市，也照亮了人们的心灵，给予每个人新的希望和动力。', '2025-05-25 16:27:01');
INSERT INTO `sheyingzuopin` VALUES ('6', '2025-05-22 16:27:01', '“清新淡雅”', '6', '沈青野', '这组作品不仅仅是视觉上的享受，更是情感上的共鸣。希望通过捕捉人物在春日里的自然状态，传递出春天的温暖和希望，以及人们对美好生活的向往。', 'file/1749094257620.jpg', '《春日轻语》是苏晴以春天为主题，创作的一组人像摄影作品。这组作品捕捉了春日温暖的阳光下，人物与自然的和谐共处，传递出春天的生机与柔美。\n', '在一次春日的午后散步中获得灵感，决心记录下这个季节独有的清新与活力。她希望通过这组作品，让观者感受到春天的气息和生命的律动。', '2025-05-22 16:27:01');
INSERT INTO `sheyingzuopin` VALUES ('7', '2025-05-22 16:27:01', '“清冷古风”', '5', '陆星河', '使用尼康D850相机进行拍摄，利用其高分辨率和色彩准确性捕捉汉服的细节和秋叶的纹理。后期处理中，摄影师使用Adobe Lightroom软件进行色彩校正和氛围渲染。', 'file/1749094387031.jpg', '《秋韵华裳》是柳风笛以秋季为背景，结合传统汉服文化所创作的一系列人像摄影作品。这组作品通过精致的汉服与秋日自然景观的和谐搭配，展现了古典与季节之美的完美融合。', '传统文化抱有深厚的情感，他希望通过摄影艺术，将汉服的古典韵味与秋天的宁静氛围相结合，让观者感受到季节变换中的文化韵味。', '2025-05-31 16:27:01');
INSERT INTO `sheyingzuopin` VALUES ('8', '2025-05-22 16:27:01', '“浪漫主义”', '4', '杨自在', '作品风格清新自然，强调情感的真实表达和环境的简洁美。通过校园内真实的场景和自然光线，营造出一种青涩而美好的氛围。', 'file/1749094477484.jpg', '青春的阶梯》是一组以校园生活为背景，捕捉青春情感瞬间的人像摄影作品。摄影师周子涵通过这组作品，展现了学生时代纯真的爱情和友情，以及对未来的憧憬和迷茫。\n', '在大学时期就对摄影产生了浓厚的兴趣，这组作品灵感来源于他对学生时代美好记忆的怀念，以及对青春时光的珍视。\n', '2025-06-01 16:27:01');
INSERT INTO `sheyingzuopin` VALUES ('9', '2025-05-22 16:27:01', '“极简主义”', '3', '许空山', '天空中云层的层次感与地面的直线条形成呼应，使整个画面充满了和谐与平衡的美感。这幅作品不仅是对自然与人造环境的对话，更是对简约美学的一次深刻诠释。', 'file/1749094582422.jpg', '这幅作品展现了一座极简主义风格的建筑，矗立在广袤的沙漠景观中。建筑主体以纯白色立方体形式呈现，简洁而富有力量感', '正对建筑的一条笔直的白色步道，引导观者的视线直达入口，仿佛在邀请人们探索这座简约而神秘的居所。周围的植被呈现出干燥的金黄色，与建筑的纯白形成鲜明对比，增强了视觉冲击力。', '2025-06-03 16:27:01');
INSERT INTO `sheyingzuopin` VALUES ('10', '2025-05-22 16:27:01', '“情绪意境”', '2', '宋停云', '这幅作品描绘了一片宁静的森林，阳光透过树叶洒下斑驳的光影，营造出一种梦幻般的氛围。', 'file/1749094754004.jpg', '高大的树木矗立在画面中，树干笔直而坚韧，仿佛守护着这片静谧的土地。阳光从树梢间穿透，形成一道道金色的光束，投射在地面上，给人一种温暖而宁静的感觉。', '地面上覆盖着一层薄薄的落叶，增添了几分秋天的气息。整个画面充满了自然的和谐美，让人感受到大自然的宁静与力量。这幅作品不仅展现了森林的美丽景色，更传达了一种内心的平静与安宁', '2025-06-03 16:27:01');
INSERT INTO `sheyingzuopin` VALUES ('11', '2025-06-05 11:42:51', '“自然风光”', '9', '叶知秋', '远处的植被和近处的黄土路形成鲜明对比，增强了画面的层次感和立体感。这幅作品不仅展示了宝塔的宏伟与精致，更传达出一种宁静与庄重的氛围，让人感受到历史的厚重与文化的传承。', 'file/1749094949696.jpg', '这幅作品展现了一座古老的宝塔，矗立在蓝天白云之下，显得格外壮丽。宝塔共有多层，每层屋檐翘角飞扬，富有典型的东方建筑风格。', '塔周围是一片开阔的土地，通向宝塔的石阶和木质栏杆引导着观者的视线，仿佛邀请人们踏上这条通往历史与文化的旅程。', '2025-06-05 11:41:30');

-- ----------------------------
-- Table structure for token
-- ----------------------------
DROP TABLE IF EXISTS `token`;
CREATE TABLE `token` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userid` bigint(20) NOT NULL COMMENT '用户id',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `tablename` varchar(100) DEFAULT NULL COMMENT '表名',
  `role` varchar(100) DEFAULT NULL COMMENT '角色',
  `token` varchar(200) NOT NULL COMMENT '密码',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '新增时间',
  `expiratedtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='token表';

-- ----------------------------
-- Records of token
-- ----------------------------
INSERT INTO `token` VALUES ('1', '61', '用户账号1', 'yonghu', '用户', 'rbigjguzz8ag5md8g7o1unngxc4iqleg', '2025-05-30 23:49:12', '2025-06-04 14:15:41');
INSERT INTO `token` VALUES ('2', '1', 'admin', 'users', '管理员', 'ewe399t9hcfipq3oc2y5i1jw6nakqc00', '2025-05-30 23:50:10', '2025-06-05 18:42:46');
INSERT INTO `token` VALUES ('3', '71', '摄影账号1', 'sheyingshi', '摄影师', '8yyb3ks164ii2ubt3ghq04fbh1k3vybz', '2025-05-30 23:51:32', '2025-06-05 00:06:15');
INSERT INTO `token` VALUES ('4', '1748621356334', '235022126', 'yonghu', '用户', 'k9kzpuu0p9vminq9uln7ibwderwt4th8', '2025-05-31 00:09:25', '2025-06-04 13:18:31');
INSERT INTO `token` VALUES ('5', '1748621434959', '235022125', 'yonghu', '用户', 'jzsix0dlt9z48s6tjpmtxp52kof37dcu', '2025-05-31 00:10:48', '2025-06-05 00:05:13');
INSERT INTO `token` VALUES ('6', '1748678396017', '10086', 'users', '管理员', 'ttjw19k8wbqygaob22c0ojhndh1i83kj', '2025-05-31 16:00:09', '2025-06-04 14:06:49');
INSERT INTO `token` VALUES ('7', '1748678347218', '10086', 'yonghu', '用户', '7cixczhag0k4o74wd89rz97azsmcq1gi', '2025-05-31 16:02:14', '2025-06-05 12:46:10');
INSERT INTO `token` VALUES ('8', '1748791211018', '123456', 'yonghu', '用户', 'i34cncad4neicd16hln4nbu38nw3cfvf', '2025-06-01 23:20:20', '2025-06-04 18:59:45');
INSERT INTO `token` VALUES ('9', '1749017782595', '123', 'yonghu', '用户', 'q8s72jnk5fi8p1rffp7vsjtso3n30tu0', '2025-06-04 14:16:33', '2025-06-04 15:16:33');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `username` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `password` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `role` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT '管理员' COMMENT '角色',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1748678396018 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员';

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('1', '2025-05-22 16:27:01', 'admin', 'admin', '管理员');
INSERT INTO `users` VALUES ('1748678396017', '2025-05-31 15:59:56', '10086', '123456', '');

-- ----------------------------
-- Table structure for yonghu
-- ----------------------------
DROP TABLE IF EXISTS `yonghu`;
CREATE TABLE `yonghu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `yonghuzhanghao` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户账号',
  `mima` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `yonghuxingming` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户姓名',
  `touxiang` longtext COLLATE utf8mb4_unicode_ci COMMENT '头像',
  `xingbie` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '性别',
  `lianxidianhua` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联系电话',
  PRIMARY KEY (`id`),
  UNIQUE KEY `yonghuzhanghao` (`yonghuzhanghao`)
) ENGINE=InnoDB AUTO_INCREMENT=1749017782596 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';

-- ----------------------------
-- Records of yonghu
-- ----------------------------
INSERT INTO `yonghu` VALUES ('61', '2025-05-22 16:27:00', '用户账号1', '123456', '用户姓名1', 'file/yonghuTouxiang1.jpg', '男', '19819881111');
INSERT INTO `yonghu` VALUES ('62', '2025-05-22 16:27:00', '用户账号2', '123456', '用户姓名2', 'file/yonghuTouxiang2.jpg', '男', '19819881112');
INSERT INTO `yonghu` VALUES ('63', '2025-05-22 16:27:00', '用户账号3', '123456', '用户姓名3', 'file/yonghuTouxiang3.jpg', '男', '19819881113');
INSERT INTO `yonghu` VALUES ('64', '2025-05-22 16:27:00', '用户账号4', '123456', '用户姓名4', 'file/yonghuTouxiang4.jpg', '男', '19819881114');
INSERT INTO `yonghu` VALUES ('65', '2025-05-22 16:27:00', '用户账号5', '123456', '用户姓名5', 'file/yonghuTouxiang5.jpg', '男', '19819881115');
INSERT INTO `yonghu` VALUES ('66', '2025-05-22 16:27:00', '用户账号6', '123456', '用户姓名6', 'file/yonghuTouxiang6.jpg', '男', '19819881116');
INSERT INTO `yonghu` VALUES ('67', '2025-05-22 16:27:00', '用户账号7', '123456', '用户姓名7', 'file/yonghuTouxiang7.jpg', '男', '19819881117');
INSERT INTO `yonghu` VALUES ('68', '2025-05-22 16:27:00', '用户账号8', '123456', '用户姓名8', 'file/yonghuTouxiang8.jpg', '男', '19819881118');
INSERT INTO `yonghu` VALUES ('69', '2025-05-22 16:27:00', '用户账号9', '123456', '用户姓名9', 'file/yonghuTouxiang9.jpg', '男', '19819881119');
INSERT INTO `yonghu` VALUES ('70', '2025-05-22 16:27:00', '用户账号10', '123456', '用户姓名10', 'file/yonghuTouxiang10.jpg', '男', '198198811110');
INSERT INTO `yonghu` VALUES ('1748621356334', '2025-05-31 00:09:16', '235022126', '123456', '方云枫', null, '男', '15659394050');
INSERT INTO `yonghu` VALUES ('1748621434959', '2025-05-31 00:10:34', '235022125', '123456', '小城', null, '男', '15659394051');
INSERT INTO `yonghu` VALUES ('1748678347218', '2025-05-31 15:59:07', '10086', '123456', '小城', null, '男', '15659394050');
INSERT INTO `yonghu` VALUES ('1748678486990', '2025-05-31 16:01:26', '235022129', '123456', '小城', null, '男', '15659394000');
INSERT INTO `yonghu` VALUES ('1748679714704', '2025-05-31 16:21:54', '2', '2', '1', null, '男', '');
INSERT INTO `yonghu` VALUES ('1748791211018', '2025-06-01 23:20:11', '123456', '123456', '123456', null, '女', '19978778787');
INSERT INTO `yonghu` VALUES ('1749017782595', '2025-06-04 14:16:22', '123', '123', '111', null, '男', null);

-- ----------------------------
-- Table structure for yonhuyuyuexinxi
-- ----------------------------
DROP TABLE IF EXISTS `yonhuyuyuexinxi`;
CREATE TABLE `yonhuyuyuexinxi` (
  `reservation_id` bigint(20) DEFAULT NULL COMMENT '主键id，自增',
  `aphotographer_name` varchar(50) DEFAULT NULL COMMENT '摄影师姓名',
  `photographer_account` varchar(50) DEFAULT NULL COMMENT '摄影师账号',
  `reservation_price` decimal(10,2) DEFAULT NULL COMMENT '预约价格',
  `user_account` varchar(50) DEFAULT NULL COMMENT '用户账号',
  `reservation_date` datetime DEFAULT NULL COMMENT '预约日期',
  `contact_number` varchar(20) DEFAULT NULL COMMENT '联系方式',
  `customer_name` varchar(50) DEFAULT NULL COMMENT '预约人姓名',
  `reservation_status` tinyint(4) DEFAULT NULL COMMENT '预约状态：0-待确认，1-已确认，2-已取消，3-已完成',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yonhuyuyuexinxi
-- ----------------------------
