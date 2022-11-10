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


CREATE TABLE IF NOT EXISTS `CM_System`.`user_judge`
(
    `id`      int primary key auto_increment comment '裁判id',
    `user_id` int         not null comment '用户id',
    `name`    char(12)    not null comment '姓名',
    `id_card` char(18)    not null comment '身份证',
    `company` varchar(20) not null comment '单位',
    `career`  varchar(14) not null comment '职业',
    `profile` TEXT comment '团队简介',
    foreign key (`user_id`) references `CM_System`.`base_user` (`id`) on delete cascade,
    unique index user_judge_id_user_card (`id`, `user_id`, `id_card`)
) comment '裁判表';

CREATE TABLE IF NOT EXISTS `CM_System`.`user_team`
(
    `id`         int primary key auto_increment comment '团队id',
    `user_id`    int         not null comment '用户id',
    `leader`     varchar(12) not null comment '队长',
    `name`       varchar(12) not null comment '团队名称',
    `member`     TEXT comment '参赛成员',
    `instructor` varchar(12) comment '指导老师',
    `profile`    TEXT comment '团队简介',
    foreign key (`user_id`) references `CM_System`.`base_user` (`id`) on delete cascade,
    unique index user_team_id_user_name (`id`, `user_id`, `name`)
) comment '参赛团队表';

CREATE TABLE IF NOT EXISTS `CM_System`.`user_organization`
(
    `id`      int primary key auto_increment comment '组织id',
    `user_id` int         not null comment '用户id',
    `name`    varchar(36) not null comment '组织名称',
    `owner`   varchar(12) not null comment '组织负责人名字',
    `idCard`  char(18)    not null comment '组织负责人身份证号',
    `profile` TEXT comment '组织简介',
    foreign key (`user_id`) references `CM_System`.`base_user` (`id`) on delete cascade,
    unique index user_organization_id_user_name (`id`, `user_id`, `name`)
) comment '大赛组织者表';

CREATE TABLE IF NOT EXISTS `CM_System`.`user_admin`
(
    `id`      int primary key auto_increment comment '管理id',
    `user_id` int         not null comment '用户id',
    `name`    varchar(12) not null comment '管理员名字',
    foreign key (`user_id`) references `CM_System`.`base_user` (`id`) on delete cascade,
    unique index user_admin_id_user_id (`id`, `user_id`)
) comment '大赛组织者表';

-- 默认账号 admin@mail.com admin123654
insert ignore into `CM_System`.`base_user` (id, mail, password, role)
values (1, 'admin@mail.com', '$2a$10$DiPF2HTX9Uzvuy83CRh4jOWzm1ZwC7CH4A6o7dXBTN9h7HiDZBv7G', 0);
insert ignore into `CM_System`.`user_admin` (id, user_id, name)
values (1, 1, '系统管理员');

CREATE TABLE IF NOT EXISTS `CM_System`.`real_work`
(
    `id`             int primary key auto_increment comment '作品id',
    `team_id`        int          not null comment '团队id',
    `name`           varchar(255) not null comment '作品名称',
    `content`        longblob comment '作品内容',
    `post`           TEXT comment '邮寄信息',
    `in_store_time`  datetime comment '入库时间',
    `position`       text comment '入库存放地点',
    `out_store_time` datetime comment '出库时间',
    `out_state`      varchar(15) comment '出库状态',
    foreign key (`team_id`) references `user_team` (`id`) on delete cascade,
    index real__team_id (`team_id`)
) comment '实物作品表';

CREATE TABLE IF NOT EXISTS `CM_System`.`work`
(
    `id`      int primary key not null auto_increment comment '作品id',
    `team_id` int             not null comment '团队id',
    `real_id` int comment '实物作品id',
    `name`    varchar(255)    not null comment '作品名称',
    `profile` text comment '作品简介',
    `content` text comment '作品内容',
    `realID`  int comment '实物作品ID',
    `score`   float comment '得分',
    foreign key (`team_id`) references `user_team` (`id`) on delete cascade,
    foreign key (`real_id`) references `real_work` (`id`) on delete set null,
    index work_team_id (`team_id`)
) comment '作品表';