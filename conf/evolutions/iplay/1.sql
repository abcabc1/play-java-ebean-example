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
create table address (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
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
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  name                          varchar(32) DEFAULT '' COMMENT '名称' not null,
  constraint uq_goods_code unique (code),
  constraint pk_goods primary key (id)
);

create table merchandise (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  name                          varchar(32) DEFAULT '' COMMENT '商品名称' not null,
  constraint uq_merchandise_code unique (code),
  constraint pk_merchandise primary key (id)
);

create table merchandise_pack (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  merchandise_id                bigint COMMENT 'ID',
  pack_id                       bigint COMMENT 'ID',
  constraint pk_merchandise_pack primary key (id)
);

create table merchandise_tag (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  merchandise_id                bigint COMMENT 'ID',
  tag                           varchar(32) COMMENT '配置编码',
  constraint pk_merchandise_tag primary key (id)
);

create table operator (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  pass                          varchar(16) DEFAULT '111111' COMMENT '密码' not null,
  type                          TINYINT UNSIGNED DEFAULT 1  COMMENT '类别[0 admin,1 user,2 store]' not null,
  constraint uq_operator_code unique (code),
  constraint pk_operator primary key (id)
);

create table order_detail (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_detail_code unique (code),
  constraint pk_order_detail primary key (id)
);

create table order_detail_item (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_detail_item_code unique (code),
  constraint pk_order_detail_item primary key (id)
);

create table order_main (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_main_code unique (code),
  constraint pk_order_main primary key (id)
);

create table order_package (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_package_code unique (code),
  constraint pk_order_package primary key (id)
);

create table order_package_item (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_package_item_code unique (code),
  constraint pk_order_package_item primary key (id)
);

create table pack (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  name                          varchar(32) DEFAULT '' COMMENT '套餐名称' not null,
  constraint uq_pack_code unique (code),
  constraint pk_pack primary key (id)
);

create table store (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  operator_id                   bigint COMMENT 'ID',
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  name                          varchar(16) DEFAULT '' COMMENT '名称' not null,
  area                          varchar(6) DEFAULT '' COMMENT '区域编码' not null,
  constraint uq_store_operator_id unique (operator_id),
  constraint uq_store_code unique (code),
  constraint pk_store primary key (id)
);

create table store_address (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  store_id                      bigint COMMENT 'ID',
  address_id                    bigint COMMENT 'ID',
  constraint pk_store_address primary key (id)
);

create table store_license (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  store_id                      bigint COMMENT 'ID',
  license                       varchar(32) COMMENT '配置编码',
  constraint pk_store_license primary key (id)
);

create table store_tag (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  store_id                      bigint COMMENT 'ID',
  tag                           varchar(32) COMMENT '配置编码',
  constraint pk_store_tag primary key (id)
);

create table user (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  operator_id                   bigint COMMENT 'ID',
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  name                          varchar(32) DEFAULT '' COMMENT '名称' not null,
  area                          varchar(6) DEFAULT '' COMMENT '区域编码' not null,
  level                         varchar(32) COMMENT '配置编码',
  type                          varchar(32) COMMENT '配置编码',
  constraint uq_user_operator_id unique (operator_id),
  constraint uq_user_code unique (code),
  constraint pk_user primary key (id)
);

create table user_address (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  user_id                       bigint COMMENT 'ID',
  address_id                    bigint COMMENT 'ID',
  constraint pk_user_address primary key (id)
);

create table user_license (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  user_id                       bigint COMMENT 'ID',
  license_code                  varchar(32) COMMENT '配置编码',
  constraint pk_user_license primary key (id)
);

create table user_tag (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  user_id                       bigint COMMENT 'ID',
  tag                           varchar(32) COMMENT '配置编码',
  constraint pk_user_tag primary key (id)
);

create index ix_config_parent on config (parent);
alter table config add constraint fk_config_parent foreign key (parent) references config (code) on delete restrict on update restrict;

create index ix_merchandise_pack_merchandise_id on merchandise_pack (merchandise_id);
alter table merchandise_pack add constraint fk_merchandise_pack_merchandise_id foreign key (merchandise_id) references merchandise (id) on delete restrict on update restrict;

create index ix_merchandise_pack_pack_id on merchandise_pack (pack_id);
alter table merchandise_pack add constraint fk_merchandise_pack_pack_id foreign key (pack_id) references pack (id) on delete restrict on update restrict;

create index ix_merchandise_tag_merchandise_id on merchandise_tag (merchandise_id);
alter table merchandise_tag add constraint fk_merchandise_tag_merchandise_id foreign key (merchandise_id) references merchandise (id) on delete restrict on update restrict;

create index ix_merchandise_tag_tag on merchandise_tag (tag);
alter table merchandise_tag add constraint fk_merchandise_tag_tag foreign key (tag) references config (code) on delete restrict on update restrict;

alter table store add constraint fk_store_operator_id foreign key (operator_id) references operator (id) on delete restrict on update restrict;

create index ix_store_address_store_id on store_address (store_id);
alter table store_address add constraint fk_store_address_store_id foreign key (store_id) references store (id) on delete restrict on update restrict;

create index ix_store_address_address_id on store_address (address_id);
alter table store_address add constraint fk_store_address_address_id foreign key (address_id) references address (id) on delete restrict on update restrict;

create index ix_store_license_store_id on store_license (store_id);
alter table store_license add constraint fk_store_license_store_id foreign key (store_id) references store (id) on delete restrict on update restrict;

create index ix_store_license_license on store_license (license);
alter table store_license add constraint fk_store_license_license foreign key (license) references config (code) on delete restrict on update restrict;

create index ix_store_tag_store_id on store_tag (store_id);
alter table store_tag add constraint fk_store_tag_store_id foreign key (store_id) references store (id) on delete restrict on update restrict;

create index ix_store_tag_tag on store_tag (tag);
alter table store_tag add constraint fk_store_tag_tag foreign key (tag) references config (code) on delete restrict on update restrict;

alter table user add constraint fk_user_operator_id foreign key (operator_id) references operator (id) on delete restrict on update restrict;

create index ix_user_level on user (level);
alter table user add constraint fk_user_level foreign key (level) references config (code) on delete restrict on update restrict;

create index ix_user_type on user (type);
alter table user add constraint fk_user_type foreign key (type) references config (code) on delete restrict on update restrict;

create index ix_user_address_user_id on user_address (user_id);
alter table user_address add constraint fk_user_address_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_user_address_address_id on user_address (address_id);
alter table user_address add constraint fk_user_address_address_id foreign key (address_id) references address (id) on delete restrict on update restrict;

create index ix_user_license_user_id on user_license (user_id);
alter table user_license add constraint fk_user_license_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_user_license_license_code on user_license (license_code);
alter table user_license add constraint fk_user_license_license_code foreign key (license_code) references config (code) on delete restrict on update restrict;

create index ix_user_tag_user_id on user_tag (user_id);
alter table user_tag add constraint fk_user_tag_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_user_tag_tag on user_tag (tag);
alter table user_tag add constraint fk_user_tag_tag foreign key (tag) references config (code) on delete restrict on update restrict;


# --- !Downs

alter table config drop foreign key fk_config_parent;
drop index ix_config_parent on config;

alter table merchandise_pack drop foreign key fk_merchandise_pack_merchandise_id;
drop index ix_merchandise_pack_merchandise_id on merchandise_pack;

alter table merchandise_pack drop foreign key fk_merchandise_pack_pack_id;
drop index ix_merchandise_pack_pack_id on merchandise_pack;

alter table merchandise_tag drop foreign key fk_merchandise_tag_merchandise_id;
drop index ix_merchandise_tag_merchandise_id on merchandise_tag;

alter table merchandise_tag drop foreign key fk_merchandise_tag_tag;
drop index ix_merchandise_tag_tag on merchandise_tag;

alter table store drop foreign key fk_store_operator_id;

alter table store_address drop foreign key fk_store_address_store_id;
drop index ix_store_address_store_id on store_address;

alter table store_address drop foreign key fk_store_address_address_id;
drop index ix_store_address_address_id on store_address;

alter table store_license drop foreign key fk_store_license_store_id;
drop index ix_store_license_store_id on store_license;

alter table store_license drop foreign key fk_store_license_license;
drop index ix_store_license_license on store_license;

alter table store_tag drop foreign key fk_store_tag_store_id;
drop index ix_store_tag_store_id on store_tag;

alter table store_tag drop foreign key fk_store_tag_tag;
drop index ix_store_tag_tag on store_tag;

alter table user drop foreign key fk_user_operator_id;

alter table user drop foreign key fk_user_level;
drop index ix_user_level on user;

alter table user drop foreign key fk_user_type;
drop index ix_user_type on user;

alter table user_address drop foreign key fk_user_address_user_id;
drop index ix_user_address_user_id on user_address;

alter table user_address drop foreign key fk_user_address_address_id;
drop index ix_user_address_address_id on user_address;

alter table user_license drop foreign key fk_user_license_user_id;
drop index ix_user_license_user_id on user_license;

alter table user_license drop foreign key fk_user_license_license_code;
drop index ix_user_license_license_code on user_license;

alter table user_tag drop foreign key fk_user_tag_user_id;
drop index ix_user_tag_user_id on user_tag;

alter table user_tag drop foreign key fk_user_tag_tag;
drop index ix_user_tag_tag on user_tag;

drop table if exists address;

drop table if exists config;

drop table if exists goods;

drop table if exists merchandise;

drop table if exists merchandise_pack;

drop table if exists merchandise_tag;

drop table if exists operator;

drop table if exists order_detail;

drop table if exists order_detail_item;

drop table if exists order_main;

drop table if exists order_package;

drop table if exists order_package_item;

drop table if exists pack;

drop table if exists store;

drop table if exists store_address;

drop table if exists store_license;

drop table if exists store_tag;

drop table if exists user;

drop table if exists user_address;

drop table if exists user_license;

drop table if exists user_tag;

