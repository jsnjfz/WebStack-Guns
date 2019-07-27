/*
 Navicat Premium Data Transfer

 Source Server         : 49.4.91.137
 Source Server Type    : MySQL
 Source Server Version : 50726
 Source Host           : 49.4.91.137:3306
 Source Schema         : guns

 Target Server Type    : MySQL
 Target Server Version : 50726
 File Encoding         : 65001

 Date: 26/07/2019 22:21:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL DEFAULT 0,
  `sort` int(11) NOT NULL DEFAULT 0,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `icon` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `levels` int(11) NULL DEFAULT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, 0, 2, '常用推荐', 'fa-star-o', 1, '2019-01-21 12:48:58', '2019-05-22 16:29:50');
INSERT INTO `category` VALUES (2, 0, 1, '社区资讯', 'fa-bullhorn', 1, '2019-01-21 12:50:17', '2019-05-22 16:29:50');
INSERT INTO `category` VALUES (3, 0, 3, '灵感采集', 'fa-lightbulb-o', 1, '2019-01-21 13:53:10', '2019-01-21 13:53:34');
INSERT INTO `category` VALUES (4, 3, 4, '发现产品', 'fa-star-o', 2, '2019-01-21 13:53:31', '2019-01-21 13:53:34');
INSERT INTO `category` VALUES (5, 3, 5, '界面灵感', 'fa-star-o', 2, '2019-01-21 13:53:49', '2019-01-21 13:55:42');
INSERT INTO `category` VALUES (6, 3, 6, '网页灵感', 'fa-star-o', 2, '2019-01-21 13:54:00', '2019-01-21 13:55:42');
INSERT INTO `category` VALUES (7, 0, 7, '素材资源', 'fa-thumbs-o-up', 1, '2019-01-21 13:54:42', '2019-01-21 13:55:42');
INSERT INTO `category` VALUES (8, 7, 8, '图标素材', 'fa-star-o', 2, '2019-01-21 13:54:59', '2019-01-21 14:12:01');
INSERT INTO `category` VALUES (9, 7, 9, 'LOGO设计', 'fa-star-o', 2, '2019-01-21 13:55:11', '2019-01-21 13:55:42');
INSERT INTO `category` VALUES (10, 7, 10, '平面素材', 'fa-star-o', 2, '2019-01-21 13:55:25', '2019-01-21 13:55:42');
INSERT INTO `category` VALUES (11, 7, 11, 'UI资源', 'fa-star-o', 2, '2019-01-21 13:55:31', '2019-01-21 13:55:42');
INSERT INTO `category` VALUES (12, 7, 12, 'Sketch资源', 'fa-star-o', 2, '2019-01-22 01:00:51', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (13, 7, 13, '字体资源', 'fa-star-o', 2, '2019-01-22 01:01:02', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (14, 7, 14, 'Mockup', 'fa-star-o', 2, '2019-01-22 01:01:12', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (15, 7, 15, '摄影图库', 'fa-star-o', 2, '2019-01-22 01:01:23', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (16, 7, 16, 'PPT资源', 'fa-star-o', 2, '2019-01-22 01:01:33', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (17, 0, 17, '常用工具', 'fa-cogs', 1, '2019-01-22 01:02:04', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (18, 17, 18, '图形创意', 'fa-star-o', 2, '2019-01-22 01:02:14', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (19, 17, 19, '界面设计', 'fa-star-o', 2, '2019-01-22 01:02:24', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (20, 17, 20, '交互动效', 'fa-star-o', 2, '2019-01-22 01:02:32', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (21, 17, 21, '在线配色', 'fa-star-o', 2, '2019-01-22 01:02:40', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (22, 17, 22, '在线工具', 'fa-star-o', 2, '2019-01-22 01:02:49', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (23, 17, 23, 'Chrome插件', 'fa-star-o', 2, '2019-01-22 01:02:58', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (24, 0, 24, '学习教程', 'fa-pencil', 1, '2019-01-22 01:03:17', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (25, 24, 25, '设计规范', 'fa-star-o', 2, '2019-01-22 01:03:29', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (26, 24, 26, '视频教程', 'fa-star-o', 2, '2019-01-22 01:03:36', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (27, 24, 27, '设计文章', 'fa-star-o', 2, '2019-01-22 01:03:44', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (28, 24, 28, '设计电台', 'fa-star-o', 2, '2019-01-22 01:03:52', '2019-01-22 01:07:04');
INSERT INTO `category` VALUES (35, 0, 0, '媒体导航', 'fa-anchor', 1, '2019-07-26 16:08:10', '2019-07-26 17:02:12');

-- ----------------------------
-- Table structure for code_dbinfo
-- ----------------------------
DROP TABLE IF EXISTS `code_dbinfo`;
CREATE TABLE `code_dbinfo`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '别名',
  `db_driver` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据库驱动',
  `db_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据库地址',
  `db_user_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据库账户',
  `db_password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '连接密码',
  `db_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库类型',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据库链接信息' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for site
-- ----------------------------
DROP TABLE IF EXISTS `site`;
CREATE TABLE `site`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `category_id` int(11) NOT NULL,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `thumb` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` timestamp(0) NULL DEFAULT NULL,
  `update_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 235 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of site
-- ----------------------------
INSERT INTO `site` VALUES (1, 1, 'Dribbble', '7db1257f40b9b04482744387a00b359d.png', '全球UI设计师作品分享平台。', 'https://dribbble.com/', '2019-01-21 15:23:29', '2019-03-12 02:13:08');
INSERT INTO `site` VALUES (2, 1, 'Behance', 'c3b59ad338e7e122072b867381e4b599.png', 'Adobe旗下的设计师交流平台，来自世界各地的设计师在这里分享自己的作品。', 'https://behance.net/', '2019-01-22 01:11:24', '2019-01-22 01:11:24');
INSERT INTO `site` VALUES (3, 1, 'UI中国', '9458ececbfeea651b5e871179f245ce5.png', '图形交互与界面设计交流、作品展示、学习平台。', 'http://www.ui.cn/', '2019-01-22 01:13:10', '2019-01-22 01:13:10');
INSERT INTO `site` VALUES (4, 1, '站酷', 'c2aa8b63006d36a026e419bf3e0d7e50.png', '中国人气设计师互动平台。', 'http://www.zcool.com.cn/', '2019-01-22 01:14:04', '2019-01-22 01:14:04');
INSERT INTO `site` VALUES (5, 1, 'Pinterest', '4b8c9c66df25d3867961ebbd6a824eea.png', '全球美图收藏采集站。', 'https://www.pinterest.com/', '2019-01-22 01:14:51', '2019-01-22 01:14:51');
INSERT INTO `site` VALUES (6, 1, '花瓣', '48c7741c42e39eae508c3d9eef4e7a97.png', '收集灵感,保存有用的素材。', 'http://huaban.com/', '2019-01-22 01:15:35', '2019-01-22 01:15:35');
INSERT INTO `site` VALUES (7, 1, 'Medium', '11b56d4a9cc9297638d46c4b67c73693.png', '高质量设计文章。', 'https://medium.com/', '2019-01-22 01:16:14', '2019-01-22 01:16:14');
INSERT INTO `site` VALUES (8, 1, '优设', '636342035cdd2fbc08097d012b5e5fad.png', '设计师交流学习平台。', 'http://www.uisdc.com/', '2019-01-22 01:17:07', '2019-01-22 01:17:07');
INSERT INTO `site` VALUES (9, 1, 'Producthunt', '8bdf511e23b183f9aac30aee3368152b.png', '发现新鲜有趣的产品。', 'https://www.producthunt.com/', '2019-01-22 01:17:50', '2019-01-22 01:17:50');
INSERT INTO `site` VALUES (10, 1, 'Youtube', '15b4f46186f718e92b97f6f282f140fd.png', '全球最大的学习分享平台。', 'https://www.youtube.com/', '2019-01-22 01:18:31', '2019-01-22 01:18:31');
INSERT INTO `site` VALUES (11, 1, 'Google', '643faef109965a9cedcf9cde74d0adb1.png', '全球最大的搜索平台。', 'https://www.google.com/', '2019-01-22 01:19:35', '2019-01-22 01:19:35');
INSERT INTO `site` VALUES (12, 2, '雷锋网', '57ab570b631cc25a23390f507d496bc6.png', '人工智能和智能硬件领域的互联网科技媒体。', 'https://www.leiphone.com/', '2019-01-22 01:21:05', '2019-01-22 01:21:05');
INSERT INTO `site` VALUES (13, 2, '36kr', 'adef44c84185d959446024452ad615cc.png', '创业资讯、科技新闻。', 'http://36kr.com/', '2019-01-22 01:21:52', '2019-01-22 01:21:52');
INSERT INTO `site` VALUES (14, 2, '数英网', 'cee8fd836f98652f52fcc6046ba9db89.png', '数字媒体及职业招聘网站。', 'https://www.digitaling.com/', '2019-01-22 01:23:15', '2019-01-22 01:23:15');
INSERT INTO `site` VALUES (15, 2, '猎云网', 'f9b1cfa239abdda5ee2dca7c04ca2d73.png', '互联网创业项目推荐和创业创新资讯。', 'http://www.lieyunwang.com/', '2019-01-22 01:24:21', '2019-01-22 01:24:21');
INSERT INTO `site` VALUES (16, 2, '人人都是产品经理', 'fe7dcb7b5937c3bcf6043ba7d9379fbe.png', '产品经理、产品爱好者学习交流平台。', 'http://www.woshipm.com/', '2019-01-22 01:25:07', '2019-01-22 01:25:07');
INSERT INTO `site` VALUES (17, 2, '互联网早读课', 'ecd5eefc4ec4bac49916a91c2da44412.png', '互联网行业深度阅读与学习平台。', 'https://www.zaodula.com/', '2019-01-22 01:25:41', '2019-01-22 01:25:41');
INSERT INTO `site` VALUES (18, 2, '产品壹佰', '2f4235fb1eb75822a44903d9894760d7.png', '为产品经理爱好者提供最优质的产品资讯、原创内容和相关视频课程。', 'http://www.chanpin100.com/', '2019-01-22 01:26:27', '2019-01-22 01:26:27');
INSERT INTO `site` VALUES (19, 2, 'PMCAFF', 'a76e60c2d42f5bbdacbf7dd120810194.png', '中国第一产品经理人气组织，专注于研究互联网产品。', 'http://www.pmcaff.com/', '2019-01-22 01:27:08', '2019-01-22 01:27:08');
INSERT INTO `site` VALUES (20, 2, '爱运营', 'f84a9e00d63b007ea09df75dbdaf9549.png', '网站运营人员学习交流，专注于网站产品运营管理、淘宝运营。', 'http://www.iyunying.org/', '2019-01-22 01:28:09', '2019-01-22 01:28:09');
INSERT INTO `site` VALUES (21, 2, '鸟哥笔记', 'e424308a81c187bc42e95addf462b4c8.png', '移动互联网第一干货平台。', 'http://www.niaogebiji.com/', '2019-01-22 01:29:56', '2019-01-22 01:29:56');
INSERT INTO `site` VALUES (22, 2, '古田路9号', 'e22a54099a69f7e76c471d7f6a253fe8.png', '国内专业品牌创意平台。', 'http://www.gtn9.com/', '2019-01-22 01:30:29', '2019-01-22 01:30:29');
INSERT INTO `site` VALUES (23, 2, '优阁网', '74a25f510e403f4ef28212146510dfa7.png', 'UI设计师学习交流社区。', 'http://www.uigreat.com/', '2019-01-22 01:32:17', '2019-01-22 01:32:17');
INSERT INTO `site` VALUES (24, 4, 'Producthunt', 'a1cc88fa0a3bf74349ba9c08a67abdc7.png', '发现新鲜有趣的产品。', 'https://www.producthunt.com/', '2019-01-22 01:33:37', '2019-01-22 01:33:37');
INSERT INTO `site` VALUES (25, 4, 'NEXT', '0e2b6c9a5afd4f83d2e22632b08f56ef.png', '不错过任何一个新产品。', 'http://next.36kr.com/posts', '2019-01-22 01:34:41', '2019-01-22 01:34:41');
INSERT INTO `site` VALUES (26, 4, '少数派', '2d01ac82bb496b607c43e7b2b29cd069.png', '高品质数字消费指南。', 'https://sspai.com/', '2019-01-22 01:36:08', '2019-01-22 01:36:08');
INSERT INTO `site` VALUES (27, 4, '利器', '98e2ee62a90b6243630f4aa479b4b47b.png', '创造者和他们的工具。', 'http://liqi.io/', '2019-01-22 01:36:53', '2019-01-22 01:36:53');
INSERT INTO `site` VALUES (28, 4, 'Today', '1726189292537c3a2733ebc673b7f1d6.png', '为身边的新产品喝彩。', 'http://today.itjuzi.com/', '2019-01-22 01:37:37', '2019-01-22 01:37:37');
INSERT INTO `site` VALUES (29, 4, '小众软件', '76b49053ce87ab3c7419c7cdf6fa4f07.png', '在这里发现更多有趣的应用。', 'https://faxian.appinn.com', '2019-01-22 01:38:04', '2019-01-22 01:38:04');
INSERT INTO `site` VALUES (30, 5, 'Pttrns', '1c759dc53774e5758a31fee0401e213a.png', 'Check out the finest collection of design patterns, resources, mobile apps and inspiration', 'https://www.pttrns.com/', '2019-01-22 01:39:09', '2019-01-22 01:39:09');
INSERT INTO `site` VALUES (31, 5, 'Collect UI', '7e802c77c7bb6c85c1e2bb608a4a13cd.png', 'Daily inspiration collected from daily ui archive and beyond.', 'http://collectui.com/', '2019-01-22 01:39:58', '2019-01-22 01:39:58');
INSERT INTO `site` VALUES (32, 5, 'UI uigreat', 'd140fe4bd548f273ddd00f35e1ad5ff5.png', 'APP界面截图参考。', 'http://ui.uigreat.com/', '2019-01-22 01:40:40', '2019-01-22 01:40:40');
INSERT INTO `site` VALUES (33, 5, 'Android Niceties', '8b6e0af7df3a5d77a14e41a0f5f36dc5.png', 'A collection of screenshots encompassing some of the most beautiful looking Android apps.', 'http://androidniceties.tumblr.com/', '2019-01-22 01:41:21', '2019-01-22 01:41:21');
INSERT INTO `site` VALUES (34, 6, 'Awwwards', 'bdd6c88417790c97de2c2d0643cc602c.png', 'aaa', 'https://www.awwwards.com/', '2019-01-22 01:44:09', '2019-01-22 01:51:33');
INSERT INTO `site` VALUES (35, 6, 'CSS Design Awards', '481219fe4285f1f4ec1311acce7deb06.png', 'Website Awards & Inspiration - CSS Gallery', 'https://www.cssdesignawards.com/', '2019-01-22 01:44:45', '2019-01-22 01:51:41');
INSERT INTO `site` VALUES (36, 6, 'The FWA', '8fe5280ff7dc3012fb88781dd9ff4b93.png', 'FWA - showcasing innovation every day since 2000', 'https://thefwa.com/', '2019-01-22 01:45:19', '2019-01-22 01:51:48');
INSERT INTO `site` VALUES (37, 6, 'Ecommercefolio', '950d52c71e28f1c9ed964732d6ed18fd.png', 'Only the Best Ecommerce Design Inspiration.', 'http://www.ecommercefolio.com/', '2019-01-22 01:50:41', '2019-01-22 01:50:41');
INSERT INTO `site` VALUES (38, 6, 'Lapa', '1824678ec13d01b76df47fc5975178fa.png', 'The best landing page design inspiration from around the web.', 'http://www.lapa.ninja/', '2019-01-22 01:51:15', '2019-01-22 01:51:15');
INSERT INTO `site` VALUES (39, 6, 'Reeoo', '5205b768b9b640bfada244ce9d15318d.png', 'web design inspiration and website gallery.', 'http://reeoo.com/', '2019-01-22 01:52:43', '2019-01-22 01:52:43');
INSERT INTO `site` VALUES (40, 6, 'Designmunk', '31de409b71235b76d605e98293d68cb3.png', 'Best Homepage Design Inspiration.', 'https://designmunk.com/', '2019-01-22 01:53:28', '2019-01-22 01:53:28');
INSERT INTO `site` VALUES (41, 6, 'Best Websites Gallery', '862823249aa701d8bc8af16ae98f1e3a.png', 'Website Showcase Inspiration | Best Websites Gallery.', 'https://bestwebsite.gallery/', '2019-01-22 01:57:32', '2019-01-22 01:57:32');
INSERT INTO `site` VALUES (42, 6, 'Pages', '90fd20bd3e7ae7c7fe37ea689dcca32c.png', 'Curated directory of the best Pages.', 'http://www.pages.xyz/', '2019-01-22 01:58:07', '2019-01-22 01:58:07');
INSERT INTO `site` VALUES (43, 6, 'SiteSee', 'da24d08a597456be98191b4a08eff4d6.png', 'SiteSee is a curated gallery of beautiful, modern websites collections.', 'https://sitesee.co/', '2019-01-22 01:58:42', '2019-01-22 01:58:42');
INSERT INTO `site` VALUES (44, 6, 'Site Inspire', 'c15c9017ad6874faae0df64bd969115b.png', 'A CSS gallery and showcase of the best web design inspiration.', 'https://www.siteinspire.com/', '2019-01-22 01:59:13', '2019-01-22 01:59:13');
INSERT INTO `site` VALUES (45, 6, 'WebInspiration', 'f8fe63594e2083755086ee294b036108.png', '网页设计欣赏,全球顶级网页设计。', 'http://web.uedna.com/', '2019-01-22 01:59:54', '2019-01-22 01:59:54');
INSERT INTO `site` VALUES (46, 6, 'navnav', '86b9e596068f2a71d2a2444733a4094e.png', 'A ton of CSS, jQuery, and JavaScript responsive navigation examples, demos, and tutorials from all over the web.', 'http://navnav.co/', '2019-01-22 02:00:24', '2019-01-22 02:00:24');
INSERT INTO `site` VALUES (47, 6, 'Really Good UX', '948b0f5b62e59ef0a97edf4b9a51c404.png', 'A library of screenshots and examples of really good UX. Brought to you by .', 'https://www.reallygoodux.io/', '2019-01-22 02:01:05', '2019-01-22 02:01:05');
INSERT INTO `site` VALUES (48, 8, 'Iconfinder', 'e3325f68179436ccfc25b9f0ffff5a39.png', '2,100,000+ free and premium vector icons.', 'https://www.iconfinder.com', '2019-01-22 03:01:41', '2019-01-22 03:01:41');
INSERT INTO `site` VALUES (49, 8, 'iconfont', 'e1f63337915f79f8bcad1952adb9f6e1.png', '阿里巴巴矢量图标库。', 'http://www.iconfont.cn/', '2019-01-22 03:02:16', '2019-01-22 03:02:16');
INSERT INTO `site` VALUES (50, 8, 'iconmonstr', 'afd4885651455f12dcac4f214460dd99.png', 'Free simple icons for your next project.', 'https://iconmonstr.com/', '2019-01-22 03:02:46', '2019-01-22 03:02:46');
INSERT INTO `site` VALUES (51, 8, 'FindIcons', '0171a46b0f643752aa90aa314a22a546.png', 'Search through 300,000 free icons.', 'https://findicons.com/', '2019-01-22 03:03:24', '2019-01-22 03:03:24');
INSERT INTO `site` VALUES (52, 8, 'Icon Archive', '40c43a8932f24370cf456789b2ab51db.png', 'Search 590,912 free icons.', 'http://www.iconarchive.com/', '2019-01-22 03:04:28', '2019-01-22 03:04:28');
INSERT INTO `site` VALUES (53, 8, 'IcoMoonApp', 'd19c97ead3760f1b70efa4ee9ad6859c.png', 'Icon Font, SVG, PDF & PNG Generator.', 'https://icomoon.io/app/', '2019-01-22 03:05:49', '2019-01-22 03:05:49');
INSERT INTO `site` VALUES (54, 8, 'easyicon', '34b4382075e047c6d1456f8fe591a1ef.png', 'PNG、ICO、ICNS格式图标搜索、图标下载服务。', 'http://www.easyicon.net/', '2019-01-22 03:06:47', '2019-01-22 03:06:47');
INSERT INTO `site` VALUES (55, 8, 'flaticon', '582cf7361a0b4f444628c68b98e5cfc7.png', '634,000+ Free vector icons in SVG, PSD, PNG, EPS format or as ICON FONT.', 'https://www.flaticon.com/', '2019-01-22 03:07:28', '2019-01-22 03:07:28');
INSERT INTO `site` VALUES (56, 8, 'UICloud', 'f9840e127d500449da1c5c721f3634c3.png', 'The largest user interface design database in the world.', 'http://ui-cloud.com/', '2019-01-22 03:08:10', '2019-01-22 03:08:10');
INSERT INTO `site` VALUES (57, 8, 'Material icons', '32a037ffbdd2f97f6f6e62e56321c519.png', 'Access over 900 material system icons, available in a variety of sizes and densities, and as a web font.', 'https://material.io/icons/', '2019-01-22 03:09:00', '2019-01-22 03:09:00');
INSERT INTO `site` VALUES (58, 8, 'Font Awesome Icon', '88440b8b0d5dc43a3f766670e2d11746.png', 'The complete set of 675 icons in Font Awesome.', 'https://fontawesome.com/', '2019-01-22 03:09:59', '2019-01-22 03:09:59');
INSERT INTO `site` VALUES (59, 8, 'ion icons', '6d0fd0bf35549f6d61037bd86e2ca242.png', 'The premium icon font for Ionic Framework.', 'http://ionicons.com/', '2019-01-22 03:10:37', '2019-01-22 03:10:37');
INSERT INTO `site` VALUES (60, 8, 'Simpleline Icons', 'acf446f1af754f863260cc10dd8d546e.png', 'Simple line Icons pack.', 'http://simplelineicons.com/', '2019-01-22 03:11:11', '2019-01-22 03:11:11');
INSERT INTO `site` VALUES (61, 9, 'Iconsfeed', 'aee21da67d9771c2ebf3f6779afc9649.png', 'iOS icons gallery.', 'http://www.iconsfeed.com/', '2019-01-22 03:12:11', '2019-01-22 03:12:11');
INSERT INTO `site` VALUES (62, 9, 'iOS Icon Gallery', '98c9f52ede06a56532d5d16afda9d570.png', 'Showcasing beautiful icon designs from the iOS App Store.', 'http://iosicongallery.com/', '2019-01-22 03:12:59', '2019-01-22 03:12:59');
INSERT INTO `site` VALUES (63, 9, 'World Vector Logo', 'c8da40d11f726d974293efc40c9acfb5.png', 'Brand logos free to download.', 'https://worldvectorlogo.com/', '2019-01-22 03:14:10', '2019-01-22 03:14:10');
INSERT INTO `site` VALUES (64, 9, 'Instant Logo Search', '907f35950eae72526a314306cc59efa7.png', 'Search & download thousands of logos instantly.', 'http://instantlogosearch.com/', '2019-01-22 03:14:50', '2019-01-22 03:14:50');
INSERT INTO `site` VALUES (65, 10, 'freepik', '6a96564b2d100bad3674db5e56794a97.png', 'More than a million free vectors, PSD, photos and free icons.', 'https://www.freepik.com/', '2019-01-22 03:17:22', '2019-01-22 03:17:22');
INSERT INTO `site` VALUES (66, 10, 'wallhalla', '9354f99621e8530d0f996fe4b96ad2c3.png', 'Find awesome high quality wallpapers for desktop and mobile in one place.', 'https://wallhalla.com/', '2019-01-22 03:17:55', '2019-01-22 03:17:55');
INSERT INTO `site` VALUES (67, 10, '365PSD', '13b10a2f9388e83101d7a35b83ff28bc.png', 'Free PSD & Graphics, Illustrations.', 'https://365psd.com/', '2019-01-22 03:18:22', '2019-01-22 03:18:22');
INSERT INTO `site` VALUES (68, 10, 'Medialoot', 'b117eb768a44d662ded91d1f0a9cb1c2.png', 'Free & Premium Design Resources — Medialoot.', 'https://medialoot.com/', '2019-01-22 03:18:52', '2019-01-22 03:18:52');
INSERT INTO `site` VALUES (69, 10, '千图网', '9a24027c0e9d498efb4ad88a330882f8.png', '专注免费设计素材下载的网站。', 'http://www.58pic.com/', '2019-01-22 03:19:35', '2019-01-22 03:19:35');
INSERT INTO `site` VALUES (70, 10, '千库网', '15ffa7b3a5cab15c7d23d402be12cc4c.png', '免费png图片背景素材下载。', 'http://588ku.com/', '2019-01-22 03:20:14', '2019-01-22 03:20:14');
INSERT INTO `site` VALUES (71, 10, '我图网', 'a887a255bbe7fe994e0479ae988372c7.png', '我图网,提供图片素材及模板下载,专注正版设计作品交易。', 'http://www.ooopic.com/', '2019-01-22 03:20:54', '2019-01-22 03:20:54');
INSERT INTO `site` VALUES (72, 10, '90设计', 'c510c1946d6191a98c6fd3b08ca720ec.png', '电商设计（淘宝美工）千图免费淘宝素材库。', 'http://90sheji.com/', '2019-01-22 03:21:29', '2019-01-22 03:21:29');
INSERT INTO `site` VALUES (73, 10, '昵图网', 'd4fba2a16c7a1692ea21f4f0a8ae7672.png', '原创素材共享平台。', 'http://www.nipic.com/', '2019-01-22 03:22:11', '2019-01-22 03:22:11');
INSERT INTO `site` VALUES (74, 10, '懒人图库', 'a7e5f98173ea111df83da146a86436a1.png', '懒人图库专注于提供网页素材下载。', 'http://www.lanrentuku.com/', '2019-01-22 03:23:20', '2019-01-22 03:23:20');
INSERT INTO `site` VALUES (75, 10, '素材搜索', 'cbca7fabfd7c6d1b117547466bc564ad.png', '设计素材搜索聚合。', 'http://so.ui001.com/', '2019-01-22 03:24:03', '2019-01-22 03:24:03');
INSERT INTO `site` VALUES (76, 10, 'PS饭团网', '7ffad2eac8cbad395d33914344d3aa0a.png', '不一样的设计素材库！让自己的设计与众不同！', 'http://psefan.com/', '2019-01-22 03:24:44', '2019-01-22 03:24:44');
INSERT INTO `site` VALUES (77, 10, '素材中国', 'ced6b2a53069c7d360ba78706244081f.png', '免费素材共享平台。', 'http://www.sccnn.com/', '2019-01-22 03:25:22', '2019-01-22 03:25:22');
INSERT INTO `site` VALUES (78, 11, 'Freebiesbug', '4288052485ced84952e206a4acfb92ad.png', 'Hand-picked resources for web designer and developers, constantly updated.', 'https://freebiesbug.com/', '2019-01-22 03:26:32', '2019-01-22 03:26:32');
INSERT INTO `site` VALUES (79, 11, 'Freebie Supply', '2ae393ad916a108ba20d21a8b907477e.png', 'Free Resources For Designers.', 'https://freebiesupply.com/', '2019-01-22 03:27:09', '2019-01-22 03:27:09');
INSERT INTO `site` VALUES (80, 11, '云瑞', '528f9304b0dab49f5fe2426d4d9d047c.png', '优秀设计资源的分享网站。', 'https://www.yrucd.com/', '2019-01-22 04:20:59', '2019-01-22 04:20:59');
INSERT INTO `site` VALUES (81, 11, 'Designmodo', '2061e0ccebfbb1a94a28d86237589b23.png', 'Web Design Blog and Shop.', 'https://designmodo.com/', '2019-01-22 04:21:38', '2019-01-22 04:21:38');
INSERT INTO `site` VALUES (82, 11, '稀土区', '4f5171443ad0ec6b13b7f96b8cead4bd.png', '优质设计开发资源分享。', 'https://xituqu.com/', '2019-01-22 04:22:15', '2019-01-22 04:22:15');
INSERT INTO `site` VALUES (83, 11, 'ui8', 'b815917aad63f449a96900979a16eb4e.png', 'UI Kits, Wireframe Kits, Templates, Icons and More.', 'https://ui8.net/', '2019-01-22 04:22:57', '2019-01-22 04:22:57');
INSERT INTO `site` VALUES (84, 11, 'uplabs', '42d3d29c62a19e8d4ca6395586d79ee7.png', 'Daily resources for product designers & developers.', 'https://www.uplabs.com/', '2019-01-22 04:23:39', '2019-01-22 04:23:39');
INSERT INTO `site` VALUES (85, 11, 'UIkit.me', '13b4ac8efdc9f92e52e7f271b8034b24.png', '最便捷新鲜的uikit资源下载网站。', 'http://www.uikit.me/', '2019-01-22 04:24:19', '2019-01-22 04:24:19');
INSERT INTO `site` VALUES (86, 11, 'Fribbble', '04c1b51de97ceac330358fa7d1685034.png', 'Free PSD files and other free design resources by Dribbblers.', 'http://www.fribbble.com/', '2019-01-22 04:24:54', '2019-01-22 04:24:54');
INSERT INTO `site` VALUES (87, 11, 'PrincipleRepo', 'e3e93f407dcc94461bea06979e89e4a2.png', 'Free, High Quality Principle Resources.', 'http://principlerepo.com/', '2019-01-22 04:25:30', '2019-01-22 04:25:30');
INSERT INTO `site` VALUES (88, 12, 'Sketch', 'ff6a2f8afaeb91004416c96788f9da95.png', 'The digital design toolkit.', 'https://sketchapp.com/', '2019-01-22 04:26:17', '2019-01-22 04:26:17');
INSERT INTO `site` VALUES (89, 12, 'Sketch Measure', '69bf814d311d932ea13b746ffc1f9f54.png', 'Friendly user interface offers you a more intuitive way of making marks.', 'http://utom.design/measure/', '2019-01-22 04:26:55', '2019-01-22 04:26:55');
INSERT INTO `site` VALUES (90, 12, 'Sketch App Sources', 'ccf82c5a27a285ba63cf3c4ff8964a25.png', 'Free design resources and plugins - Icons, UI Kits, Wireframes, iOS, Android Templates for Sketch', 'https://www.sketchappsources.com/', '2019-01-22 04:27:43', '2019-01-22 04:27:43');
INSERT INTO `site` VALUES (91, 12, 'Sketch.im', '6055276a55b62db423c2b060d3f6b044.png', 'Sketch 相关资源汇聚。', 'http://www.sketch.im/', '2019-01-22 04:28:20', '2019-01-22 04:28:20');
INSERT INTO `site` VALUES (92, 12, 'Sketch Hunt', 'c4bf007a61d761db1f895672a2519cd0.png', 'Sketch Hunt is an independent blog sharing gems in learning, plugins & design tools for fans of Sketch app.', 'http://sketchhunt.com/', '2019-01-22 04:29:44', '2019-01-22 04:29:44');
INSERT INTO `site` VALUES (93, 12, 'Sketch中文网', 'a26a90da0d304cd3502cdb53e85995b9.png', '分享最新的Sketch中文手册。', 'http://www.sketchcn.com/', '2019-01-22 04:30:21', '2019-01-22 04:30:21');
INSERT INTO `site` VALUES (94, 12, 'Awesome Sketch Plugins', '255cf49affef2fbaba8cd15c3e7329b5.png', 'A collection of really useful Sketch plugins.', 'https://awesome-sket.ch/', '2019-01-22 04:31:24', '2019-01-22 04:31:24');
INSERT INTO `site` VALUES (95, 12, 'Sketchcasts', 'fa8c8b179ab48ad61e61a18d1720e019.png', 'Learn Sketch Train your design skills with a weekly video tutorial', 'https://www.sketchcasts.net/', '2019-01-22 04:32:00', '2019-01-22 04:32:00');
INSERT INTO `site` VALUES (96, 13, 'Google Font', '91c604a4ca98b1bb5719e04c80043419.png', 'Making the web more beautiful, fast, and open through great typography.', 'https://fonts.google.com/', '2019-01-22 04:32:42', '2019-01-22 04:32:42');
INSERT INTO `site` VALUES (97, 13, 'Typekit', '7dbc17741d30274995615612dc1d075f.png', 'Quality fonts from the world’s best foundries.', 'https://typekit.com/', '2019-01-22 04:33:14', '2019-01-22 04:33:14');
INSERT INTO `site` VALUES (98, 13, '方正字库', '8ffeec1d3ad96a39dd4ede9794756b87.png', '方正字库官方网站。', 'http://www.foundertype.com/', '2019-01-22 04:37:57', '2019-01-22 04:37:57');
INSERT INTO `site` VALUES (99, 13, '字体传奇网', 'd5fc1ea541fe215ae449a0ae27a09a76.png', '中国首个字体品牌设计师交流网。', 'http://ziticq.com/', '2019-01-22 04:38:51', '2019-01-22 04:38:51');
INSERT INTO `site` VALUES (100, 13, '私藏字体', '125e538efd8b3ea68d3655cb81ccc06f.png', '优质字体免费下载站。', 'http://sicangziti.com/', '2019-01-22 04:40:03', '2019-01-22 04:40:03');
INSERT INTO `site` VALUES (101, 13, 'Fontsquirrel', '94684e5203623eb5540a4b5a0e0b70b0.png', 'FREE fonts for graphic designers.', 'https://www.fontsquirrel.com/', '2019-01-22 04:40:43', '2019-01-22 04:40:43');
INSERT INTO `site` VALUES (102, 13, 'Urban Fonts', '40139afeda032d1a3cd54459d065b31b.png', 'Download Free Fonts and Free Dingbats.', 'https://www.urbanfonts.com/', '2019-01-22 04:41:23', '2019-01-22 04:41:23');
INSERT INTO `site` VALUES (103, 13, 'Lost Type', 'cfe0904ec3e37914be51687a2b15f5cf.png', 'Lost Type is a Collaborative Digital Type Foundry.', 'http://www.losttype.com/', '2019-01-22 04:41:56', '2019-01-22 04:41:56');
INSERT INTO `site` VALUES (104, 13, 'FONTS2U', '19bd844dc123066620d1f6fda7287e48.png', 'Download free fonts for Windows and Macintosh.', 'https://fonts2u.com/', '2019-01-22 04:42:46', '2019-01-22 04:42:46');
INSERT INTO `site` VALUES (105, 13, 'Fontex', '1576a7303fb2fa3839016a599418203b.png', 'Free Fonts to Download + Premium Typefaces.', 'http://www.fontex.org/', '2019-01-22 04:43:17', '2019-01-22 04:43:17');
INSERT INTO `site` VALUES (106, 13, 'FontM', '5ee459b3c52027eb8dcd9c8c6e9266a0.png', 'Free Fonts.', 'http://fontm.com/', '2019-01-22 04:43:49', '2019-01-22 04:43:49');
INSERT INTO `site` VALUES (107, 13, 'My Fonts', '22572b2d9b38ea02f173074d59acf334.png', 'Fonts for Print, Products & Screens.', 'http://www.myfonts.com/', '2019-01-22 04:45:27', '2019-01-22 04:45:27');
INSERT INTO `site` VALUES (108, 13, 'Da Font', 'd0478f80b89bff215437714e62c6d997.png', 'Archive of freely downloadable fonts.', 'https://www.dafont.com/', '2019-01-22 04:46:04', '2019-01-22 04:46:04');
INSERT INTO `site` VALUES (109, 13, 'OnlineWebFonts', 'bccc59c04f6f283a63430700273ffdee.png', 'WEB Free Fonts for Windows and Mac / Font free Download.', 'https://www.onlinewebfonts.com/', '2019-01-22 04:46:36', '2019-01-22 04:46:36');
INSERT INTO `site` VALUES (110, 13, 'Abstract Fonts', 'fbedc66f865056e650a036f042625057.png', 'Abstract Fonts (13,866 free fonts).', 'http://www.abstractfonts.com/', '2019-01-22 04:47:08', '2019-01-22 04:47:08');
INSERT INTO `site` VALUES (111, 14, 'MockupZone', '75e599ff2f061dc25fa272de94045ca9.png', 'Mockup Zone is an online store where you can find free and premium PSD mockup files to show your designs in a professional way.', 'https://mockup.zone/', '2019-01-22 05:07:01', '2019-01-22 05:07:01');
INSERT INTO `site` VALUES (112, 14, 'Dunnnk', '7949d12743b95779412dd8673c324164.png', 'Generate Product Mockups For Free.', 'http://dunnnk.com/', '2019-01-22 05:08:13', '2019-01-22 05:08:13');
INSERT INTO `site` VALUES (113, 14, 'Graphberry', '1216f3642b463e7e9d493e4d00506566.png', 'Free design resources, Mockups, PSD web templates, Icons.', 'http://www.graphberry.com/', '2019-01-22 05:08:51', '2019-01-22 05:08:51');
INSERT INTO `site` VALUES (114, 14, 'Threed', '252114418dc086100cd58d10035a9436.png', 'Generate 3D Mockups right in your Browser', 'http://threed.io/', '2019-01-22 05:09:22', '2019-01-22 05:09:22');
INSERT INTO `site` VALUES (115, 14, 'Mockup World', 'c94ee98e4ada29c0916888820da31744.png', 'The best free Mockups from the Web', 'https://free.lstore.graphics/', '2019-01-22 05:09:54', '2019-01-22 05:09:54');
INSERT INTO `site` VALUES (116, 14, 'Lstore', '965f25d08ae3cd33fab21d764a514967.png', 'Exclusive mindblowing freebies for designers and developers.', 'https://free.lstore.graphics/', '2019-01-22 05:11:00', '2019-01-22 05:11:00');
INSERT INTO `site` VALUES (117, 14, 'pixeden', 'f8b5261bc1d5e5189b9c1216a6de8b3b.png', 'free web resources and graphic design templates.', 'https://www.pixeden.com/', '2019-01-22 05:11:36', '2019-01-22 05:11:36');
INSERT INTO `site` VALUES (118, 14, 'For Graphic TM', '20fceec1b9dd6c1183ad73a90becce7f.png', 'High Quality PSD Mockups for Graphic Designers.', 'http://forgraphictm.com/', '2019-01-22 05:12:28', '2019-01-22 05:12:28');
INSERT INTO `site` VALUES (119, 15, 'Unsplash', '72880b02dbea40fd84472abc05e6d23b.png', 'Beautiful, free photos.', 'https://unsplash.com/', '2019-01-22 05:16:19', '2019-01-22 05:16:19');
INSERT INTO `site` VALUES (120, 15, 'visualhunt', 'b2a1a1e4c043858ac2411f02b9236ff3.png', '100% Free High Quality Photos.', 'https://visualhunt.com/', '2019-01-22 05:16:55', '2019-01-22 05:16:55');
INSERT INTO `site` VALUES (121, 15, 'librestock', '94c5305f78dfadb241f9edcf3d9b870d.png', '65,084 high quality do-what-ever-you-want stock photos.', 'https://librestock.com/', '2019-01-22 05:17:27', '2019-01-22 05:17:34');
INSERT INTO `site` VALUES (122, 15, 'pixabay', '310cb7b52774323c7fdffe67aa0f12aa.png', '可在任何地方使用的免费图片和视频。', 'https://pixabay.com/', '2019-01-22 05:18:10', '2019-01-22 05:18:10');
INSERT INTO `site` VALUES (123, 15, 'SplitShire', '0e9933021af7cc4714e900c247010b30.png', 'Free Stock Photos and Videos for commercial use.', 'https://www.splitshire.com/', '2019-01-22 05:19:07', '2019-01-22 05:19:07');
INSERT INTO `site` VALUES (124, 15, 'StockSnap', 'fabf86558eb3a7c943c124f7f62f3542.png', 'Beautiful free stock photos.', 'https://stocksnap.io/', '2019-01-22 05:19:40', '2019-01-22 05:19:40');
INSERT INTO `site` VALUES (125, 15, 'albumarium', 'de8b7f26a21ea0b781f93a3163341731.png', 'The best place to find & share beautiful images.', 'http://albumarium.com/', '2019-01-22 05:20:11', '2019-01-22 05:20:11');
INSERT INTO `site` VALUES (126, 15, 'myphotopack', '80d85ea59d293bd43731a890f63c5dc9.png', 'A free photo pack just for you. Every month.', 'https://myphotopack.com/', '2019-01-22 05:20:44', '2019-01-22 05:20:44');
INSERT INTO `site` VALUES (127, 15, 'Notaselfie', 'eb5f9a9661e582883c9d3128bb9b4482.png', 'Photos that happen along the way. You can use the images anyway you like. Have fun!', 'http://notaselfie.com/', '2019-01-22 05:21:31', '2019-01-22 05:21:31');
INSERT INTO `site` VALUES (128, 15, 'papers', '3a6396ba24d253502f40432751a11b07.png', 'Wallpapers Every Hour!Hand collected :)', 'http://papers.co/', '2019-01-22 05:22:06', '2019-01-22 05:22:06');
INSERT INTO `site` VALUES (129, 15, 'stokpic', '9dce238279b24893eaa20a99fba802ea.png', 'Free Stock Photos For Commercial Use.', 'http://stokpic.com/', '2019-01-22 05:22:55', '2019-01-22 05:22:55');
INSERT INTO `site` VALUES (130, 15, '55mm', 'dd8adcbc65cc20e8fb6d6335fd57814a.png', 'Use our FREE photos to tell your story!', 'https://55mm.co/visuals', '2019-01-22 05:23:27', '2019-01-22 05:23:27');
INSERT INTO `site` VALUES (131, 15, 'thestocks', '2be533b5b00139b9022f09604f3bd136.png', 'Use our FREE photos to tell your story!', 'http://thestocks.im/', '2019-01-22 05:24:11', '2019-01-22 05:24:11');
INSERT INTO `site` VALUES (132, 15, 'freenaturestock', '85c87259ac26b4f48b084066b9e3ec8e.png', 'Exclusive mindblowing freebies for designers and developers.', 'http://freenaturestock.com/', '2019-01-22 05:27:20', '2019-01-22 05:27:20');
INSERT INTO `site` VALUES (133, 15, 'negativespace', '9b470b26c5e7e6604f3f17d2fe518af7.png', 'Beautiful, High-Resolution Free Stock Photos.', 'https://negativespace.co/', '2019-01-22 05:28:11', '2019-01-22 05:28:11');
INSERT INTO `site` VALUES (134, 15, 'gratisography', '37a9bff7f4d756e7b227ef295aa5ff82.png', 'Free high-resolution pictures you can use on your personal and commercial projects, free of copyright restrictions.', 'https://gratisography.com/', '2019-01-22 05:28:53', '2019-01-22 05:28:53');
INSERT INTO `site` VALUES (135, 15, 'imcreator', '568ae371ba49ce83463d5833af6a8e88.png', 'A curated collection of free web design resources, all for commercial use.', 'http://imcreator.com/free', '2019-03-12 02:21:55', '2019-03-12 02:21:55');
INSERT INTO `site` VALUES (136, 15, 'lifeofpix', '94bf5d51c1367552f337610dbc6aa44b.png', 'Free high resolution photography', 'http://www.lifeofpix.com/', '2019-03-12 02:22:49', '2019-03-12 02:22:49');
INSERT INTO `site` VALUES (137, 15, 'skitterphoto', '23663c43cb7025f3bf36e9733bea6171.png', 'Free Stock Photos for Creative Professionals', 'https://skitterphoto.com/', '2019-03-12 02:23:32', '2019-03-12 02:23:32');
INSERT INTO `site` VALUES (138, 15, 'mmtstock', 'd8d5768d2dc63763480478ae25aa176a.png', 'Free photos for commercial use', 'https://mmtstock.com/', '2019-03-12 02:24:19', '2019-03-12 02:24:19');
INSERT INTO `site` VALUES (139, 15, 'magdeleine', '12ca6edef00d1d897eb28c4a8e2f8915.png', 'HAND-PICKED FREE PHOTOS FOR YOUR INSPIRATION', 'https://magdeleine.co/browse/', '2019-03-12 02:25:17', '2019-03-12 02:25:17');
INSERT INTO `site` VALUES (140, 15, 'jeshoots', 'a016e8d2ae3ee88f0ec136440e92fca8.png', 'New Free Photos & Mockups in to your Inbox!', 'http://jeshoots.com/', '2019-03-12 02:25:49', '2019-03-12 02:25:49');
INSERT INTO `site` VALUES (141, 15, 'hdwallpapers', '74db036ddf1bbfc49a22a5a6dcd392ab.png', 'High Definition Wallpapers & Desktop Backgrounds', 'https://www.hdwallpapers.net', '2019-03-12 02:26:19', '2019-03-12 02:26:19');
INSERT INTO `site` VALUES (142, 15, 'publicdomainarchive', '3c7427a4bab6bb40c12a77014f809a2a.png', 'New 100% Free Stock Photos. Every. Single. Week.', 'http://publicdomainarchive.com/', '2019-03-12 02:26:49', '2019-03-12 02:26:49');
INSERT INTO `site` VALUES (143, 16, 'OfficePLUS', '4773ef0cfcf8c9fd158fc7db0bc2cf0b.png', 'OfficePLUS，微软Office官方在线模板网站！', 'http://www.officeplus.cn/Template/Home.shtml', '2019-03-12 02:27:23', '2019-03-12 02:27:23');
INSERT INTO `site` VALUES (144, 16, '优品PPT', 'b1d803179735ea628d1d914c63c0b9f7.png', '高质量的模版，而且还有PPT图表，PPT背景图等资源', 'http://www.ypppt.com/', '2019-03-12 02:27:52', '2019-03-12 02:27:52');
INSERT INTO `site` VALUES (145, 16, 'PPT+', 'bfabed8750869b0214622d158ec9bddf.png', 'PPT加直播、录制和分享—PPT+语音内容分享平台', 'http://www.pptplus.cn/', '2019-03-12 02:28:22', '2019-03-12 02:28:22');
INSERT INTO `site` VALUES (146, 16, 'PPTMind', '857bb0f6927c2a8c246653cb41136ce7.png', '分享高端ppt模板与keynote模板的数字作品交易平台', 'http://www.pptmind.com/', '2019-03-12 02:28:53', '2019-03-12 02:28:53');
INSERT INTO `site` VALUES (147, 16, 'tretars', '14a77db5ab4af0ba947b1e1707295c5d.png', 'The best free Mockups from the Web', 'http://www.tretars.com/ppt-templates', '2019-03-12 02:29:21', '2019-03-12 02:29:21');
INSERT INTO `site` VALUES (148, 16, '5百丁', '3f735ae4b6e18cd6cff3965661289aac.png', '中国领先的PPT模板共享平台', 'http://ppt.500d.me/', '2019-03-12 02:29:51', '2019-03-12 02:29:51');
INSERT INTO `site` VALUES (149, 18, 'photoshop', 'e7117e80fdb340589bc8969900e2af61.png', 'Photoshop不需要解释', 'https://www.adobe.com/cn/products/photoshop.html', '2019-03-12 02:30:54', '2019-03-12 02:30:54');
INSERT INTO `site` VALUES (150, 18, 'Affinity Designer', '290add1cdb3cdb80e6e30af137d48525.png', '专业创意软件', 'https://affinity.serif.com/', '2019-03-12 02:32:06', '2019-03-12 02:32:06');
INSERT INTO `site` VALUES (151, 18, 'Illustrator', '6882fdb094820bae95054ea1c38a3baf.png', '矢量图形和插图。', 'https://www.adobe.com/cn/products/illustrator/', '2019-03-12 02:32:51', '2019-03-12 02:32:51');
INSERT INTO `site` VALUES (152, 18, 'indesign', '4db54894b6751e253212a690dada0df8.png', '页面设计、布局和出版。', 'http://www.adobe.com/cn/products/indesign.html', '2019-03-12 02:33:19', '2019-03-12 02:33:19');
INSERT INTO `site` VALUES (153, 18, 'cinema-4d', 'ac0344f03fc1e59b4144fef92a12e211.png', 'Cinema 4D is the perfect package for all 3D artists who want to achieve breathtaking results fast and hassle-free.', 'https://www.maxon.net/en/products/cinema-4d/overview/', '2019-03-12 02:34:04', '2019-03-12 02:34:04');
INSERT INTO `site` VALUES (154, 18, '3ds-max', '3aacac5d23583cdc250a970a0e30a9aa.png', '3D modeling, animation, and rendering software', 'https://www.autodesk.com/products/3ds-max/overview', '2019-03-12 02:34:39', '2019-03-12 02:34:39');
INSERT INTO `site` VALUES (155, 18, 'Blender', '5d61addac4350caee364f0a3e850a3f7.png', 'Blender is the free and open source 3D creation suite.', 'https://www.blender.org/', '2019-03-12 02:35:09', '2019-03-12 02:35:09');
INSERT INTO `site` VALUES (156, 19, 'Sketch', '75cc5b5775361d5f0b471b706a115403.png', 'The digital design toolkit', 'https://sketchapp.com/', '2019-03-12 02:35:55', '2019-03-12 02:35:55');
INSERT INTO `site` VALUES (157, 19, 'Adobe XD', '9eda46042e2ad058951fa4e4bb3a9957.png', 'Introducing Adobe XD. Design. Prototype. Experience.', 'http://www.adobe.com/products/xd.html', '2019-03-12 02:36:39', '2019-03-12 02:36:39');
INSERT INTO `site` VALUES (158, 19, 'invisionapp', '7d5620f1b4fd85c4a7ea4733deed8823.png', 'Powerful design prototyping tools', 'https://www.invisionapp.com/', '2019-03-12 02:37:36', '2019-03-12 02:37:36');
INSERT INTO `site` VALUES (159, 19, 'marvelapp', '28ec81158c67d9783afccf8fcd1bbee6.png', 'Simple design, prototyping and collaboration', 'https://marvelapp.com/', '2019-03-12 02:38:14', '2019-03-12 02:38:14');
INSERT INTO `site` VALUES (160, 19, 'Muse CC', '612cf530fe80b5f28dc826c4384087bf.png', '无需利用编码即可进行网站设计。', 'https://creative.adobe.com/zh-cn/products/download/muse', '2019-03-12 02:38:53', '2019-03-12 02:38:53');
INSERT INTO `site` VALUES (161, 19, 'figma', '0623aab0bc72437206deca9d4c55df1c.png', 'Design, prototype, and gather feedback all in one place with Figma.', 'https://www.figma.com/', '2019-03-12 02:39:20', '2019-03-12 02:39:20');
INSERT INTO `site` VALUES (162, 20, 'Adobe After Effects CC', '01306292e590f37b934785ed67288f80.png', '电影般的视觉效果和动态图形。', 'https://www.adobe.com/cn/products/aftereffects/', '2019-03-12 02:40:17', '2019-03-12 02:40:17');
INSERT INTO `site` VALUES (163, 20, 'principle', '485a410f2076ad20856199caa300f548.png', 'Animate Your Ideas, Design Better Apps', 'http://principleformac.com/', '2019-03-12 02:40:48', '2019-03-12 02:40:48');
INSERT INTO `site` VALUES (164, 20, 'flinto', 'f655d57c7d752c429510e0f649d69666.png', 'Flinto is a Mac app used by top designers around the world to create interactive and animated prototypes of their app designs.', 'https://www.flinto.com/', '2019-03-12 02:41:26', '2019-03-12 02:41:26');
INSERT INTO `site` VALUES (165, 20, 'framer', 'e64c3cd0283a3bf6a75c9c4ba821049d.png', 'Design everything from detailed icons to high-fidelity interactions—all in one place.', 'https://framer.com/', '2019-03-12 02:42:05', '2019-03-12 02:42:05');
INSERT INTO `site` VALUES (166, 20, 'ProtoPie', 'bdbb0e0485d816b88c75c9276d273873.png', '高保真交互原型设计', 'http://www.protopie.cn/', '2019-03-12 02:42:41', '2019-03-12 02:42:41');
INSERT INTO `site` VALUES (167, 21, 'khroma', '8b158c18b49f0160100086bfcdbe158d.png', 'Khroma is the fastest way to discover, search, and save color combos you\'ll want to use.', 'http://khroma.co/generator/', '2019-03-12 02:45:06', '2019-03-12 02:45:06');
INSERT INTO `site` VALUES (168, 21, 'uigradients', '9842ff5c221d6411444d3c13660ba097.png', 'Beautiful colored gradients', 'https://uigradients.com', '2019-03-12 02:45:49', '2019-03-12 02:45:49');
INSERT INTO `site` VALUES (169, 21, 'gradients', '64d0ac5dc78b65d83ba500df5b1eab30.png', 'Curated gradients for designers and developers', 'http://gradients.io/', '2019-03-12 02:46:19', '2019-03-12 02:46:19');
INSERT INTO `site` VALUES (170, 21, 'Coolest', '9b77eaad5ef27823b9feb3f765b9d593.png', 'Coolest handpicked Gradient Hues for your next super ⚡ amazing stuff', 'https://webkul.github.io/coolhue/', '2019-03-12 02:46:47', '2019-03-12 02:46:47');
INSERT INTO `site` VALUES (171, 21, 'webgradients', '49bfc25c217107d7209eea098ad0307c.png', 'WebGradients is a free collection of 180 linear gradients that you can use as content backdrops in any part of your website.', 'https://webgradients.com/', '2019-03-12 02:47:17', '2019-03-12 02:47:17');
INSERT INTO `site` VALUES (172, 21, 'grabient', '8ab1a1044ef9bc5c306c60b81d83b0a2.png', '2017 Grabient by unfold', 'https://www.grabient.com/', '2019-03-12 02:48:04', '2019-03-12 02:48:04');
INSERT INTO `site` VALUES (173, 21, 'thedayscolor', '6e63366cb896fa19e204cf6b95691062.png', 'The daily color digest', 'http://www.thedayscolor.com/', '2019-03-12 02:48:34', '2019-03-12 02:48:34');
INSERT INTO `site` VALUES (174, 21, 'flatuicolors', '0b6e14ae22ff962a96ad66de4fc86aff.png', 'Copy Paste Color Pallette from Flat UI Theme', 'http://flatuicolors.com/', '2019-03-12 02:49:25', '2019-03-12 02:49:25');
INSERT INTO `site` VALUES (175, 21, 'coolors', '9176968478c5c42ed20bce8b69f25bf6.png', 'The super fast color schemes generator!', 'https://coolors.co/', '2019-03-12 02:50:11', '2019-03-12 02:50:11');
INSERT INTO `site` VALUES (176, 0, 'colorhunt', '37cf035215ac7e51cc0064c207f298e1.png', 'Beautiful Color Palettes', 'http://www.colorhunt.co/', '2019-03-12 02:50:35', '2019-03-12 02:50:35');
INSERT INTO `site` VALUES (177, 21, 'Adobe Color CC', 'ff4d69bedb642bd132297ed22018369b.png', 'Create color schemes with the color wheel or browse thousands of color combinations from the Color community.', 'https://color.adobe.com/zh/create/color-wheel', '2019-03-12 02:51:03', '2019-03-12 02:51:03');
INSERT INTO `site` VALUES (178, 21, 'flatuicolorpicker', '2faf82318597d846e9522c5f52500031.png', 'Best Flat Colors For UI Design', 'http://www.flatuicolorpicker.com/', '2019-03-12 02:51:35', '2019-03-12 02:51:35');
INSERT INTO `site` VALUES (179, 21, 'trianglify', '88261a86b35e5b015bbe35ab9141bc8f.png', 'Trianglify Generator', 'http://qrohlf.com/trianglify-generator/', '2019-03-12 02:52:12', '2019-03-12 02:52:12');
INSERT INTO `site` VALUES (180, 21, 'klart', 'c51065aaec56c7c65aafd40f4797dba0.png', 'Beautiful colors and designs to your inbox every week', 'https://klart.co/colors/', '2019-03-12 02:52:40', '2019-03-12 02:52:40');
INSERT INTO `site` VALUES (181, 21, 'vanschneider', '4690e9281c23d5fc9df0e2cfbe018edd.png', 'Color Claim was created in 2012 by Tobias van Schneider with the goal to collect & combine unique colors for my future projects.', 'http://www.vanschneider.com/colors', '2019-03-12 02:53:23', '2019-03-12 02:53:23');
INSERT INTO `site` VALUES (182, 22, 'tinypng', '9344c4d9769745c1e63d8f1e7b2f3f25.png', 'Optimize your images with a perfect balance in quality and file size.', 'https://tinypng.com/', '2019-03-12 02:54:39', '2019-03-12 02:54:39');
INSERT INTO `site` VALUES (183, 0, 'goqr', 'c3abf8084a1699d9a2618e76a90d3935.png', 'create QR codes for free (Logo, T-Shirt, vCard, EPS)', 'http://goqr.me/', '2019-03-12 02:55:04', '2019-03-12 02:55:04');
INSERT INTO `site` VALUES (184, 22, 'Android 9 patch', 'dc5b75e3455673384a8f738429789d4b.png', 'Android 9-patch shadow generator fully customizable shadows', 'http://inloop.github.io/shadow4android/', '2019-03-12 02:55:38', '2019-03-12 02:55:38');
INSERT INTO `site` VALUES (185, 22, 'screen sizes', 'd79583290bc400c0e8a2629d0e7f9f63.png', 'Viewport Sizes and Pixel Densities for Popular Devices', 'http://screensiz.es/', '2019-03-12 02:56:06', '2019-03-12 02:56:06');
INSERT INTO `site` VALUES (186, 22, 'svgomg', 'f573dc81e4689cb9ce482f35a6fb82f1.png', 'SVG在线压缩平台', 'https://jakearchibald.github.io/svgomg/', '2019-03-12 02:56:37', '2019-03-12 02:56:37');
INSERT INTO `site` VALUES (187, 22, '稿定抠图', '0a6b1b1ea1d5ca5cb49e8cf95470a3b5.png', '免费在线抠图软件,图片快速换背景-抠白底图', 'https://www.gaoding.com', '2019-03-12 02:56:59', '2019-03-12 02:56:59');
INSERT INTO `site` VALUES (188, 23, 'wappalyzer', '60696fcbba523de88eca68121dee7be7.png', 'Identify technology on websites', 'https://www.wappalyzer.com/', '2019-03-12 02:58:27', '2019-03-12 02:58:27');
INSERT INTO `site` VALUES (189, 23, 'Panda', '35e1bbf29c1116cb1dbb703b52ea2ae9.png', 'A smart news reader built for productivity.', 'http://usepanda.com/', '2019-03-12 02:58:50', '2019-03-12 02:58:50');
INSERT INTO `site` VALUES (190, 23, 'sizzy', '5ca0b65bcc3606640ba1b4aadd25c7df.png', 'A tool for developing responsive websites crazy-fast', 'https://sizzy.co/', '2019-03-12 02:59:17', '2019-03-12 02:59:17');
INSERT INTO `site` VALUES (191, 23, 'csspeeper', '84fedbd61bf8c93726b713bae36a88ae.png', 'Smart CSS viewer tailored for Designers.', 'https://csspeeper.com/', '2019-03-12 02:59:48', '2019-03-12 02:59:48');
INSERT INTO `site` VALUES (192, 23, 'insight', 'fc5a318293079a2674f1d92f3dce7650.png', 'IDE-like code search and navigation, on the cloud', 'http://insight.io/', '2019-03-12 03:00:22', '2019-03-12 03:00:22');
INSERT INTO `site` VALUES (193, 23, 'mustsee', '709ff744a41559fa06b8e8dc199206a3.png', 'Discover the world\'s most beautiful places at every opened tab.', 'http://mustsee.earth/', '2019-03-12 03:00:50', '2019-03-12 03:00:50');
INSERT INTO `site` VALUES (194, 25, 'Design Guidelines', 'cd3c32252bb659437d63d59ca6fec8fb.png', 'Design Guidelines — The way products are built.', 'http://designguidelines.co/', '2019-03-12 03:01:29', '2019-03-12 03:01:29');
INSERT INTO `site` VALUES (195, 25, 'Awesome design systems', '3adfef3862ba2083e737bbb0c9b734c1.png', 'A collection of awesome design systems', 'https://github.com/alexpate/awesome-design-systems', '2019-03-12 03:01:55', '2019-03-12 03:01:55');
INSERT INTO `site` VALUES (196, 25, 'Material Design', '34061dd29655cfb0781381568b26b1d6.png', 'Introduction - Material Design', 'https://material.io/guidelines/', '2019-03-12 03:02:28', '2019-03-12 03:02:28');
INSERT INTO `site` VALUES (197, 25, 'Human Interface Guidelines', '4fa8f4924f62370774915faf0fbc476d.png', 'Human Interface Guidelines iOS', 'https://developer.apple.com/ios/human-interface-guidelines', '2019-03-12 03:02:57', '2019-03-12 03:02:57');
INSERT INTO `site` VALUES (198, 25, 'Photoshop Etiquette', '9d6924419746baef1dcc5c203a321e6d.png', 'PS礼仪-WEB设计指南', 'http://viggoz.com/photoshopetiquette/', '2019-03-12 03:03:33', '2019-03-12 03:03:33');
INSERT INTO `site` VALUES (199, 26, 'Photoshop Lady', '975585f8d4d7efa6646b50dbc860cf5a.png', 'Your Favourite Photoshop Tutorials in One Place', 'http://www.photoshoplady.com/', '2019-03-12 03:04:09', '2019-03-12 03:04:09');
INSERT INTO `site` VALUES (200, 26, 'doyoudo', 'd810229b8e5bc36d3ac498fe4ee5ebee.png', '创意设计软件学习平台', 'http://doyoudo.com/', '2019-03-12 03:04:38', '2019-03-12 03:04:38');
INSERT INTO `site` VALUES (201, 26, '没位道', '1bb8a33edda0aaf2871ba17e6b265cf9.png', 'WEB UI免费视频公开课', 'http://www.c945.com/web-ui-tutorial/', '2019-03-12 03:05:04', '2019-03-12 03:05:04');
INSERT INTO `site` VALUES (202, 26, '慕课网', '806fbd13251a921287ec2a3e815eafc8.png', '程序员的梦工厂（有UI课程）', 'https://www.imooc.com/', '2019-03-12 03:05:29', '2019-03-12 03:05:29');
INSERT INTO `site` VALUES (203, 27, '优设网', '03508090a91cb0380760bb568054f31b.png', '设计师交流学习平台', 'http://www.uisdc.com/', '2019-03-12 03:06:02', '2019-03-12 03:06:02');
INSERT INTO `site` VALUES (204, 27, 'Web Design Ledger', '29345c700e66d707075758c51421b718.png', 'Web Design Blog', 'https://webdesignledger.com', '2019-03-12 03:06:28', '2019-03-12 03:06:28');
INSERT INTO `site` VALUES (205, 27, 'Medium', 'f9d1866ec8568b6a7744936842d773ac.png', 'Read, write and share stories that matter', 'https://medium.com/', '2019-03-12 03:06:57', '2019-03-12 03:06:57');
INSERT INTO `site` VALUES (206, 28, 'UX Coffee', 'c9aae7e06ebabe9549d78c8b0cd0757d.png', '《UX Coffee 设计咖》是一档关于用户体验的播客节目。我们邀请来自硅谷和国内的学者和职人来聊聊「产品设计」、「用户体验」和「个人成长」。', 'http://uxcoffee.co/', '2019-03-12 03:08:40', '2019-03-12 03:08:40');
INSERT INTO `site` VALUES (207, 28, 'Anyway.FM', '7fe5dade254de0528e78ce32736ff9f4.png', '设计杂谈 • UI 设计师 JJ 和 Leon 主播的设计播客', 'https://anyway.fm/', '2019-03-12 03:09:19', '2019-03-12 03:09:19');
INSERT INTO `site` VALUES (208, 28, '异能电台', 'c8e819279d4110794e2588dfb1a4bd64.png', '将全宇宙设计师的故事讲给你听。', 'https://www.yineng.fm', '2019-03-12 03:09:52', '2019-03-12 03:09:52');
INSERT INTO `site` VALUES (233, 35, '新媒体百宝箱', 'de81021e-a822-48e7-b01d-99861f295c5e.png', '新媒体百宝箱', 'https://www.xmtbbx.com', '2019-07-26 16:09:54', NULL);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `num` int(11) NULL DEFAULT NULL COMMENT '排序',
  `pid` int(11) NULL DEFAULT NULL COMMENT '父部门id',
  `pids` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父级ids',
  `simplename` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '简称',
  `fullname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '全称',
  `tips` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提示',
  `version` int(11) NULL DEFAULT NULL COMMENT '版本（乐观锁保留字段）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (24, 1, 0, '[0],', '总公司', '总公司', '', NULL);
INSERT INTO `sys_dept` VALUES (25, 2, 29, '[0],[24],[28],[29],', '开发部', '开发部', '', NULL);
INSERT INTO `sys_dept` VALUES (26, 3, 25, '[0],[24],[28],[29],[25],', '运营部', '运营部', '', NULL);
INSERT INTO `sys_dept` VALUES (27, 4, 24, '[0],[24],', '战略部', '战略部', '', NULL);
INSERT INTO `sys_dept` VALUES (28, NULL, 24, '[0],[24],', '111', '111', '', NULL);
INSERT INTO `sys_dept` VALUES (29, NULL, 28, '[0],[24],[28],', '222', '222', '222', NULL);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `num` int(11) NULL DEFAULT NULL COMMENT '排序',
  `pid` int(11) NULL DEFAULT NULL COMMENT '父级字典',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `tips` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提示',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (50, 0, 0, '性别', NULL, 'sys_sex');
INSERT INTO `sys_dict` VALUES (51, 1, 50, '男', NULL, '1');
INSERT INTO `sys_dict` VALUES (52, 2, 50, '女', NULL, '2');
INSERT INTO `sys_dict` VALUES (53, 0, 0, '状态', NULL, 'sys_state');
INSERT INTO `sys_dict` VALUES (54, 1, 53, '启用', NULL, '1');
INSERT INTO `sys_dict` VALUES (55, 2, 53, '禁用', NULL, '2');
INSERT INTO `sys_dict` VALUES (56, 0, 0, '账号状态', NULL, 'account_state');
INSERT INTO `sys_dict` VALUES (57, 1, 56, '启用', NULL, '1');
INSERT INTO `sys_dict` VALUES (58, 2, 56, '冻结', NULL, '2');
INSERT INTO `sys_dict` VALUES (59, 3, 56, '已删除', NULL, '3');

-- ----------------------------
-- Table structure for sys_expense
-- ----------------------------
DROP TABLE IF EXISTS `sys_expense`;
CREATE TABLE `sys_expense`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `money` decimal(20, 2) NULL DEFAULT NULL COMMENT '报销金额',
  `desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '描述',
  `createtime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `state` int(11) NULL DEFAULT NULL COMMENT '状态: 1.待提交  2:待审核   3.审核通过 4:驳回',
  `userid` int(11) NULL DEFAULT NULL COMMENT '用户id',
  `processId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '流程定义id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '报销表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_login_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`  (
  `id` int(65) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `logname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志名称',
  `userid` int(65) NULL DEFAULT NULL COMMENT '管理员id',
  `createtime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `succeed` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否执行成功',
  `message` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '具体消息',
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录ip',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 346 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '登录记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单编号',
  `pcode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单父编号',
  `pcodes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前菜单的所有父菜单编号',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url地址',
  `num` int(65) NULL DEFAULT NULL COMMENT '菜单排序号',
  `levels` int(65) NULL DEFAULT NULL COMMENT '菜单层级',
  `ismenu` int(11) NULL DEFAULT NULL COMMENT '是否是菜单（1：是  0：不是）',
  `tips` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `status` int(65) NULL DEFAULT NULL COMMENT '菜单状态 :  1:启用   0:不启用',
  `isopen` int(11) NULL DEFAULT NULL COMMENT '是否打开:    1:打开   0:不打开',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 174 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (105, 'system', '0', '[0],', '系统管理', 'fa-user', '#', 8, 1, 1, NULL, 1, 1);
INSERT INTO `sys_menu` VALUES (106, 'mgr', 'system', '[0],[system],', '用户管理', '', '/mgr', 1, 2, 1, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (107, 'mgr_add', 'mgr', '[0],[system],[mgr],', '添加用户', NULL, '/mgr/add', 1, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (108, 'mgr_edit', 'mgr', '[0],[system],[mgr],', '修改用户', NULL, '/mgr/edit', 2, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (109, 'mgr_delete', 'mgr', '[0],[system],[mgr],', '删除用户', NULL, '/mgr/delete', 3, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (110, 'mgr_reset', 'mgr', '[0],[system],[mgr],', '重置密码', NULL, '/mgr/reset', 4, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (111, 'mgr_freeze', 'mgr', '[0],[system],[mgr],', '冻结用户', NULL, '/mgr/freeze', 5, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (112, 'mgr_unfreeze', 'mgr', '[0],[system],[mgr],', '解除冻结用户', NULL, '/mgr/unfreeze', 6, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (113, 'mgr_setRole', 'mgr', '[0],[system],[mgr],', '分配角色', NULL, '/mgr/setRole', 7, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (114, 'role', 'system', '[0],[system],', '角色管理', NULL, '/role', 2, 2, 1, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (115, 'role_add', 'role', '[0],[system],[role],', '添加角色', NULL, '/role/add', 1, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (116, 'role_edit', 'role', '[0],[system],[role],', '修改角色', NULL, '/role/edit', 2, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (117, 'role_remove', 'role', '[0],[system],[role],', '删除角色', NULL, '/role/remove', 3, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (118, 'role_setAuthority', 'role', '[0],[system],[role],', '配置权限', NULL, '/role/setAuthority', 4, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (119, 'menu', 'system', '[0],[system],', '菜单管理', NULL, '/menu', 4, 2, 1, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (120, 'menu_add', 'menu', '[0],[system],[menu],', '添加菜单', NULL, '/menu/add', 1, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (121, 'menu_edit', 'menu', '[0],[system],[menu],', '修改菜单', NULL, '/menu/edit', 2, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (122, 'menu_remove', 'menu', '[0],[system],[menu],', '删除菜单', NULL, '/menu/remove', 3, 3, 0, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (128, 'log', 'system', '[0],[system],', '业务日志', NULL, '/log', 6, 2, 1, NULL, 1, 0);
INSERT INTO `sys_menu` VALUES (130, 'druid', 'system', '[0],[system],', '监控管理', NULL, '/druid', 7, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (131, 'dept', 'system', '[0],[system],', '部门管理', NULL, '/dept', 3, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (132, 'dict', 'system', '[0],[system],', '字典管理', NULL, '/dict', 4, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (133, 'loginLog', 'system', '[0],[system],', '登录日志', NULL, '/loginLog', 6, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (134, 'log_clean', 'log', '[0],[system],[log],', '清空日志', NULL, '/log/delLog', 3, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (135, 'dept_add', 'dept', '[0],[system],[dept],', '添加部门', NULL, '/dept/add', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (136, 'dept_update', 'dept', '[0],[system],[dept],', '修改部门', NULL, '/dept/update', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (137, 'dept_delete', 'dept', '[0],[system],[dept],', '删除部门', NULL, '/dept/delete', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (138, 'dict_add', 'dict', '[0],[system],[dict],', '添加字典', NULL, '/dict/add', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (139, 'dict_update', 'dict', '[0],[system],[dict],', '修改字典', NULL, '/dict/update', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (140, 'dict_delete', 'dict', '[0],[system],[dict],', '删除字典', NULL, '/dict/delete', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (141, 'notice', 'system', '[0],[system],', '通知管理', NULL, '/notice', 9, 2, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (142, 'notice_add', 'notice', '[0],[system],[notice],', '添加通知', NULL, '/notice/add', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (143, 'notice_update', 'notice', '[0],[system],[notice],', '修改通知', NULL, '/notice/update', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (144, 'notice_delete', 'notice', '[0],[system],[notice],', '删除通知', NULL, '/notice/delete', 3, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (145, 'hello', '0', '[0],', '通知', 'fa-rocket', '/notice/hello', 3, 1, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (148, 'code', '0', '[0],', '代码生成', 'fa-code', '/code', 7, 1, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (149, 'api_mgr', '0', '[0],', '接口文档', 'fa-leaf', '/swagger-ui.html', 6, 1, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (150, 'to_menu_edit', 'menu', '[0],[system],[menu],', '菜单编辑跳转', '', '/menu/menu_edit', 4, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (151, 'menu_list', 'menu', '[0],[system],[menu],', '菜单列表', '', '/menu/list', 5, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (152, 'to_dept_update', 'dept', '[0],[system],[dept],', '修改部门跳转', '', '/dept/dept_update', 4, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (153, 'dept_list', 'dept', '[0],[system],[dept],', '部门列表', '', '/dept/list', 5, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (154, 'dept_detail', 'dept', '[0],[system],[dept],', '部门详情', '', '/dept/detail', 6, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (155, 'to_dict_edit', 'dict', '[0],[system],[dict],', '修改菜单跳转', '', '/dict/dict_edit', 4, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (156, 'dict_list', 'dict', '[0],[system],[dict],', '字典列表', '', '/dict/list', 5, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (157, 'dict_detail', 'dict', '[0],[system],[dict],', '字典详情', '', '/dict/detail', 6, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (158, 'log_list', 'log', '[0],[system],[log],', '日志列表', '', '/log/list', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (159, 'log_detail', 'log', '[0],[system],[log],', '日志详情', '', '/log/detail', 3, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (160, 'del_login_log', 'loginLog', '[0],[system],[loginLog],', '清空登录日志', '', '/loginLog/delLoginLog', 1, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (161, 'login_log_list', 'loginLog', '[0],[system],[loginLog],', '登录日志列表', '', '/loginLog/list', 2, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (162, 'to_role_edit', 'role', '[0],[system],[role],', '修改角色跳转', '', '/role/role_edit', 5, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (163, 'to_role_assign', 'role', '[0],[system],[role],', '角色分配跳转', '', '/role/role_assign', 6, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (164, 'role_list', 'role', '[0],[system],[role],', '角色列表', '', '/role/list', 7, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (165, 'to_assign_role', 'mgr', '[0],[system],[mgr],', '分配角色跳转', '', '/mgr/role_assign', 8, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (166, 'to_user_edit', 'mgr', '[0],[system],[mgr],', '编辑用户跳转', '', '/mgr/user_edit', 9, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (167, 'mgr_list', 'mgr', '[0],[system],[mgr],', '用户列表', '', '/mgr/list', 10, 3, 0, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (171, 'category', '0', '[0],', '分类管理', 'fa-archive', '/category', 0, 1, 1, NULL, 1, NULL);
INSERT INTO `sys_menu` VALUES (173, 'site', '0', '[0],', '网站管理', 'fa-internet-explorer', '/site', 1, 1, 1, NULL, 1, NULL);

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `type` int(11) NULL DEFAULT NULL COMMENT '类型',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '内容',
  `createtime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `creater` int(11) NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (6, '世界', 10, '欢迎使用Guns管理系统', '2017-01-11 08:53:20', 1);
INSERT INTO `sys_notice` VALUES (8, '你好', NULL, '你好', '2017-05-10 19:28:57', 1);
INSERT INTO `sys_notice` VALUES (10, '你好', NULL, '这是一个开源的网址导航网站项目，您可以拿来制作自己的网址导航，也可以做与导航无关的网站。如果对本项目有任何建议都可以发起 issue。', '2019-07-26 18:23:52', 1);

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `id` int(65) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `logtype` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志类型',
  `logname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志名称',
  `userid` int(65) NULL DEFAULT NULL COMMENT '用户id',
  `classname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '类名称',
  `method` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '方法名称',
  `createtime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `succeed` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '是否成功',
  `message` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 608 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '操作日志' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_relation`;
CREATE TABLE `sys_relation`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menuid` bigint(11) NULL DEFAULT NULL COMMENT '菜单id',
  `roleid` int(11) NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3905 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_relation
-- ----------------------------
INSERT INTO `sys_relation` VALUES (3377, 105, 5);
INSERT INTO `sys_relation` VALUES (3378, 106, 5);
INSERT INTO `sys_relation` VALUES (3379, 107, 5);
INSERT INTO `sys_relation` VALUES (3380, 108, 5);
INSERT INTO `sys_relation` VALUES (3381, 109, 5);
INSERT INTO `sys_relation` VALUES (3382, 110, 5);
INSERT INTO `sys_relation` VALUES (3383, 111, 5);
INSERT INTO `sys_relation` VALUES (3384, 112, 5);
INSERT INTO `sys_relation` VALUES (3385, 113, 5);
INSERT INTO `sys_relation` VALUES (3386, 114, 5);
INSERT INTO `sys_relation` VALUES (3387, 115, 5);
INSERT INTO `sys_relation` VALUES (3388, 116, 5);
INSERT INTO `sys_relation` VALUES (3389, 117, 5);
INSERT INTO `sys_relation` VALUES (3390, 118, 5);
INSERT INTO `sys_relation` VALUES (3391, 119, 5);
INSERT INTO `sys_relation` VALUES (3392, 120, 5);
INSERT INTO `sys_relation` VALUES (3393, 121, 5);
INSERT INTO `sys_relation` VALUES (3394, 122, 5);
INSERT INTO `sys_relation` VALUES (3395, 150, 5);
INSERT INTO `sys_relation` VALUES (3396, 151, 5);
INSERT INTO `sys_relation` VALUES (3848, 105, 1);
INSERT INTO `sys_relation` VALUES (3849, 106, 1);
INSERT INTO `sys_relation` VALUES (3850, 107, 1);
INSERT INTO `sys_relation` VALUES (3851, 108, 1);
INSERT INTO `sys_relation` VALUES (3852, 109, 1);
INSERT INTO `sys_relation` VALUES (3853, 110, 1);
INSERT INTO `sys_relation` VALUES (3854, 111, 1);
INSERT INTO `sys_relation` VALUES (3855, 112, 1);
INSERT INTO `sys_relation` VALUES (3856, 113, 1);
INSERT INTO `sys_relation` VALUES (3857, 165, 1);
INSERT INTO `sys_relation` VALUES (3858, 166, 1);
INSERT INTO `sys_relation` VALUES (3859, 167, 1);
INSERT INTO `sys_relation` VALUES (3860, 114, 1);
INSERT INTO `sys_relation` VALUES (3861, 115, 1);
INSERT INTO `sys_relation` VALUES (3862, 116, 1);
INSERT INTO `sys_relation` VALUES (3863, 117, 1);
INSERT INTO `sys_relation` VALUES (3864, 118, 1);
INSERT INTO `sys_relation` VALUES (3865, 162, 1);
INSERT INTO `sys_relation` VALUES (3866, 163, 1);
INSERT INTO `sys_relation` VALUES (3867, 164, 1);
INSERT INTO `sys_relation` VALUES (3868, 119, 1);
INSERT INTO `sys_relation` VALUES (3869, 120, 1);
INSERT INTO `sys_relation` VALUES (3870, 121, 1);
INSERT INTO `sys_relation` VALUES (3871, 122, 1);
INSERT INTO `sys_relation` VALUES (3872, 150, 1);
INSERT INTO `sys_relation` VALUES (3873, 151, 1);
INSERT INTO `sys_relation` VALUES (3874, 128, 1);
INSERT INTO `sys_relation` VALUES (3875, 134, 1);
INSERT INTO `sys_relation` VALUES (3876, 158, 1);
INSERT INTO `sys_relation` VALUES (3877, 159, 1);
INSERT INTO `sys_relation` VALUES (3878, 130, 1);
INSERT INTO `sys_relation` VALUES (3879, 131, 1);
INSERT INTO `sys_relation` VALUES (3880, 135, 1);
INSERT INTO `sys_relation` VALUES (3881, 136, 1);
INSERT INTO `sys_relation` VALUES (3882, 137, 1);
INSERT INTO `sys_relation` VALUES (3883, 152, 1);
INSERT INTO `sys_relation` VALUES (3884, 153, 1);
INSERT INTO `sys_relation` VALUES (3885, 154, 1);
INSERT INTO `sys_relation` VALUES (3886, 132, 1);
INSERT INTO `sys_relation` VALUES (3887, 138, 1);
INSERT INTO `sys_relation` VALUES (3888, 139, 1);
INSERT INTO `sys_relation` VALUES (3889, 140, 1);
INSERT INTO `sys_relation` VALUES (3890, 155, 1);
INSERT INTO `sys_relation` VALUES (3891, 156, 1);
INSERT INTO `sys_relation` VALUES (3892, 157, 1);
INSERT INTO `sys_relation` VALUES (3893, 133, 1);
INSERT INTO `sys_relation` VALUES (3894, 160, 1);
INSERT INTO `sys_relation` VALUES (3895, 161, 1);
INSERT INTO `sys_relation` VALUES (3896, 141, 1);
INSERT INTO `sys_relation` VALUES (3897, 142, 1);
INSERT INTO `sys_relation` VALUES (3898, 143, 1);
INSERT INTO `sys_relation` VALUES (3899, 144, 1);
INSERT INTO `sys_relation` VALUES (3900, 145, 1);
INSERT INTO `sys_relation` VALUES (3901, 148, 1);
INSERT INTO `sys_relation` VALUES (3902, 149, 1);
INSERT INTO `sys_relation` VALUES (3903, 171, 1);
INSERT INTO `sys_relation` VALUES (3904, 173, 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `num` int(11) NULL DEFAULT NULL COMMENT '序号',
  `pid` int(11) NULL DEFAULT NULL COMMENT '父角色id',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `deptid` int(11) NULL DEFAULT NULL COMMENT '部门名称',
  `tips` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提示',
  `version` int(11) NULL DEFAULT NULL COMMENT '保留字段(暂时没用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 1, 0, '超级管理员', 24, 'administrator', 1);
INSERT INTO `sys_role` VALUES (5, 2, 1, '临时', 26, 'temp', NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像',
  `account` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `salt` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'md5密码盐',
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名字',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '生日',
  `sex` int(11) NULL DEFAULT NULL COMMENT '性别（1：男 2：女）',
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `roleid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色id',
  `deptid` int(11) NULL DEFAULT NULL COMMENT '部门id',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态(1：启用  2：冻结  3：删除）',
  `createtime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `version` int(11) NULL DEFAULT NULL COMMENT '保留字段',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, '54660837-c66b-4293-a916-fbb8ca02407a.jpg', 'admin', 'ecfadcde9305f8891bcfe5a1e28c253e', '8pgby', '管理员', '2017-05-05 00:00:00', 1, 'jsnjfz@gmail.com', '18200000000', '1', 27, 1, '2016-01-29 08:49:53', 25);
INSERT INTO `sys_user` VALUES (44, NULL, 'test', '45abb7879f6a8268f1ef600e6038ac73', 'ssts3', 'test', '2017-05-01 00:00:00', 1, 'abc@123.com', '', '5', 26, 3, '2017-05-16 20:33:37', NULL);
INSERT INTO `sys_user` VALUES (45, NULL, 'boss', '71887a5ad666a18f709e1d4e693d5a35', '1f7bf', '老板', '2017-12-04 00:00:00', 1, '', '', '1', 24, 1, '2017-12-04 22:24:02', NULL);
INSERT INTO `sys_user` VALUES (46, NULL, 'manager', 'b53cac62e7175637d4beb3b16b2f7915', 'j3cs9', '经理', '2017-12-04 00:00:00', 1, '', '', '1', 24, 1, '2017-12-04 22:24:24', NULL);

SET FOREIGN_KEY_CHECKS = 1;
