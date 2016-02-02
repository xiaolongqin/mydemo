/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50621
Source Host           : localhost:3306
Source Database       : oss

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2015-04-24 09:55:33
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `orders`
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `order_num` varchar(50) NOT NULL,
  `goods_name` varchar(50) NOT NULL DEFAULT '开放存储服务 OSS',
  `order_time` bigint(20) NOT NULL,
  `user_email` varchar(50) NOT NULL,
  `user_realname` varchar(50) NOT NULL,
  `user_phone` varchar(50) NOT NULL,
  `space_id` int(11) NOT NULL,
  `pay_state` int(5) NOT NULL DEFAULT '0',
  `check_state` int(5) NOT NULL DEFAULT '0',
  `price` int(10) NOT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `add_space` int(50) NOT NULL,
  `user_operate` int(4) DEFAULT '0',
  PRIMARY KEY (`order_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for `space`
-- ----------------------------
DROP TABLE IF EXISTS `space`;
CREATE TABLE `space` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `space_name` varchar(255) NOT NULL DEFAULT '开放存储服务 OSS',
  `space_remarks` varchar(255) DEFAULT NULL,
  `all_space` float(20,0) NOT NULL DEFAULT '5000000000',
  `user_email` varchar(255) NOT NULL,
  `used_space` float(20,0) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of space
-- ----------------------------
