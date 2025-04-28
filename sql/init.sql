CREATE TABLE `t_user` (
  `USER_ID` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(10) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码',
  `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `status` char(1) NOT NULL DEFAULT '1' COMMENT '状态（0锁定 1有效）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `ssex` char(1) NOT NULL COMMENT '性别（0男 1女 2保密）',
  `description` varchar(100) DEFAULT NULL COMMENT '个人描述',
  `avatar` varchar(255) DEFAULT 'default.jpg' COMMENT '头像',
  `images` varchar(255) DEFAULT NULL COMMENT '人脸图片',
  PRIMARY KEY (`USER_ID`),
  UNIQUE KEY `idx_username` (`username`),
  KEY `idx_dept_id` (`dept_id`),
  KEY `idx_email` (`email`),
  KEY `idx_mobile` (`mobile`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE t_dept (
    DEPT_ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '部门ID',
    parent_id BIGINT COMMENT '父部门ID',
    dept_name VARCHAR(20) NOT NULL COMMENT '部门名称',
    order_num DOUBLE COMMENT '排序号',
    create_time DATETIME COMMENT '创建时间',
    modify_time DATETIME COMMENT '修改时间',
    create_time_from VARCHAR(255) COMMENT '创建时间范围起始（临时字段）',
    create_time_to VARCHAR(255) COMMENT '创建时间范围结束（临时字段）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门信息表';

CREATE TABLE t_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';


CREATE TABLE t_role (
    ROLE_ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    role_name VARCHAR(10) NOT NULL COMMENT '角色名称',
    remark VARCHAR(50) COMMENT '角色描述',
    create_time DATETIME COMMENT '创建时间',
    modify_time DATETIME COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';

CREATE TABLE t_role_menu (
    role_id BIGINT NOT NULL COMMENT '角色ID',
    menu_id BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (role_id, menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

CREATE TABLE t_menu (
    MENU_ID BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '菜单ID',
    parent_id BIGINT COMMENT '父菜单ID',
    menu_name VARCHAR(10) NOT NULL COMMENT '菜单名称',
    path VARCHAR(50) COMMENT '路由地址',
    component VARCHAR(100) COMMENT '对应Vue组件',
    perms VARCHAR(50) COMMENT '权限标识',
    icon VARCHAR(50) COMMENT '图标',
    type CHAR(1) NOT NULL COMMENT '类型(0:菜单,1:按钮)',
    order_num DOUBLE COMMENT '排序号',
    create_time DATETIME COMMENT '创建时间',
    modify_time DATETIME COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单信息表';

CREATE TABLE t_user_config (
    USER_ID BIGINT PRIMARY KEY COMMENT '用户ID',
    theme VARCHAR(20) DEFAULT 'light' COMMENT '系统主题(dark:暗色风格,light:明亮风格)',
    layout VARCHAR(20) DEFAULT 'head' COMMENT '系统布局(side:侧边栏,head:顶部栏)',
    multi_page VARCHAR(2) DEFAULT '0' COMMENT '页面风格(1:多标签页,0:单页)',
    fix_siderbar VARCHAR(2) DEFAULT '1' COMMENT '固定侧边栏(1:固定,0:不固定)',
    fix_header VARCHAR(2) DEFAULT '1' COMMENT '固定顶栏(1:固定,0:不固定)',
    color VARCHAR(50) DEFAULT 'rgb(66, 185, 131)' COMMENT '主题颜色(RGB值)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户配置表';
