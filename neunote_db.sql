-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 2018-05-01 11:18:44
-- 服务器版本： 10.1.21-MariaDB
-- PHP Version: 7.1.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `neunote`
--
CREATE DATABASE IF NOT EXISTS `neunote` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `neunote`;

-- --------------------------------------------------------

--
-- 表的结构 `connection`
--

CREATE TABLE `connection` (
  `C_code` varchar(50) NOT NULL,
  `stu_id` varchar(30) DEFAULT NULL,
  `overtime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- 转存表中的数据 `connection`
--

INSERT INTO `connection` (`C_code`, `stu_id`, `overtime`) VALUES
('7165d7dc-7933-4204-96ac-3b9320100f60', NULL, '2018-05-01 17:16:38'),
('9f409674-e354-49cb-a49f-dd9155228584', '20164929', '2018-05-01 18:10:47'),
('e38521c3-de41-42b0-bd96-dbd42fa73203', NULL, '2018-05-01 16:46:08'),
('f476c047-a6ef-454b-8e2c-08472b247236', '20160000', '2018-05-01 18:16:33');

-- --------------------------------------------------------

--
-- 表的结构 `deleted_notes`
--

CREATE TABLE `deleted_notes` (
  `id` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `notes`
--

CREATE TABLE `notes` (
  `noteID` varchar(50) NOT NULL,
  `title` varchar(50) NOT NULL,
  `author` varchar(50) NOT NULL,
  `releaseTime` datetime NOT NULL,
  `pages` int(11) NOT NULL,
  `lastEditTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `notes`
--

INSERT INTO `notes` (`noteID`, `title`, `author`, `releaseTime`, `pages`, `lastEditTime`) VALUES
('ewLaN4P2', '测试账号的新笔记', '20160000', '2018-05-01 17:15:16', 1, '2018-05-01 17:15:33'),
('LL6pGyby', '新笔记', '20164929', '2018-05-01 17:09:53', 1, '2018-05-01 17:10:20'),
('wchfmzJ0', '使用说明', '20164929', '2018-05-01 16:45:26', 2, '2018-05-01 17:10:31');

-- --------------------------------------------------------

--
-- 表的结构 `shared_notes`
--

CREATE TABLE `shared_notes` (
  `noteID` varchar(50) NOT NULL,
  `title` varchar(50) NOT NULL,
  `author` varchar(50) NOT NULL,
  `pages` int(11) NOT NULL,
  `sharedtime` datetime DEFAULT NULL,
  `lastEditTime` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `shared_notes`
--

INSERT INTO `shared_notes` (`noteID`, `title`, `author`, `pages`, `sharedtime`, `lastEditTime`) VALUES
('ewLaN4P2', '测试账号的新笔记', '20160000', 1, '2018-05-01 17:15:31', '2018-05-01 17:15:30'),
('LL6pGyby', '新笔记', '20164929', 1, '2018-05-01 17:10:18', '2018-05-01 17:10:12'),
('wchfmzJ0', '使用说明', '20164929', 2, '2018-05-01 16:46:03', '2018-05-01 17:09:54');

-- --------------------------------------------------------

--
-- 表的结构 `student`
--

CREATE TABLE `student` (
  `stu_id` varchar(30) NOT NULL,
  `stu_name` varchar(30) NOT NULL,
  `sex` varchar(30) NOT NULL,
  `college` varchar(30) NOT NULL,
  `major` varchar(30) NOT NULL,
  `stu_class` varchar(30) NOT NULL,
  `grade` varchar(30) NOT NULL,
  `level` varchar(30) NOT NULL,
  `gpa` double NOT NULL,
  `password` varchar(30) NOT NULL,
  `tree` text NOT NULL,
  `editing` varchar(50) NOT NULL,
  `nickname` varchar(50) NOT NULL,
  `introduction` varchar(50) NOT NULL,
  `headphoto` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

--
-- 转存表中的数据 `student`
--

INSERT INTO `student` (`stu_id`, `stu_name`, `sex`, `college`, `major`, `stu_class`, `grade`, `level`, `gpa`, `password`, `tree`, `editing`, `nickname`, `introduction`, `headphoto`) VALUES
('20160000', '张格皓', '男', '软件学院', '信息安全', '软件1602', '2016级', '全日制本科', 3.5, '654321', '%7B%22key%22%3A%22%E6%96%B0%E7%AC%94%E8%AE%B0%E6%9C%AC%22%2C%22value%22%3A%5B%7B%22key%22%3A%22%E6%96%B0%E5%88%86%E5%8C%BA%22%2C%22value%22%3A%5B%22ewLaN4P2%22%5D%7D%5D%7D%2C%7B%22trash%22%3A%22%22%2C%22value%22%3A%5B%5D%7D', 'f476c047-a6ef-454b-8e2c-08472b247236', '测试账号', '用于测试。', 'http://upload.mnw.cn/2016/0515/1463284616412.jpg'),
('20164929', '张格皓', '男', '软件学院', '软件工程', '软件1602', '2016级', '全日制本科', 3.6218, '123456', '%7B%22key%22%3A%22%E6%96%B0%E7%AC%94%E8%AE%B0%E6%9C%AC%22%2C%22value%22%3A%5B%7B%22key%22%3A%22%E6%96%B0%E5%88%86%E5%8C%BA%22%2C%22value%22%3A%5B%22wchfmzJ0%22%2C%22LL6pGyby%22%5D%7D%5D%7D%2C%7B%22trash%22%3A%22%22%2C%22value%22%3A%5B%5D%7D', 'null', '儿童玩具开发招商', '从此与前端永别。', 'http://mpic.tiankong.com/7e3/47a/7e347a4a8b21f085bccc32941550976c/640.jpg@!670w');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `connection`
--
ALTER TABLE `connection`
  ADD PRIMARY KEY (`C_code`);

--
-- Indexes for table `deleted_notes`
--
ALTER TABLE `deleted_notes`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `notes`
--
ALTER TABLE `notes`
  ADD PRIMARY KEY (`noteID`);

--
-- Indexes for table `shared_notes`
--
ALTER TABLE `shared_notes`
  ADD PRIMARY KEY (`noteID`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
  ADD PRIMARY KEY (`stu_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
