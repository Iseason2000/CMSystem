use `CM_System`;
-- 默认账号 admin@mail.com admin123654
insert
ignore into `CM_System`.`base_user` (id, mail, password, role)
values (1, 'admin@mail.com', '$2a$10$DiPF2HTX9Uzvuy83CRh4jOWzm1ZwC7CH4A6o7dXBTN9h7HiDZBv7G', 0);
insert
ignore into `CM_System`.`user_admin` (id, user_id, name)
values (1, 1, '系统管理员');
