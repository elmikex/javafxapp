-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Apr 10, 2016 at 06:40 AM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `testjavafx`
--

-- --------------------------------------------------------

--
-- Table structure for table `checkins`
--

CREATE TABLE IF NOT EXISTS `checkins` (
  `id` int(11) NOT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `member_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKoaiq3p31bg0477qks26pknpkg` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `employees`
--

CREATE TABLE IF NOT EXISTS `employees` (
  `emp_salary` double DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `hibernate_sequence`
--

CREATE TABLE IF NOT EXISTS `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `members`
--

CREATE TABLE IF NOT EXISTS `members` (
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `memberships`
--

CREATE TABLE IF NOT EXISTS `memberships` (
  `id` int(11) NOT NULL,
  `chargeTax` varchar(255) DEFAULT NULL,
  `cycle` varchar(255) DEFAULT NULL,
  `cycleAmount` double DEFAULT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `dateUpdated` datetime DEFAULT NULL,
  `daysAllowed` varchar(255) DEFAULT NULL,
  `daysRestriction` bit(1) DEFAULT NULL,
  `expire` bit(1) DEFAULT NULL,
  `expireDate` datetime DEFAULT NULL,
  `isActive` bit(1) DEFAULT NULL,
  `isDeleted` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `timesAllowed` varchar(255) DEFAULT NULL,
  `timesRestriction` bit(1) DEFAULT NULL,
  `userCreated` int(11) DEFAULT NULL,
  `userUpdated` int(11) DEFAULT NULL,
  `visitsAllowed` int(11) DEFAULT NULL,
  `visitsRestriction` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `members_memberships`
--

CREATE TABLE IF NOT EXISTS `members_memberships` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `beginDate` datetime DEFAULT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `dateUpdated` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `member_id` int(11) DEFAULT NULL,
  `membership_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKc869ereys7rj40s9uv1l4hpko` (`member_id`),
  KEY `FK11xbqt683x9wl1coaowr1xbn0` (`membership_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=4 ;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `address2` varchar(255) DEFAULT NULL,
  `birthdate` date DEFAULT NULL,
  `cellPhone` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `dateUpdated` datetime DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `homePhone` varchar(255) DEFAULT NULL,
  `isActive` bit(1) DEFAULT NULL,
  `isDeleted` bit(1) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `occupation` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `userCreated` int(11) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `userUpdated` int(11) DEFAULT NULL,
  `workPhone` varchar(255) DEFAULT NULL,
  `zipCode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=9 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `checkins`
--
ALTER TABLE `checkins`
  ADD CONSTRAINT `FKoaiq3p31bg0477qks26pknpkg` FOREIGN KEY (`member_id`) REFERENCES `members` (`user_id`);

--
-- Constraints for table `employees`
--
ALTER TABLE `employees`
  ADD CONSTRAINT `FKgph7ais5uxsg9qyv486wu989i` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `members`
--
ALTER TABLE `members`
  ADD CONSTRAINT `FKnjrw57sxjersn2f6r5oyda8i5` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

--
-- Constraints for table `members_memberships`
--
ALTER TABLE `members_memberships`
  ADD CONSTRAINT `FK11xbqt683x9wl1coaowr1xbn0` FOREIGN KEY (`membership_id`) REFERENCES `memberships` (`id`),
  ADD CONSTRAINT `FKc869ereys7rj40s9uv1l4hpko` FOREIGN KEY (`member_id`) REFERENCES `members` (`user_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
