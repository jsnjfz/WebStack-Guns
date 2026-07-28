-- 已有数据库升级到本版本时执行一次。执行前必须先备份数据库。
-- 新导入 sql/guns.sql 的数据库无需重复执行。

ALTER TABLE `sys_user`
    MODIFY COLUMN `password` varchar(255) NULL DEFAULT NULL COMMENT '密码摘要';

-- 移除已经下线的服务器端代码生成器入口及其明文数据库连接信息表。
DELETE FROM `sys_relation`
WHERE `menuid` IN (
    SELECT `id` FROM `sys_menu` WHERE `code` = 'code' OR `url` = '/code'
);
DELETE FROM `sys_menu` WHERE `code` = 'code' OR `url` = '/code';
DROP TABLE IF EXISTS `code_dbinfo`;

-- 只删除仍保持原始指纹的演示账号，避免误删用户自行创建的同名账号。
DELETE FROM `sys_user`
WHERE (`account` = 'test' AND `password` = '45abb7879f6a8268f1ef600e6038ac73' AND `salt` = 'ssts3')
   OR (`account` = 'boss' AND `password` = '71887a5ad666a18f709e1d4e693d5a35' AND `salt` = '1f7bf')
   OR (`account` = 'manager' AND `password` = 'b53cac62e7175637d4beb3b16b2f7915' AND `salt` = 'j3cs9');
