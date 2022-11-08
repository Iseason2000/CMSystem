CREATE SCHEMA IF NOT EXISTS `CM_System`;
use `CM_System`;
CREATE TABLE IF NOT EXISTS `CM_System`.`BASE_USER`
(
    `id`       int primary key not null auto_increment,
    `mail`     varchar(255)    not null,
    `password` varchar(255)    not null,
    `role`     int             not null,
    unique index mail_password (`mail`, `password`)
);
-- 默认账号 admin@mail.com admin123654
insert ignore into `CM_System`.`BASE_USER` (id, mail, password, role)
values (1, 'admin@mail.com', '$2a$10$DiPF2HTX9Uzvuy83CRh4jOWzm1ZwC7CH4A6o7dXBTN9h7HiDZBv7G', 0);