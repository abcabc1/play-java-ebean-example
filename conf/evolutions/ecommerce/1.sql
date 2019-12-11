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
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  name                          varchar(32) DEFAULT '' COMMENT '名称' not null,
  type                          TINYINT DEFAULT -1 COMMENT '类别:[1 秒杀Ms,2 满减Mj,3 满折Mz]' not null,
  level                         TINYINT UNSIGNED DEFAULT 1 COMMENT '级别:[1,2...]' not null,
  enabled                       TINYINT DEFAULT -1 COMMENT '是否启用:[启用 1,禁用 -1]' not null,
  running                       TINYINT DEFAULT -1 COMMENT '运行状态:[停止 -1,运行 1]' not null,
  start_time                    DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '开始时间' not null,
  end_time                      DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '结束时间' not null,
  constraint pk_activity primary key (id)
);

create table activity_mj (
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0/1' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  activity_id                   bigint COMMENT 'ID',
  constraint uq_activity_mj_activity_id unique (activity_id)
);

create table activity_ms (
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0/1' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  activity_id                   bigint COMMENT 'ID',
  constraint uq_activity_ms_activity_id unique (activity_id)
);

create table activity_mz (
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效 0/1' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  activity_id                   bigint COMMENT 'ID',
  constraint uq_activity_mz_activity_id unique (activity_id)
);

create table activity_q (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  name                          varchar(32) DEFAULT '' COMMENT '名称' not null,
  activity_id                   bigint COMMENT 'ID',
  constraint uq_activity_q_code unique (code),
  constraint uq_activity_q_activity_id unique (activity_id),
  constraint pk_activity_q primary key (id)
);

create table activity_range (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  activity_id                   bigint COMMENT 'ID',
  range_customer_black_white    TINYINT DEFAULT -1 COMMENT '客户黑白名单:[+ 白名单, - 黑名单]' not null,
  range_merchandise_black_white TINYINT DEFAULT -1 COMMENT '商品黑白名单:[+ 白名单, - 黑名单]' not null,
  constraint pk_activity_range primary key (id)
);

create table activity_range_customer (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  activity_range_id             bigint COMMENT 'ID',
  customer_type                 CHAR(2) DEFAULT 'UU' COMMENT '类别:[UU 用户, UT 标签, UA 区域, UC 类型]' not null,
  customer_id                   bigint COMMENT 'ID',
  customer_tag_id               bigint COMMENT 'ID',
  customer_category_id          bigint COMMENT 'ID',
  customer_area_id              bigint COMMENT 'ID',
  constraint pk_activity_range_customer primary key (id)
);

create table activity_range_merchandise (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  activity_range_id             bigint COMMENT 'ID',
  merchandise_type              CHAR(2) DEFAULT 'MM' COMMENT '类别:[MM 商品, MT 标签, MC 类别]' not null,
  merchandise_id                bigint COMMENT 'ID',
  merchandise_tag_id            bigint COMMENT 'ID',
  store_merchandise_id          bigint COMMENT 'ID',
  constraint pk_activity_range_merchandise primary key (id)
);

create table address (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  detail                        varchar(255) DEFAULT '' COMMENT '详细' not null,
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

create table customer (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  operator_id                   bigint COMMENT 'ID',
  name                          varchar(32) DEFAULT '' COMMENT '名称' not null,
  level                         varchar(32) COMMENT '配置编码',
  constraint uq_customer_operator_id unique (operator_id),
  constraint pk_customer primary key (id)
);

create table customer_address (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  customer_id                   bigint COMMENT 'ID',
  address_id                    bigint COMMENT 'ID',
  constraint pk_customer_address primary key (id)
);

create table customer_area (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  customer_id                   bigint COMMENT 'ID',
  area                          varchar(32) COMMENT '配置编码',
  constraint pk_customer_area primary key (id)
);

create table customer_category (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  customer_id                   bigint COMMENT 'ID',
  category                      varchar(32) COMMENT '配置编码',
  constraint pk_customer_category primary key (id)
);

create table customer_license (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  customer_id                   bigint COMMENT 'ID',
  license_code                  varchar(32) COMMENT '配置编码',
  constraint pk_customer_license primary key (id)
);

create table customer_merchandise (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  customer                      bigint COMMENT 'ID',
  merchandise                   bigint COMMENT 'ID',
  constraint pk_customer_merchandise primary key (id)
);

create table customer_tag (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  customer_id                   bigint COMMENT 'ID',
  tag                           varchar(32) COMMENT '配置编码',
  constraint pk_customer_tag primary key (id)
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
  name                          varchar(32) DEFAULT '' COMMENT '商品名称' not null,
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
  customer_tag_id               bigint COMMENT 'ID',
  constraint pk_merchandise_tag primary key (id)
);

create table operator (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '编码' not null,
  pass                          varchar(16) DEFAULT '111111' COMMENT '密码' not null,
  type                          TINYINT UNSIGNED DEFAULT 1  COMMENT '类别[0 admin, 1 customer, 2 store]' not null,
  constraint uq_operator_code unique (code),
  constraint pk_operator primary key (id)
);

create table order_detail (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  constraint pk_order_detail primary key (id)
);

create table order_detail_item (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  constraint pk_order_detail_item primary key (id)
);

create table order_main (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  code                          varchar(16) DEFAULT '' COMMENT '流水号' not null,
  constraint uq_order_main_code unique (code),
  constraint pk_order_main primary key (id)
);

create table order_package (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  constraint pk_order_package primary key (id)
);

create table order_package_item (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  constraint pk_order_package_item primary key (id)
);

create table pack (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  name                          varchar(32) DEFAULT '' COMMENT '套餐名称' not null,
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

create table store_customer (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  store_id                      bigint COMMENT 'ID',
  customer_id                   bigint COMMENT 'ID',
  constraint pk_store_customer primary key (id)
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

create table store_merchandise (
  id                            bigint COMMENT 'ID' auto_increment not null,
  status                        TINYINT UNSIGNED DEFAULT 1 COMMENT '数据是否有效[0 无效,1 有效]' not null,
  create_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) COMMENT '创建时间' not null,
  update_time                   DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间' not null,
  store                         bigint COMMENT 'ID',
  merchandise                   bigint COMMENT 'ID',
  constraint pk_store_merchandise primary key (id)
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

alter table activity_mj add constraint fk_activity_mj_activity_id foreign key (activity_id) references activity (id) on delete restrict on update restrict;

alter table activity_ms add constraint fk_activity_ms_activity_id foreign key (activity_id) references activity (id) on delete restrict on update restrict;

alter table activity_mz add constraint fk_activity_mz_activity_id foreign key (activity_id) references activity (id) on delete restrict on update restrict;

alter table activity_q add constraint fk_activity_q_activity_id foreign key (activity_id) references activity (id) on delete restrict on update restrict;

create index ix_activity_range_activity_id on activity_range (activity_id);
alter table activity_range add constraint fk_activity_range_activity_id foreign key (activity_id) references activity (id) on delete restrict on update restrict;

create index ix_activity_range_customer_activity_range_id on activity_range_customer (activity_range_id);
alter table activity_range_customer add constraint fk_activity_range_customer_activity_range_id foreign key (activity_range_id) references activity_range (id) on delete restrict on update restrict;

create index ix_activity_range_customer_customer_id on activity_range_customer (customer_id);
alter table activity_range_customer add constraint fk_activity_range_customer_customer_id foreign key (customer_id) references customer (id) on delete restrict on update restrict;

create index ix_activity_range_customer_customer_tag_id on activity_range_customer (customer_tag_id);
alter table activity_range_customer add constraint fk_activity_range_customer_customer_tag_id foreign key (customer_tag_id) references customer_tag (id) on delete restrict on update restrict;

create index ix_activity_range_customer_customer_category_id on activity_range_customer (customer_category_id);
alter table activity_range_customer add constraint fk_activity_range_customer_customer_category_id foreign key (customer_category_id) references customer_category (id) on delete restrict on update restrict;

create index ix_activity_range_customer_customer_area_id on activity_range_customer (customer_area_id);
alter table activity_range_customer add constraint fk_activity_range_customer_customer_area_id foreign key (customer_area_id) references customer_area (id) on delete restrict on update restrict;

create index ix_activity_range_merchandise_activity_range_id on activity_range_merchandise (activity_range_id);
alter table activity_range_merchandise add constraint fk_activity_range_merchandise_activity_range_id foreign key (activity_range_id) references activity_range (id) on delete restrict on update restrict;

create index ix_activity_range_merchandise_merchandise_id on activity_range_merchandise (merchandise_id);
alter table activity_range_merchandise add constraint fk_activity_range_merchandise_merchandise_id foreign key (merchandise_id) references merchandise (id) on delete restrict on update restrict;

create index ix_activity_range_merchandise_merchandise_tag_id on activity_range_merchandise (merchandise_tag_id);
alter table activity_range_merchandise add constraint fk_activity_range_merchandise_merchandise_tag_id foreign key (merchandise_tag_id) references merchandise_tag (id) on delete restrict on update restrict;

create index ix_activity_range_merchandise_store_merchandise_id on activity_range_merchandise (store_merchandise_id);
alter table activity_range_merchandise add constraint fk_activity_range_merchandise_store_merchandise_id foreign key (store_merchandise_id) references store_merchandise (id) on delete restrict on update restrict;

create index ix_config_parent on config (parent);
alter table config add constraint fk_config_parent foreign key (parent) references config (code) on delete restrict on update restrict;

alter table customer add constraint fk_customer_operator_id foreign key (operator_id) references operator (id) on delete restrict on update restrict;

create index ix_customer_level on customer (level);
alter table customer add constraint fk_customer_level foreign key (level) references config (code) on delete restrict on update restrict;

create index ix_customer_address_customer_id on customer_address (customer_id);
alter table customer_address add constraint fk_customer_address_customer_id foreign key (customer_id) references customer (id) on delete restrict on update restrict;

create index ix_customer_address_address_id on customer_address (address_id);
alter table customer_address add constraint fk_customer_address_address_id foreign key (address_id) references address (id) on delete restrict on update restrict;

create index ix_customer_area_customer_id on customer_area (customer_id);
alter table customer_area add constraint fk_customer_area_customer_id foreign key (customer_id) references customer (id) on delete restrict on update restrict;

create index ix_customer_area_area on customer_area (area);
alter table customer_area add constraint fk_customer_area_area foreign key (area) references config (code) on delete restrict on update restrict;

create index ix_customer_category_customer_id on customer_category (customer_id);
alter table customer_category add constraint fk_customer_category_customer_id foreign key (customer_id) references customer (id) on delete restrict on update restrict;

create index ix_customer_category_category on customer_category (category);
alter table customer_category add constraint fk_customer_category_category foreign key (category) references config (code) on delete restrict on update restrict;

create index ix_customer_license_customer_id on customer_license (customer_id);
alter table customer_license add constraint fk_customer_license_customer_id foreign key (customer_id) references customer (id) on delete restrict on update restrict;

create index ix_customer_license_license_code on customer_license (license_code);
alter table customer_license add constraint fk_customer_license_license_code foreign key (license_code) references config (code) on delete restrict on update restrict;

create index ix_customer_merchandise_customer on customer_merchandise (customer);
alter table customer_merchandise add constraint fk_customer_merchandise_customer foreign key (customer) references customer (id) on delete restrict on update restrict;

create index ix_customer_merchandise_merchandise on customer_merchandise (merchandise);
alter table customer_merchandise add constraint fk_customer_merchandise_merchandise foreign key (merchandise) references merchandise (id) on delete restrict on update restrict;

create index ix_customer_tag_customer_id on customer_tag (customer_id);
alter table customer_tag add constraint fk_customer_tag_customer_id foreign key (customer_id) references customer (id) on delete restrict on update restrict;

create index ix_customer_tag_tag on customer_tag (tag);
alter table customer_tag add constraint fk_customer_tag_tag foreign key (tag) references config (code) on delete restrict on update restrict;

create index ix_merchandise_pack_merchandise_id on merchandise_pack (merchandise_id);
alter table merchandise_pack add constraint fk_merchandise_pack_merchandise_id foreign key (merchandise_id) references merchandise (id) on delete restrict on update restrict;

create index ix_merchandise_pack_pack_id on merchandise_pack (pack_id);
alter table merchandise_pack add constraint fk_merchandise_pack_pack_id foreign key (pack_id) references pack (id) on delete restrict on update restrict;

create index ix_merchandise_tag_merchandise_id on merchandise_tag (merchandise_id);
alter table merchandise_tag add constraint fk_merchandise_tag_merchandise_id foreign key (merchandise_id) references merchandise (id) on delete restrict on update restrict;

create index ix_merchandise_tag_customer_tag_id on merchandise_tag (customer_tag_id);
alter table merchandise_tag add constraint fk_merchandise_tag_customer_tag_id foreign key (customer_tag_id) references customer_tag (id) on delete restrict on update restrict;

alter table store add constraint fk_store_operator_id foreign key (operator_id) references operator (id) on delete restrict on update restrict;

create index ix_store_address_store_id on store_address (store_id);
alter table store_address add constraint fk_store_address_store_id foreign key (store_id) references store (id) on delete restrict on update restrict;

create index ix_store_address_address_id on store_address (address_id);
alter table store_address add constraint fk_store_address_address_id foreign key (address_id) references address (id) on delete restrict on update restrict;

create index ix_store_customer_store_id on store_customer (store_id);
alter table store_customer add constraint fk_store_customer_store_id foreign key (store_id) references store (id) on delete restrict on update restrict;

create index ix_store_customer_customer_id on store_customer (customer_id);
alter table store_customer add constraint fk_store_customer_customer_id foreign key (customer_id) references customer (id) on delete restrict on update restrict;

create index ix_store_license_store_id on store_license (store_id);
alter table store_license add constraint fk_store_license_store_id foreign key (store_id) references store (id) on delete restrict on update restrict;

create index ix_store_license_license on store_license (license);
alter table store_license add constraint fk_store_license_license foreign key (license) references config (code) on delete restrict on update restrict;

create index ix_store_merchandise_store on store_merchandise (store);
alter table store_merchandise add constraint fk_store_merchandise_store foreign key (store) references store (id) on delete restrict on update restrict;

create index ix_store_merchandise_merchandise on store_merchandise (merchandise);
alter table store_merchandise add constraint fk_store_merchandise_merchandise foreign key (merchandise) references merchandise (id) on delete restrict on update restrict;

create index ix_store_tag_store_id on store_tag (store_id);
alter table store_tag add constraint fk_store_tag_store_id foreign key (store_id) references store (id) on delete restrict on update restrict;

create index ix_store_tag_tag on store_tag (tag);
alter table store_tag add constraint fk_store_tag_tag foreign key (tag) references config (code) on delete restrict on update restrict;


# --- !Downs

alter table activity_mj drop foreign key fk_activity_mj_activity_id;

alter table activity_ms drop foreign key fk_activity_ms_activity_id;

alter table activity_mz drop foreign key fk_activity_mz_activity_id;

alter table activity_q drop foreign key fk_activity_q_activity_id;

alter table activity_range drop foreign key fk_activity_range_activity_id;
drop index ix_activity_range_activity_id on activity_range;

alter table activity_range_customer drop foreign key fk_activity_range_customer_activity_range_id;
drop index ix_activity_range_customer_activity_range_id on activity_range_customer;

alter table activity_range_customer drop foreign key fk_activity_range_customer_customer_id;
drop index ix_activity_range_customer_customer_id on activity_range_customer;

alter table activity_range_customer drop foreign key fk_activity_range_customer_customer_tag_id;
drop index ix_activity_range_customer_customer_tag_id on activity_range_customer;

alter table activity_range_customer drop foreign key fk_activity_range_customer_customer_category_id;
drop index ix_activity_range_customer_customer_category_id on activity_range_customer;

alter table activity_range_customer drop foreign key fk_activity_range_customer_customer_area_id;
drop index ix_activity_range_customer_customer_area_id on activity_range_customer;

alter table activity_range_merchandise drop foreign key fk_activity_range_merchandise_activity_range_id;
drop index ix_activity_range_merchandise_activity_range_id on activity_range_merchandise;

alter table activity_range_merchandise drop foreign key fk_activity_range_merchandise_merchandise_id;
drop index ix_activity_range_merchandise_merchandise_id on activity_range_merchandise;

alter table activity_range_merchandise drop foreign key fk_activity_range_merchandise_merchandise_tag_id;
drop index ix_activity_range_merchandise_merchandise_tag_id on activity_range_merchandise;

alter table activity_range_merchandise drop foreign key fk_activity_range_merchandise_store_merchandise_id;
drop index ix_activity_range_merchandise_store_merchandise_id on activity_range_merchandise;

alter table config drop foreign key fk_config_parent;
drop index ix_config_parent on config;

alter table customer drop foreign key fk_customer_operator_id;

alter table customer drop foreign key fk_customer_level;
drop index ix_customer_level on customer;

alter table customer_address drop foreign key fk_customer_address_customer_id;
drop index ix_customer_address_customer_id on customer_address;

alter table customer_address drop foreign key fk_customer_address_address_id;
drop index ix_customer_address_address_id on customer_address;

alter table customer_area drop foreign key fk_customer_area_customer_id;
drop index ix_customer_area_customer_id on customer_area;

alter table customer_area drop foreign key fk_customer_area_area;
drop index ix_customer_area_area on customer_area;

alter table customer_category drop foreign key fk_customer_category_customer_id;
drop index ix_customer_category_customer_id on customer_category;

alter table customer_category drop foreign key fk_customer_category_category;
drop index ix_customer_category_category on customer_category;

alter table customer_license drop foreign key fk_customer_license_customer_id;
drop index ix_customer_license_customer_id on customer_license;

alter table customer_license drop foreign key fk_customer_license_license_code;
drop index ix_customer_license_license_code on customer_license;

alter table customer_merchandise drop foreign key fk_customer_merchandise_customer;
drop index ix_customer_merchandise_customer on customer_merchandise;

alter table customer_merchandise drop foreign key fk_customer_merchandise_merchandise;
drop index ix_customer_merchandise_merchandise on customer_merchandise;

alter table customer_tag drop foreign key fk_customer_tag_customer_id;
drop index ix_customer_tag_customer_id on customer_tag;

alter table customer_tag drop foreign key fk_customer_tag_tag;
drop index ix_customer_tag_tag on customer_tag;

alter table merchandise_pack drop foreign key fk_merchandise_pack_merchandise_id;
drop index ix_merchandise_pack_merchandise_id on merchandise_pack;

alter table merchandise_pack drop foreign key fk_merchandise_pack_pack_id;
drop index ix_merchandise_pack_pack_id on merchandise_pack;

alter table merchandise_tag drop foreign key fk_merchandise_tag_merchandise_id;
drop index ix_merchandise_tag_merchandise_id on merchandise_tag;

alter table merchandise_tag drop foreign key fk_merchandise_tag_customer_tag_id;
drop index ix_merchandise_tag_customer_tag_id on merchandise_tag;

alter table store drop foreign key fk_store_operator_id;

alter table store_address drop foreign key fk_store_address_store_id;
drop index ix_store_address_store_id on store_address;

alter table store_address drop foreign key fk_store_address_address_id;
drop index ix_store_address_address_id on store_address;

alter table store_customer drop foreign key fk_store_customer_store_id;
drop index ix_store_customer_store_id on store_customer;

alter table store_customer drop foreign key fk_store_customer_customer_id;
drop index ix_store_customer_customer_id on store_customer;

alter table store_license drop foreign key fk_store_license_store_id;
drop index ix_store_license_store_id on store_license;

alter table store_license drop foreign key fk_store_license_license;
drop index ix_store_license_license on store_license;

alter table store_merchandise drop foreign key fk_store_merchandise_store;
drop index ix_store_merchandise_store on store_merchandise;

alter table store_merchandise drop foreign key fk_store_merchandise_merchandise;
drop index ix_store_merchandise_merchandise on store_merchandise;

alter table store_tag drop foreign key fk_store_tag_store_id;
drop index ix_store_tag_store_id on store_tag;

alter table store_tag drop foreign key fk_store_tag_tag;
drop index ix_store_tag_tag on store_tag;

drop table if exists activity;

drop table if exists activity_mj;

drop table if exists activity_ms;

drop table if exists activity_mz;

drop table if exists activity_q;

drop table if exists activity_range;

drop table if exists activity_range_customer;

drop table if exists activity_range_merchandise;

drop table if exists address;

drop table if exists config;

drop table if exists customer;

drop table if exists customer_address;

drop table if exists customer_area;

drop table if exists customer_category;

drop table if exists customer_license;

drop table if exists customer_merchandise;

drop table if exists customer_tag;

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

drop table if exists store_customer;

drop table if exists store_license;

drop table if exists store_merchandise;

drop table if exists store_tag;

