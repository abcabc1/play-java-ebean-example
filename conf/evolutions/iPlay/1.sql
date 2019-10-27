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
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_activity_code unique (code),
  constraint pk_activity primary key (id)
);

create table activity_merchandise (
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0/1' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  merchandise_id                bigint COMMENT 'ID',
  activity_id                   bigint COMMENT 'ID'
);

create table activity_mj (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  activity_id                   bigint COMMENT 'ID',
  constraint uq_activity_mj_code unique (code),
  constraint uq_activity_mj_activity_id unique (activity_id),
  constraint pk_activity_mj primary key (id)
);

create table activity_ms (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  activity_id                   bigint COMMENT 'ID',
  constraint uq_activity_ms_code unique (code),
  constraint uq_activity_ms_activity_id unique (activity_id),
  constraint pk_activity_ms primary key (id)
);

create table activity_mz (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  activity_id                   bigint COMMENT 'ID',
  constraint uq_activity_mz_code unique (code),
  constraint uq_activity_mz_activity_id unique (activity_id),
  constraint pk_activity_mz primary key (id)
);

create table activity_q (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  activity_id                   bigint COMMENT 'ID',
  constraint uq_activity_q_code unique (code),
  constraint uq_activity_q_activity_id unique (activity_id),
  constraint pk_activity_q primary key (id)
);

create table activity_user (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_activity_user_code unique (code),
  constraint pk_activity_user primary key (id)
);

create table address (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_address_code unique (code),
  constraint pk_address primary key (id)
);

create table config (
  code                          varchar(32) COMMENT '配置编码' not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0/1' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  name                          varchar(32) DEFAULT '' COMMENT '配置名称' not null,
  category                      varchar(32) DEFAULT '' COMMENT '配置类型' not null,
  category_name                 varchar(32) DEFAULT '' COMMENT '配置类型名称' not null,
  config_order                  TINYINT UNSIGNED DEFAULT 0 COMMENT '配置次序' not null,
  parent                        varchar(32) COMMENT '配置编码',
  constraint pk_config primary key (code)
);

create table goods (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_goods_code unique (code),
  constraint pk_goods primary key (id)
);

create table merchandise (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  name                          varchar(32) DEFAULT '' COMMENT '商品名称' not null,
  constraint uq_merchandise_code unique (code),
  constraint pk_merchandise primary key (id)
);

create table merchandise_pack (
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0/1' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  merchandise_id                bigint COMMENT 'ID',
  pack_id                       bigint COMMENT 'ID'
);

create table operator (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  pass                          varchar(16) DEFAULT '111111' COMMENT '密码' not null,
  type                          TINYINT UNSIGNED DEFAULT 1  COMMENT '类别(0 admin/1 user/2 store)' not null,
  constraint uq_operator_code unique (code),
  constraint pk_operator primary key (id)
);

create table order_detail (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_detail_code unique (code),
  constraint pk_order_detail primary key (id)
);

create table order_detail_item (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_detail_item_code unique (code),
  constraint pk_order_detail_item primary key (id)
);

create table order_main (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_main_code unique (code),
  constraint pk_order_main primary key (id)
);

create table order_package (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_package_code unique (code),
  constraint pk_order_package primary key (id)
);

create table order_package_item (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_package_item_code unique (code),
  constraint pk_order_package_item primary key (id)
);

create table pack (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  name                          varchar(32) DEFAULT '' COMMENT '套餐名称' not null,
  constraint uq_pack_code unique (code),
  constraint pk_pack primary key (id)
);

create table store (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  operator_id                   bigint COMMENT 'ID',
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  area_code                     varchar(6) DEFAULT '' COMMENT '区域编码' not null,
  constraint uq_store_operator_id unique (operator_id),
  constraint uq_store_code unique (code),
  constraint pk_store primary key (id)
);

create table user (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效(0 无效/1 有效)' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  operator_id                   bigint COMMENT 'ID',
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  area_code                     varchar(6) DEFAULT '' COMMENT '区域编码' not null,
  level                         varchar(32) COMMENT '配置编码',
  constraint uq_user_operator_id unique (operator_id),
  constraint uq_user_code unique (code),
  constraint pk_user primary key (id)
);

create index ix_activity_merchandise_merchandise_id on activity_merchandise (merchandise_id);
alter table activity_merchandise add constraint fk_activity_merchandise_merchandise_id foreign key (merchandise_id) references merchandise (id) on delete restrict on update restrict;

create index ix_activity_merchandise_activity_id on activity_merchandise (activity_id);
alter table activity_merchandise add constraint fk_activity_merchandise_activity_id foreign key (activity_id) references activity (id) on delete restrict on update restrict;

alter table activity_mj add constraint fk_activity_mj_activity_id foreign key (activity_id) references activity (id) on delete restrict on update restrict;

alter table activity_ms add constraint fk_activity_ms_activity_id foreign key (activity_id) references activity (id) on delete restrict on update restrict;

alter table activity_mz add constraint fk_activity_mz_activity_id foreign key (activity_id) references activity (id) on delete restrict on update restrict;

alter table activity_q add constraint fk_activity_q_activity_id foreign key (activity_id) references activity (id) on delete restrict on update restrict;

create index ix_config_parent on config (parent);
alter table config add constraint fk_config_parent foreign key (parent) references config (code) on delete restrict on update restrict;

create index ix_merchandise_pack_merchandise_id on merchandise_pack (merchandise_id);
alter table merchandise_pack add constraint fk_merchandise_pack_merchandise_id foreign key (merchandise_id) references merchandise (id) on delete restrict on update restrict;

create index ix_merchandise_pack_pack_id on merchandise_pack (pack_id);
alter table merchandise_pack add constraint fk_merchandise_pack_pack_id foreign key (pack_id) references pack (id) on delete restrict on update restrict;

alter table store add constraint fk_store_operator_id foreign key (operator_id) references operator (id) on delete restrict on update restrict;

alter table user add constraint fk_user_operator_id foreign key (operator_id) references operator (id) on delete restrict on update restrict;

create index ix_user_level on user (level);
alter table user add constraint fk_user_level foreign key (level) references config (code) on delete restrict on update restrict;


# --- !Downs

alter table activity_merchandise drop foreign key fk_activity_merchandise_merchandise_id;
drop index ix_activity_merchandise_merchandise_id on activity_merchandise;

alter table activity_merchandise drop foreign key fk_activity_merchandise_activity_id;
drop index ix_activity_merchandise_activity_id on activity_merchandise;

alter table activity_mj drop foreign key fk_activity_mj_activity_id;

alter table activity_ms drop foreign key fk_activity_ms_activity_id;

alter table activity_mz drop foreign key fk_activity_mz_activity_id;

alter table activity_q drop foreign key fk_activity_q_activity_id;

alter table config drop foreign key fk_config_parent;
drop index ix_config_parent on config;

alter table merchandise_pack drop foreign key fk_merchandise_pack_merchandise_id;
drop index ix_merchandise_pack_merchandise_id on merchandise_pack;

alter table merchandise_pack drop foreign key fk_merchandise_pack_pack_id;
drop index ix_merchandise_pack_pack_id on merchandise_pack;

alter table store drop foreign key fk_store_operator_id;

alter table user drop foreign key fk_user_operator_id;

alter table user drop foreign key fk_user_level;
drop index ix_user_level on user;

drop table if exists activity;

drop table if exists activity_merchandise;

drop table if exists activity_mj;

drop table if exists activity_ms;

drop table if exists activity_mz;

drop table if exists activity_q;

drop table if exists activity_user;

drop table if exists address;

drop table if exists config;

drop table if exists goods;

drop table if exists merchandise;

drop table if exists merchandise_pack;

drop table if exists operator;

drop table if exists order_detail;

drop table if exists order_detail_item;

drop table if exists order_main;

drop table if exists order_package;

drop table if exists order_package_item;

drop table if exists pack;

drop table if exists store;

drop table if exists user;

