-- 已有数据库升级到本版本时执行一次。
-- 新导入 sql/guns.sql 的数据库无需重复执行。

ALTER TABLE `sys_user`
    MODIFY COLUMN `password` varchar(255) NULL DEFAULT NULL COMMENT '密码摘要';
