# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

-- init script create procs
-- Inital script to create stored procedures etc for mysql platform
DROP PROCEDURE IF EXISTS usp_ebean_drop_foreign_keys;

delimiter $$
--
-- PROCEDURE: usp_ebean_drop_foreign_keys TABLE, COLUMN
-- deletes all constraints and foreign keys referring to TABLE.COLUMN
--
CREATE PROCEDURE usp_ebean_drop_foreign_keys(IN p_table_name VARCHAR(255), IN p_column_name VARCHAR(255))
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE c_fk_name CHAR(255);
  DECLARE curs CURSOR FOR SELECT CONSTRAINT_NAME from information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = DATABASE() and TABLE_NAME = p_table_name and COLUMN_NAME = p_column_name
      AND REFERENCED_TABLE_NAME IS NOT NULL;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  OPEN curs;

  read_loop: LOOP
    FETCH curs INTO c_fk_name;
    IF done THEN
      LEAVE read_loop;
    END IF;
    SET @sql = CONCAT('ALTER TABLE ', p_table_name, ' DROP FOREIGN KEY ', c_fk_name);
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
  END LOOP;

  CLOSE curs;
END
$$

DROP PROCEDURE IF EXISTS usp_ebean_drop_column;

delimiter $$
--
-- PROCEDURE: usp_ebean_drop_column TABLE, COLUMN
-- deletes the column and ensures that all indices and constraints are dropped first
--
CREATE PROCEDURE usp_ebean_drop_column(IN p_table_name VARCHAR(255), IN p_column_name VARCHAR(255))
BEGIN
  CALL usp_ebean_drop_foreign_keys(p_table_name, p_column_name);
  SET @sql = CONCAT('ALTER TABLE ', p_table_name, ' DROP COLUMN ', p_column_name);
  PREPARE stmt FROM @sql;
  EXECUTE stmt;
END
$$
create table activity (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_activity_code unique (code),
  constraint pk_activity primary key (id)
);

create table activity_mj (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_activity_mj_code unique (code),
  constraint pk_activity_mj primary key (id)
);

create table activity_ms (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_activity_ms_code unique (code),
  constraint pk_activity_ms primary key (id)
);

create table activity_mz (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_activity_mz_code unique (code),
  constraint pk_activity_mz primary key (id)
);

create table activity_q (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_activity_q_code unique (code),
  constraint pk_activity_q primary key (id)
);

create table address (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_address_code unique (code),
  constraint pk_address primary key (id)
);

create table config (
  config_code                   varchar(32) COMMENT '配置编码' not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0/1' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  config_name                   varchar(32) DEFAULT '' COMMENT '配置名称' not null,
  config_category               varchar(32) DEFAULT '' COMMENT '配置类型' not null,
  config_category_name          varchar(32) DEFAULT '' COMMENT '配置类型名称' not null,
  config_order                  TINYINT UNSIGNED DEFAULT 0 COMMENT '配置次序' not null,
  parent                        varchar(32) COMMENT '配置编码',
  constraint uq_config_config_code_config_category unique (config_code,config_category),
  constraint pk_config primary key (config_code)
);

create table goods (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_goods_code unique (code),
  constraint pk_goods primary key (id)
);

create table merchandise (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_merchandise_code unique (code),
  constraint pk_merchandise primary key (id)
);

create table merchandise_group (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_merchandise_group_code unique (code),
  constraint pk_merchandise_group primary key (id)
);

create table operator (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_operator_code unique (code),
  constraint pk_operator primary key (id)
);

create table order_detail (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_detail_code unique (code),
  constraint pk_order_detail primary key (id)
);

create table order_detail_item (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_detail_item_code unique (code),
  constraint pk_order_detail_item primary key (id)
);

create table order_main (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_main_code unique (code),
  constraint pk_order_main primary key (id)
);

create table order_package (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_package_code unique (code),
  constraint pk_order_package primary key (id)
);

create table order_package_item (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_package_item_code unique (code),
  constraint pk_order_package_item primary key (id)
);

create table store (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_store_code unique (code),
  constraint pk_store primary key (id)
);

create table user (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_user_code unique (code),
  constraint pk_user primary key (id)
);

create index ix_config_parent on config (parent);
alter table config add constraint fk_config_parent foreign key (parent) references config (config_code) on delete restrict on update restrict;


# --- !Downs

alter table config drop foreign key fk_config_parent;
drop index ix_config_parent on config;

drop table if exists activity;

drop table if exists activity_mj;

drop table if exists activity_ms;

drop table if exists activity_mz;

drop table if exists activity_q;

drop table if exists address;

drop table if exists config;

drop table if exists goods;

drop table if exists merchandise;

drop table if exists merchandise_group;

drop table if exists operator;

drop table if exists order_detail;

drop table if exists order_detail_item;

drop table if exists order_main;

drop table if exists order_package;

drop table if exists order_package_item;

drop table if exists store;

drop table if exists user;

