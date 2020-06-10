/*
SQLyog Community v12.4.1 (64 bit)
MySQL - 5.7.18-log : Database - qs_test
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
USE `qs_test`;

/*Table structure for table `t_comment` */

DROP TABLE IF EXISTS `t_comment`;

CREATE TABLE `t_comment` (
  `cid` int(16) NOT NULL AUTO_INCREMENT,
  `uid` int(16) DEFAULT NULL,
  `commented` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `content` varchar(1024) DEFAULT NULL,
  `replyId` int(16) DEFAULT NULL,
  `pid` int(16) DEFAULT NULL,
  `pcid` int(16) DEFAULT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB AUTO_INCREMENT=216 DEFAULT CHARSET=utf8;

/*Data for the table `t_comment` */

/*Table structure for table `t_message` */

DROP TABLE IF EXISTS `t_message`;

CREATE TABLE `t_message` (
  `mid` int(16) NOT NULL AUTO_INCREMENT,
  `sender` int(16) DEFAULT NULL,
  `receiver` int(16) DEFAULT NULL,
  `sendtime` timestamp DEFAULT NULL,
  `state` int(8) DEFAULT NULL,
  `content` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`mid`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

/*Data for the table `t_message` */

/*Table structure for table `t_post` */

DROP TABLE IF EXISTS `t_post`;

CREATE TABLE `t_post` (
  `pid` int(32) NOT NULL AUTO_INCREMENT,
  `published` timestamp NULL DEFAULT NULL,
  `title` varchar(320) DEFAULT NULL,
  `content` varchar(1024) DEFAULT NULL,
  `uid` varchar(32) DEFAULT NULL,
  `level` int(16) DEFAULT NULL,
  `views` int(16) DEFAULT NULL,
  `state` int(16) DEFAULT '0',
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=67174426 DEFAULT CHARSET=utf8;

/*Data for the table `t_post` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
