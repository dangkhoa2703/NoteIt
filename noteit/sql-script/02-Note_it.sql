
DROP SCHEMA IF EXISTS `note_it`;

CREATE SCHEMA `note_it`;

USE `note_it`;

DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `note`;
DROP TABLE IF EXISTS `shared_note`;

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` char(68) NOT NULL,
  `role` varchar(50) NOT NULL,
  `enabled` tinyint NOT NULL DEFAULT 1,  
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


-- Dumping data for table `user`
INSERT INTO user(`username`,`password`,`role`,`enabled`)
VALUES
('HungryRabbit', '$2a$10$DKy/Y0XoaN3JDzlx1jdbV.2vbNeSN7VgMEwvjw5q.w9x1VB7UzB1u',"USER",'1'),
('SleepyCat', '$2a$10$ylg6eed0P4ft4Tnpxi5F/.KJopCaWQRAaKXeuFQdwx09NsPot7Kci',"USER",'1'),
('MuscleWhale', '$2a$10$mZFp25PkDQfTOr2Wn19FtueBD6pdyDpgbtJD1MVekV8c4SKdDOZqu',"USER",'1'),
('FunnyBanana', '$2a$10$vh7qmmov8cBXZxmI7/KA3OVAYcQp1l6XuvI44HACGcXIxg8nR4xbi',"USER",'1'),
('ChillyAlligator', '$2a$10$w/146uddsT0NlPK4NvDZtOLy9lv20ABPP6HtM7ykmFyFqiRNE0J8q',"USER",'1');


--
-- Table structure for table `note`
--

CREATE TABLE `note` (
  `id`int NOT NULL AUTO_INCREMENT,
  `subject` mediumtext ,
  `content` mediumtext NOT NULL,
  `author_id` int NOT NULL ,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `note`(`subject`,`content`,`author_id`)
VALUES
('Camping on Sunday', 'Please show up ON TIME and wear sunscreen', '1'),
('Wake me up', 'FunnyBanana! Please wake me up at 6:00 on Sunday', '2'),
('Cardio day', 'Swim 100km', '3'),
('Dad Jokes', '\"I used to be addicted to soap, but I\'m clean now.\"', '4'),
('To do list', 'buy sunscreen and mosquito repellent', '5');


CREATE TABLE `shared_note` (
  `user_id`int NOT NULL AUTO_INCREMENT,
  `note_id` int NOT NULL,
  `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `PK_user_note` PRIMARY KEY (`user_id`,`note_id`),
  CONSTRAINT `FK_user` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_note` FOREIGN KEY (`note_id`) REFERENCES `note`(`id`)
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `shared_note`(`user_id`,`note_id`)
VALUES
('2', '1'),
('3', '1'),
('4', '1'),
('5', '1'),
('4', '2');







