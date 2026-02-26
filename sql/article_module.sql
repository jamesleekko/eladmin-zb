-- =============================================
-- 文章管理模块 - 生产环境部署脚本
-- 前提：sys_article_list、sys_article_type、sys_article_kind 表已存在
-- =============================================

-- 1. 补充 sys_article_list 表可能缺少的列（Article 实体 extends BaseEntity 需要）
--    如果列已存在，会报错可忽略；也可提前 SHOW COLUMNS 检查
ALTER TABLE `sys_article_list` ADD COLUMN `create_by` varchar(255) DEFAULT NULL COMMENT '创建人';
ALTER TABLE `sys_article_list` ADD COLUMN `update_by` varchar(255) DEFAULT NULL COMMENT '更新人';
ALTER TABLE `sys_article_list` ADD COLUMN `create_time` datetime DEFAULT NULL COMMENT '创建时间';

-- 2. 插入菜单：文章管理（挂在"系统管理" pid=1 下）
INSERT INTO `sys_menu` (`pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`)
VALUES (1, 3, 1, '文章管理', 'Article', 'system/article/index', 4, 'documentation', 'article', b'0', b'0', b'0', 'article:list', 'admin', NULL, NOW(), NULL);

SET @article_menu_id = LAST_INSERT_ID();

-- 3. 插入按钮权限
INSERT INTO `sys_menu` (`pid`, `sub_count`, `type`, `title`, `name`, `component`, `menu_sort`, `icon`, `path`, `i_frame`, `cache`, `hidden`, `permission`, `create_by`, `update_by`, `create_time`, `update_time`)
VALUES
(@article_menu_id, 0, 2, '文章新增', NULL, '', 2, '', '', b'0', b'0', b'0', 'article:add', NULL, NULL, NOW(), NULL),
(@article_menu_id, 0, 2, '文章编辑', NULL, '', 3, '', '', b'0', b'0', b'0', 'article:edit', NULL, NULL, NOW(), NULL),
(@article_menu_id, 0, 2, '文章删除', NULL, '', 4, '', '', b'0', b'0', b'0', 'article:del', NULL, NULL, NOW(), NULL);

-- 4. 更新"系统管理"(menu_id=1)的子菜单计数
UPDATE `sys_menu` SET `sub_count` = `sub_count` + 1 WHERE `menu_id` = 1;

-- 5. 将菜单分配给管理员角色 (role_id=1)
INSERT INTO `sys_roles_menus` (`menu_id`, `role_id`) VALUES (@article_menu_id, 1);
INSERT INTO `sys_roles_menus` (`menu_id`, `role_id`)
SELECT `menu_id`, 1 FROM `sys_menu` WHERE `pid` = @article_menu_id;

-- =============================================
-- 6. 服务器文件目录（需手动在 Linux 生产服务器上执行）
--    mkdir -p /home/eladmin/article
--    chown eladmin:eladmin /home/eladmin/article
-- =============================================
