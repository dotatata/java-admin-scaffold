/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50723
 Source Host           : localhost:3306
 Source Schema         : wms

 Target Server Type    : MySQL
 Target Server Version : 50723
 File Encoding         : 65001

 Date: 28/02/2024 01:35:03
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限名称',
  `permission_key` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '权限key',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限描述',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `delete_date` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_permission_key`(`permission_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of permissions
-- ----------------------------
INSERT INTO `permissions` VALUES (1, '添加用户', 'user:create', '添加用户', '2024-02-22 10:04:17', '2024-02-22 10:04:17', NULL);
INSERT INTO `permissions` VALUES (2, '修改用户', 'user:update', '修改用户', '2024-02-22 10:04:42', '2024-02-22 10:04:42', NULL);
INSERT INTO `permissions` VALUES (3, '删除用户', 'user:delete', '删除用户', '2024-02-22 10:04:57', '2024-02-22 10:04:57', NULL);
INSERT INTO `permissions` VALUES (4, '查询用户', 'user:read', '查询用户', '2024-02-22 10:05:28', '2024-02-22 10:05:28', NULL);
INSERT INTO `permissions` VALUES (5, '添加角色', 'role:create', '添加角色', '2024-02-22 11:09:05', '2024-02-22 11:09:05', NULL);
INSERT INTO `permissions` VALUES (6, '修改角色', 'role:update', '修改角色', '2024-02-22 11:09:05', '2024-02-22 11:09:05', NULL);
INSERT INTO `permissions` VALUES (7, '删除角色', 'role:delete', '删除角色', '2024-02-22 11:09:05', '2024-02-22 11:09:05', NULL);
INSERT INTO `permissions` VALUES (8, '查询角色', 'role:read', '查询角色', '2024-02-22 11:09:05', '2024-02-22 11:09:05', NULL);
INSERT INTO `permissions` VALUES (9, '添加权限', 'permission:create', '添加权限', '2024-02-22 18:07:10', NULL, NULL);
INSERT INTO `permissions` VALUES (10, '修改权限', 'permission:update', '修改权限', '2024-02-22 18:07:51', NULL, NULL);
INSERT INTO `permissions` VALUES (11, '删除权限', 'permission:delete', '删除权限', '2024-02-22 18:08:16', NULL, NULL);
INSERT INTO `permissions` VALUES (12, '查询权限', 'permission:read', '查询权限', '2024-02-22 18:08:48', NULL, NULL);
INSERT INTO `permissions` VALUES (13, 'test11', 'test:test11', 'test111222', '2024-02-22 18:10:19', '2024-02-22 18:29:10', NULL);

-- ----------------------------
-- Table structure for role_permissions
-- ----------------------------
DROP TABLE IF EXISTS `role_permissions`;
CREATE TABLE `role_permissions`  (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `permission_id` int(11) NOT NULL COMMENT '权限ID',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `delete_date` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`role_id`, `permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_permissions
-- ----------------------------
INSERT INTO `role_permissions` VALUES (2, 1, '2024-02-22 15:32:17', NULL, NULL);
INSERT INTO `role_permissions` VALUES (2, 2, '2024-02-22 15:32:17', NULL, NULL);
INSERT INTO `role_permissions` VALUES (2, 3, '2024-02-22 15:32:17', NULL, NULL);
INSERT INTO `role_permissions` VALUES (2, 4, '2024-02-22 15:32:17', NULL, NULL);
INSERT INTO `role_permissions` VALUES (2, 5, '2024-02-22 15:32:17', NULL, NULL);
INSERT INTO `role_permissions` VALUES (2, 6, '2024-02-22 15:32:17', NULL, NULL);
INSERT INTO `role_permissions` VALUES (2, 7, '2024-02-22 15:32:17', NULL, NULL);
INSERT INTO `role_permissions` VALUES (2, 8, '2024-02-22 15:32:17', NULL, NULL);
INSERT INTO `role_permissions` VALUES (8, 1, '2024-02-22 14:43:20', NULL, NULL);
INSERT INTO `role_permissions` VALUES (8, 2, '2024-02-22 14:43:20', NULL, NULL);
INSERT INTO `role_permissions` VALUES (8, 3, '2024-02-22 14:43:20', NULL, NULL);
INSERT INTO `role_permissions` VALUES (8, 4, '2024-02-22 14:43:20', NULL, NULL);
INSERT INTO `role_permissions` VALUES (8, 5, '2024-02-22 14:43:20', NULL, NULL);
INSERT INTO `role_permissions` VALUES (8, 6, '2024-02-22 14:43:20', NULL, NULL);
INSERT INTO `role_permissions` VALUES (8, 7, '2024-02-22 14:43:20', NULL, NULL);
INSERT INTO `role_permissions` VALUES (8, 8, '2024-02-22 14:43:20', NULL, NULL);

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_key` char(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色键值',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '角色名称',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `delete_date` timestamp(0) NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_role_key`(`role_key`) USING BTREE COMMENT 'role_key不能重复'
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of roles
-- ----------------------------
INSERT INTO `roles` VALUES (1, 'super-admin', '超级管理员', '超级管理员', '2024-02-11 15:31:54', '2024-02-21 10:22:48', NULL);
INSERT INTO `roles` VALUES (2, 'admin', '管理员', '管理员', '2024-02-11 15:32:07', '2024-02-16 22:53:08', NULL);
INSERT INTO `roles` VALUES (3, 'COO', '运营总监', '负责平台及业务运营', '2024-02-11 15:33:11', '2024-02-16 23:20:42', NULL);
INSERT INTO `roles` VALUES (4, 'CTO', '技术总监', '负责平台技术规划及业务技术支持', '2024-02-11 15:33:33', '2024-02-16 23:20:38', NULL);
INSERT INTO `roles` VALUES (5, 'operator', '运营人员', '平台及线下业务运营实施', '2024-02-11 15:33:41', '2024-02-16 23:21:15', NULL);
INSERT INTO `roles` VALUES (6, 'tech', '开发人员', '负责技术支持及实施', '2024-02-11 15:33:50', '2024-02-16 23:21:23', NULL);
INSERT INTO `roles` VALUES (7, 'up', '主播', '完成CRM及营销直播间的直播营销活动', '2024-02-11 15:34:14', '2024-02-16 23:21:51', NULL);
INSERT INTO `roles` VALUES (8, 'storer', '仓库管理', '仓库管理', '2024-02-22 14:43:20', NULL, NULL);
INSERT INTO `roles` VALUES (9, 'inspector', '分检员', '货品的分拣和检查管理', '2024-02-22 15:48:13', '2024-02-22 15:50:09', NULL);
INSERT INTO `roles` VALUES (10, 'assistant', '配货员', '负责订单的配货管理', '2024-02-22 15:48:58', '2024-02-22 15:52:38', NULL);

-- ----------------------------
-- Table structure for test
-- ----------------------------
DROP TABLE IF EXISTS `test`;
CREATE TABLE `test`  (
  `user_id` int(10) UNSIGNED NOT NULL COMMENT '用户ID',
  `role_id` int(10) UNSIGNED NOT NULL COMMENT '角色ID',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `delete_date` timestamp(0) NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户-角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of test
-- ----------------------------
INSERT INTO `test` VALUES (24, 4, '2024-02-12 02:26:48', NULL, NULL);
INSERT INTO `test` VALUES (24, 6, '2024-02-12 02:26:48', NULL, NULL);

-- ----------------------------
-- Table structure for user_roles
-- ----------------------------
DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE `user_roles`  (
  `user_id` int(10) UNSIGNED NOT NULL COMMENT '用户ID',
  `role_id` int(10) UNSIGNED NOT NULL COMMENT '角色ID',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `delete_date` timestamp(0) NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户-角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_roles
-- ----------------------------
INSERT INTO `user_roles` VALUES (1, 1, '2024-02-11 15:34:02', NULL, NULL);
INSERT INTO `user_roles` VALUES (1, 2, '2024-02-22 08:31:53', NULL, NULL);
INSERT INTO `user_roles` VALUES (1, 4, '2024-02-16 23:15:06', NULL, NULL);
INSERT INTO `user_roles` VALUES (2, 4, '2024-02-11 15:36:10', NULL, NULL);
INSERT INTO `user_roles` VALUES (2, 6, '2024-02-11 15:35:12', NULL, NULL);
INSERT INTO `user_roles` VALUES (4, 7, '2024-02-11 15:35:40', NULL, NULL);
INSERT INTO `user_roles` VALUES (4, 26, '2024-02-12 02:30:40', NULL, NULL);
INSERT INTO `user_roles` VALUES (6, 26, '2024-02-12 02:30:40', NULL, NULL);
INSERT INTO `user_roles` VALUES (7, 7, '2024-02-22 00:29:08', NULL, NULL);
INSERT INTO `user_roles` VALUES (10, 6, '2024-02-22 00:29:17', NULL, NULL);
INSERT INTO `user_roles` VALUES (11, 4, '2024-02-21 16:30:12', NULL, NULL);
INSERT INTO `user_roles` VALUES (11, 6, '2024-02-21 16:30:12', NULL, NULL);
INSERT INTO `user_roles` VALUES (11, 7, '2024-02-21 16:30:12', NULL, NULL);
INSERT INTO `user_roles` VALUES (35, 2, '2024-02-21 12:06:10', NULL, NULL);
INSERT INTO `user_roles` VALUES (35, 7, '2024-02-21 12:06:10', NULL, NULL);
INSERT INTO `user_roles` VALUES (38, 7, '2024-02-21 12:15:12', NULL, NULL);

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `title` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '岗位',
  `phone` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '电话',
  `salt` char(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '盐',
  `pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `delete_date` timestamp(0) NULL DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `phone_idx`(`phone`) USING BTREE COMMENT '电话索引'
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, '张三', '超级管理员', '13888888888', '9effaa', 'ooxxooxx', '2024-02-10 16:18:56', '2024-02-16 22:34:11', NULL);
INSERT INTO `users` VALUES (2, '李四', '开发者', '13777777777', '9effaa', 'ooxx', '2024-02-10 16:23:26', NULL, NULL);
INSERT INTO `users` VALUES (3, '王五', '商品运营', '13666666666', '9effaa', 'ooxx', '2024-02-10 17:51:06', '2024-02-10 17:54:04', NULL);
INSERT INTO `users` VALUES (4, '李佳琪', '主播', '123123', '9effaa', '33333', '2024-02-10 17:54:36', '2024-02-11 01:12:36', NULL);
INSERT INTO `users` VALUES (5, '王五', '商品运营', '11112', '9effaa', 'ooxx', '2024-02-10 17:56:28', '2024-02-11 20:59:59', NULL);
INSERT INTO `users` VALUES (6, '张三', '商品运营', '12312333', '9effaa', 'ooxx', '2024-02-11 13:15:49', NULL, '2024-02-11 21:15:07');
INSERT INTO `users` VALUES (7, '张三', '商品运营', '665656', '9effaa', 'ooxx', '2024-02-11 13:39:40', NULL, NULL);
INSERT INTO `users` VALUES (9, '张三', '商品运营', '3232323', '9effaa', 'ooxx', '2024-02-12 00:52:05', '2024-02-12 02:18:24', '2024-02-12 02:18:24');
INSERT INTO `users` VALUES (10, '张三', '商品运营', '334343434', '9effaa', 'ooxx', '2024-02-12 00:59:52', NULL, NULL);
INSERT INTO `users` VALUES (11, 'Linus', '开发人员', '132328323', '9effaa', '123123', '2024-02-12 01:33:31', NULL, NULL);
INSERT INTO `users` VALUES (13, 'Linus', '开发人员', '444321', '9effaa', '123123', '2024-02-12 01:37:33', '2024-02-21 17:00:15', '2024-02-21 17:00:15');
INSERT INTO `users` VALUES (14, 'Linus', '开发人员', '342342', '9effaa', '123123', '2024-02-12 01:38:56', '2024-02-21 17:00:20', '2024-02-21 17:00:20');
INSERT INTO `users` VALUES (15, 'Linus', '开发人员', '3434344433', '9effaa', '123123', '2024-02-12 01:48:02', '2024-02-21 17:00:10', '2024-02-21 17:00:10');
INSERT INTO `users` VALUES (16, 'Linus', '开发人员', '14141414', '9effaa', '123123', '2024-02-12 02:04:58', '2024-02-21 17:00:24', '2024-02-21 17:00:24');
INSERT INTO `users` VALUES (18, 'Linus', '开发人员', '3123123123', '9effaa', '123123', '2024-02-12 02:11:10', '2024-02-12 02:35:48', '2024-02-12 02:35:48');
INSERT INTO `users` VALUES (20, 'Linus', '开发人员', '1234342', '9effaa', '123123', '2024-02-12 02:12:51', '2024-02-21 17:00:06', '2024-02-21 17:00:06');
INSERT INTO `users` VALUES (21, 'Linus', '开发人员', '124', '9effaa', '123123', '2024-02-12 02:13:43', '2024-02-21 17:00:02', '2024-02-21 17:00:02');
INSERT INTO `users` VALUES (23, 'Linus', '开发人员', '125', '9effaa', '123123', '2024-02-12 02:15:20', '2024-02-21 16:58:33', '2024-02-21 16:58:33');
INSERT INTO `users` VALUES (24, 'Linus', '开发人员', '126', '9effaa', '123123', '2024-02-12 02:26:48', '2024-02-21 16:58:26', '2024-02-21 16:58:26');
INSERT INTO `users` VALUES (25, 'Linus', '开发人员', '127', '9effaa', '123123', '2024-02-12 02:29:41', '2024-02-21 16:56:21', '2024-02-21 16:56:21');
INSERT INTO `users` VALUES (26, 'Linus', '开发人员', '128', '9effaa', '123123', '2024-02-12 02:30:40', '2024-02-21 16:56:13', '2024-02-21 16:56:13');
INSERT INTO `users` VALUES (28, 'Linus', '开发人员', '129', '9effaa', '123123', '2024-02-12 02:31:53', '2024-02-21 16:55:12', '2024-02-21 16:55:12');
INSERT INTO `users` VALUES (30, '张三', '商品运营', '130', '9effaa', 'ooxx', '2024-02-12 23:58:35', NULL, NULL);
INSERT INTO `users` VALUES (31, '张三', '商品运营', '131', '9effaa', 'ooxx', '2024-02-13 00:19:45', NULL, NULL);
INSERT INTO `users` VALUES (32, '张三', '商品运营', '132', '9effaa', 'ooxx', '2024-02-13 00:20:18', NULL, NULL);
INSERT INTO `users` VALUES (33, '张三', '商品运营', '133', '9effaa', 'ooxx', '2024-02-13 01:09:39', NULL, NULL);
INSERT INTO `users` VALUES (34, '张三', '商品运营', '134', '9effaa', 'ooxx', '2024-02-13 01:10:14', '2024-02-21 16:43:51', '2024-02-21 16:43:51');
INSERT INTO `users` VALUES (35, '老嘎', '主播', '13999999999', '9effaa', 'ooxxooxx', '2024-02-21 12:06:10', NULL, NULL);
INSERT INTO `users` VALUES (37, '老嘎', '主播', '13111111111', '9effaa', 'ooxxooxx', '2024-02-21 12:12:00', '2024-02-21 17:02:02', '2024-02-21 17:02:02');
INSERT INTO `users` VALUES (38, '罗永浩', '主播', '13222222222', '9effaa', 'ooxxooxx', '2024-02-21 12:15:12', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
