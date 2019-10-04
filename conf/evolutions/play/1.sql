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
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  activity_code                 varchar(16) DEFAULT '' COMMENT '编码' not null,
  activity_name                 varchar(16) DEFAULT '' COMMENT '名称' not null,
  activity_level                tinyint DEFAULT 1 COMMENT '级别' not null,
  activity_type                 tinyint DEFAULT 1 COMMENT '类别 1 平台/2 站点/3 店铺' not null,
  activity_status               tinyint DEFAULT 1 COMMENT '生效状态 0 失效/1 生效' not null,
  activity_run_status           tinyint DEFAULT 1 COMMENT '运行状态 0 未运行/1 已启动/2 暂停中/ 3 已结束' not null,
  constraint uq_activity_activity_code unique (activity_code),
  constraint pk_activity primary key (id)
);

create table address (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  address_content               varchar(255) DEFAULT '' COMMENT '地址' not null,
  province_code                 varchar(255) DEFAULT '' COMMENT '省/直辖市' not null,
  province_name                 varchar(255) DEFAULT '' COMMENT '省/直辖市名称' not null,
  city_code                     varchar(255) DEFAULT '' COMMENT '市' not null,
  city_name                     varchar(255) DEFAULT '' COMMENT '市名称' not null,
  district_code                 varchar(255) DEFAULT '' COMMENT '区' not null,
  district_name                 varchar(255) DEFAULT '' COMMENT '区名称' not null,
  customer_id                   bigint COMMENT 'ID',
  constraint pk_address primary key (id)
);

create table config (
  code                          varchar(32) COMMENT '编码' not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0/1' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  config_name                   varchar(64) DEFAULT '' COMMENT '配置名称' not null,
  constraint pk_config primary key (code)
);

create table config_detail (
  code                          varchar(32) COMMENT '编码' not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0/1' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  config_detail_name            varchar(64) DEFAULT '' COMMENT '配置明细名称' not null,
  config_detail_order           int unsigned DEFAULT 1 COMMENT '节点排序' not null,
  config_code                   varchar(32) COMMENT '编码',
  parent_code                   varchar(32) COMMENT '编码',
  constraint pk_config_detail primary key (code)
);

create table customer (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  customer_code                 varchar(16) DEFAULT '' COMMENT '编码' not null,
  customer_name                 varchar(16) DEFAULT '' COMMENT '名称' not null,
  operator_id                   bigint COMMENT 'ID' not null,
  store_id                      bigint COMMENT 'ID',
  customer_type                 varchar(32) COMMENT '编码',
  constraint uq_customer_customer_code unique (customer_code),
  constraint uq_customer_operator_id unique (operator_id),
  constraint pk_customer primary key (id)
);

create table endpoint (
  path_pattern                  varchar(256) COMMENT '编码' not null,
  controller_method             varchar(32) DEFAULT '' COMMENT '请求方式' not null,
  http_method                   varchar(32) DEFAULT '' COMMENT '' not null,
  constraint pk_endpoint primary key (path_pattern)
);

create table goods (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  goods_code                    varchar(16) DEFAULT '' COMMENT '编码' not null,
  goods_name                    varchar(32) DEFAULT '' COMMENT '名称' not null,
  business_code                 varchar(32) DEFAULT '' COMMENT '经营简码' not null,
  constraint uq_goods_goods_code unique (goods_code),
  constraint pk_goods primary key (id)
);

create table menu (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  model_code                    varchar(16) DEFAULT '' COMMENT '节点编码' not null,
  model_name                    varchar(32) DEFAULT '' COMMENT '节点名称' not null,
  model_order                   int(10) unsigned DEFAULT 1 COMMENT '节点排序' not null,
  model_order_seq               varchar(255) DEFAULT '' COMMENT '节点排序链' not null,
  model_code_seq                varchar(255) DEFAULT '' COMMENT '节点编码链' not null,
  model_level                   int(10) unsigned DEFAULT 1 COMMENT '节点级别' not null,
  parent_code                   bigint COMMENT 'ID',
  constraint uq_menu_model_code unique (model_code),
  constraint pk_menu primary key (id)
);

create table merchandise (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  merchandise_code              varchar(16) DEFAULT '' COMMENT '编码' not null,
  merchandise_name              varchar(32) DEFAULT '' COMMENT '名称' not null,
  merchandise_price             decimal(11,5) DEFAULT '0.00000' COMMENT '价格' not null,
  merchandise_storage           decimal(11,2) DEFAULT '0.00000' COMMENT '库存' not null,
  business_code                 varchar(32) DEFAULT '' COMMENT '经营简码' not null,
  goods_id                      bigint COMMENT 'ID',
  store_id                      bigint COMMENT 'ID',
  constraint uq_merchandise_merchandise_code unique (merchandise_code),
  constraint pk_merchandise primary key (id)
);

create table operator (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  operator_code                 varchar(16) DEFAULT '' COMMENT '编码' not null,
  operator_pass                 varchar(16) DEFAULT '111111' COMMENT '密码' not null,
  operator_name                 varchar(32) DEFAULT '' COMMENT '名称' not null,
  operator_gender               varchar(32) COMMENT '编码',
  constraint uq_operator_operator_code unique (operator_code),
  constraint pk_operator primary key (id)
);

create table order (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  order_code                    varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_order_code unique (order_code),
  constraint pk_order primary key (id)
);

create table order_item (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_item_code unique (code),
  constraint pk_order_item primary key (id)
);

create table order_package (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_package_code unique (code),
  constraint pk_order_package primary key (id)
);

create table order_package_detail (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  constraint uq_order_package_detail_code unique (code),
  constraint pk_order_package_detail primary key (id)
);

create table org (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  model_code                    varchar(16) DEFAULT '' COMMENT '节点编码' not null,
  model_name                    varchar(32) DEFAULT '' COMMENT '节点名称' not null,
  model_order                   int(10) unsigned DEFAULT 1 COMMENT '节点排序' not null,
  model_order_seq               varchar(255) DEFAULT '' COMMENT '节点排序链' not null,
  model_code_seq                varchar(255) DEFAULT '' COMMENT '节点编码链' not null,
  model_level                   int(10) unsigned DEFAULT 1 COMMENT '节点级别' not null,
  parent_code                   bigint COMMENT 'ID',
  constraint uq_org_model_code unique (model_code),
  constraint pk_org primary key (id)
);

create table org_operator (
  org_id                        bigint COMMENT 'ID' not null,
  operator_id                   bigint COMMENT 'ID' not null,
  constraint pk_org_operator primary key (org_id,operator_id)
);

create table permission (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  permission_code               varchar(16) DEFAULT '' COMMENT '编码' not null,
  permission_name               varchar(32) DEFAULT '' COMMENT '名称' not null,
  constraint uq_permission_permission_code unique (permission_code),
  constraint pk_permission primary key (id)
);

create table permission_endpoint (
  permission_id                 bigint COMMENT 'ID' not null,
  endpoint_path_pattern         varchar(256) COMMENT '编码' not null,
  constraint pk_permission_endpoint primary key (permission_id,endpoint_path_pattern)
);

create table role (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  role_code                     varchar(16) DEFAULT '' COMMENT '编码' not null,
  role_name                     varchar(32) DEFAULT '' COMMENT '名称' not null,
  constraint uq_role_role_code unique (role_code),
  constraint pk_role primary key (id)
);

create table role_operator (
  role_id                       bigint COMMENT 'ID' not null,
  operator_id                   bigint COMMENT 'ID' not null,
  constraint pk_role_operator primary key (role_id,operator_id)
);

create table role_menu (
  role_id                       bigint COMMENT 'ID' not null,
  menu_id                       bigint COMMENT 'ID' not null,
  constraint pk_role_menu primary key (role_id,menu_id)
);

create table role_permission (
  role_id                       bigint COMMENT 'ID' not null,
  permission_id                 bigint COMMENT 'ID' not null,
  constraint pk_role_permission primary key (role_id,permission_id)
);

create table store (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        tinyint DEFAULT 1 COMMENT '数据是否有效 0 无效/1 有效' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  store_code                    varchar(16) DEFAULT '' COMMENT '编码' not null,
  store_name                    varchar(32) DEFAULT '' COMMENT '名称' not null,
  constraint uq_store_store_code unique (store_code),
  constraint pk_store primary key (id)
);

create index ix_address_customer_id on address (customer_id);
alter table address add constraint fk_address_customer_id foreign key (customer_id) references customer (id) on delete restrict on update restrict;

create index ix_config_detail_config_code on config_detail (config_code);
alter table config_detail add constraint fk_config_detail_config_code foreign key (config_code) references config (code) on delete restrict on update restrict;

create index ix_config_detail_parent_code on config_detail (parent_code);
alter table config_detail add constraint fk_config_detail_parent_code foreign key (parent_code) references config_detail (code) on delete restrict on update restrict;

alter table customer add constraint fk_customer_operator_id foreign key (operator_id) references operator (id) on delete restrict on update restrict;

create index ix_customer_store_id on customer (store_id);
alter table customer add constraint fk_customer_store_id foreign key (store_id) references store (id) on delete restrict on update restrict;

create index ix_customer_customer_type on customer (customer_type);
alter table customer add constraint fk_customer_customer_type foreign key (customer_type) references config (code) on delete restrict on update restrict;

create index ix_menu_parent_code on menu (parent_code);
alter table menu add constraint fk_menu_parent_code foreign key (parent_code) references menu (id) on delete restrict on update restrict;

create index ix_merchandise_goods_id on merchandise (goods_id);
alter table merchandise add constraint fk_merchandise_goods_id foreign key (goods_id) references goods (id) on delete restrict on update restrict;

create index ix_merchandise_store_id on merchandise (store_id);
alter table merchandise add constraint fk_merchandise_store_id foreign key (store_id) references store (id) on delete restrict on update restrict;

create index ix_operator_operator_gender on operator (operator_gender);
alter table operator add constraint fk_operator_operator_gender foreign key (operator_gender) references config_detail (code) on delete restrict on update restrict;

create index ix_org_parent_code on org (parent_code);
alter table org add constraint fk_org_parent_code foreign key (parent_code) references org (id) on delete restrict on update restrict;

create index ix_org_operator_org on org_operator (org_id);
alter table org_operator add constraint fk_org_operator_org foreign key (org_id) references org (id) on delete restrict on update restrict;

create index ix_org_operator_operator on org_operator (operator_id);
alter table org_operator add constraint fk_org_operator_operator foreign key (operator_id) references operator (id) on delete restrict on update restrict;

create index ix_permission_endpoint_permission on permission_endpoint (permission_id);
alter table permission_endpoint add constraint fk_permission_endpoint_permission foreign key (permission_id) references permission (id) on delete restrict on update restrict;

create index ix_permission_endpoint_endpoint on permission_endpoint (endpoint_path_pattern);
alter table permission_endpoint add constraint fk_permission_endpoint_endpoint foreign key (endpoint_path_pattern) references endpoint (path_pattern) on delete restrict on update restrict;

create index ix_role_operator_role on role_operator (role_id);
alter table role_operator add constraint fk_role_operator_role foreign key (role_id) references role (id) on delete restrict on update restrict;

create index ix_role_operator_operator on role_operator (operator_id);
alter table role_operator add constraint fk_role_operator_operator foreign key (operator_id) references operator (id) on delete restrict on update restrict;

create index ix_role_menu_role on role_menu (role_id);
alter table role_menu add constraint fk_role_menu_role foreign key (role_id) references role (id) on delete restrict on update restrict;

create index ix_role_menu_menu on role_menu (menu_id);
alter table role_menu add constraint fk_role_menu_menu foreign key (menu_id) references menu (id) on delete restrict on update restrict;

create index ix_role_permission_role on role_permission (role_id);
alter table role_permission add constraint fk_role_permission_role foreign key (role_id) references role (id) on delete restrict on update restrict;

create index ix_role_permission_permission on role_permission (permission_id);
alter table role_permission add constraint fk_role_permission_permission foreign key (permission_id) references permission (id) on delete restrict on update restrict;


# --- !Downs

alter table address drop foreign key fk_address_customer_id;
drop index ix_address_customer_id on address;

alter table config_detail drop foreign key fk_config_detail_config_code;
drop index ix_config_detail_config_code on config_detail;

alter table config_detail drop foreign key fk_config_detail_parent_code;
drop index ix_config_detail_parent_code on config_detail;

alter table customer drop foreign key fk_customer_operator_id;

alter table customer drop foreign key fk_customer_store_id;
drop index ix_customer_store_id on customer;

alter table customer drop foreign key fk_customer_customer_type;
drop index ix_customer_customer_type on customer;

alter table menu drop foreign key fk_menu_parent_code;
drop index ix_menu_parent_code on menu;

alter table merchandise drop foreign key fk_merchandise_goods_id;
drop index ix_merchandise_goods_id on merchandise;

alter table merchandise drop foreign key fk_merchandise_store_id;
drop index ix_merchandise_store_id on merchandise;

alter table operator drop foreign key fk_operator_operator_gender;
drop index ix_operator_operator_gender on operator;

alter table org drop foreign key fk_org_parent_code;
drop index ix_org_parent_code on org;

alter table org_operator drop foreign key fk_org_operator_org;
drop index ix_org_operator_org on org_operator;

alter table org_operator drop foreign key fk_org_operator_operator;
drop index ix_org_operator_operator on org_operator;

alter table permission_endpoint drop foreign key fk_permission_endpoint_permission;
drop index ix_permission_endpoint_permission on permission_endpoint;

alter table permission_endpoint drop foreign key fk_permission_endpoint_endpoint;
drop index ix_permission_endpoint_endpoint on permission_endpoint;

alter table role_operator drop foreign key fk_role_operator_role;
drop index ix_role_operator_role on role_operator;

alter table role_operator drop foreign key fk_role_operator_operator;
drop index ix_role_operator_operator on role_operator;

alter table role_menu drop foreign key fk_role_menu_role;
drop index ix_role_menu_role on role_menu;

alter table role_menu drop foreign key fk_role_menu_menu;
drop index ix_role_menu_menu on role_menu;

alter table role_permission drop foreign key fk_role_permission_role;
drop index ix_role_permission_role on role_permission;

alter table role_permission drop foreign key fk_role_permission_permission;
drop index ix_role_permission_permission on role_permission;

drop table if exists activity;

drop table if exists address;

drop table if exists config;

drop table if exists config_detail;

drop table if exists customer;

drop table if exists endpoint;

drop table if exists goods;

drop table if exists menu;

drop table if exists merchandise;

drop table if exists operator;

drop table if exists order;

drop table if exists order_item;

drop table if exists order_package;

drop table if exists order_package_detail;

drop table if exists org;

drop table if exists org_operator;

drop table if exists permission;

drop table if exists permission_endpoint;

drop table if exists role;

drop table if exists role_operator;

drop table if exists role_menu;

drop table if exists role_permission;

drop table if exists store;

