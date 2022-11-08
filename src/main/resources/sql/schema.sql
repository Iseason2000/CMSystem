CREATE SCHEMA IF NOT EXISTS `CM_System`;

use `CM_System`;

CREATE TABLE IF NOT EXISTS `CM_System`.`base_user`
(
    `id`       int primary key not null auto_increment comment '用户id',
    `mail`     varchar(255)    not null comment '用户邮箱',
    `password` varchar(255)    not null comment '用户密码',
    `role`     int             not null comment '用户权限',
    unique index base_user_mail (`mail`),
    unique index base_user_mail_password (`mail`, `password`)
);

-- 默认账号 admin@mail.com admin123654
insert ignore into `CM_System`.`base_user` (id, mail, password, role)
values (1, 'admin@mail.com', '$2a$10$DiPF2HTX9Uzvuy83CRh4jOWzm1ZwC7CH4A6o7dXBTN9h7HiDZBv7G', 0);

CREATE TABLE IF NOT EXISTS `CM_System`.`user_judge`
(
    `id`      int primary key auto_increment comment '裁判id',
    `user_id` int         not null comment '用户id',
    `name`    char(12)    not null comment '姓名',
    `id_card` char(18)    not null comment '身份证',
    `company` varchar(20) not null comment '单位',
    `career`  varchar(14) not null comment '职业',
    `profile` TEXT comment '团队简介',
    unique index user_judge_id_user_card (`id`, `user_id`, `id_card`)
) comment '裁判表';

CREATE TABLE IF NOT EXISTS `CM_System`.`user_team`
(
    `id`         int primary key auto_increment comment '团队id',
    `user_id`    int         not null comment '用户id',
    `leader`     varchar(12) not null comment '队长',
    `name`       varchar(12) not null comment '团队名称',
    `member`     TEXT comment '参赛成员',
    `instructor` char(12) comment '指导老师',
    `profile`    TEXT comment '团队简介',
    unique index user_team_id_user_name (`id`, `user_id`, `name`)
) comment '参赛团队表';

CREATE TABLE IF NOT EXISTS `CM_System`.`user_organization`
(
    `id`      int primary key auto_increment comment '组织id',
    `user_id` int      not null comment '用户id',
    `name`    char(12) not null comment '组织名称',
    `owner`   char(12) not null comment '组织负责人名字',
    `idCard`  char(18) not null comment '组织负责人身份证号',
    `profile` TEXT comment '组织简介',
    unique index user_organization_id_user_name (`id`, `user_id`, `name`)
) comment '大赛组织者表';

CREATE TABLE IF NOT EXISTS `CM_System`.`user_admin`
(
    `id`      int primary key auto_increment comment '管理id',
    `user_id` int      not null comment '用户id',
    `name`    char(12) not null comment '管理员名字',
    unique index user_admin_id_user_id (`id`, `user_id`)
) comment '大赛组织者表';

